package org.tbm.common.access;

/**
 * Created by Jason.Xia on 17/5/31.
 */
public class DataAccessorFactory {
    private static DataAccessor dataAccessor;

    public static DataAccessor getInstance() {
        synchronized (DataAccessor.class) {
            if (null == dataAccessor) {
                dataAccessor = new DataAccessor();
            }
        }

        return dataAccessor;
    }
}
