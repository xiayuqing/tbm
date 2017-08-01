package org.tbm.server.collect;

import org.tbm.common.CollectorPool;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class CollectorPoolManager {
    private static final CollectorPool taskPool = new BaseCollectorPool();
    private static boolean started = false;

    public static void start() {
        taskPool.run();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                taskPool.stop();
            }
        }));
    }

    public static CollectorPool getTaskPool() {
        if (!started) {
            start();
        }

        return taskPool;
    }
}
