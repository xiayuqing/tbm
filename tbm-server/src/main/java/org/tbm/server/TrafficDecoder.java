package org.tbm.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;
import org.tbm.server.support.TrafficCollectWorker;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Jason.Xia on 17/10/29.
 */
public class TrafficDecoder extends StringDecoder {

    private TrafficCollectWorker trafficCollectWorker;

    public TrafficDecoder(Charset charset, TrafficCollectWorker trafficCollectWorker) {
        super(charset);
        this.trafficCollectWorker = trafficCollectWorker;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        trafficCollectWorker.count(ctx.channel(), msg.readableBytes(), true);
        super.decode(ctx, msg, out);
    }
}
