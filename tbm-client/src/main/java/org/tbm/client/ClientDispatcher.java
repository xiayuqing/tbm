package org.tbm.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.MachineInfo;
import org.tbm.common.bean.PacketLite;


/**
 * Created by Jason.Xia on 17/5/27.
 */
public class ClientDispatcher implements Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ClientDispatcher.class);

    @Override
    public void dispatch(ChannelHandlerContext ctx, PacketLite packet) {
        if (PacketLite.PACKET_TYPE.ACK == packet.type) {
            // nothing to do
            if (logger.isDebugEnabled()) {
                logger.debug("received ACK from server.");
            }

            return;
        }

        if (PacketLite.PACKET_TYPE.HANDSHAKE == packet.type) {
            MachineInfo hostInfo = JSON.parseObject(packet.payload, MachineInfo.class);
            if (null == hostInfo) {
                throw new IllegalStateException("tbm client handshake error.cause:server receive binding info is null");
            }

            ClientContext.BINDING_ID = hostInfo.getBindingId();
            return;
        }

        logger.info("no match type, discard msg:{}", packet);
    }
}
