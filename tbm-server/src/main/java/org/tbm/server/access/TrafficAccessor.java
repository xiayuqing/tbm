package org.tbm.server.access;


import org.tbm.common.bean.Traffic;
import org.tbm.server.TbmContext;
import org.tbm.server.sharding.Operation;
import org.tbm.server.sharding.ShardingUnits;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Jason.Xia on 17/10/29.
 */
@Named
public class TrafficAccessor extends Operation {

    @Inject
    private TrafficMapper mapper;

    @PostConstruct
    public void init() {
        this.setBaseName("traffic");
        this.setUnits(ShardingUnits.valueOf(TbmContext.getString("table.traffic.unit")));
        this.setSchema("CREATE TABLE IF NOT EXISTS ${tableName} ( `channel` varchar(100) NOT NULL,  `identity` " +
                "varchar(100) NOT NULL, `host` varchar(100) NOT NULL,`address` char(15) NOT NULL, `time` datetime NOT" +
                " NULL DEFAULT CURRENT_TIMESTAMP,  `type` tinyint(4) NOT NULL, `flow` bigint(20) NOT NULL,  `period` " +
                "int(11) NOT NULL ) ENGINE = ARCHIVE CHARSET = utf8");
    }

    public int insert(List<Traffic> list) {
        return this.mapper.insert(this.getCurrentName(), list);
    }

    public void create(boolean nextSchema) {
        if (nextSchema) {
            this.mapper.createNext(this);
        } else {
            this.mapper.create(this);
        }
    }
}
