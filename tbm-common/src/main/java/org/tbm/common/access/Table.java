package org.tbm.common.access;

import org.tbm.common.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jason.Xia on 17/6/19.
 */
public class Table {

    public static final String MACHINE_INFO = "MACHINE_INFO";

    public static final String MEMORY_POOL = "MEMORY_POOL";

    public static final String MEMORY_SUMMARY = "MEMORY_SUMMARY";

    public static final String THREAD = "THREAD";

    public static final String CLASS_LOAD = "CLASS_LOAD";

    public static final String BUSINESS = "BUSINESS";

    private String baseName;

    private ShardingUnits units;

    private String schemaStr;

    public Table() {
    }

    public Table(String baseName, ShardingUnits units, String schemaStr) {
        this.baseName = baseName;
        this.units = units;
        this.schemaStr = schemaStr;
    }

    public String getNextSql() {
        return schemaStr.replace("?", getNextName());
    }

    public String getCurrentSql() {
        return schemaStr.replace("?", getCurrentName());
    }

    public String getCurrentName() {
        return getNameByTime(new Date());
    }

    public String getNextName() {
        return getNameByTime(getNextUnitTime());
    }

    public String getNameByTime(Date time) {
        return baseName + getSuffix(time, units);
    }

    private Date getNextUnitTime() {
        Calendar now = Calendar.getInstance();
        switch (units) {
            case SINGLETON:
                break;
            case HOUR:
                now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) + 1);
            case DAY:
                now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
            case MONTH:
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
            case YEAR:
                now.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
        }

        return now.getTime();
    }

    private String getSuffix(Date time, ShardingUnits units) {
        switch (units) {
            case SINGLETON:
                return "";
            case HOUR:
                return DateUtils.getDateString(time, "yyyyMMddHH");
            case DAY:
                return DateUtils.getDateString(time, "yyyyMMdd");
            case MONTH:
                return DateUtils.getDateString(time, "yyyyMM");
            case YEAR:
                return DateUtils.getDateString(time, "yyyy");
            case HASH:
                throw new IllegalArgumentException("unimplemented");
            default:
                throw new IllegalArgumentException("unknown Sharding Units");
        }
    }

    public ShardingUnits getUnits() {
        return units;
    }
}
