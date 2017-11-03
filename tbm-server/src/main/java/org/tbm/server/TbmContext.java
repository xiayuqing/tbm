package org.tbm.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/10/5.
 */
public class TbmContext {
    private static final String CONFIG_FILE_NAME = "tbm.cfg";
    private static Properties config = new Properties();
    private static AtomicBoolean started = new AtomicBoolean(false);

    public static void init(String path) {
        load(path);
    }

    public static void load(String path) {
        if (started.compareAndSet(false, true)) {
            if (null == path || 0 == path.length()) {
                throw new IllegalArgumentException("Not found config file path :" + path);
            }

            String file = path;
            if (!path.endsWith(".cfg")) {
                file = path + (path.endsWith("/") ? CONFIG_FILE_NAME : "/" + CONFIG_FILE_NAME);
            }

            try {
                config.load(new FileInputStream(file));
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public static void load(Properties properties) {
        config = properties;
    }

    public static Properties getConfig() {
        return config;
    }

    public static boolean getBoolean(String key, boolean defValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defValue;
        }

        return Boolean.valueOf(p);
    }

    public static int getInt(String key, int defValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defValue;
        }

        return Integer.valueOf(p);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static double getDouble(String key) {
        return getDouble(key, 0);
    }

    public static double getDouble(String key, double defValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defValue;
        }

        return Double.valueOf(p);
    }

    public static long getLong(String key, long defValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defValue;
        }

        return Long.valueOf(p);
    }

    public static String getString(String key, String defValue) {
        String p = config.getProperty(key);
        if (null == p || 0 == p.length()) {
            return defValue;
        }

        return p;
    }

    public static String getString(String key) {
        return config.getProperty(key);
    }

    public static String getSerializeString() {
        return config.toString();
    }
}
