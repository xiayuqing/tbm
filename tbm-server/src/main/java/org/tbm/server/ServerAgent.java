package org.tbm.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.State;
import org.tbm.server.connection.ConnectionManager;
import org.tbm.server.support.TrafficCollectWorker;

import java.nio.charset.Charset;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class ServerAgent {
    private static final Logger logger = LoggerFactory.getLogger(ServerAgent.class);
    private int port = 9411;
    private AtomicInteger state = new AtomicInteger(State.STOP);
    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;

    public void start(ConnectionManager connectionManager, TrafficCollectWorker trafficCollectWorker) {
        if (!state.compareAndSet(State.STOP, State.STARTING)) {
            throw new IllegalStateException("server already started.");
        }

        create(connectionManager, trafficCollectWorker);
    }

    private void create(final ConnectionManager connectionManager, final TrafficCollectWorker trafficCollectWorker) {

        this.boss = new NioEventLoopGroup();
        this.worker = new NioEventLoopGroup();
        worker.setIoRatio(TbmContext.getInt("io.ratio ", 70));
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(TbmContext.getInt("frame" +
                                ".length.max", 32768), Delimiters.lineDelimiter()));
                        ch.pipeline().addLast("decoder", new TrafficDecoder(Charset.forName("utf-8"),
                                trafficCollectWorker));
                        ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("utf-8")));
                        ch.pipeline().addLast(new IdleStateHandler(40, 0, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new ServerIdleStateTrigger());
                        ch.pipeline().addLast(new ServerChannelHandler(connectionManager));
                        ch.pipeline().addLast(new GlobalChannelTrafficShapingHandler(new ScheduledThreadPoolExecutor
                                (3), TbmContext.getInt("traffic.shaping.write.global.limit"), TbmContext.getInt
                                ("traffic.shaping.read.global.limit"), TbmContext.getInt("traffic.shaping.write" +
                                ".channel.limit"), TbmContext.getInt("traffic.shaping.read.channel.limit"), TbmContext
                                .getInt("traffic.shaping.check.interval")));
                    }
                });

        bootstrap.childOption(ChannelOption.SO_SNDBUF, TbmContext.getInt("so.send.buf", 32 * 1024));
        bootstrap.childOption(ChannelOption.SO_RCVBUF, TbmContext.getInt("so.receive.buf", 32 * 1024));

        // 限制写缓冲水位
        bootstrap.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark.DEFAULT);

        try {
            ChannelFuture future = bootstrap.bind(port).sync().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("tbm server start success on:{}", port);
                    } else {
                        logger.error("tbm server start failure on:{}, cause:{}", port, future.cause());
                    }
                }
            });

            if (future.isSuccess()) {
                state.set(State.STARTED);
                future.channel().closeFuture().sync();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            stop();
        }
    }

    public void stop() {
        if (state.compareAndSet(State.STARTED, State.SHUTDOWN)) {
            if (null != this.boss) {
                this.boss.shutdownGracefully().awaitUninterruptibly();
            }

            if (null != worker) {
                this.worker.shutdownGracefully().awaitUninterruptibly();
            }
        } else {
            logger.warn("tbm server already shutdown");
        }
    }


}
