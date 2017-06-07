package org.tbm.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Dispatcher;
import org.tbm.common.access.DataAccessor;
import org.tbm.common.access.DataAccessorFactory;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.DigestUtils;
import org.tbm.server.collect.CollectorPool;
import org.tbm.server.collect.CollectorPoolManager;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);
    private Dispatcher dispatcher;
    private DataAccessor dataAccessor = DataAccessorFactory.getInstance();
    private CollectorPool collectorPool = CollectorPoolManager.getTaskPool();
    private AtomicInteger count = new AtomicInteger(0);

    public ServerChannelHandler() {
        dispatcher = new ServerDispatcher(dataAccessor, collectorPool);
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        if (logger.isDebugEnabled()) {
        logger.info("{} received msg from:{}", new Date(), ctx.channel().remoteAddress());
        logger.info("msg content:{}", msg);
        logger.info("{}===================================================", count.getAndIncrement());
//        }

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

        dispatcher.dispatch(ctx, lite);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connected from :{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("disconnected from:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("channel exception.cause:{}", cause);
    }
}
