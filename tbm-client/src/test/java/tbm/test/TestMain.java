package tbm.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbm.client.ClientAgentStartup;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Jason.Xia on 17/7/8.
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException, SQLException, IOException {
        ClientAgentStartup startup = new ClientAgentStartup();
        startup.start();

        Logger testClass = LoggerFactory.getLogger(TestMain.class);
        while (true) {
            testClass.info(System.currentTimeMillis() + " a log message");
            Thread.sleep(5000);
        }
    }
}
