package org.tbm.client;

import org.tbm.common.access.DataAccessor;
import org.tbm.common.bean.vo.PackageData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class AgentMain {
    public static void main(String[] args) throws InterruptedException, SQLException {
        LocalJvmAccessor accessor = new LocalJvmAccessor();
        PackageData packageData = accessor.fullPackageData();
        packageData.setBindingId(10001);
        DataAccessor jvmStash = new DataAccessor();
        List<PackageData> list = new ArrayList<>();
        list.add(accessor.fullPackageData().setBindingId(10001));
        list.add(accessor.fullPackageData().setBindingId(10001));
        list.add(accessor.fullPackageData().setBindingId(10001));
        list.add(accessor.fullPackageData().setBindingId(10001));

        jvmStash.insert(accessor.fullPackageData().setBindingId(10002));
//        MonitorAgent agent = new MonitorAgent();
//        agent.start();
//        ChannelFuture future = agent.getFuture();
//        int count = 0;
//        do {
//            future.channel().writeAndFlush(accessor.fullPackageData() + "\r\n");
//            count++;
//            Thread.sleep(3000);
//        } while (count <= 1000);


    }
}
