package org.tbm.client.channel;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.client.ClientContext;
import org.tbm.client.ClientDispatcher;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.PacketLite;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private Dispatcher dispatcher;

    public ClientHandler() {
        dispatcher = new ClientDispatcher();
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.debug("{} msg content:{}", new Date(), msg);
        dispatcher.dispatch(null, JSON.parseObject(msg, PacketLite.class));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(PacketLite.createHandshake(ClientContext.getWorkNode()) + "\r\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("channel exception.cause:{}", cause);
    }
}
