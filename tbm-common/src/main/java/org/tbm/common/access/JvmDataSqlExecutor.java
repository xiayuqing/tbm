package org.tbm.common.access;

import org.tbm.common.bean.vo.JvmData;
import org.tbm.common.utils.ObjectUtil;

import java.sql.Connection;
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
        ObjectUtil.convertObject(objects, result);
    }
}
