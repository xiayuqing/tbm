package org.tbm.server.collect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.AppContext;
import org.tbm.common.TaskResult;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.DataAccessorFactory;
import org.tbm.common.access.SqlExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class CollectorPool {
    private static final Logger logger = LoggerFactory.getLogger(CollectorPool.class);
    private ExecutorService executor;
    private AtomicBoolean start = new AtomicBoolean(false);

    void run() {
        init();
    }

    private void init() {

        if (!start.compareAndSet(false, true)) {
            return;
        }

        executor = Executors.newFixedThreadPool(AppContext.getInt("executor.size", 30));
    }

    public Future add(final SqlExecutor sqlExecutor) {
        return executor.submit(new Callable<TaskResult>() {
            @Override
            public TaskResult call() throws Exception {
                TaskResult result = new TaskResult();
                try {
                    sqlExecutor.run();
                    result.setSuccess(true);
                } catch (Exception e) {
                    result.setSuccess(false);
                    result.setCause(e);
                }

                return result;
            }
        });
    }

    void stop() {
        if (!start.compareAndSet(true, false)) {
            return;
        }

        executor.shutdown();
        int wait = AppContext.getInt("executor.shutdown.wait", 60000);
        try {
            if (-1 == wait) {
                executor.shutdownNow();
            } else if (!executor.awaitTermination(wait, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(wait, TimeUnit.MILLISECONDS)) {
                    logger.error("task executor did not terminate");
                }
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
