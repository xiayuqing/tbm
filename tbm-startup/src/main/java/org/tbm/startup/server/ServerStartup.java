package org.tbm.startup.server;

import org.tbm.server.Main;

/**
 * Created by Jason.Xia on 17/7/12.
 */
public class ServerStartup {
    public static void main(String[] args) {
        String cfgPath = args[0];
        if (null == cfgPath) {
            System.err.println("Need config path");
        }

        Main.run(cfgPath);
    }
}
