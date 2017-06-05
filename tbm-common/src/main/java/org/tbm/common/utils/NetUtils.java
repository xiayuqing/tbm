package org.tbm.common.utils;

import org.tbm.common.bean.HostInfo;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Created by Jason.Xia on 17/5/27.
 */
public class NetUtils {

    public static InetAddress getLocalAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    public static HostInfo convertHostInfo(SocketAddress socketAddress) {
        String substring = socketAddress.toString().substring(1);
        HostInfo result = new HostInfo();
        int indexOf = substring.indexOf(":");
        if (-1 != indexOf) {
            String[] split = substring.split(":");
            result.setIp(split[0]);
            result.setPort(Integer.valueOf(split[1]));
        } else {
            result.setIp(substring);
            result.setPort(0);
        }

        return result;
    }
}
