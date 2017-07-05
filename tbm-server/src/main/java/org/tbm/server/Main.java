package org.tbm.server;

import org.apache.log4j.NDC;
import org.slf4j.MDC;
import org.tbm.server.collect.CollectorPoolManager;
import org.tbm.server.operation.OpsFactory;
import org.tbm.server.sharding.ShardingScheduleExecutor;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class Main {
    public static void main(String[] args) {
        MDC.put("traceId","testTraceId");
//        AppContext.init(args[0]);
//        OperationManager.init(Main.class.getResource("/operation.json").getFile());
        OpsFactory opsFactory = OpsFactory.initAndGet();
        new ShardingScheduleExecutor().start();
        CollectorPoolManager.start();
        ServerAgent monitorServer = new ServerAgent();
        monitorServer.start();
    }
}
