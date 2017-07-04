package org.tbm.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.bean.PacketLite;

/**
 * Created by Jason.Xia on 17/7/4.
 */
@ChannelHandler.Sharable
public class ClientIdleStateTrigger extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ClientIdleStateTrigger.class);

    private String host;

    private int port;

    public ClientIdleStateTrigger(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (IdleState.WRITER_IDLE == state) {
                logger.info("heartbeat to server -> {}", host + ":" + port);
                ctx.writeAndFlush(PacketLite.HEARTBEAT_PACKET.duplicate());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
