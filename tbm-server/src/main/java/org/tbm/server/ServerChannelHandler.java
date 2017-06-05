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
import org.tbm.server.collect.CollectorPool;
import org.tbm.server.collect.CollectorPoolManager;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);
    private Dispatcher dispatcher;
    private DataAccessor dataAccessor = DataAccessorFactory.getInstance();
    private CollectorPool collectorPool = CollectorPoolManager.getTaskPool();

    public ServerChannelHandler() {
        dispatcher = new ServerDispatcher(dataAccessor, collectorPool);
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("{} received msg from:{}", new Date(), ctx.channel().remoteAddress());
            logger.debug("msg content:{}", msg);
            logger.debug("===================================================");
        }

        dispatcher.dispatch(ctx, JSON.parseObject(msg, PacketLite.class));
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
