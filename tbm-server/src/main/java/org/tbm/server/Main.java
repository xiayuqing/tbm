package org.tbm.server;

import org.tbm.server.collect.CollectorPoolManager;
import org.tbm.server.sharding.ShardingScheduleExecutor;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class Main {
    public static void main(String[] args) {
        new ShardingScheduleExecutor().start();
        CollectorPoolManager.start();
        ServerAgent monitorServer = new ServerAgent();
        monitorServer.start();
    }
}
