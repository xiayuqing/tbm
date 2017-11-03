package org.tbm.common.bean;

import org.tbm.common.Serialize;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class BaseInfo extends Serialize {
    private long timestamp = System.currentTimeMillis();

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
