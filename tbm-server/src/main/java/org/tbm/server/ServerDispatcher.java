package org.tbm.server;

import org.tbm.common.Dispatcher;
import org.tbm.common.bean.PacketLite;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class ServerDispatcher implements Dispatcher{

    

    public PacketLite dispatch(PacketLite packet) {
        if (PacketLite.PACKET_TYPE.HANDSHAKE == packet.getType()) {

        }

        return null;
    }
}
