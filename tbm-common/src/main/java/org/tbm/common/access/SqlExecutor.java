package org.tbm.common.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public abstract class SqlExecutor<T> {
    protected List<T> result;
    private Connection connection;
    private Operation operation;
    private String currentSql;
    private List<Object> args;

    public SqlExecutor() {
    }

    public SqlExecutor(Connection connection, Operation operation, List<Object> args) {
        this.connection = connection;
        this.operation = operation;
        this.args = args;
        this.currentSql = operation.getCurrentSql();
    }

    public List<T> run() throws Exception {
        if (null == connection) {
            throw new NullPointerException("connection not be null");
        }

        if (null == operation) {
            throw new NullPointerException("operation not be null");
        }

        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement(currentSql);
        build(ps, currentSql, args);
        try {
            switch (operation.type) {
                case CREATE:
                    ps.executeBatch();
                    break;
                case RETRIEVE:
                    result = new ArrayList<>();
                    convertResult(ps.executeQuery());
                    break;
                case UPDATE:
                    ps.executeBatch();
                    break;
                case DELETE:
                    break;
                default:
                    throw new SQLException("unknown data manipulation language");
            }

            connection.commit();
        } catch (Exception e) {
            if (null != connection) {
                connection.rollback();
            }

            e.printStackTrace();
            throw e;
        } finally {
            ps.close();
            connection.close();
        }

        return result;
    }

    /***
     * convert result set to object
     * @param resultSet
     */
    protected abstract void convertResult(ResultSet resultSet) throws Exception;

    protected abstract PreparedStatement build(PreparedStatement ps, String sql, List<Object> args)
            throws Exception;

    public List<T> getResult() {
        return result;
    }

    public void setArguments(List<Object> args, Operation operation) {
        this.args = args;
        this.operation = operation;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * safe method
     *
     * @param args
     * @param operation
     * @return
     */
    public List<T> setAndRun(List<Object> args, Operation operation, Connection connection) throws Exception {
        synchronized (this) {
            this.connection = connection;
            this.args = args;
            this.operation = operation;
            this.currentSql = operation.getCurrentSql();
            return run();
        }
    }
}
