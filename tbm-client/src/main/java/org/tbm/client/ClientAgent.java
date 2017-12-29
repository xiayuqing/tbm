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
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.client.channel.ClientHandler;
import org.tbm.client.channel.ClientIdleStateTrigger;
import org.tbm.client.channel.ConnectWatcher;
import org.tbm.client.executor.MonitorExecutor;
import org.tbm.common.State;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ClientAgent {
    private static final Logger logger = LoggerFactory.getLogger(ClientAgent.class);
    private AtomicInteger state = new AtomicInteger(State.STOP);
    private NioEventLoopGroup worker;
    private String host = "127.0.0.1";
    private int port = 9413;
    private ChannelFuture future;
    private Bootstrap bootstrap;
    private MonitorExecutor monitorExecutor;

    public ChannelFuture start(String host, MonitorExecutor monitorExecutor) {
        if (!state.compareAndSet(State.STOP, State.STARTING)) {
            throw new IllegalStateException("client already started.");
        }

        this.host = host;
        this.monitorExecutor = monitorExecutor;
        ChannelFuture future = create();
        if (null != monitorExecutor) {
            monitorExecutor.updateFuture(future);
        }

        return future;
    }

    public ChannelFuture create() {
        synchronized (this) {
            if (null == this.worker) {
                this.worker = new NioEventLoopGroup();
            }

            worker.setIoRatio(ClientContext.getInt("io.ratio ", 70));
            if (null == bootstrap) {
                bootstrap = new Bootstrap();
            }
        }

        bootstrap.group(worker).channel(NioSocketChannel.class);

        initialHandler(new ConnectWatcher(new HashedWheelTimer(), this, host, port, monitorExecutor) {

            @Override
            public ChannelHandler[] getHandlers() {
                return new ChannelHandler[]{
                        this,
                        new IdleStateHandler(0, ClientContext.getInt("idle.write.time", 30), 30, TimeUnit.SECONDS),
                        new ClientIdleStateTrigger(host, port),
                        new ClientHandler()
                };
            }
        }.getHandlers());

        initialOption(bootstrap);

        connect();

        return future;
    }

    public void initialHandler(final ChannelHandler[] handlers) {
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                initialDefaultChannel(ch.pipeline());
                if (null != handlers) {
                    ch.pipeline().addLast(handlers);
                }
            }
        });
    }

    private void initialDefaultChannel(ChannelPipeline pipeline) {
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(ClientContext.getInt("frame" +
                ".length.max", 1048576), Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder(Charset.forName("utf-8")));
        pipeline.addLast("encoder", new StringEncoder(Charset.forName("utf-8")));
    }

    private ChannelFuture connect() {
        return connect(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("[tbm] Client Start Success. Connect to:{}", ClientAgent.this.getRemoteAddress());
                } else {
                    logger.error("[tbm] Client Start Failure. Connect to{}, cause:{}", ClientAgent.this
                            .getRemoteAddress(), future.cause());
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        });
    }

    public ChannelFuture connect(ChannelFutureListener listener) {
        this.future = bootstrap.connect(host, port).addListener(listener);
        if (future.isSuccess()) {
            state.set(State.STARTED);
            future.channel().closeFuture();
        }

        return future;
    }

    private void initialOption(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_SNDBUF, ClientContext.getInt("so.send.buf", 32 * 1024));
        bootstrap.option(ChannelOption.SO_RCVBUF, ClientContext.getInt("so.receive.buf", 32 * 1024));

        // 限制写缓冲水位
        bootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT);
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

    public String getRemoteAddress() {
        return host + ":" + port;
    }

    public ChannelFuture getFuture() {
        return future;
    }
}
