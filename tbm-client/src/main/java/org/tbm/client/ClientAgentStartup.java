package org.tbm.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.tbm.client.execute.ExecutorFactory;
import org.tbm.client.execute.LocalJvmAccessor;
import org.tbm.client.execute.LogExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Jason.Xia on 17/6/14.
 */
public class ClientAgentStartup {
    private static AtomicBoolean start = new AtomicBoolean(false);

    private final Logger logger = LoggerFactory.getLogger(ClientAgentStartup.class);

    private Resource location;

    private boolean ignoreResourceNotFound = false;

    public void start() throws IOException {
        if (!start.compareAndSet(false, true)) {
            return;
        }

        Properties properties = new Properties();
        if (this.location == null) {
            String file = ClientAgentStartup.class.getResource("/tbm-config.cfg").getFile();
            logger.info("[tbm]Loading tbm properties file from:" + file);
            properties.load(new FileInputStream(new File(file)));
        } else {
            logger.info("[tbm]Loading tbm properties file from:" + location);
            try {
                PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(location, Charset.defaultCharset
                        ()));
            } catch (IOException e) {
                if (this.ignoreResourceNotFound) {
                    logger.warn("[tbm]Could not load properties from" + location + ":" + e.getMessage());
                } else {
                    throw e;
                }
            }
        }

        ClientContext.setProperties(properties);

        final LogExecutor logExecutor = ExecutorFactory.getInstance();
        logExecutor.initAndStart();

        ClientContext.initJvmBaseInfo(new LocalJvmAccessor().getJvmInfo());
        String host = null == ClientContext.getString("host") ? "localhost" : ClientContext.getString("host");
        int port = ClientContext.getInt("port", 9411);
        final ClientAgent clientAgent = new ClientAgent();
        clientAgent.start(host, port, logExecutor);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                logExecutor.stop();

                clientAgent.stop();
            }
        }));
    }

    public Resource getLocation() {
        return location;
    }

    public void setLocation(Resource location) {
        this.location = location;
    }

    public boolean isIgnoreResourceNotFound() {
        return ignoreResourceNotFound;
    }

    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
        this.ignoreResourceNotFound = ignoreResourceNotFound;
    }
}
