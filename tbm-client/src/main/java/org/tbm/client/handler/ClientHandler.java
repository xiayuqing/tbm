package org.tbm.client.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.client.ClientContext;
import org.tbm.client.ClientDispatcher;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.MachineInfo;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.ValuePair;
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
        ValuePair<String/*ip*/, Integer/*port*/> pair = NetUtils.convertHostInfo(ctx.channel().localAddress());
        MachineInfo machineInfo = ClientContext.getMachineInfo();
        machineInfo.setAddress(pair.getKey(), pair.getValue());
        ctx.channel().writeAndFlush(PacketLite.createHandshake(machineInfo) + "\r\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("channel exception.cause:{}", cause);
    }
}
