package org.tbm.common;

import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlOperations;

import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public abstract class AbstractOp {
    protected final DataAccessor dataAccessor;
    protected Map<String/*operationName*/, Operation> operationMap;

    public AbstractOp(DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public Operation getOp(SqlOperations operation) {
        return getOp(operation.name());
    }

    public Operation getOp(String operation) {
        Operation op = operationMap.get(operation);
        if (null == op) {
            throw new IllegalStateException("Not Found Match Operation for:" + operation);
        }

        return op;
    }
}
