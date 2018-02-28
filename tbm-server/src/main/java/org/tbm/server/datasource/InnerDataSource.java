package org.tbm.server.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.tbm.server.TbmContext;

/**
 * Created by Jason.Xia on 17/10/25.
 */
public class InnerDataSource extends DruidDataSource {
    public static InnerDataSource ref;

    public void start() {
        this.setDriverClassName("com.mysql.jdbc.Driver");
        this.setUsername(TbmContext.getString("datasource.username"));
        this.setPassword(TbmContext.getString("datasource.password"));
        this.setUrl(TbmContext.getString("datasource.url"));
        this.setInitialSize(TbmContext.getInt("datasource.init.size"));
        this.setMinIdle(TbmContext.getInt("datasource.min.idle"));
        this.setMaxActive(TbmContext.getInt("datasource.max.active"));
        this.setMaxWait(60000);
        this.setTimeBetweenEvictionRunsMillis(60000);
        this.setPoolPreparedStatements(true);
        this.setMaxPoolPreparedStatementPerConnectionSize(10);

        this.setRemoveAbandoned(TbmContext.getBoolean("datasource.remove.abandoned", false));
        this.setRemoveAbandonedTimeout(120);
        this.setLogAbandoned(TbmContext.getBoolean("datasource.log.abandoned", true));
        ref = this;
    }
}
