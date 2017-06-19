package org.tbm.common.access;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public enum SqlTemplate {

    INSERT_MEMORY_SUMMARY(SQL_TYPE.CREATE, "INSERT INTO `memory_summary` (`binding_id`,`type`,`timestamp`,`max`," +
            "`init`," +
            "`committed`,`used`) VALUES (?,?,?,?,?,?,?)"),
    INSERT_MEMORY_POOL(SQL_TYPE.CREATE, "INSERT INTO `memory_pool` (`binding_id`,`cate`,`type`,`name`,`timestamp`," +
            "`peak_max`,`peak_init`,`peak_committed`,`peak_used`,`usage_max`,`usage_init`,`usage_committed`," +
            "`usage_used`,`usage_threshold`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)"),
    INSERT_CLASS_LOAD(SQL_TYPE.CREATE, "INSERT INTO `class_load` (`binding_id`,`timestamp`,`loaded`,`total`,`unload`)" +
            " VALUES (?,?,?,?,?)"),
    INSERT_THREAD(SQL_TYPE.CREATE, "INSERT INTO `thread` (`binding_id`,`timestamp`,`active`,`daemon`,`peak`,`locked`)" +
            " VALUES (?,?,?,?,?,?)"),
    /**
     * MACHINE INFO
     */
    INSERT_MACHINE_INFO(SQL_TYPE.CREATE, "INSERT INTO `machine_info` (`system_id`,`ip`,`binding_id`) VALUES(?,?,?)"),
    SELECT_MACHINE_INFO(SQL_TYPE.RETRIEVE, "SELECT * FROM `machine_info` WHERE `system_id`=? AND `ip`=?");

    public int type;
    public String sql;

    SqlTemplate(int type, String sql) {
        this.type = type;
        this.sql = sql;
    }

    interface SQL_TYPE {
        int CREATE = 1;
        int RETRIEVE = 2;
        int UPDATE = 30;
        int UPDATE_BATCH = 31;
        int DELETE = 4;
    }
}
