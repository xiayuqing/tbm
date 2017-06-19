package org.tbm.server.sharding;

import org.tbm.common.AppContext;
import org.tbm.common.access.ShardingUnits;
import org.tbm.common.access.Table;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Jason.Xia on 17/6/15.
 */
public class ShardingConfig {
    private static ShardingConfig config;

    private Map<String, Table> tableMap = new HashMap<>();

    public static void load(String path) {
        Properties properties = new Properties();
        if (null == path || 0 == path.length()) {
            throw new IllegalArgumentException("Not found config file path :" + path);
        }

        String file = path;
        if (!path.endsWith(".properties")) {
            file = path + (path.endsWith("/") ? "schema.properties" : "/schema.properties");
        }

        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        createConfig(properties);
    }

    public static ShardingConfig getConfig() {
        if (null == config) {
            URL resource = ShardingConfig.class.getResource("/schema.properties");
            load(resource.getFile());
        }

        return config;
    }

    private static ShardingConfig createConfig(Properties schema) {
        ShardingConfig shardingConfig = new ShardingConfig();

        shardingConfig.putTableConfig(Table.MACHINE_INFO, new Table(Table.MACHINE_INFO, ShardingUnits.SINGLETON,
                schema.getProperty("MACHINE_INFO")));

        String pool = AppContext.getString("sharding.memory.pool", ShardingUnits.DAY.toString());
        shardingConfig.putTableConfig(Table.MEMORY_POOL, new Table(Table.MEMORY_POOL, ShardingUnits.valueOf(pool),
                schema.getProperty("MEMORY_POOL")));

        String summary = AppContext.getString("sharding.memory.summary", ShardingUnits.DAY.toString());
        shardingConfig.putTableConfig(Table.MEMORY_SUMMARY, new Table(Table.MEMORY_SUMMARY, ShardingUnits.valueOf
                (summary), schema.getProperty("MEMORY_SUMMARY")));

        String thread = AppContext.getString("sharding.thread", ShardingUnits.DAY.toString());
        shardingConfig.putTableConfig(Table.THREAD, new Table(Table.THREAD, ShardingUnits.valueOf(thread), schema
                .getProperty("THREAD")));

        String classload = AppContext.getString("sharing.classload", ShardingUnits.DAY.toString());
        shardingConfig.putTableConfig(Table.CLASS_LOAD, new Table(Table.CLASS_LOAD, ShardingUnits.valueOf(classload),
                schema.getProperty("CLASS_LOAD")));

        String business = AppContext.getString("sharding.business", ShardingUnits.DAY.toString());
        shardingConfig.putTableConfig(Table.BUSINESS, new Table(Table.BUSINESS, ShardingUnits.valueOf(business),
                schema.getProperty("BUSINESS")));
        return shardingConfig;
    }

    public void putTableConfig(String table, Table config) {
        tableMap.put(table, config);
    }

    public Table getTableConfig(String table) {
        return tableMap.get(table);
    }

    public Map<String, Table> getTableMap() {
        return tableMap;
    }
}
