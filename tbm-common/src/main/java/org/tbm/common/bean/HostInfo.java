package org.tbm.common.bean;

import org.tbm.common.annotation.Column;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class HostInfo extends Serialize {
    @Column("system_id")
    private long systemId;
    @Column("ip")
    private String ip;
    @Column("port")
    private int port;
    @Column("binding_id")
    private long bindingId;

    public HostInfo() {
    }

    public HostInfo(long systemId, String ip, int port) {
        this.systemId = systemId;
        this.ip = ip;
        this.port = port;
    }

    public long getSystemId() {
        return systemId;
    }

    public void setSystemId(long systemId) {
        this.systemId = systemId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getBindingId() {
        return bindingId;
    }

    public void setBindingId(long bindingId) {
        this.bindingId = bindingId;
    }
}
