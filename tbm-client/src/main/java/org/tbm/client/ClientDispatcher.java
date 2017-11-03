package org.tbm.client;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Connection;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.WorkNode;
import org.tbm.common.bean.PacketLite;


/**
 * Created by Jason.Xia on 17/5/27.
 */
public class ClientDispatcher implements Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ClientDispatcher.class);

    @Override
    public void dispatch(Connection connection, PacketLite packet) {
        if (PacketLite.PACKET_TYPE.ACK == packet.type) {
            // nothing to do
            if (logger.isDebugEnabled()) {
                logger.debug("received ACK from server.");
            }

            return;
        }

        if (PacketLite.PACKET_TYPE.HANDSHAKE == packet.type) {
            WorkNode hostInfo = JSON.parseObject(packet.payload, WorkNode.class);
            if (null == hostInfo) {
                throw new IllegalStateException("tbm client handshake error.cause:server receive binding info is null");
            }

            return;
        }

        logger.info("no match type, discard msg:{}", packet);
    }
}
