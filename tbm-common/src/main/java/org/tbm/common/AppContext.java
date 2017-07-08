package org.tbm.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class AppContext {
    private static final String CONFIG_FILE_NAME = "tbm-config.cfg";
    public static long SYSTEM_ID;
    private static long BINDING_ID;
    private static boolean contextEnable = false;
    private static Properties config = new Properties();

    private static AtomicBoolean started = new AtomicBoolean(false);

    public static void init(String path) {
        load(path);
    }

    public static void setProperties(Properties properties) {
        config = properties;
    }

    public static void load(String path) {
        if (started.compareAndSet(false, true)) {
            if (null == path || 0 == path.length()) {
                throw new IllegalArgumentException("Not found config file path :" + path);
            }

            String file = path;
            if (!path.endsWith(".cfg")) {
                file = path + CONFIG_FILE_NAME;
            }

            try {
                config.load(new FileInputStream(file));
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
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

    public static long getBindingId() {
        return BINDING_ID;
    }

    public static boolean isContextEnable() {
        return contextEnable;
    }

    public static void updateBindingId(long bindingId) {
        BINDING_ID = bindingId;
        contextEnable = true;
    }
}
