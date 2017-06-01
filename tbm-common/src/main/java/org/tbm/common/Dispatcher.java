package org.tbm.common;

import io.netty.channel.ChannelHandlerContext;
import org.tbm.common.bean.PacketLite;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public interface Dispatcher {
    void dispatch(ChannelHandlerContext ctx,PacketLite packet);
}
