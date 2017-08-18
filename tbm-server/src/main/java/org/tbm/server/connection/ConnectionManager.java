package org.tbm.server.connection;

import io.netty.channel.Channel;
import org.tbm.common.CollectorPool;
import org.tbm.common.Connection;
import org.tbm.common.utils.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jason.Xia on 17/6/28.
 */
public class ConnectionManager {
    private static final ConcurrentHashMap<String/*bindingId*/, String/*channelShortId*/> bundles = new
            ConcurrentHashMap<>();
    private ConcurrentHashMap<String/*channelId*/, Connection> connections = new ConcurrentHashMap<>();
    private CollectorPool collectorPool;

    public static void bind(String bindingId, Connection connection) {
        bundles.putIfAbsent(bindingId, connection.getChannel().id().asShortText());
    }

    public void init(CollectorPool collectorPool) {
        this.collectorPool = collectorPool;
    }

    public void add(Connection connection) {
        connection.init(collectorPool);
        this.connections.putIfAbsent(connection.getChannel().id().asShortText(), connection);
    }

    public Connection get(String bindingId) {
        String s = bundles.get(bindingId);
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        return connections.get(s);
    }

    public Connection get(final Channel channel) {
        return connections.get(channel.id().asShortText());
    }

    public Connection removeAndClose(final Channel channel) {
        Connection connection = this.connections.remove(channel.id().asShortText());
        if (null != connection) {
            connection.close();
            bundles.remove(String.valueOf(connection.getMachineBinding().getBindingId()));
        }

        return connection;
    }

    public void destroy() {
        for (Connection item : connections.values()) {
            item.close();
        }
    }
}
