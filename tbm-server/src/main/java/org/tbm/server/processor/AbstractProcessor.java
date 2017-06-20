package org.tbm.server.processor;

import org.tbm.common.Processor;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.OperationManager;
import org.tbm.server.collect.CollectorPool;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public abstract class AbstractProcessor implements Processor {
    protected final DataAccessor dataAccessor;
    protected final CollectorPool collectorPool;
    protected final OperationManager om;

    public AbstractProcessor(DataAccessor dataAccessor, CollectorPool collectorPool, OperationManager
            operationManager) {
        this.dataAccessor = dataAccessor;
        this.collectorPool = collectorPool;
        this.om = operationManager;
    }
}
