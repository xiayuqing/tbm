package org.tbm.admin;

import org.tbm.common.Serialize;

/**
 * Created by Jason.Xia on 17/7/13.
 */
public class Response<T> extends Serialize {
    private String code = ResponseCode.SUCCESS.code;
    private String msg = ResponseCode.SUCCESS.msg;
    private T data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public enum ResponseCode {
        SUCCESS("200", "success"),
        FALIURE("300", "failure"),
        EXCEPTION("999", "exception");

        private String code;
        private String msg;

        ResponseCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
