package org.tbm.client.log4tbm;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.tbm.client.ClientContext;
import org.tbm.client.execute.ExecutorFactory;
import org.tbm.client.execute.LogExecutor;
import org.tbm.common.bean.vo.BizData;
import org.tbm.common.utils.CollectionUtils;

import java.util.List;

/**
 * Created by Jason.Xia on 17/7/5.
 */
public class TbmLog4jAppender extends AppenderSkeleton {
    private boolean initialized = false;
    private volatile LogExecutor logExecutor;
    private Shaper shaper = new Shaper();

    @Override
    protected void append(LoggingEvent event) {
        if (!initialized) {
            if (!initialize()) {
                return;
            }
        }

        LocationInfo location = event.getLocationInformation();
        // 自身的日志不上传
        if (location.getClassName().startsWith("org.tbm")) {
            return;
        }

        List<BizData> shaping = shaper.shaping(event);
        if (CollectionUtils.isEmpty(shaping)) {
            return;
        }

        try {
            logExecutor.write(shaping);
        } catch (Exception e) {
            errorHandler.error("Failed to insert BizData to Tbm", e, ErrorCode.WRITE_FAILURE);
        }
    }

    @Override
    public void close() {
        initialized = false;
        logExecutor.stop();
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    private boolean initialize() {
        if (ClientContext.isContextEnable() && ExecutorFactory.executorIsEnable()) {
            if (null == logExecutor) {
                synchronized (this) {
                    if (null == logExecutor) {
                        logExecutor = ExecutorFactory.getInstance();
                        initialized = true;
                    }
                }
            }

        }

        return initialized;
    }
}
