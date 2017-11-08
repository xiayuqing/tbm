package org.tbm.server.sharding;


import org.tbm.server.SpringContainer;
import org.tbm.server.access.LogDataAccessor;
import org.tbm.server.access.NodeLogAccessor;
import org.tbm.server.access.TrafficAccessor;
import org.tbm.server.access.WorkNodeAccessor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jason.Xia on 17/10/25.
 */
public class ShardingInventory {

    private static Set<Operation> shardingInventory = new HashSet<>();

    static {
        shardingInventory.add((WorkNodeAccessor) SpringContainer.getBean(WorkNodeAccessor.class));
        shardingInventory.add((NodeLogAccessor) SpringContainer.getBean(NodeLogAccessor.class));
        shardingInventory.add((LogDataAccessor) SpringContainer.getBean(LogDataAccessor.class));
        shardingInventory.add((TrafficAccessor) SpringContainer.getBean(TrafficAccessor.class));
    }

    public static Set<Operation> getShardingInventory() {
        return shardingInventory;
    }
}
