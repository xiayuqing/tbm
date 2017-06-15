package org.tbm.server.sharding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.State;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.DataAccessorFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/6/15.
 */
public class ShardingScheduleExecutor {
    private static AtomicInteger status = new AtomicInteger(State.STOP);
    private final Logger logger = LoggerFactory.getLogger(ShardingScheduleExecutor.class);
    private final DataAccessor dataAccessor = DataAccessorFactory.getInstance();
    private ShardingConfig shardingConfig;

    public void start() {
        if (!status.compareAndSet(State.STOP, State.STARTING)) {
            logger.info("[tbm] ShardingScheduleExecutor is already started");
            return;
        }

        this.shardingConfig = ShardingConfig.getConfig();


    }

}
