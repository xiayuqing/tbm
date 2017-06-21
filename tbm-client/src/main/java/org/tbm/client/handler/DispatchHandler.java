package org.tbm.client.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.client.ClientContext;
import org.tbm.client.ClientDispatcher;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.HostInfo;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.NetUtils;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class DispatchHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(DispatchHandler.class);
    private Dispatcher dispatcher;

    public DispatchHandler() {
        dispatcher = new ClientDispatcher();
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("{} msg content:{}", new Date(), msg);
        dispatcher.dispatch(ctx, JSON.parseObject(msg, PacketLite.class));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HostInfo hostInfo = NetUtils.convertHostInfo(ctx.channel().localAddress());
        hostInfo.setSystemId(ClientContext.SYSTEM_ID);
        ctx.channel().writeAndFlush(PacketLite.createHandshake(hostInfo.getSystemId(), hostInfo.getIp(), hostInfo
                .getPort()) + "\r\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("channel exception.cause:{}", cause);
    }
}
