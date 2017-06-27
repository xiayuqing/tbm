package org.tbm.server.sharding;

import org.tbm.common.access.Table;
import org.tbm.common.utils.JsonConfigReader;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/6/15.
 */
public class ShardingConfig {
    private static ShardingConfig config;

    private Map<String, Table> tableMap = new HashMap<>();

    public static void load(String path) {
        if (null == path || 0 == path.length()) {
            throw new IllegalArgumentException("Not found config file path :" + path);
        }

        String file = path;
        if (!path.endsWith(".json")) {
            file = path + (path.endsWith("/") ? "table.json" : "/table.json");
        }

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
            Map<String, Table> map = new HashMap<>();
            List<Table> list = JsonConfigReader.readerArray(file, Table.class);
            for (Table item : list) {
                map.put(item.getName(), item);
            }
            
            config.setTableMap(map);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
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
