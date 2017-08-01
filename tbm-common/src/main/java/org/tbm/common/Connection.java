package org.tbm.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.tbm.common.bean.MachineBinding;

/**
 * Created by Jason.Xia on 17/6/30.
 */
public interface Connection {
    int NEW = 0;
    int CONNECTED = 1;
    int AUTHORIZED = 2;
    int SUSPEND = 3;
    int DISCONNECTED = 4;

    void init(CollectorPool pool);

    int status();

    ChannelFuture close();

    MachineBinding auth(MachineBinding machine);

    void updateLastReadTime();

    void updateLastWriteTime();

    MachineBinding getMachineBinding();

    Channel getChannel();
}
