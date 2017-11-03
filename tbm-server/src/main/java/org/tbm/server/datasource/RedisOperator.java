package org.tbm.server.datasource;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Jason.Xia on 17/7/31.
 */
@Named
public class RedisOperator {
    private static RedisOperator holder;
    @Inject
    private RedisTemplate<String, String> redisCache;

    public RedisOperator() {
    }

    public static boolean existKey(String key) {
        if (null == holder) {
            throw new IllegalStateException("RedisHolder Uninitialized");
        }

        return holder.redisCache.hasKey(key);
    }

    public static RedisTemplate<String, String> getRedisCache() {
        return holder.redisCache;
    }

    @PostConstruct
    public void init() {
        holder = this;
    }
}
