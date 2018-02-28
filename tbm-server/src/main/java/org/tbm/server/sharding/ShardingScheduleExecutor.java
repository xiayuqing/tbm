package org.tbm.server.sharding;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.State;
import org.tbm.common.util.Utils;
import org.tbm.server.TbmContext;
import org.tbm.server.datasource.InnerDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/6/15.
 */
public class ShardingScheduleExecutor {
    private static AtomicInteger status = new AtomicInteger(State.STOP);
    private final Logger logger = LoggerFactory.getLogger(ShardingScheduleExecutor.class);
    private ScheduledExecutorService executor;

    public void start() {
        if (!status.compareAndSet(State.STOP, State.STARTING)) {
            logger.info("[tbm] ShardingScheduleExecutor is already started");
            return;
        }

        executor = Executors.newScheduledThreadPool(1);

        try {

            for (Operation item : ShardingInventory.getShardingInventory()) {
                long period;
                if (ShardingUnits.HOUR == item.getUnits()) {
                    period = 60 * 60;
                } else {
                    period = 60 * 60 * 24;
                }

                if (ShardingUnits.SINGLETON != item.getUnits() && ShardingUnits.HASH != item.getUnits()) {
                    executor.scheduleAtFixedRate(new CreateTableTask(item), 0, period, TimeUnit
                            .SECONDS);
                }

                item.create(false);
                logger.info("[tbm] Initial Table:{}", item.getCurrentName());
            }

            // clear table task once a day
            executor.scheduleWithFixedDelay(new ClearTableTask(), 10, 60 * 60 * 24, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        logger.info("[tbm] Sharding Schedule Executor Started");
    }

    public void stop() {
        if (State.STOP == status.get()) {
            return;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60000, TimeUnit.MILLISECONDS)) {
                    logger.error("ShardingScheduleExecutor did not terminate");
                }
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private class CreateTableTask implements Runnable {
        private Operation operation;

        CreateTableTask(Operation op) {
            this.operation = op;
        }

        @Override
        public void run() {
            try {
                logger.info("[tbm] Run Create Table Task for [{}]", operation.getBaseName());
                operation.create(true);
                logger.info("[tbm] Success to Create Table: [{}]", operation.getNextName());
            } catch (Exception e) {
                logger.error("Create Table " + operation.getNextName() + " Failed.", e);
            }
        }
    }

    private class ClearTableTask implements Runnable {

        @Override
        public void run() {
            logger.info("[tbm] Run Clear Table Task");
            InnerDataSource dataSource = InnerDataSource.ref;
            DruidPooledConnection connection = null;
            try {
                connection = dataSource.getConnection();
                Statement s1 = connection.createStatement();
                ResultSet tablesRecord = s1.executeQuery("SHOW tables");
                List<Long> tablesTime = new ArrayList<>();
                while (tablesRecord.next()) {
                    String string = tablesRecord.getString(1);
                    if (string.startsWith("log")) {
                        tablesTime.add(Long.valueOf(string.substring(3)));
                    }
                }

                logger.info("总共表数量:{}", tablesTime.size());
                s1.close();

                String pattern;
                switch (ShardingUnits.valueOf(TbmContext.getString("table.log.unit"))) {
                    case HOUR:
                        pattern = "yyyyMMddHH";
                        break;
                    case DAY:
                        pattern = "yyyyMMdd";
                        break;
                    default:
                        throw new IllegalStateException("Current Unit Cannot Clear Table");
                }

                Date dateAfter = Utils.getDateAfter(new Date(), -29);
                Long stdTime = Long.valueOf(Utils.getDateString(dateAfter, pattern));
                Iterator<Long> iterator = tablesTime.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next() > stdTime) {
                        iterator.remove();
                    }
                }

                logger.info("需要清理的表数量:{}", tablesTime.size());
                if (!Utils.isEmpty(tablesTime)) {
                    Statement s2 = connection.createStatement();
                    for (Long item : tablesTime) {
                        String s = "log" + item;
                        s2.execute("drop table " + s);
                        logger.info("Drop Tables:{}", s);
                    }

                    s2.close();
                }

                logger.info("[tbm] Run Clear Table Task Success");
            } catch (Exception e) {
                logger.error("Clear Table Error", e);
            } finally {
                if (null != connection) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        logger.error("Close Connection Error",e);
                    }
                }
            }
        }
    }
}
