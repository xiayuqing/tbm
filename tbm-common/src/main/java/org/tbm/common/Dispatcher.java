package org.tbm.common;

import org.tbm.common.bean.PacketLite;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public interface Dispatcher {
    PacketLite dispatch(PacketLite packet);
}
