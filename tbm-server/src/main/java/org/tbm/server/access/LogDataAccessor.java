package org.tbm.server.access;


import org.tbm.common.bean.LogData;
import org.tbm.server.TbmContext;
import org.tbm.server.sharding.Operation;
import org.tbm.server.sharding.ShardingUnits;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by Jason.Xia on 17/10/25.
 */
@Named
public class LogDataAccessor extends Operation {

    @Inject
    private LogDataMapper mapper;

    @PostConstruct
    public void init() {
        this.setBaseName("log");
        this.setUnits(ShardingUnits.valueOf(TbmContext.getString("table.log.unit")));
        this.setSchema("CREATE TABLE IF NOT EXISTS ${tableName} ( `identity` char(32) NOT NULL,  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,  `host` char(15) NOT NULL,  `level` int(11) NOT NULL,  `trace` char(100) NOT NULL,  `clazz` varchar(300) NOT NULL,  `method` varchar(100) NOT NULL,  `line` int(11) NOT NULL,  `content` text,  `persist_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ) ENGINE = ARCHIVE CHARSET = utf8");
    }

    public int insert(List<LogData> list) {
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