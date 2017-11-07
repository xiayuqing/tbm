package org.tbm.common.bean;


import org.tbm.common.Serialize;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/10/29.
 */
public class Traffic extends Serialize {
    private String channel;
    private String identity;
    private String host;
    private String address;
    private Date time;
    // 1:read ;2:write
    private int type;
    // Byte
    private long flow;
    // second
    private int period;


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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getFlow() {
        return flow;
    }

    public void setFlow(long flow) {
        this.flow = flow;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
