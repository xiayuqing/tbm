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
        int count = 0;
        while (true) {
//            testClass.debug(" a DEBUG log message");
            testClass.info(++count+" a INFO log message");
            testClass.warn(++count+" a WARN log message");
            testClass.info(++count+" a INFO log message");
            testClass.warn(++count+" a WARN log message");
//            testClass.error(System.currentTimeMillis() + " a ERROR log message", new IllegalArgumentException("error " +
//                    "args"));
            Thread.sleep(100);
            if (count > 1000) {
                break;
            }
        }

        System.out.println("end....");

    }
}
