package org.tbm.server.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.tbm.server.TbmContext;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * Created by Jason.Xia on 17/2/22.
 */
public class InnerRedisPool extends JedisConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(InnerRedisPool.class);

    @PostConstruct
    private void init() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(TbmContext.getInt("redis.max.total"));
        config.setMinIdle(TbmContext.getInt("redis.min.idle"));
        config.setMaxIdle(TbmContext.getInt("redis.max.idle"));
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        String host = TbmContext.getString("redis.host");
        int port = TbmContext.getInt("redis.port");
        int index = TbmContext.getInt("redis.index");
        logger.info("Initialized JedisPool success! host:{},index:{},config:{}", host + ":" + port, index, config);

        this.setPoolConfig(config);
        this.setHostName(host);
        this.setPort(port);
        String pwd = TbmContext.getString("redis.password");
        if (null != pwd) {
            this.setPassword(pwd);
        }

        this.setDatabase(index);
        this.afterPropertiesSet();
    }
}
