package org.tbm.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.DigestUtils;
import org.tbm.common.Connection;
import org.tbm.server.connection.ConnectionManager;
import org.tbm.server.connection.ServerConnection;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);

    private Dispatcher dispatcher;

    private AtomicInteger count = new AtomicInteger(0);

    private ConnectionManager connectionManager;

    public ServerChannelHandler(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        dispatcher = new ServerDispatcher();
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("{} received msg from:{}", new Date(), ctx.channel().remoteAddress());
            logger.debug("msg content:{}", msg);
            logger.debug("{}===================================================", count.getAndIncrement());
        }

        PacketLite lite = null;
        try {
            lite = JSON.parseObject(msg, PacketLite.class);
        } catch (Exception e) {
            ctx.channel().writeAndFlush(PacketLite.createException("packet format error", DigestUtils
                    .getUUIDWithoutStrike()) + "\r\n");
        }

        if (null == lite) {
            return;
        }

        dispatcher.dispatch(connectionManager.get(ctx.channel()), lite);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connected from :{}", ctx.channel().remoteAddress());
        Connection conn = new ServerConnection(ctx.channel());
        connectionManager.add(conn);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("disconnected from:{}", ctx.channel().remoteAddress());
        connectionManager.removeAndClose(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("channel exception.cause:{}", cause);
    }
}
