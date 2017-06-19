package org.tbm.server.sharding;

import org.tbm.common.access.ShardingTable;
import org.tbm.common.access.ShardingUnits;
import org.tbm.common.access.SqlTemplate;
import org.tbm.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/6/19.
 */
public class SchemaSharding {

    public static String createShardingTableSql(ShardingTable table, ShardingUnits units) {
        String sql;
        switch (table) {
            case MACHINE_INFO:
                sql = SqlTemplate.CREATE_MACHINE_INFO.sql;
                break;
            case MEMORY_POOL:
                sql = SqlTemplate.CREATE_MEMORY_POOL.sql;
                break;
            case MEMORY_SUMMARY:
                sql = SqlTemplate.CREATE_MEMORY_SUMMARY.sql;
                break;
            case CLASS_LOAD:
                sql = SqlTemplate.CREATE_MEMORY_CLASS_LOAD.sql;
                break;
            case THREAD:
                sql = SqlTemplate.CREATE_MEMORY_THREAD.sql;
                break;
            case BUSINESS:
                throw new IllegalArgumentException("unimplemented");
            default:
                throw new IllegalArgumentException("unknown table type");
        }

        switch (units) {
            case SINGLETON:
                return sql.replace("?", table.name());
            case HOUR:
                return sql.replace("?", table.name() + DateUtils.getDateString(new Date(), "yyyyMMddHH"));
            case DAY:
                return sql.replace("?", table.name() + DateUtils.getDateString(new Date(), "yyyyMMdd"));
            case MONTH:
                return sql.replace("?", table.name() + DateUtils.getDateString(new Date(), "yyyyMM"));
            case YEAR:
                return sql.replace("?", table.name() + DateUtils.getDateString(new Date(), "yyyy"));
            case HASH:
                throw new IllegalArgumentException("unimplemented");
            default:
                throw new IllegalArgumentException("unknown Sharding Units");
        }
    }
}
