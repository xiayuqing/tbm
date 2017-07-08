package org.tbm.client.log4tbm;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.tbm.client.ClientContext;
import org.tbm.client.execute.ExecutorFactory;
import org.tbm.client.execute.LogExecutor;
import org.tbm.common.bean.vo.BizData;

/**
 * Created by Jason.Xia on 17/7/5.
 */
public class TbmLog4jAppender extends AppenderSkeleton {
    private boolean initialized = false;
    private volatile LogExecutor logExecutor;

    @Override
    protected void append(LoggingEvent event) {
        if (!initialized) {
            if (!initialize()) {
                return;
            }
        }

        LocationInfo location = event.getLocationInformation();
        // 本体的日志不上传
        if (location.getClassName().startsWith("org.tbm")) {
            return;
        }

        BizData bizData = new BizData(ClientContext.getBindingId());
        bizData.setTime(event.getTimeStamp());
        bizData.setLevel(event.getLevel().toInt());
        Object traceId = event.getMDC("traceId");
        bizData.setTraceId(null == traceId ? "0" : traceId.toString());
        System.out.println("当前日志所在类:" + location.getClassName());
        bizData.setClazz(location.getClassName());
        bizData.setMethod(location.getMethodName());
        bizData.setContent(event.getMessage().toString());
        logExecutor.write(bizData);
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
