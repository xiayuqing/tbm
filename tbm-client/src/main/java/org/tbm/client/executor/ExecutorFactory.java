package org.tbm.client.executor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/7/8.
 */
public class ExecutorFactory {
    private static MonitorExecutor monitorExecutor;
    private static AtomicBoolean executorEnable = new AtomicBoolean(false);

    public static MonitorExecutor getInstance() {
        if (null == monitorExecutor) {
            synchronized (ExecutorFactory.class) {
                if (null == monitorExecutor) {
                    monitorExecutor = new MonitorExecutor();
                    executorEnable.set(true);
                }
            }
        }

        return monitorExecutor;
    }

    public static boolean executorIsEnable() {
        return executorEnable.get();
    }
}
