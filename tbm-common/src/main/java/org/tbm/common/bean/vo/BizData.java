package org.tbm.common.bean.vo;

import org.tbm.common.bean.Serialize;

/**
 * Created by Jason.Xia on 17/7/8.
 */
public class BizData extends Serialize implements Cloneable {

    public static final int MAX_LEN_TRACE = 20;

    public static final int MAX_LEN_CLAZZ = 100;

    public static final int MAX_LEN_METHOD = 100;

    public static final int MAX_LEN_CONTENT = 700;

    private long bindingId;

    private long time;

    private int level;

    private String trace;

    private String clazz;

    private String method;

    private int line;

    private String content;

    public BizData(long bindingId) {
        this.bindingId = bindingId;
    }

    public BizData() {
    }

    /**
     * exclude field:content
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public BizData clone() throws CloneNotSupportedException {
        BizData clone = (BizData) super.clone();
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
}
