package org.tbm.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.PacketLite;


/**
 * Created by Jason.Xia on 17/5/27.
 */
public class ClientDispatcher implements Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ClientDispatcher.class);

    @Override
    public PacketLite dispatch(PacketLite packet) {
        if (PacketLite.PACKET_TYPE.ACK == packet.getType()) {
            // nothing to do
            if (logger.isDebugEnabled()) {
                logger.debug("received ACK from server.");
            }

        }

        return null;
    }
}
