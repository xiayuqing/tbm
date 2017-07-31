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
        RedisPoolManager.init();
        OpsFactory opsFactory = OpsFactory.initAndGet(cfgPath);
        new ShardingScheduleExecutor().start(cfgPath);
        CollectorPoolManager.start();
        ServerAgent monitorServer = new ServerAgent();
        monitorServer.start();
    }

    public static void main(String[] args) {
        RedisPoolManager.init();
        OpsFactory opsFactory = OpsFactory.initAndGet(Main.class.getResource("/operation.json").getFile());
        new ShardingScheduleExecutor().start(Main.class.getResource("/table.json").getFile());
        CollectorPoolManager.start();
        ServerAgent monitorServer = new ServerAgent();
        monitorServer.start();
    }
}
