package org.tbm.server.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.common.Connection;
import org.tbm.common.bean.NodeLog;
import org.tbm.common.bean.WorkNode;
import org.tbm.server.SpringContainer;
import org.tbm.server.access.NodeLogAccessor;
import org.tbm.server.access.WorkNodeAccessor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jason.Xia on 17/8/1.
 */
public class ServerConnection implements Connection, ChannelFutureListener {
    private static Logger logger = LoggerFactory.getLogger(ServerConnection.class);

    private WorkNodeAccessor accessor;
    private NodeLogAccessor logAccessor;
    private Channel channel;

    private WorkNode workNode;

    private AtomicInteger status = new AtomicInteger(Connection.NEW);

    private volatile long lastReadTime;

    private volatile long lastWriteTime;

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
    public void init() {
        this.accessor = (WorkNodeAccessor) SpringContainer.getBean(WorkNodeAccessor.class);
        this.logAccessor = (NodeLogAccessor) SpringContainer.getBean(NodeLogAccessor.class);
        this.lastReadTime = System.currentTimeMillis();
        this.status.set(Connection.CONNECTED);
    }

    @Override
    public int status() {
        return this.status.get();
    }

    @Override
    public ChannelFuture close() {
        status.set(Connection.DISCONNECTED);
        if (null != workNode) {
            try {
                accessor.updateStatus(workNode.getIdentity(), workNode.getAddress(), 0, workNode.getPath());
                logAccessor.insert(new NodeLog(workNode.getIdentity(), workNode.getPath(), workNode.getHost(),
                        workNode.getAddress(), 0));
            } catch (Exception e) {
                logger.error("failure to update monitor node connection status", e);
                throw new IllegalStateException(e);
            }
        }

        return this.channel.close();
    }

    @Override
    public WorkNode auth(WorkNode node) {
        try {
            WorkNode select = accessor.select(node.getIdentity(), node.getAddress());
            if (null == select) {
                node.setStatus(1);
                accessor.insert(node);
            } else {
                accessor.updateStatus(node.getIdentity(), node.getAddress(), 1, node.getPath());
                logAccessor.insert(new NodeLog(node.getIdentity(), node.getPath(), node.getHost(), node.getAddress(),
                        1));
            }

            status.set(Connection.AUTHORIZED);
            workNode = node;
            ConnectionManager.bind(node.getIdentity(), this);
            return node;
        } catch (Exception e) {
            logger.error("auth error", e);
            return node;
        }
    }

    @Override
    public String getIdentity() {
        return null == workNode ? "Unknown" : workNode.getIdentity();
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
    public WorkNode getWorkNode() {
        return this.workNode;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public String toString() {
        return "[channel=" + channel + ", workNode=" + workNode + ", status=" + status + ", lastReadTime=" +
                lastReadTime + ", lastWriteTime=" + lastWriteTime + "]";
    }
}
