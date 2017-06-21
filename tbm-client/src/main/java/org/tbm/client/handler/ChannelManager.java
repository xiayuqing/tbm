package org.tbm.client.handler;

import io.netty.channel.ChannelHandler;

/**
 * Created by Jason.Xia on 17/6/21.
 */
public interface ChannelManager {
    ChannelHandler[] getHandlers();
}
