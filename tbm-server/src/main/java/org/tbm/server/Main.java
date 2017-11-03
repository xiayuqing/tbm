package org.tbm.server;

import org.tbm.server.connection.ConnectionManager;
import org.tbm.server.sharding.ShardingScheduleExecutor;
import org.tbm.server.support.MonitorCollectWorker;
import org.tbm.server.support.TrafficCollectWorker;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class Main {

    public static void main(String[] args) {
        try {
            String cfgPath = args[0];
            if (null == cfgPath) {
                System.err.println("Need config path");
            }

            start(cfgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void start(String path) {
        try {
            TbmContext.init(path);

            final SpringContainer container = new SpringContainer();
            container.start("classpath*:/tbm-server.xml");

            final ShardingScheduleExecutor executor = new ShardingScheduleExecutor();
            executor.start();

            final MonitorCollectWorker collectWorker = new MonitorCollectWorker();
            collectWorker.run();

            final ConnectionManager connectionManager = new ConnectionManager();
            connectionManager.init(collectWorker);

            final TrafficCollectWorker trafficCollectWorker = new TrafficCollectWorker();
            trafficCollectWorker.run(connectionManager);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    trafficCollectWorker.stop();
                    connectionManager.destroy();
                    executor.stop();
                    collectWorker.stop();
                    container.stop();
                }
            }));

            new ServerAgent().start(connectionManager, trafficCollectWorker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
