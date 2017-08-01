package org.tbm.server.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.CollectorPool;
import org.tbm.common.Connection;
import org.tbm.common.bean.MachineBinding;
import org.tbm.server.OpsFactory;
import org.tbm.server.operation.MachineBindingOp;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/8/1.
 */
public class ServerConnection implements Connection, ChannelFutureListener {
    private static Logger logger = LoggerFactory.getLogger(ServerConnection.class);

    private final MachineBindingOp machineBindingOp = (MachineBindingOp) OpsFactory.get(MachineBindingOp.class);

    private Channel channel;

    private MachineBinding machineBinding;

    private AtomicInteger status = new AtomicInteger(Connection.NEW);

    private volatile long lastReadTime;

    private volatile long lastWriteTime;

    private CollectorPool pool;

    public ServerConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            updateLastWriteTime();
        }
    }

    @Override
    public void init(CollectorPool pool) {
//        this.channel = channel;
        this.lastReadTime = System.currentTimeMillis();
        this.status.set(Connection.CONNECTED);
        this.pool = pool;
    }

    @Override
    public int status() {
        return this.status.get();
    }

    @Override
    public ChannelFuture close() {
        status.set(Connection.DISCONNECTED);
        if (null != machineBinding) {
            try {
                machineBindingOp.UPDATE_MACHINE_BINDING_STATUS(machineBinding.getSystemId(), machineBinding.getIp());
            } catch (Exception e) {
                logger.error("failure to update machine connection status", e);
                throw new IllegalStateException(e);
            }

            pool.removeScheduleJob(String.valueOf(machineBinding.getBindingId()));
        }

        return this.channel.close();
    }

    @Override
    public MachineBinding auth(MachineBinding param) {
        try {
            List<MachineBinding> select = machineBindingOp.SELECT_MACHINE_BINDING(param.getSystemId(), param
                    .getIp());
            if (null == select || 0 == select.size()) {
                machineBinding = param;
                machineBinding.setBindingId(System.currentTimeMillis());
                machineBindingOp.INSERT_MACHINE_BINDING(machineBinding);
            } else {
                machineBinding = select.get(0);
                machineBindingOp.UPDATE_MACHINE_BINDING_STATUS(machineBinding.getSystemId(), machineBinding.getIp());
            }

            status.set(Connection.AUTHORIZED);
            pool.addScheduleJob(String.valueOf(machineBinding.getBindingId()));
            ConnectionManager.bind(String.valueOf(machineBinding.getBindingId()), this);
            return machineBinding;
        } catch (Exception e) {
            logger.error("auth error", e);
            return param;
        }
    }

    @Override
    public void updateLastReadTime() {
        this.lastReadTime = System.currentTimeMillis();
    }

    @Override
    public void updateLastWriteTime() {
        this.lastWriteTime = System.currentTimeMillis();
    }

    @Override
    public MachineBinding getMachineBinding() {
        return this.machineBinding;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public String toString() {
        return "[channel=" + channel + ", machine=" + machineBinding + ", status=" + status + ", lastReadTime=" +
                lastReadTime + ", lastWriteTime=" + lastWriteTime + "]";
    }
}
