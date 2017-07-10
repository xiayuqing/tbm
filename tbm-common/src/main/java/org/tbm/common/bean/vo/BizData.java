package org.tbm.common.bean.vo;

import org.tbm.common.annotation.Max;
import org.tbm.common.bean.Serialize;

/**
 * Created by Jason.Xia on 17/7/8.
 */
public class BizData extends Serialize {
    private long bindingId;
    private long time;
    private int level;
    @Max(20)
    private String trace;
    @Max(100)
    private String clazz;
    @Max(100)
    private String method;
    private int line;
    @Max(500)
    private String content;

    public BizData(long bindingId) {
        this.bindingId = bindingId;
    }

    public BizData() {
    }

    public void safeSetTrace(String trace) {
        if (null == trace || "".equals(trace)) {
            this.trace = "0";
        } else {
            if (trace.trim().length() > 0) {
                this.trace = trace.length() < 20 ? trace : trace.substring(0, 20);
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
                this.clazz = clazz.length() < 100 ? clazz : clazz.substring(0, 100);
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
                this.method = method.length() < 100 ? method : method.substring(0, 100);
            } else {
                this.method = "0";
            }
        }
    }

    public void safeSetContent(String content) {
        if (null == content || "".equals(content)) {
            this.content = "0";
        } else {
            if (content.trim().length() > 0) {
                this.content = content.length() < 500 ? content : content.substring(0, 500);
            } else {
                this.content = "0";
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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
