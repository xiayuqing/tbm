package org.tbm.common;

import org.tbm.common.bean.PacketLite;

/**
 * Created by Jason.Xia on 17/5/31.
 */
public interface Processor {

    PacketLite process(PacketLite packetLite, Connection connection);
}
