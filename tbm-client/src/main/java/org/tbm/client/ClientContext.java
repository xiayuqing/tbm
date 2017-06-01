package org.tbm.client;

import org.tbm.common.AppContext;

/**
 * Created by Jason.Xia on 17/6/1.
 */
public class ClientContext extends AppContext {

    public static long BINDING_ID;

    public ClientContext() {

    }

    public static void load(String path) {
        AppContext.load(path);
    }
}
