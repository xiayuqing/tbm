package org.tbm.client;

import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.tbm.client.execute.JvmStatExecutor;

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

        if (this.location == null) {
            return;
        }

        logger.info("[tbm]Loading tbm properties file from:" + location);
        Properties properties = new Properties();
        try {
            PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(location, Charset.defaultCharset()));
        } catch (IOException e) {
            if (this.ignoreResourceNotFound) {
                logger.warn("[tbm]Could not load properties from" + location + ":" + e.getMessage());
            } else {
                throw e;
            }
        }

        ClientContext.setProperties(properties);
        String host = null == ClientContext.getString("host") ? "localhost" : ClientContext.getString("host");
        int port = ClientContext.getInt("port", 9411);
        final ClientAgent clientAgent = new ClientAgent();
        ChannelFuture channelFuture = clientAgent.start(host, port);
        JvmStatExecutor statExecutor = null;
        if (ClientContext.getBoolean("jvm.stat.enable", true)) {
            statExecutor = new JvmStatExecutor();
            statExecutor.initAndStart(channelFuture);
        }

        final JvmStatExecutor finalStatExecutor = statExecutor;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != finalStatExecutor) {
                    finalStatExecutor.stop();
                }

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
