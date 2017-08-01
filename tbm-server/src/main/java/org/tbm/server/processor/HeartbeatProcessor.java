package org.tbm.server.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Connection;
import org.tbm.common.Processor;
import org.tbm.common.bean.PacketLite;

/**
 * Created by Jason.Xia on 17/7/4.
 */
public class HeartbeatProcessor implements Processor {
    private Logger logger = LoggerFactory.getLogger(HeartbeatProcessor.class);

    @Override
    public PacketLite process(PacketLite packetLite, Connection connection) {
        logger.debug("received heartbeat from {}", packetLite.payload);
        connection.updateLastReadTime();
        return null;
    }
}
