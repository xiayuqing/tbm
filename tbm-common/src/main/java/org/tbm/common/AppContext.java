package org.tbm.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class AppContext {
    private static final String CONFIG_FILE_NAME = "tbm-config.cfg";
    private static Properties config = new Properties();
    private static AtomicBoolean started = new AtomicBoolean(false);

    private static Map<String, Processor> processorMap = new HashMap<>();

    public static void init(String path) {
        load(path);
//        if (config.contains("filters")) {
//            String[] processors = config.getProperty("filters").split(",");
//        }
//
    }

    public static void load(String path) {
        if (started.compareAndSet(false, true)) {
            if (null == path || 0 == path.length()) {
                throw new IllegalArgumentException("Not found config file path :" + path);
            }

            String file = path + CONFIG_FILE_NAME;
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
}
