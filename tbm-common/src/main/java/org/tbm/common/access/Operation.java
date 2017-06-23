package org.tbm.common.access;

import org.tbm.common.bean.Serialize;
import org.tbm.common.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jason.Xia on 17/6/20.
 */
public class Operation extends Serialize {

    protected String name;

    protected String baseName;

    protected DB_OP_TYPE type;

    protected ShardingUnits units;

    protected String schemaStr;

    public String getNextSql() {
        return schemaStr.replace("${tableName}", getNextName());
    }

    public String getCurrentSql() {
        return schemaStr.replace("${tableName}", getCurrentName());
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
                break;
            case DAY:
                now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);
                break;
            case MONTH:
                now.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
                break;
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

    public void setUnits(ShardingUnits units) {
        this.units = units;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public void setOps(DB_OP_TYPE ops) {
        this.type = type;
    }

    public void setSchemaStr(String schemaStr) {
        this.schemaStr = schemaStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum DB_OP_TYPE {
        CREATE,
        RETRIEVE,
        UPDATE,
        DELETE
    }
}
