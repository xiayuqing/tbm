package org.tbm.server.sharding;

import org.tbm.common.access.Table;
import org.tbm.common.utils.JsonConfigReader;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/15.
 */
public class ShardingConfig {
    private static ShardingConfig config;

    private Map<String, Table> tableMap = new HashMap<>();

    public static void load(String path) {
//        Properties properties = new Properties();
        if (null == path || 0 == path.length()) {
            throw new IllegalArgumentException("Not found config file path :" + path);
        }

        String file = path;
        if (!path.endsWith(".json")) {
            file = path + (path.endsWith("/") ? "table.json" : "/table.json");
        }

//        try {
//            properties.load(new FileInputStream(file));
//        } catch (IOException e) {
//            throw new IllegalStateException(e.getMessage(), e);
//        }

        createConfig(file);
    }

    static ShardingConfig getConfig() {
        if (null == config) {
            URL resource = ShardingConfig.class.getResource("/table.json");
            load(resource.getFile());
        }

        return config;
    }

    private static void createConfig(String file) {
        config = new ShardingConfig();

        try {
            Map<String, Table> map = JsonConfigReader.readerObject(file, Table.class);
            config.setTableMap(map);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

//        config.putTableConfig(Table.MACHINE_INFO, new Table(Table.MACHINE_INFO, ShardingUnits.SINGLETON,
//                schema.getProperty("MACHINE_INFO")));
//
//        String pool = AppContext.getString("sharding.memory.pool", ShardingUnits.DAY.toString());
//        config.putTableConfig(Table.MEMORY_POOL, new Table(Table.MEMORY_POOL, ShardingUnits.valueOf(pool),
//                schema.getProperty("MEMORY_POOL")));
//
//        String summary = AppContext.getString("sharding.memory.summary", ShardingUnits.DAY.toString());
//        config.putTableConfig(Table.MEMORY_SUMMARY, new Table(Table.MEMORY_SUMMARY, ShardingUnits.valueOf
//                (summary), schema.getProperty("MEMORY_SUMMARY")));
//
//        String thread = AppContext.getString("sharding.thread", ShardingUnits.DAY.toString());
//        config.putTableConfig(Table.THREAD, new Table(Table.THREAD, ShardingUnits.valueOf(thread), schema
//                .getProperty("THREAD")));
//
//        String classload = AppContext.getString("sharing.classload", ShardingUnits.DAY.toString());
//        config.putTableConfig(Table.CLASS_LOAD, new Table(Table.CLASS_LOAD, ShardingUnits.valueOf(classload),
//                schema.getProperty("CLASS_LOAD")));

//        String business = AppContext.getString("sharding.business", ShardingUnits.DAY.toString());
//        config.putTableConfig(Table.BUSINESS, new Table(Table.BUSINESS, ShardingUnits.valueOf(business),
//                schema.getProperty("BUSINESS")));
    }

    public void putTableConfig(String table, Table config) {
        tableMap.put(table, config);
    }

    public Table getTableConfig(String table) {
        return tableMap.get(table);
    }

    Map<String, Table> getTableMap() {
        return tableMap;
    }

    private void setTableMap(Map<String, Table> tableMap) {
        this.tableMap = tableMap;
    }
}
