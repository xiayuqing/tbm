package org.tbm.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.tbm.common.Dispatcher;
import org.tbm.common.bean.PacketLite;

import java.util.Date;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {

    private Dispatcher dispatcher;

    public ServerChannelHandler() {
        dispatcher = new ServerDispatcher();
    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(new Date() + " received msg from:" + ctx.channel().remoteAddress());
        System.out.println("msg content:" + msg);
        System.out.println("===================================================");
        dispatcher.dispatch(ctx, JSON.parseObject(msg, PacketLite.class));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connected from :" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("disconnected from:" + ctx.channel().remoteAddress());
    }


}
