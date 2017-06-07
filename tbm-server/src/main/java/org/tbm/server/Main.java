package org.tbm.server;

import org.tbm.server.collect.CollectorPoolManager;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class Main {
    public static void main(String[] args) {

        CollectorPoolManager.start();
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.start();
    }
}
