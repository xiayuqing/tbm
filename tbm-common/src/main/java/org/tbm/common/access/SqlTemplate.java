package org.tbm.common.access;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public enum SqlTemplate {

    INSERT_MEMORY_SUMMARY(SQL_TYPE.CREATE, "INSERT INTO `memory_summary` (`binding_id`,`type`,`max`,`init`," +
            "`committed`,`used`) VALUES (?,?,?,?,?,?)"),
    INSERT_MACHINE_INFO(SQL_TYPE.CREATE, "INSERT INTO `machine_info` (`system_id`,`ip`,`port`,`binding_id`) " +
            "VALUES(?,?,?,?)"),
    SELECT_MACHINE_INFO(SQL_TYPE.RETRIEVE, "SELECT * FROM `machine_info` WHERE `system_id`=? AND `ip`=? AND " +
            "`port`=? ");


    public int type;
    public String sql;

    SqlTemplate(int type, String sql) {
        this.type = type;
        this.sql = sql;
    }

    interface SQL_TYPE {
        int CREATE = 1;
        int RETRIEVE = 2;
        int UPDATE = 3;
        int DELETE = 4;
    }
}
