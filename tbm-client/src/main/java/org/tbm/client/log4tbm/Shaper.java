package org.tbm.client.log4tbm;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.tbm.client.ClientContext;
import org.tbm.common.bean.LogData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jason.Xia on 17/7/10.
 */
public class Shaper {

    public List<LogData> shaping(final LoggingEvent event) {
        List<LogData> result = new ArrayList<>();
        if (null == event) {
            return null;
        }

        LogData logData = new LogData();
        logData.setIdentity(ClientContext.IDENTITY);
        logData.setTime(new Date(event.getTimeStamp()));
        logData.setHost(ClientContext.LOCAL);
        logData.setLevel(event.getLevel().toInt());
        logData.safeSetTrace(event.getMDC("traceId"));

        LocationInfo location = event.getLocationInformation();
        logData.safeSetClazz(location.getClassName());
        logData.safeSetMethod(location.getMethodName());
        logData.setLine(Integer.valueOf(location.getLineNumber()));

        // 使用 text 存储content部分,但是为了保证一定安全,还是限定最大长度为2W.
        StringBuilder content = new StringBuilder();
        content.append(event.getMessage().toString().trim());

        if (null != event.getThrowableInformation()) {
            content.append(" [EXCEPTION_STACK]:");
            String[] strRep = event.getThrowableInformation().getThrowableStrRep();
            if (null != strRep) {
                for (String item : strRep) {
                    content.append(item.trim());
                }
            }
        }

        logData.safeSetContent(content.toString());
        result.add(logData);
        return result;
    }
}
