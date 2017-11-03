package org.tbm.client;

import org.springframework.util.Assert;
import org.tbm.client.executor.ExecutorFactory;
import org.tbm.client.executor.MonitorExecutor;
import org.tbm.common.util.Utils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/14.
 */
public class ClientAgentStartup {
    private static AtomicBoolean start = new AtomicBoolean(false);

    private MonitorExecutor monitorExecutor;

    private String host;

    private String identity;

    public MonitorExecutor start() throws IOException {
        if (!start.compareAndSet(false, true)) {
            return monitorExecutor;
        }

        Assert.notNull(host, "ClientAgentStartup 'host' Cannot be null");
        if (Utils.isEmpty(identity)) {
            String id = System.getProperty("identity");
            if (Utils.isEmpty(id)) {
                throw new IllegalArgumentException("ClientAgentStartup identity Cannot be null");
            } else {
                identity = id;
            }
        }

        ClientContext.init(host, identity);
        monitorExecutor = ExecutorFactory.getInstance();
        monitorExecutor.initAndStart();

        final ClientAgent clientAgent = new ClientAgent();
        clientAgent.start(ClientContext.HOST, monitorExecutor);

        ClientContext.setContextEnable(true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                monitorExecutor.stop();

                clientAgent.stop();
            }
        }));

        return monitorExecutor;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
