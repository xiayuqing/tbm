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

        logData.safeSetContent(event.getMessage().toString().trim());
        result.add(logData);

        if (null != event.getThrowableInformation()) {
            String[] strRep = event.getThrowableInformation().getThrowableStrRep();
            List<StringBuilder> list = new ArrayList<>();
            StringBuilder s = new StringBuilder();
            for (String item : strRep) {
                if (s.length() + item.length() <= LogData.MAX_LEN_CONTENT) {
                    s.append(item.trim());
                } else {
                    list.add(s);
                    s = new StringBuilder();
                    s.append(item);
                }
            }

            list.add(s);
            try {
                for (StringBuilder item : list) {
                    if (0 == item.length()) {
                        continue;
                    }

                    LogData clone = logData.clone();
                    clone.setContent(item.toString());
                    result.add(clone);
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
