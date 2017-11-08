package org.tbm.server.support;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.tbm.common.bean.LogData;
import org.tbm.common.util.Utils;
import org.tbm.server.SpringContainer;
import org.tbm.server.TbmContext;
import org.tbm.server.access.LogDataAccessor;
import org.tbm.server.datasource.RedisOperator;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/10/26.
 */
public class MonitorCollectWorker {
    private static final Logger logger = LoggerFactory.getLogger(MonitorCollectWorker.class);
    private final Set<String/*identity-address*/> removedSet = new HashSet<>();
    private ScheduledExecutorService cacheScheduledExecutor;
    private Map<String/*identity-address*/, ScheduledFuture> jobFuture = new ConcurrentHashMap<>();
    private AtomicBoolean start = new AtomicBoolean(false);

    private long cacheFlushDelay;

    private int cacheFlushBatch;

    public void run() {
        init();
    }

    public void init() {
        if (start.compareAndSet(false, true)) {
            cacheFlushDelay = TbmContext.getInt("cache.flush.delay", 10);
            cacheFlushBatch = TbmContext.getInt("cache.flush.batch", 300);

            cacheScheduledExecutor = new ScheduledThreadPoolExecutor(TbmContext.getInt("cache.pool.core", 10));
        }
    }

    // 添加一个工作线程,单独负责一个队列
    public void addScheduleJob(String identity) {
        ScheduledFuture<?> future = cacheScheduledExecutor.scheduleWithFixedDelay(new FlushJob(identity,
                (LogDataAccessor) SpringContainer.getBean(LogDataAccessor.class)), new Random().nextInt
                (10), cacheFlushDelay, TimeUnit.SECONDS);
        jobFuture.put(identity, future);
        synchronized (removedSet) {
            removedSet.remove(identity);
        }

        logger.info("添加flush任务, Identity:{}", identity);
    }

    public void removeScheduleJob(String identity) {
        removedSet.add(identity);
        logger.info("标记-移除flush任务,Identity:{}", identity);
    }

    public void cancelScheduleJob(String identity) {
        ScheduledFuture future = jobFuture.get(identity);
        if (null != future) {
            future.cancel(false);
            logger.info("移除flush任务,Identity:{}", identity);
            jobFuture.remove(identity);
        }
    }

    public void stop() {
        if (start.compareAndSet(true, false)) {
            cacheScheduledExecutor.shutdown();
            try {
                if (!cacheScheduledExecutor.awaitTermination(60000, TimeUnit.MILLISECONDS)) {
                    cacheScheduledExecutor.shutdownNow();
                    if (!cacheScheduledExecutor.awaitTermination(60000, TimeUnit.MILLISECONDS)) {
                        logger.error("task executor did not terminate");
                    }
                }

            } catch (InterruptedException e) {
                cacheScheduledExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private class FlushJob implements Runnable {
        private String identity;
        private LogDataAccessor accessor;

        public FlushJob(String identity, LogDataAccessor accessor) {
            this.identity = identity;
            this.accessor = accessor;
        }

        @Override
        public void run() {
            try {
                int total = 0;

                RedisTemplate<String, String> redisCache = RedisOperator.getRedisCache();
                int unitSize;
                while (true) {

                    List<String> lrange = redisCache.opsForList().range(identity, 0, cacheFlushBatch - 1);
                    if (Utils.isEmpty(lrange)) {
                        logger.debug("{} lrange size is 0,exit task.", identity);
                        break;
                    }

                    total += lrange.size();
                    unitSize = lrange.size();
                    List<LogData> data = new ArrayList<>();
                    for (String item : lrange) {
                        data.add(JSONObject.parseObject(item, LogData.class));
                    }

                    accessor.insert(data);
                    redisCache.opsForList().trim(identity, unitSize, -1);
                }

                logger.info("{} total num:{}", identity, total);
            } catch (Exception e) {
                logger.error("flush cache error", e);
            }

            if (removedSet.contains(identity)) {
                cancelScheduleJob(identity);
                removedSet.remove(identity);
            }
        }
    }
}
