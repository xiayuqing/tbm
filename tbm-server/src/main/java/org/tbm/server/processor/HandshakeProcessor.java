package org.tbm.server.processor;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Connection;
import org.tbm.common.Processor;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.WorkNode;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class HandshakeProcessor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(HandshakeProcessor.class);

    @Override
    public PacketLite process(PacketLite packetLite, Connection connection) {
        if (null == packetLite.payload) {
            return PacketLite.createException("payload no data", packetLite.seq);
        }

        WorkNode workNode;
        try {
            workNode = JSON.parseObject(packetLite.payload, WorkNode.class);
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.error("handshake json parse error.{}", e);
            }

            return PacketLite.createException("payload format illegal", packetLite.seq);
        }

        return PacketLite.createHandshakeAck(packetLite.seq, connection.auth(workNode));
    }
}
