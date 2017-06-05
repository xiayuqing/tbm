package org.tbm.common.access;

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
    public JvmDataSqlExecutor(Connection connection, SqlTemplate sqlTemplate, List<Object> args) {
        super(connection, sqlTemplate, args);
    }

    @Override
    void convertResult(ResultSet resultSet) throws Exception {
        List<Object> objects = SqlAssembler.convertResult(resultSet, JvmData.class);
        ObjectUtils.convertObject(objects, result);
    }

    @Override
    PreparedStatement build(PreparedStatement ps, SqlTemplate sqlTemplate, List<Object> args) throws Exception {
        SqlAssembler.build(ps, sqlTemplate.sql, args);
        return ps;
    }


}
