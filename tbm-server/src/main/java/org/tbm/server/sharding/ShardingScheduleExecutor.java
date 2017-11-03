package org.tbm.server.sharding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.State;

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
}
