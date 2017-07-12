package org.tbm.server.operation;

import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.DataAccessorFactory;
import org.tbm.common.access.OperationManager;
import org.tbm.common.access.Table;
import org.tbm.server.collect.CollectorPool;
import org.tbm.server.collect.CollectorPoolManager;

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

    public static Object get(Class key) {
        return opsFactory.getOpsPool().get(key);
    }

    public static void init(String opsPath) {
        if (!initialized.compareAndSet(false, true)) {
            return;
        }

        opsFactory = new OpsFactory();
        OperationManager om = OperationManager.init(opsPath);
        DataAccessor dataAccessor = DataAccessorFactory.getInstance();
        CollectorPool taskPool = CollectorPoolManager.getTaskPool();
        Map<Class, Object> opsPool = opsFactory.getOpsPool();
        opsPool.put(MachineBindingOp.class, new MachineBindingOp(dataAccessor, om.getOperations(Table
                .MACHINE_BINDING)));
        opsPool.put(MemoryPoolOp.class, new MemoryPoolOp(taskPool, dataAccessor, om.getOperations(Table.MEMORY_POOL)));
        opsPool.put(MemorySummaryOp.class, new MemorySummaryOp(taskPool, dataAccessor, om.getOperations(Table
                .MEMORY_SUMMARY)));
        opsPool.put(ClassLoadOp.class, new ClassLoadOp(taskPool, dataAccessor, om.getOperations(Table.CLASS_LOAD)));
        opsPool.put(ThreadOp.class, new ThreadOp(taskPool, dataAccessor, om.getOperations(Table.THREAD)));
        opsPool.put(BizOp.class, new BizOp(taskPool, dataAccessor, om.getOperations(Table.BIZ)));
    }

    public static OpsFactory initAndGet(String opsPath) {
        init(opsPath);
        return opsFactory;
    }

    private Map<Class, Object> getOpsPool() {
        return opsPool;
    }
}
