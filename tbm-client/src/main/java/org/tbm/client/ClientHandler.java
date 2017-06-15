package org.tbm.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.HostInfo;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.NetUtils;

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
        logger.info("{} msg content:{}", new Date(), msg);
        dispatcher.dispatch(ctx, JSON.parseObject(msg, PacketLite.class));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("connected from :{}", ctx.channel().remoteAddress());

        HostInfo hostInfo = NetUtils.convertHostInfo(ctx.channel().localAddress());
        hostInfo.setSystemId(ClientContext.SYSTEM_ID);
        ctx.channel().writeAndFlush(PacketLite.createHandshake(hostInfo.getSystemId(), hostInfo.getIp(), hostInfo
                .getPort()) + "\r\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("[tbm]Disconnected From:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("channel exception.cause:{}", cause);
    }
}
