package org.tbm.client;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Jason.Xia on 17/5/24.
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException, SQLException, IOException {
        ClientAgentStartup startup = new ClientAgentStartup();
        startup.start();
    }
}
