package org.tbm.client;

import io.netty.channel.ChannelFuture;
import org.tbm.common.bean.PacketLite;
import org.tbm.common.utils.ObjectUtils;

import java.sql.SQLException;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class AgentMain {
    public static void main(String[] args) throws InterruptedException, SQLException {
//        LocalJvmAccessor accessor = new LocalJvmAccessor();
//        System.out.println(accessor.fullPackageData());
//        PackageData packageData = accessor.fullPackageData();
//        packageData.setBindingId(10001);
//        DataAccessor jvmStash = new DataAccessor();
//        List<PackageData> list = new ArrayList<>();
//        list.add(accessor.fullPackageData().setBindingId(10001));
//        list.add(accessor.fullPackageData().setBindingId(10001));
//        list.add(accessor.fullPackageData().setBindingId(10001));
//        list.add(accessor.fullPackageData().setBindingId(10001));

//        jvmStash.insert(accessor.fullPackageData().setBindingId(10002));
        MonitorAgent agent = new MonitorAgent();
        agent.start();
        ChannelFuture future = agent.getFuture();
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
//        ChannelFuture future = agent.getFuture();
//        int count = 0;
//        do {
//            future.channel().writeAndFlush(accessor.fullPackageData() + "\r\n");
//            count++;
//            Thread.sleep(3000);
//        } while (count <= 1000);


    }
}
