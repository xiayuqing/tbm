package org.tbm.server.operation;

import org.tbm.common.AbstractOp;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.Operation;

import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/27.
 */
public class MemoryPoolOp extends AbstractOp {
    public MemoryPoolOp(DataAccessor dataAccessor, Map<String, Operation> map) {
        super(dataAccessor, map);
    }


}
