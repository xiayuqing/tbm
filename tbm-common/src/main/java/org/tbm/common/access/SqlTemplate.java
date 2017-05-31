package org.tbm.common.access;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public abstract class SqlTemplate {
    public static String INSERT_MEMORY_SUMMARY = "INSERT INTO `memory_summary` (`binding_id`,`type`,`max`,`init`," +
            "`committed`,`used`) VALUES (?,?,?,?,?,?)";
    public static String INSERT_MACHINE_INFO = "INSERT INTO `machine_info` (`system_id`,`ip`,`port`,`binding_id`) " +
            "VALUES(?,?,?,?,?)";
    public static String SELECT_MACHINE_INFO = "SELECT * FROM `machine_info` WHERE `system_id`=? AND `ip`=? AND " +
            "`port`=? ";
}
