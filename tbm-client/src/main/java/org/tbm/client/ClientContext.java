package org.tbm.client;

import org.tbm.common.AppContext;
import org.tbm.common.utils.StringUtils;

import java.util.Properties;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class ClientContext extends AppContext {

    public static long BINDING_ID;
    public static long SYSTEM_ID;

    public ClientContext() {
    }

    public static void load(String path) {
        AppContext.load(path);

        setSystemId();
    }

    public static void setProperties(Properties properties) {
        AppContext.setProperties(properties);

        setSystemId();
    }

    private static void setSystemId() {
        if (StringUtils.isEmpty(AppContext.getString("system.id"))) {
            throw new IllegalArgumentException("not found properties:system.id");
        }

        SYSTEM_ID = Long.valueOf(AppContext.getString("system.id"));
    }
}
