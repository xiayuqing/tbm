package org.tbm.common;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class TaskResult<T> {
    private boolean success;
    private String msg;
    private Exception cause;

    public Exception getCause() {
        return cause;
    }

    public void setCause(Exception cause) {
        this.cause = cause;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
