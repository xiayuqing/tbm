package org.tbm.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Jason.Xia on 17/5/24.
 */
class ClientMain {
    public static void main(String[] args) throws InterruptedException, SQLException, IOException {
        ClientAgentStartup startup = new ClientAgentStartup();
        startup.start();

        Logger testClass = LoggerFactory.getLogger("testClass");
        while (true) {
            testClass.info(System.currentTimeMillis() + " a log message");
            Thread.sleep(3000);
        }
    }
}
