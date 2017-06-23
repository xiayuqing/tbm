package org.tbm.common.bean;

import org.tbm.common.annotation.Column;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public class MachineLog extends Serialize {
    @Column("binding_id")
    private long bindingId;

    @Column("status")
    private int status;

    @Column("time")
    private long time;

    public long getBindingId() {
        return bindingId;
    }

    public void setBindingId(long bindingId) {
        this.bindingId = bindingId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
