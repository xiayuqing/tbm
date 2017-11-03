package org.tbm.server.connection;

import io.netty.channel.Channel;
import org.tbm.common.Connection;
import org.tbm.common.util.Utils;
import org.tbm.server.support.MonitorCollectWorker;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jason.Xia on 17/6/28.
 */
public class ConnectionManager {
    private static final ConcurrentHashMap<String/*@MonitorNode.getIdentity*/, String/*channelShortId*/> bundles = new
            ConcurrentHashMap<>();

    private static MonitorCollectWorker monitorCollectWorker;

    private ConcurrentHashMap<String/*channelId*/, Connection> connections = new ConcurrentHashMap<>();

    static void bind(String identity, Connection connection) {
        bundles.putIfAbsent(identity, connection.getChannel().id().asShortText());
        monitorCollectWorker.addScheduleJob(identity);
    }

    public void init(MonitorCollectWorker worker) {
        monitorCollectWorker = worker;
    }

    public void add(Connection connection) {
        connection.init();
        this.connections.putIfAbsent(connection.getChannel().id().asShortText(), connection);
    }

    public Connection get(String bindingId) {
        String s = bundles.get(bindingId);
        if (Utils.isEmpty(s)) {
            return null;
        }

        return connections.get(s);
    }

    public Connection get(final Channel channel) {
        return connections.get(channel.id().asShortText());
    }

    public void removeAndClose(final Channel channel) {
        Connection connection = this.connections.remove(channel.id().asShortText());
        if (null != connection) {
            connection.close();
            monitorCollectWorker.removeScheduleJob(connection.getWorkNode().getIdentity());
            bundles.remove(connection.getWorkNode().getIdentity());
        }
    }

    public void destroy() {
        for (Connection item : connections.values()) {
            item.close();
        }
    }
}
