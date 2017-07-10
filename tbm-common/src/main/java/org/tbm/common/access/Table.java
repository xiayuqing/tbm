package org.tbm.common.access;

/**
 * Created by Jason.Xia on 17/6/19.
 */
public class Table extends Operation {
    public static final String MACHINE_BINDING = "MACHINE_BINDING";

    public static final String SYSTEM_INFO = "SYSTEM_INFO";

    public static final String MACHINE_LOG = "MACHINE_LOG";

    public static final String MEMORY_POOL = "MEMORY_POOL";

    public static final String MEMORY_SUMMARY = "MEMORY_SUMMARY";

    public static final String THREAD = "THREAD";

    public static final String CLASS_LOAD = "CLASS_LOAD";

    public static final String BIZ = "BIZ";

    public Table() {
    }

    public Table(String baseName, ShardingUnits units, String schemaStr) {
        this.baseName = baseName;
        this.units = units;
        this.schemaStr = schemaStr;
//        this.ops = DB_OPS.CREATE;
    }


}
