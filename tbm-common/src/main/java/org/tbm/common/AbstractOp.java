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

    private final Map<String/*operationName*/, Operation> om;

    public AbstractOp(DataAccessor dataAccessor, Map<String, Operation> operationMap) {
        this.dataAccessor = dataAccessor;
        this.om = operationMap;
    }

    protected Operation getOp(SqlOperations operation) {
        return getOp(operation.name());
    }

    private Operation getOp(String operation) {
        Operation op = om.get(operation);
        if (null == op) {
            throw new IllegalStateException("Not Found Match Operation for:" + operation);
        }

        return op;
    }
}
