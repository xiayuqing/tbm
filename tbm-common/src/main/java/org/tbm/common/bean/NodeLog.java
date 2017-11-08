package org.tbm.common.bean;

import org.tbm.common.Serialize;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/11/8.
 */
public class NodeLog extends Serialize {
    private String identity;
    private String path = "Unknown";
    private String host;
    private String address;
    private int status;
    private Date time;

    public NodeLog() {
    }


    public NodeLog(String identity, String path, String host, String address, int status) {
        this.identity = identity;
        this.path = path;
        this.host = host;
        this.address = address;
        this.status = status;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
