package org.tbm.server.collect;

import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlAssembler;
import org.tbm.common.access.SqlExecutor;
import org.tbm.common.bean.vo.JvmData;
import org.tbm.common.utils.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class JvmDataSqlExecutor extends SqlExecutor<JvmData> {
    public JvmDataSqlExecutor(Connection connection, Operation operation, List<Object> args) {
        super(connection, operation, args);
    }

    @Override
    protected void convertResult(ResultSet resultSet) throws Exception {
        List<Object> objects = SqlAssembler.convertResult(resultSet, JvmData.class);
        ObjectUtils.convertObject(objects, result);
    }

    @Override
    protected PreparedStatement build(PreparedStatement ps, String sql, List<Object> args) throws
            Exception {
        SqlAssembler.build(ps, sql, args);
        return ps;
    }


}
