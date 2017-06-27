package org.tbm.server.executor;

import org.tbm.common.access.Operation;
import org.tbm.common.access.SqlAssembler;
import org.tbm.common.access.SqlExecutor;
import org.tbm.common.bean.MachineBinding;
import org.tbm.common.utils.ObjectUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/20.
 */
public class MachineInfoSqlExecutor extends SqlExecutor<MachineBinding> {

    public MachineInfoSqlExecutor() {
    }

    public MachineInfoSqlExecutor(Connection connection, Operation operation) {
        super(connection, operation, null);
    }

    public MachineInfoSqlExecutor(Connection connection, Operation operation, List<Object> args) {
        super(connection, operation, args);
    }

    @Override
    protected void convertResult(ResultSet resultSet) throws Exception {
        List<Object> objects = SqlAssembler.convertResult(resultSet, MachineBinding.class);
        ObjectUtils.convertObject(objects, result);
    }

    @Override
    protected PreparedStatement build(PreparedStatement ps, String sql, List<Object> args) throws
            Exception {
        SqlAssembler.build(ps, sql, args);
        return ps;
    }
}
