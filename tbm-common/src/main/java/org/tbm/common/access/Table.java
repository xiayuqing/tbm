package org.tbm.common.access;

/**
 * Created by Jason.Xia on 17/6/19.
 */
public class Table extends Operation {
    public Table() {
    }

    public Table(String baseName, ShardingUnits units, String schemaStr) {
        this.baseName = baseName;
        this.units = units;
        this.schemaStr = schemaStr;
//        this.ops = DB_OPS.CREATE;
    }


}
