package org.tbm.server.operation;

import org.tbm.common.AbstractOp;
import org.tbm.common.CollectorPool;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlOperations;
import org.tbm.server.collect.BaseCollectorPool;
import org.tbm.server.executor.LogDataSqlExecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by Jason.Xia on 17/6/27.
 */
public class MemoryPoolOp extends AbstractOp {
    private final CollectorPool collectorPool;

    public MemoryPoolOp(CollectorPool collectorPool, DataAccessor dataAccessor, Map<String, Operation> map) {
        super(dataAccessor, map);
        this.collectorPool = collectorPool;
    }

    public Future INSERT_MEMORY_POOL(List<Object> args) throws Exception {
        return collectorPool.add(new LogDataSqlExecutor(dataAccessor.getConnection(), getOp(SqlOperations
                .INSERT_MEMORY_POOL), args));
    }

}
