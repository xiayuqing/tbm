package org.tbm.common.access;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.tbm.common.AppContext;
import org.tbm.common.MemoryType;
import org.tbm.common.bean.vo.JvmData;

import java.sql.*;
import java.util.List;

/**
 * Created by Jason.Xia on 17/5/26.
 */
public class DataAccessor {

    private DruidDataSource dataSource;

    public DataAccessor() {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(AppContext.getString("datasource.driver", "com.mysql.jdbc.Driver"));
        dataSource.setUsername(AppContext.getString("datasource.username", "root"));
        dataSource.setPassword(AppContext.getString("datasource.password", "123"));
        dataSource.setUrl(AppContext.getString("datasource.url",
                "jdbc:mysql://localhost:3306/tbm?useUnicode=true&amp;characterEncoding=UTF-8&allowMultiQueries=true"));
        dataSource.setInitialSize(AppContext.getInt("datasource.initial.size", 10));
        dataSource.setMinIdle(AppContext.getInt("datasource.min.idle", 5));
        dataSource.setMaxActive(AppContext.getInt("datasource.max.active", 30));
        dataSource.setMaxWait(AppContext.getInt("datasource.max.wait", 60000));
        dataSource.setTimeBetweenEvictionRunsMillis(AppContext.getInt("datasource.eviction.time", 60000));
        dataSource.setPoolPreparedStatements(AppContext.getBoolean("datasource.prepared.statement", true));
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(AppContext.getInt("datasource.ps.conn.size", 30));
        try {
            dataSource.setFilters(AppContext.getString("datasource.filter", "stat"));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void createTable(List<String> sqls) throws SQLException {
        DruidPooledConnection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            for (String item : sqls) {
                statement.addBatch(item);
            }

            statement.executeBatch();
            statement.close();
            connection.close();
        } catch (Exception e) {
            if (null != connection) {
                connection.rollback();
            }

            throw e;
        }
    }
}
