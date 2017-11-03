package org.tbm.client;

import org.tbm.common.bean.WorkNode;
import org.tbm.common.util.Utils;

import java.util.Properties;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class ClientContext {
    public static String IDENTITY;
    public static String HOST;
    public static String LOCAL;
    private static Properties config = new Properties();
    private static WorkNode localNode;
    private static boolean contextEnable;

    public ClientContext() {
    }

    public static void init(String host, String identity) {
        HOST = host;
        IDENTITY = identity;
        LOCAL = Utils.getAddress();
    }

    public static WorkNode getWorkNode() {
        if (null == localNode) {
            localNode = new WorkNode();
            localNode.setIdentity(IDENTITY);
            localNode.setHost(Utils.getHost());
            localNode.setAddress(LOCAL);
            localNode.setOs(System.getProperty("os.name"));
            localNode.setVersion(System.getProperty("os.version"));
            localNode.setArch(System.getProperty("os.arch"));
            localNode.setUserName(System.getProperty("user.name"));
            localNode.setUserHome(System.getProperty("user.home"));
            localNode.setUserDir(System.getProperty("user.dir"));
            localNode.setJavaVersion(System.getProperty("java.version"));
            localNode.setJavaHome(System.getProperty("java.home"));
        }

        return localNode;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defaultValue;
        }

        return Boolean.valueOf(p);
    }

    public static int getInt(String key, int defaultValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defaultValue;
        }

        return Integer.valueOf(p);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static long getLong(String key, long defaultValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defaultValue;
        }

        return Long.valueOf(p);
    }

    public static String getString(String key, String defaultValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defaultValue;
        }

        return p;
    }

    public static String getString(String key) {
        return config.getProperty(key);
    }

    public static boolean isContextEnable() {
        return contextEnable;
    }

    public static void setContextEnable(boolean contextEnable) {
        ClientContext.contextEnable = contextEnable;
    }
}
