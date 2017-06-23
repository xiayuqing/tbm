package org.tbm.common.utils;

import org.tbm.common.bean.ValuePair;

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

    /***
     * key:ip
     * value:port
     * @param socketAddress
     * @return
     */
    public static ValuePair<String, Integer> convertHostInfo(SocketAddress socketAddress) {
        String substring = socketAddress.toString().substring(1);
        int indexOf = substring.indexOf(":");
        if (-1 != indexOf) {
            String[] split = substring.split(":");
            return new ValuePair<>(split[0], Integer.valueOf(split[1]));
        } else {
            return new ValuePair<>(substring, 0);
        }
    }
}
