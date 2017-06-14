package org.tbm.client;

import io.netty.channel.ChannelFuture;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.ObjectUtils;

import java.sql.SQLException;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException, SQLException {
//        if (StringUtils.isEmpty(AppContext.getString("system.id"))) {
//            throw new IllegalArgumentException("not found properties:system.id");
//        }
//
//        ClientContext.SYSTEM_ID = Long.valueOf(AppContext.getString("system.id"));
        ClientContext.SYSTEM_ID = 10000l;
        ClientAgent agent = new ClientAgent();
        ChannelFuture future = agent.start();
//        ChannelFuture future = agent.getFuture();
        LocalJvmAccessor localJvmAccessor = new LocalJvmAccessor();

        do {
            Thread.sleep(5000);
            if (0 == ClientContext.BINDING_ID) {
                continue;
            }

            PacketLite packetLite = PacketLite.createJvmDataPackage(ObjectUtils.singleObjectConvertToList
                    (localJvmAccessor.fullPackageData()).toString());
            future.channel().writeAndFlush(packetLite.toString() + "\r\n");

        } while (true);
    }
}
