package org.tbm.server.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public class OpsFactory {
    private static OpsFactory opsFactory;
    private static AtomicBoolean initialized = new AtomicBoolean(false);

    private Map<Class, Object> opsPool = new HashMap<>();

    private static void init() {
        if (!initialized.compareAndSet(false, true)) {
            return;
        }


    }

    private static OpsFactory initAndGet() {
        init();
        return opsFactory;
    }


}
