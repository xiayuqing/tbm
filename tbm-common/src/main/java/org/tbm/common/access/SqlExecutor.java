package org.tbm.common.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public abstract class SqlExecutor<T> {
    protected List<T> result;
    private Connection connection;
    private SqlTemplate sqlTemplate;
    private List<Object> args;

    public SqlExecutor(Connection connection, SqlTemplate sqlTemplate, List<Object> args) {
        this.connection = connection;
        this.sqlTemplate = sqlTemplate;
        this.args = args;
    }

    public List<T> run() throws Exception {
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement(sqlTemplate.sql);
        build(ps, sqlTemplate, args);
//        SqlAssembler.build(ps, sqlTemplate.sql, args);
        try {
            switch (sqlTemplate.type) {
                case SqlTemplate.SQL_TYPE.CREATE:
                    ps.executeBatch();
                    break;
                case SqlTemplate.SQL_TYPE.RETRIEVE:
                    convertResult(ps.executeQuery());
                    break;
                case SqlTemplate.SQL_TYPE.UPDATE:
                    break;
                case SqlTemplate.SQL_TYPE.DELETE:
                    break;
                default:
                    throw new SQLException("unknown data manipulation language");
            }

            ps.close();
            connection.commit();
        } catch (Exception e) {
            if (null != connection) {
                connection.rollback();
            }

            throw e;
        }

        return result;
    }

    /***
     * convert result set to object
     * @param resultSet
     */
    abstract void convertResult(ResultSet resultSet) throws Exception;

    abstract PreparedStatement build(PreparedStatement ps, SqlTemplate sqlTemplate, List<Object> args) throws Exception;

    public List<T> getResult() {
        return result;
    }
}
