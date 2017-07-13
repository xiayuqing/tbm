package org.tbm.common.bean;

import org.tbm.common.annotation.Column;

/**
 * Created by Jason.Xia on 17/6/23.
 */
public class SystemInfo extends Serialize {
    @Column("id")
    private long id;

    @Column("name")
    private String name;

    @Column("group")
    private String group;

    @Column("env")
    private int env;

    public SystemInfo() {
    }

    public SystemInfo(long id, String name, String group, int env) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.env = env;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getEnv() {
        return env;
    }

    public void setEnv(int env) {
        this.env = env;
    }
}
