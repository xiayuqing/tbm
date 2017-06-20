package org.tbm.server.collect;

import org.tbm.common.access.SqlAssembler;
import org.tbm.common.access.SqlExecutor;
import org.tbm.common.access.SqlTemplate;
import org.tbm.common.bean.HostInfo;
import org.tbm.common.utils.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/20.
 */
public class MachineInfoSqlExecutor extends SqlExecutor<HostInfo> {
    public MachineInfoSqlExecutor(Connection connection, SqlTemplate sqlTemplate, List<Object> args) {
        super(connection, sqlTemplate, args);
    }

    @Override
    protected void convertResult(ResultSet resultSet) throws Exception {
        List<Object> objects = SqlAssembler.convertResult(resultSet, HostInfo.class);
        ObjectUtils.convertObject(objects, result);
    }

    @Override
    protected PreparedStatement build(PreparedStatement ps, SqlTemplate sqlTemplate, List<Object> args) throws
            Exception {
        SqlAssembler.build(ps, sqlTemplate.sql, args);
        return ps;
    }
}
