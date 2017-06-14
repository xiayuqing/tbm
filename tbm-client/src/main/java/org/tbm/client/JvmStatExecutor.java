package org.tbm.client;

import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.ObjectUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/14.
 */
public class JvmStatExecutor {
    private static AtomicBoolean start = new AtomicBoolean(false);

    private final Logger logger = LoggerFactory.getLogger(JvmStatExecutor.class);

    private ChannelFuture future;

    private ScheduledExecutorService executor;

    private LocalJvmAccessor localJvmAccessor;

    public void initAndStart(final ChannelFuture future) {
        if (!start.compareAndSet(false, true)) {
            logger.info("JvmStatExecutor already started");
            return;
        }

        this.future = future;
        this.localJvmAccessor = new LocalJvmAccessor();
        executor = Executors.newScheduledThreadPool(ClientContext.getInt("jvm.stat.executor.size", 5));
        long aLong = ClientContext.getLong("jvm.stat.period", 30);
        executor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                if (0 == ClientContext.BINDING_ID) {
                    return;
                }

                PacketLite packetLite = PacketLite.createJvmDataPackage(ObjectUtils.singleObjectConvertToList
                        (localJvmAccessor.fullPackageData()).toString());
                future.channel().writeAndFlush(packetLite.toString() + "\r\n");
            }
        }, aLong, aLong, TimeUnit.SECONDS);
    }

    public void stop() {
        if (!start.compareAndSet(true, false)) {
            return;
        }

        try {
            executor.shutdownNow();
            if (!executor.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                    logger.error("JvmStatExecutor did not terminate");
                }
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public ChannelFuture getFuture() {
        return future;
    }
}
