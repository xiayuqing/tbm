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
    SELECT_MACHINE_INFO(SQL_TYPE.RETRIEVE, "SELECT * FROM `machine_info` WHERE `system_id`=? AND `ip`=?"),

    CREATE_MACHINE_INFO(SQL_TYPE.CREATE, "CREATE TABLE IF NOT EXISTS `?` (\n" +
            "  `system_id` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `ip` char(15) NOT NULL DEFAULT '127.0.0.1',\n" +
            "  `port` int(11) NOT NULL DEFAULT '0',\n" +
            "  `binding_id` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:offline;1:online',\n" +
            "  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  PRIMARY KEY (`system_id`,`ip`,`port`),\n" +
            "  UNIQUE KEY `unique_binding_id` (`binding_id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8"),

    CREATE_MEMORY_POOL(SQL_TYPE.CREATE, "CREATE TABLE IF NOT EXISTS `?` (\n" +
            "  `binding_id` bigint(20) NOT NULL,\n" +
            "  `cate` int(11) NOT NULL,\n" +
            "  `type` int(11) NOT NULL DEFAULT '0',\n" +
            "  `name` char(20) NOT NULL DEFAULT 'default',\n" +
            "  `timestamp` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `peak_max` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `peak_init` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `peak_committed` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `peak_used` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `usage_max` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `usage_init` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `usage_committed` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `usage_used` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `usage_threshold` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8"),

    CREATE_MEMORY_SUMMARY(SQL_TYPE.CREATE, "CREATE TABLE IF NOT EXISTS `?` (\n" +
            "  `binding_id` bigint(20) NOT NULL,\n" +
            "  `type` int(11) NOT NULL DEFAULT '0' COMMENT '1:heap;2:non-heap',\n" +
            "  `timestamp` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `max` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `init` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `committed` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `used` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8"),

    CREATE_MEMORY_THREAD(SQL_TYPE.CREATE, "CREATE TABLE IF NOT EXISTS `?` (\n" +
            "  `binding_id` bigint(20) NOT NULL,\n" +
            "  `timestamp` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `active` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `daemon` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `peak` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `locked` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8"),

    CREATE_MEMORY_CLASS_LOAD(SQL_TYPE.CREATE, "CREATE TABLE IF NOT EXISTS `?` (\n" +
            "  `binding_id` bigint(20) NOT NULL,\n" +
            "  `timestamp` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `loaded` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `total` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `unload` bigint(20) NOT NULL DEFAULT '0',\n" +
            "  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8");

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
