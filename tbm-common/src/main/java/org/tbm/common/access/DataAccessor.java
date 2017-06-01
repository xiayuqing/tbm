package org.tbm.common.access;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.tbm.common.AppContext;
import org.tbm.common.MemoryType;
import org.tbm.common.bean.vo.PackageData;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jason.Xia on 17/5/26.
 */
public class DataAccessor {

    private DruidDataSource dataSource;
    private SqlAssembler sqlAssembler;

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

        sqlAssembler = new SqlAssembler();
    }

    public static void main(String[] args) throws Exception {
//        DataAccessor instance = DataAccessorFactory.getInstance();
//        instance.select(new HostInfo(1000, "122", 11));
    }

    public void insert(String sql, List<Object> args) throws SQLException {
        DruidPooledConnection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            sqlAssembler.build(ps, args);
            ps.execute();
            ps.close();
            connection.commit();
        } catch (Exception e) {
            if (null != connection) {
                connection.rollback();
            }

            throw e;
        }
    }

    public void insertBatch(List<PackageData> data) throws SQLException {
        DruidPooledConnection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(SqlTemplate.INSERT_MEMORY_SUMMARY);
            for (PackageData item : data) {
                ps.setLong(1, item.getBindingId());
                ps.setLong(2, MemoryType.SUMMARY_HEAP);
                ps.setLong(3, item.getMemorySummaryInfo().getHeapInfo().getMax());
                ps.setLong(4, item.getMemorySummaryInfo().getHeapInfo().getInit());
                ps.setLong(5, item.getMemorySummaryInfo().getHeapInfo().getCommitted());
                ps.setLong(6, item.getMemorySummaryInfo().getHeapInfo().getUsed());
                ps.addBatch();
            }

            ps.executeBatch();
            ParameterMetaData parameterMetaData = ps.getParameterMetaData();
            ps.close();
            connection.commit();
        } catch (Exception e) {
            if (null != connection) {
                connection.rollback();
            }

            throw e;
        }

    }

    public List<Object> select(String sql, List<Object> args, Class resultType) throws Exception {
        DruidPooledConnection connection = null;
        List<Object> result;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            sqlAssembler.build(ps, args);
            ResultSet resultSet = ps.executeQuery();
            result = sqlAssembler.convertResult(resultSet, resultType);
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
}
