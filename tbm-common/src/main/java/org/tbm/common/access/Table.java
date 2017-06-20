package org.tbm.common.access;

/**
 * Created by Jason.Xia on 17/6/19.
 */
public class Table extends Operation {

    public static final String MACHINE_INFO = "MACHINE_INFO";

    public static final String MEMORY_POOL = "MEMORY_POOL";

    public static final String MEMORY_SUMMARY = "MEMORY_SUMMARY";

    public static final String THREAD = "THREAD";

    public static final String CLASS_LOAD = "CLASS_LOAD";

    public static final String BUSINESS = "BUSINESS";

//    private String baseName;
//
//    private ShardingUnits units;
//
//    private String schemaStr;

    public Table() {
    }

    public Table(String baseName, ShardingUnits units, String schemaStr) {
        this.baseName = baseName;
        this.units = units;
        this.schemaStr = schemaStr;
        this.ops = DB_OPS.CREATE;
    }


}
