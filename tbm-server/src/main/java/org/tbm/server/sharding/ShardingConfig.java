package org.tbm.server.sharding;

import org.tbm.common.AppContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/15.
 */
public class ShardingConfig {
    private static ShardingConfig config;
    private Map<ShardingTable, ShardingUnits> shardingUnitsMap = new HashMap<>();

    public static ShardingConfig getConfig() {
        if (null == config) {
            config = ShardingConfig.createConfig();
        }

        return config;
    }

    private static ShardingConfig createConfig() {
        ShardingConfig shardingConfig = new ShardingConfig();
        String pool = AppContext.getString("sharding.memory.pool", ShardingUnits.DAY.toString());
        shardingConfig.putShardingUnit(ShardingTable.MEMORY_POOL, ShardingUnits.valueOf(pool));

        String summary = AppContext.getString("sharding.memory.summary", ShardingUnits.DAY.toString());
        shardingConfig.putShardingUnit(ShardingTable.MEMORY_SUMMARY, ShardingUnits.valueOf(summary));

        String thread = AppContext.getString("sharding.thread", ShardingUnits.DAY.toString());
        shardingConfig.putShardingUnit(ShardingTable.THREAD, ShardingUnits.valueOf(thread));

        String classload = AppContext.getString("sharing.classload", ShardingUnits.DAY.toString());
        shardingConfig.putShardingUnit(ShardingTable.CLASS_LOAD, ShardingUnits.valueOf(classload));

        String business = AppContext.getString("sharding.business", ShardingUnits.DAY.toString());
        shardingConfig.putShardingUnit(ShardingTable.BUSINESS, ShardingUnits.valueOf(business));
        return shardingConfig;
    }

    public void putShardingUnit(ShardingTable table, ShardingUnits units) {
        shardingUnitsMap.put(table, units);
    }

    public Map<ShardingTable, ShardingUnits> getShardingUnitsMap() {
        return shardingUnitsMap;
    }

    public void setShardingUnitsMap(Map<ShardingTable, ShardingUnits> shardingUnitsMap) {
        this.shardingUnitsMap = shardingUnitsMap;
    }

    public enum ShardingTable {
        MEMORY_POOL,
        MEMORY_SUMMARY,
        THREAD,
        CLASS_LOAD,
        BUSINESS
    }
}
