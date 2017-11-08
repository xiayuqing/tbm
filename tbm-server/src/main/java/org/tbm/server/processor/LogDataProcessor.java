package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.tbm.common.Connection;
import org.tbm.common.Processor;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.WorkNode;
import org.tbm.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/10/26.
 */
public class LogDataProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(LogDataProcessor.class);
    private RedisTemplate<String, String> redisCache;

    public LogDataProcessor(RedisTemplate<String, String> redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public PacketLite process(PacketLite packetLite, Connection connection) {
        connection.updateLastReadTime();
        JSONObject payload = JSON.parseObject(packetLite.payload);
        if (Utils.isEmpty(payload.getString("data"))) {
            return PacketLite.createAck(packetLite.seq);
        }

        try {
            WorkNode workNode = connection.getWorkNode();
            if (null == workNode) {
                logger.warn("Unknown WorkNode.Discard LogData.channel:{}", connection.getChannel().id().asShortText());
                return PacketLite.createAck(packetLite.seq);
            }

//            String identity = payload.getString("identity");
            String identity = workNode.getIdentity() + "-" + workNode.getAddress();
            List<String> insert = new ArrayList<>();
            for (Object item : payload.getJSONArray("data")) {
                insert.add(item.toString());
            }

            redisCache.opsForList().rightPushAll(identity, insert);
        } catch (Exception e) {
            logger.error("Request Log Insert Cache Error.", e);
            logger.info("{}", packetLite.payload);
            PacketLite.createError(e.getMessage(), packetLite.seq);
        }

        return PacketLite.createAck(packetLite.seq);
    }
}
