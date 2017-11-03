package org.tbm.common.bean;

import org.tbm.common.Serialize;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/7/8.
 */
public class LogData extends Serialize implements Cloneable {

    public static final int MAX_LEN_TRACE = 100;

    public static final int MAX_LEN_CLAZZ = 300;

    public static final int MAX_LEN_METHOD = 100;

    public static final int MAX_LEN_CONTENT = 1000;

    private String identity;

    private Date time;

    private String host;

    private int level;

    private String trace;

    private String clazz;

    private String method;

    private int line;

    private String content;

    private Date persistTime;

    public LogData(String identity) {
        this.identity = identity;
    }

    public LogData() {
    }

    /**
     * exclude field:content
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public LogData clone() throws CloneNotSupportedException {
        LogData clone = (LogData) super.clone();
        clone.content = null;
        return clone;
    }

    public void safeSetTrace(Object properties) {
        if (null == properties || "".equals(properties.toString())) {
            this.trace = "0";
        } else {
            String s = properties.toString();
            if (s.trim().length() > 0) {
                this.trace = s.length() < MAX_LEN_TRACE ? s : s.substring(0, MAX_LEN_TRACE);
            } else {
                this.trace = "0";
            }
        }
    }

    public void safeSetClazz(String clazz) {
        if (null == clazz || "".equals(clazz)) {
            this.clazz = "0";
        } else {
            if (clazz.trim().length() > 0) {
                this.clazz = clazz.length() < MAX_LEN_CLAZZ ? clazz : clazz.substring(0, MAX_LEN_CLAZZ);
            } else {
                this.clazz = "0";
            }
        }
    }

    public void safeSetMethod(String method) {
        if (null == method || "".equals(method)) {
            this.method = "0";
        } else {
            if (method.trim().length() > 0) {
                this.method = method.length() < MAX_LEN_METHOD ? method : method.substring(0, MAX_LEN_METHOD);
            } else {
                this.method = "0";
            }
        }
    }

    public void safeSetContent(String content) {
        if (null != content && !"".equals(content)) {
            if (content.trim().length() > 0) {
                this.content = content.length() < MAX_LEN_CONTENT ? content : content.substring(0, MAX_LEN_CONTENT);
            }
        }
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addContent(String content) {
        this.content += content;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Date getPersistTime() {
        return persistTime;
    }

    public void setPersistTime(Date persistTime) {
        this.persistTime = persistTime;
    }
}
