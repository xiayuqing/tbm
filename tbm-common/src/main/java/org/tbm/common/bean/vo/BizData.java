package org.tbm.common.bean.vo;

import org.tbm.common.bean.Serialize;

/**
 * Created by Jason.Xia on 17/7/8.
 */
public class BizData extends Serialize {
    private long bindingId;
    private long time;
    private int level;
    private String traceId;
    private String clazz;
    private String method;
    private String content;

    public BizData(long bindingId) {
        this.bindingId = bindingId;
    }

    public BizData() {
    }

    public long getBindingId() {
        return bindingId;
    }

    public void setBindingId(long bindingId) {
        this.bindingId = bindingId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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
}
