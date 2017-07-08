package org.tbm.client.execute;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/7/8.
 */
public class ExecutorFactory {
    private static LogExecutor logExecutor;
    private static AtomicBoolean executorEnable = new AtomicBoolean(false);

    public static LogExecutor getInstance() {
        if (null == logExecutor) {
            synchronized (ExecutorFactory.class) {
                if (null == logExecutor) {
                    logExecutor = new LogExecutor();
                    executorEnable.set(true);
                }
            }
        }

        return logExecutor;
    }

    public static boolean executorIsEnable() {
        return executorEnable.get();
    }
}
