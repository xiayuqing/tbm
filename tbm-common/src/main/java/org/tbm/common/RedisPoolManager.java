package org.tbm.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Jason.Xia on 17/7/31.
 */
public class RedisPoolManager {
    private static JedisPool pool;

    public static Jedis getJedis() {
        if (null == pool) {
            throw new IllegalStateException("JedisPool not be initialized.");
        }

        try {
            return pool.getResource();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static void returnJedis(Jedis jedis) {
        if (null == pool) {
            throw new IllegalStateException("JedisPool not be initialized.");
        }

        try {
            if (null != jedis) {
                pool.returnResource(jedis);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Deprecated
    public static void returnBrokenJedis(Jedis jedis) {
        if (null == pool) {
            throw new IllegalStateException("JedisPool not be initialized.");
        }

        try {
            pool.returnBrokenResource(jedis);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static void releaseJedis(Jedis jedis) {
        returnJedis(jedis);
    }

    public static void init() {
        pool = RedisPoolFactory.create();
    }
}
