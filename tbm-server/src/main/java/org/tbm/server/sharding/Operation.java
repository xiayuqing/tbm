package org.tbm.server.sharding;


import org.tbm.common.Serialize;
import org.tbm.common.util.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jason.Xia on 17/10/25.
 */
public abstract class Operation extends Serialize {

    protected String baseName;

    protected ShardingUnits units;

    protected String schema;

    public String getNextSchema() {
        return schema.replace("${tableName}", getNextName());
    }

    public String getCurrentSchema() {
        return schema.replace("${tableName}", getCurrentName());
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
                return Utils.getDateString(time, "yyyyMMddHH");
            case DAY:
                return Utils.getDateString(time, "yyyyMMdd");
            case MONTH:
                return Utils.getDateString(time, "yyyyMM");
            case YEAR:
                return Utils.getDateString(time, "yyyy");
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

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public abstract void create(boolean nextSchema);
}