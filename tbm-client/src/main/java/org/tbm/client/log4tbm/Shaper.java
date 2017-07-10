package org.tbm.client.log4tbm;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.tbm.client.ClientContext;
import org.tbm.common.bean.vo.BizData;

/**
 * Created by Jason.Xia on 17/7/10.
 */
public class Shaper {

    public BizData shaping(final LoggingEvent loggingEvent) {
        BizData result = null;

        if (null != loggingEvent) {
            result = new BizData();
            result.setBindingId(ClientContext.getBindingId());
            result.setTime(loggingEvent.getTimeStamp());
            result.setLevel(loggingEvent.getLevel().toInt());
            result.safeSetTrace(loggingEvent.getMDC("traceId").toString());
            LocationInfo location = loggingEvent.getLocationInformation();
            result.safeSetClazz(location.getClassName());
            result.safeSetMethod(location.getMethodName());
            result.setLine(Integer.valueOf(location.getLineNumber()));
            result.safeSetContent(loggingEvent.getMessage().toString());
        }

        return result;
    }
}
