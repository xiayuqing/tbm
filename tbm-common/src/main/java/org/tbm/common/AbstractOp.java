package org.tbm.common;

import org.tbm.common.access.DataAccessor;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public abstract class AbstractOp {
    protected final DataAccessor dataAccessor;

    public AbstractOp(DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }
}
