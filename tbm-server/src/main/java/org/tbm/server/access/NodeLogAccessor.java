package org.tbm.server.access;


import org.tbm.common.bean.NodeLog;
import org.tbm.server.sharding.Operation;
import org.tbm.server.sharding.ShardingUnits;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Jason.Xia on 17/10/26.
 */
@Named
public class NodeLogAccessor extends Operation {

    @Inject
    private NodeLogMapper mapper;

    @PostConstruct
    public void init() {
        this.setBaseName("node_log");
        this.setUnits(ShardingUnits.SINGLETON);
        this.setSchema("CREATE TABLE IF NOT EXISTS ${tableName} ( `identity` char(32) NOT NULL DEFAULT '0', `path` " +
                "varchar(200) NOT NULL DEFAULT 'Unknown', `host` varchar(50) NOT NULL DEFAULT '0',  `address` char" +
                "(15) NOT NULL DEFAULT '127.0.0.1',  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:offline;" +
                "1:online',  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP) ENGINE = InnoDB CHARSET = utf8");
    }

    public List<NodeLog> select(String identity) {
        return this.mapper.select(identity);
    }

    public int insert(NodeLog log) {
        return this.mapper.insert(log);
    }

    @Override
    public void create(boolean nextSchema) {
        this.mapper.create(this);
    }
}
