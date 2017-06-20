package org.tbm.server.sharding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.State;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.DataAccessorFactory;
import org.tbm.common.access.ShardingUnits;
import org.tbm.common.access.Table;
import org.tbm.common.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private final DataAccessor dataAccessor = DataAccessorFactory.getInstance();
    private ShardingConfig shardingConfig;
    private ScheduledExecutorService executor;

    public void start() {
        if (!status.compareAndSet(State.STOP, State.STARTING)) {
            logger.info("[tbm] ShardingScheduleExecutor is already started");
            return;
        }

        executor = Executors.newScheduledThreadPool(1);

        this.shardingConfig = ShardingConfig.getConfig();
        try {
            Map<String/*currentName*/, String/*currentSql*/> tables = new HashMap<>();
            for (Map.Entry<String, Table> item : shardingConfig.getTableMap().entrySet()) {
                tables.put(item.getValue().getCurrentName(), item.getValue().getCurrentSql());

                long period;
                if (ShardingUnits.HOUR == item.getValue().getUnits()) {
                    period = 60 * 60;
                } else {
                    period = 60 * 60 * 24;
                }

                if (ShardingUnits.SINGLETON != item.getValue().getUnits()) {
                    executor.scheduleAtFixedRate(new CreateTableTask(item.getValue()), 0, period, TimeUnit
                            .SECONDS);
                }
            }

            dataAccessor.createTable(new ArrayList<>(tables.values()));
            logger.info("[tbm] Initial Table:{}", tables.keySet());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        logger.info("[tbm] Sharding Schedule Executor Started");
    }

    private class CreateTableTask implements Runnable {
        private Table table;

        public CreateTableTask(Table table) {
            this.table = table;
        }

        @Override
        public void run() {
            try {
                logger.info("[tbm] Create Table Task:{}", table.getBaseName());
                dataAccessor.createTable(ObjectUtils.singleObjectConvertToList(table.getNextSql()));
                logger.info("[tbm] Create Table:{}", table.getNextName());
            } catch (Exception e) {
                logger.error("Create Table Failed.table:{},msg:{},trace:{}", table, e.getMessage(), e.getStackTrace());
            }
        }
    }

}
