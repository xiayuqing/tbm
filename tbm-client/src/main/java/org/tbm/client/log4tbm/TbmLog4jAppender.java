package org.tbm.client.log4tbm;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by Jason.Xia on 17/7/5.
 */
public class TbmLog4jAppender extends AppenderSkeleton {
    private boolean initialized = false;

    @Override
    protected void append(LoggingEvent event) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
