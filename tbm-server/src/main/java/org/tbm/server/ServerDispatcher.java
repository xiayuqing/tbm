package org.tbm.server;

import org.tbm.common.AppContext;
import org.tbm.common.Connection;
import org.tbm.common.Dispatcher;
import org.tbm.common.Processor;
import org.tbm.common.bean.PacketLite;
import org.tbm.server.processor.BizDataCollectProcessor;
import org.tbm.server.processor.HandshakeProcessor;
import org.tbm.server.processor.HeartbeatProcessor;
import org.tbm.server.processor.JvmDataCollectProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class ServerDispatcher implements Dispatcher {

    private Map<Integer, Processor> processors = new HashMap<>();

    public ServerDispatcher() {
        processors.put(PacketLite.PACKET_TYPE.HEARTBEAT, new HeartbeatProcessor());
        processors.put(PacketLite.PACKET_TYPE.HANDSHAKE, new HandshakeProcessor());
        processors.put(PacketLite.PACKET_TYPE.JVM_DATA, new JvmDataCollectProcessor());
        processors.put(PacketLite.PACKET_TYPE.BIZ_DATA, new BizDataCollectProcessor(AppContext.getBoolean("redis" +
                ".enable", true)));
    }

    public void dispatch(Connection connection, PacketLite packet) {
        Processor processor = processors.get(packet.type);
        if (null == processor) {
            return;
        }


        PacketLite lite = processor.process(packet, connection);
        if (null != lite) {
            connection.getChannel().writeAndFlush(lite.toString() + "\r\n");
        }
    }
}
