package org.tbm.server.collect;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.AppContext;
import org.tbm.common.CollectorPool;
import org.tbm.common.RedisPoolManager;
import org.tbm.common.TaskResult;
import org.tbm.common.access.SqlExecutor;
import org.tbm.common.bean.vo.BizData;
import org.tbm.common.utils.CollectionUtils;
import org.tbm.server.OpsFactory;
import org.tbm.server.operation.BizOp;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class BaseCollectorPool implements CollectorPool{
    private static final Logger logger = LoggerFactory.getLogger(BaseCollectorPool.class);

    private ExecutorService executor;

    private ScheduledExecutorService cacheScheduledExecutor;

    private Map<String/*bindingId*/, ScheduledFuture> jobFuture = new ConcurrentHashMap<>();

    private AtomicBoolean start = new AtomicBoolean(false);

    private boolean redisEnable = true;

    public void run() {
        init();
    }

    public void init() {

        if (!start.compareAndSet(false, true)) {
            return;
        }

        executor = Executors.newFixedThreadPool(AppContext.getInt("collector.executor.size", 10));

        redisEnable = AppContext.getBoolean("redis.enable", true);
        if (redisEnable) {
            cacheScheduledExecutor = new ScheduledThreadPoolExecutor(AppContext.getInt("cache.pool.core", 30));
        }
    }

    // 添加一个工作线程,单独负责一个队列
    public void addScheduleJob(String bindingId) {
        if (!redisEnable) {
            return;
        }

        ScheduledFuture<?> future = cacheScheduledExecutor.scheduleWithFixedDelay(new FlushJob(bindingId),
                0, AppContext.getInt("cache.flush.delay", 10), TimeUnit.SECONDS);
        jobFuture.put(bindingId, future);
        logger.info("添加flush任务, bindingId:{}", bindingId);
    }

    public void removeScheduleJob(String bindingId) {
        ScheduledFuture future = jobFuture.get(bindingId);
        if (null != future) {
            future.cancel(false);
            logger.info("移除flush任务,bindingId:{}", bindingId);
            jobFuture.remove(bindingId);
        }
    }

    public Future add(final SqlExecutor sqlExecutor) {
        return executor.submit(new Callable<TaskResult>() {
            @Override
            public TaskResult call() throws Exception {
                TaskResult result = new TaskResult();
                try {
                    sqlExecutor.run();
                    result.setSuccess(true);
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setCause(e);
                    e.printStackTrace();
                }

                return result;
            }
        });
    }

    public void stop() {
        if (!start.compareAndSet(true, false)) {
            return;
        }

        executor.shutdown();
        int wait = AppContext.getInt("collector.executor.shutdown.wait", 60000);
        try {
            if (-1 == wait) {
                executor.shutdownNow();
            } else if (!executor.awaitTermination(wait, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(wait, TimeUnit.MILLISECONDS)) {
                    logger.error("task executor did not terminate");
                }
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private class FlushJob implements Runnable {
        private String listKey;
        private BizOp op;

        public FlushJob(String listKey) {
            this.listKey = listKey;
            this.op = (BizOp) OpsFactory.get(BizOp.class);
        }

        @Override
        public void run() {
            Jedis jedis = null;
            try {
                jedis = RedisPoolManager.getJedis();
                int step = AppContext.getInt("cache.flush.batch", 300);
                int unitSize = 0;

                do {
                    List<String> lrange = jedis.lrange(listKey, 0, step);
                    if (CollectionUtils.isEmpty(lrange)) {
                        break;
                    }

                    unitSize = lrange.size();
                    List<BizData> data = new ArrayList<>();
                    for (String item : lrange) {
                        data.add(JSONObject.parseObject(item, BizData.class));
                    }

                    op.INSERT_BIZ(data);
                    if (unitSize < step) {
                        jedis.ltrim(listKey, 1, 0);
                    } else {
                        jedis.ltrim(listKey, unitSize, -1);
                    }
                } while (unitSize == step);

            } catch (Exception e) {
                logger.error("flush cache error", e);
            } finally {
                if (null != jedis) {
                    RedisPoolManager.returnJedis(jedis);
                }
            }
        }
    }
}
