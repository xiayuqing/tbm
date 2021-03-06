package org.tbm.server.worker;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Connection;
import org.tbm.common.bean.Traffic;
import org.tbm.common.bean.ValuePair;
import org.tbm.common.bean.WorkNode;
import org.tbm.common.util.Utils;
import org.tbm.server.SpringContainer;
import org.tbm.server.TbmContext;
import org.tbm.server.access.TrafficAccessor;
import org.tbm.server.connection.ConnectionManager;
import org.tbm.server.support.DingMsg;
import org.tbm.server.support.DingTalkWebHook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Jason.Xia on 17/10/29.
 */
public class TrafficCollectWorker {
    private static final Logger logger = LoggerFactory.getLogger(TrafficCollectWorker.class);

    private ScheduledExecutorService executor;

    private ConnectionManager connectionManager;

    private ConcurrentHashMap<String/*channelShortId*/, ValuePair<String/*identity-address*/, AtomicLong>>
            trafficReadCounter = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String/*channelShortId*/, ValuePair<String/*identity-address*/, AtomicLong>>
            trafficWriteCounter = new ConcurrentHashMap<>();

    private TrafficAccessor accessor;

    private AtomicBoolean start = new AtomicBoolean(false);

    private int period;

    private DingTalkWebHook dingTalkWebHook;

    public void run(ConnectionManager connectionManager) {
        init(connectionManager);
    }

    public void init(ConnectionManager connectionManager) {

        if (start.compareAndSet(false, true)) {

            dingTalkWebHook = DingTalkWebHook.getInstance();

            this.connectionManager = connectionManager;

            this.accessor = (TrafficAccessor) SpringContainer.getBean(TrafficAccessor.class);

            period = TbmContext.getInt("traffic.flush.period", 10);

            executor = new ScheduledThreadPoolExecutor(1);

            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<Traffic> insert = new ArrayList<>();
                        iterative(trafficReadCounter, true, insert);

                        iterative(trafficWriteCounter, false, insert);

                        if (!Utils.isEmpty(insert)) {
                            accessor.insert(insert);
                        }
                    } catch (Exception e) {
                        logger.error("Insert Traffic Statistic Error.{},trace:{}", e, e.getStackTrace());
                        dingTalkWebHook.send(new DingMsg("TrafficCollectWorker", "Insert Traffic Error", e));

                    }
                }
            }, 0, period, TimeUnit.SECONDS);
        }
    }

    private void iterative(ConcurrentHashMap<String, ValuePair<String, AtomicLong>> counter, boolean isRead,
                           List<Traffic> insertSet) {
        Iterator<Map.Entry<String, ValuePair<String, AtomicLong>>> iterator = counter.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ValuePair<String, AtomicLong>> next = iterator.next();
            if (0 == next.getValue().getValue().get()) {
                iterator.remove();
            } else {
                Traffic assemble = assemble(next, isRead);
                if (null != assemble) {
                    insertSet.add(assemble);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    private Traffic assemble(Map.Entry<String, ValuePair<String, AtomicLong>> entry, boolean isRead) {
        Traffic item = new Traffic();
        item.setChannel(entry.getKey());
        String identity = entry.getValue().getKey();
        Connection connection = connectionManager.get(identity);
        if (null == connection) {
            logger.warn("Connection Already Closed.identity:{}", identity);
            return null;
        }

        WorkNode workNode = connection.getWorkNode();
        item.setIdentity(identity);
        item.setHost(null == workNode ? "Unknown" : workNode.getHost());
        item.setAddress(null == workNode ? "Unknown" : workNode.getAddress());
        item.setType(isRead ? 1 : 2);
        item.setFlow(entry.getValue().getValue().getAndSet(0));
        item.setPeriod(period);
        return item;
    }

    public void count(Channel channel, long num, boolean isRead) {
        String key = channel.id().asShortText();
        WorkNode workNode = connectionManager.get(channel).getWorkNode();
        if (null == workNode || "Unknown".equals(workNode.getIdentity())) {
            logger.warn("Discard Read Traffic Count Cause by Unknown Identity. channel:{},num:{},isRead:{}", key,
                    num, isRead);
            return;
        }

        String identity = workNode.getIdentity() + "-" + workNode.getAddress();
        if (isRead) {
            if (trafficReadCounter.containsKey(key)) {
                trafficReadCounter.get(key).getValue().addAndGet(num);
            } else {
                trafficReadCounter.put(key, new ValuePair<>(identity, new AtomicLong(num)));
            }
        } else {
            if (trafficWriteCounter.containsKey(key)) {
                trafficWriteCounter.get(key).getValue().addAndGet(num);
            } else {
                trafficWriteCounter.put(key, new ValuePair<>(identity, new AtomicLong(num)));
            }
        }
    }

    public void stop() {
        if (!start.compareAndSet(true, false)) {
            return;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(period, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(period, TimeUnit.SECONDS)) {
                    logger.error("task executor did not terminate");
                }
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
