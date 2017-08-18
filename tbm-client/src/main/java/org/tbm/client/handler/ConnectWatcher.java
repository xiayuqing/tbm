package org.tbm.client.handler;

import io.netty.channel.*;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.client.ClientAgent;
import org.tbm.client.execute.LogExecutor;
import org.tbm.common.ConnectionState;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/6/21.
 */
@ChannelHandler.Sharable
public abstract class ConnectWatcher extends ChannelInboundHandlerAdapter implements TimerTask, ChannelManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectWatcher.class);
    private final Timer timer;
    private final ClientAgent clientAgent;
    private final LogExecutor jvmStatExecutor;
    private String host;
    private int port;
    private AtomicInteger state = new AtomicInteger(ConnectionState.DISCONNECT);

    public ConnectWatcher(Timer timer, ClientAgent clientAgent, String host, int port, LogExecutor executor) {
        this.timer = timer;
        this.clientAgent = clientAgent;
        this.host = host;
        this.port = port;
        this.jvmStatExecutor = executor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("[tbm]Connected to :{}", ctx.channel().remoteAddress());
        state.set(ConnectionState.CONNECTED);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("[tbm]Disconnected From:{}", host + ":" + port);
        state.compareAndSet(ConnectionState.CONNECTED, ConnectionState.RECONNCTING);
        if (ConnectionState.RECONNCTING == state.get()) {
            logger.info("[tbm]Retry to Connect to {} after 5 seconds", host + ":" + port);
            timer.newTimeout(this, 5, TimeUnit.SECONDS);
        }

        ctx.fireChannelInactive();
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        clientAgent.initialHandler(getHandlers());

        clientAgent.connect(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("[tbm] Reconnect Success. Connect to:{}", host + ":" + port);
                    ConnectWatcher.this.state.set(ConnectionState.CONNECTED);
                    if (null != jvmStatExecutor) {
                        ConnectWatcher.this.jvmStatExecutor.updateFuture(future);
                    }
                } else {
                    logger.error("[tbm] Reconnect Failure. Connect to {}, cause:{}", host + ":" + port, future.cause
                            ());
                    future.channel().pipeline().fireChannelInactive();
                }
            }
        });


    }
}
