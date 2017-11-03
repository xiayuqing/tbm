package org.tbm.server.access;


import org.tbm.common.bean.WorkNode;
import org.tbm.server.sharding.Operation;
import org.tbm.server.sharding.ShardingUnits;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jason.Xia on 17/10/26.
 */
@Named
public class WorkNodeAccessor extends Operation {

    @Inject
    private WorkNodeMapper mapper;

    @PostConstruct
    public void init() {
        this.setBaseName("work_node");
        this.setUnits(ShardingUnits.SINGLETON);
        this.setSchema("CREATE TABLE IF NOT EXISTS ${tableName} ( `identity` char(32) NOT NULL DEFAULT '0',  `host` " +
                "varchar(50) NOT NULL DEFAULT '0',  `address` char(15) NOT NULL DEFAULT '127.0.0.1',  `status` " +
                "tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:offline;1:online',  `os` varchar(100) NOT NULL DEFAULT " +
                "'0',  `version` varchar(50) NOT NULL DEFAULT '0',  `arch` varchar(100) NOT NULL DEFAULT '0',  " +
                "`user_name` varchar(100) NOT NULL,  `user_home` varchar(300) NOT NULL,  `user_dir` varchar(300) NOT " +
                "NULL,  `java_version` varchar(300) NOT NULL,  `java_home` varchar(300) NOT NULL,  `created_at` " +
                "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,  PRIMARY KEY (`identity`, `host`, `address`) ) ENGINE =" +
                " InnoDB CHARSET = utf8");
    }

    public WorkNode select(String identity) {
        return this.mapper.select(identity);
    }

    public int insert(WorkNode node) {
        return this.mapper.insert(node);
    }

    public int updateStatus(String identity, int status) {
        return this.mapper.updateStatus(identity, status);
    }

    @Override
    public void create(boolean nextSchema) {
        this.mapper.create(this);
    }
}
