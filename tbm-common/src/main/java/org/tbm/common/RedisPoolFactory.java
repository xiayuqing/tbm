package org.tbm.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Jason.Xia on 17/2/22.
 */
public class RedisPoolFactory {
    private static final Logger logger = LoggerFactory.getLogger(RedisPoolFactory.class);

    public static JedisPool create() {
        return newInstance();
    }

    private static JedisPool newInstance() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(AppContext.getInt("redis.pool.maxTotal", 1000));
        config.setMinIdle(AppContext.getInt("redis.pool.minIdle", 10));
        config.setMaxIdle(AppContext.getInt("redis.pool.maxIdle", 100));
        config.setTestOnBorrow(AppContext.getBoolean("redis.pool.testOnBorrow", true));
        config.setTestOnReturn(AppContext.getBoolean("redis.pool.testOnReturn", true));
        String host = AppContext.getString("redis.ip", "localhost");
        int port = AppContext.getInt("redis.port", 6379);
        logger.info("Initialized JedisPool success! host:{},config:{}", host + ":" + port, config);
        return new JedisPool(config, host, port, 10, AppContext.getString("redis.password"), AppContext.getInt("redis" +
                ".database.index", 0));
    }


}
