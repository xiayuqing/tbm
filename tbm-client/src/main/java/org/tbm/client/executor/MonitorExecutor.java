package org.tbm.client.executor;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelFuture;
import org.tbm.client.ClientContext;
import org.tbm.common.bean.LogData;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.util.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/14.
 */
public class MonitorExecutor {
    private static AtomicBoolean start = new AtomicBoolean(false);
    private ChannelFuture future;
    private ExecutorService monitorTaskPool;
    private String localIdentity;

    public void initAndStart() {
        if (!start.compareAndSet(false, true)) {
            return;
        }

        localIdentity = ClientContext.getWorkNode().getIdentity();
        monitorTaskPool = Executors.newSingleThreadExecutor();
    }

    public void submit(List<LogData> data) {
        // TODO 批量传输
        monitorTaskPool.submit(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    public ChannelFuture write(LogData data) {
        return write(Utils.singleObjectConvertToList(data));
    }

    public ChannelFuture write(List<LogData> data) {
        if (unWritable()) {
            return null;
        }

        JSONObject object = new JSONObject();
        object.put("identity", localIdentity);
        object.put("data", data);

        return future.channel().writeAndFlush(PacketLite.createLogDataPackage(object).toString() + "\r\n");
    }

    public void stop() {
        if (!start.compareAndSet(true, false)) {
            return;
        }

        stopExecutor(monitorTaskPool);

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
                    System.err.print("Executor did not terminate");
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

    private boolean unWritable() {
        return null == future || !future.channel().isActive();
    }
}


