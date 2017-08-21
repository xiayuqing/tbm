package org.tbm.client.execute;

import io.netty.channel.ChannelFuture;
import org.tbm.client.ClientContext;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.bean.vo.BizData;
import org.tbm.common.utils.ObjectUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/14.
 */
public class LogExecutor {
    private static AtomicBoolean start = new AtomicBoolean(false);
    private static int count = 0;
    private ChannelFuture future;
    private ScheduledExecutorService executor;
    private ExecutorService bizLogExecutor;
    private LocalJvmAccessor localJvmAccessor;

    public void initAndStart() {
        if (!start.compareAndSet(false, true)) {
            return;
        }

        if (ClientContext.getBoolean("jvm.stat.enable", false)) {
            initScheduledExecutor();
        }

        bizLogExecutor = Executors.newSingleThreadExecutor();
    }

    public void submit(BizData data) {
        // TODO 批量传输
        bizLogExecutor.submit(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public ChannelFuture write(BizData data) {
        return write(ObjectUtils.singleObjectConvertToList(data));
    }

    public ChannelFuture write(List<BizData> data) {
        if (unWritable()) {
            System.out.println("discard size:" + data.size());
            return null;
        }

        count += data.size();
        System.out.println("count=" + count);
        return future.channel().writeAndFlush(PacketLite.createBizDataPackage(data).toString() + "\r\n");
    }

    public void stop() {
        if (!start.compareAndSet(true, false)) {
            return;
        }

        stopExecutor(executor);
        stopExecutor(bizLogExecutor);

        start.set(false);
    }

    private void stopExecutor(ExecutorService executorService) {
        if (null == executorService) {
            return;
        }

        try {
            executorService.shutdownNow();
            if (!executorService.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                    System.err.print("JvmStatExecutor did not terminate");
                }
            }

        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void updateFuture(ChannelFuture future) {
        this.future = future;
    }

    private void initScheduledExecutor() {
        this.localJvmAccessor = new LocalJvmAccessor();
        executor = Executors.newScheduledThreadPool(ClientContext.getInt("jvm.stat.executor.size", 5));
        long aLong = ClientContext.getLong("jvm.stat.period", 30);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (unWritable()) {
                    return;
                }

                PacketLite packetLite = PacketLite.createJvmDataPackage(ObjectUtils.singleObjectConvertToList
                        (localJvmAccessor.fullPackageData()));
                future.channel().writeAndFlush(packetLite.toString() + "\r\n");
            }
        }, aLong, aLong, TimeUnit.SECONDS);
    }

    private boolean unWritable() {
        return null == future || !future.channel().isActive() || 0 == ClientContext.getBindingId();
    }
}


