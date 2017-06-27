package org.tbm.server.operation;

import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.DataAccessorFactory;
import org.tbm.common.access.OperationManager;
import org.tbm.common.access.Table;

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

    public static OpsFactory getInstance() {
        return initAndGet();
    }

    public static Object get(Class key) {
        return opsFactory.getOpsPool().get(key);
    }

    public static void init() {
        if (!initialized.compareAndSet(false, true)) {
            return;
        }

        opsFactory = new OpsFactory();
        OperationManager om = OperationManager.init(OpsFactory.class.getResource("/operation.json").getFile());
        DataAccessor dataAccessor = DataAccessorFactory.getInstance();
        Map<Class, Object> opsPool = opsFactory.getOpsPool();
        opsPool.put(MachineBindingOp.class, new MachineBindingOp(dataAccessor, om.getOperations(Table
                .MACHINE_BINDING)));
        opsPool.put(MemoryPoolOp.class, new MemoryPoolOp(dataAccessor, om.getOperations(Table.MEMORY_POOL)));

        // TODO
    }

    public static OpsFactory initAndGet() {
        init();
        return opsFactory;
    }

    private Map<Class, Object> getOpsPool() {
        return opsPool;
    }
}
