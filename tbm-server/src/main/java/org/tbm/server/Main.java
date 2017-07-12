package org.tbm.server;

import org.tbm.common.AppContext;
import org.tbm.server.collect.CollectorPoolManager;
import org.tbm.server.operation.OpsFactory;
import org.tbm.server.sharding.ShardingScheduleExecutor;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class Main {
    public static void run(String cfgPath) {
        AppContext.init(cfgPath);
        OpsFactory opsFactory = OpsFactory.initAndGet(cfgPath);
        new ShardingScheduleExecutor().start(cfgPath);
        CollectorPoolManager.start();
        ServerAgent monitorServer = new ServerAgent();
        monitorServer.start();
    }
}
