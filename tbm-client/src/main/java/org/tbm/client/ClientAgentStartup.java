package org.tbm.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.tbm.client.executor.ExecutorFactory;
import org.tbm.client.executor.MonitorExecutor;
import org.tbm.common.util.Utils;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/14.
 */
public class ClientAgentStartup {
    private static final Logger logger = LoggerFactory.getLogger(ClientAgentStartup.class);
    private static AtomicBoolean start = new AtomicBoolean(false);
    private MonitorExecutor monitorExecutor;
    private ClientAgent clientAgent;
    private String host;

    private String identity;

    private String excludedPackage = "com.alibaba.dubbo.registry";

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

        ClientContext.init(host, identity, excludedPackage);
        monitorExecutor = ExecutorFactory.getInstance();
        monitorExecutor.initAndStart();

        clientAgent = new ClientAgent();
        clientAgent.start(ClientContext.HOST, monitorExecutor);
        ClientContext.setContextEnable(true);

        logger.info("TBM Client Started!");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                monitorExecutor.stop();

                clientAgent.stop();
            }
        }));

        return monitorExecutor;
    }

    @PreDestroy
    public void destroy() {
        if (null != monitorExecutor) {
            monitorExecutor.stop();
        }

        if (null != clientAgent) {
            clientAgent.stop();
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setExcludedPackage(String excludedPackage) {
        this.excludedPackage = excludedPackage;
    }
}
