package org.tbm.server.collect;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class CollectorPoolManager {
    private static final CollectorPool taskPool = new CollectorPool();

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
        return taskPool;
    }
}
