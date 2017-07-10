package org.tbm.common;

/**
 * Created by Jason.Xia on 17/7/10.
 */
public enum LogLevel {
    DEBUG(10000, "DEBUG"),
    INFO(20000, "INFO"),
    WARN(30000, "WARN"),
    ERROR(40000, "ERROR");

    private int code;
    private String desc;

    LogLevel(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
