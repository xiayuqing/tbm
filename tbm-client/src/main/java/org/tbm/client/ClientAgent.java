package org.tbm.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.AppContext;
import org.tbm.common.State;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ClientAgent {
    private static final Logger logger = LoggerFactory.getLogger(ClientAgent.class);
    private AtomicInteger state = new AtomicInteger(State.STOP);
    private NioEventLoopGroup worker;
    private String host = "127.0.0.1";
    private int port = 9411;
    private ChannelFuture future;

    public ChannelFuture start(String host, int port) {
        if (!state.compareAndSet(State.STOP, State.STARTING)) {
            throw new IllegalStateException("client already started.");
        }

        this.host = host;
        this.port = port;
        return create();
    }

    private ChannelFuture create() {
        this.worker = new NioEventLoopGroup();
        worker.setIoRatio(AppContext.getInt("io.ratio ", 70));
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192,
                                Delimiters.lineDelimiter()));
                        ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("utf-8")));
                        ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("utf-8")));

                        ch.pipeline().addLast(new ClientHandler());
                    }
                });

        bootstrap.option(ChannelOption.SO_SNDBUF, AppContext.getInt("so.send.buf", 32 * 1024));
        bootstrap.option(ChannelOption.SO_RCVBUF, AppContext.getInt("so.receive.buf", 32 * 1024));

        // 限制写缓冲水位
        bootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT);

        try {
            this.future = bootstrap.connect(host, port).sync().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("[tbm] Client Start Success. Connect to:{}", host + ":" + port);
                    } else {
                        logger.error("[tbm] Client Start Failure. Connect to{}, cause:{}", host + ":" + port, future
                                .cause());
                    }
                }
            });

            if (future.isSuccess()) {
                state.set(State.STARTED);
                future.channel().closeFuture();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        return future;
    }

    public void stop() {
        if (state.compareAndSet(State.STARTED, State.SHUTDOWN)) {
            if (null != worker) {
                this.worker.shutdownGracefully().awaitUninterruptibly();
            }
        } else {
            logger.warn("tbm client already shutdown");
        }
    }

    public ChannelFuture getFuture() {
        return future;
    }
}
