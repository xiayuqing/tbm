package org.tbm.client.node;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.tbm.client.ClientDispatcher;
import org.tbm.common.AppContext;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.HostInfo;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.NetUtil;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private Dispatcher dispatcher;

    public ClientHandler() {
        dispatcher = new ClientDispatcher();
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
        PacketLite lite = dispatcher.dispatch(JSON.parseObject(msg, PacketLite.class));
        if (null != lite) {
            ctx.channel().writeAndFlush(lite + "\r\n");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connected to :" + ctx.channel().remoteAddress());
        HostInfo hostInfo = NetUtil.convertHostInfo(ctx.channel().localAddress());
        hostInfo.setSystemId(AppContext.getLong("system.id", 10000L));
        ctx.channel().writeAndFlush(PacketLite.createHandshake(hostInfo.getSystemId(), hostInfo.getIp(), hostInfo
                .getPort()) + "\r\n");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("disconnected to:" + ctx.channel().remoteAddress());
    }
}
