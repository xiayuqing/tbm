package org.tbm.client.log4tbm;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.tbm.client.ClientContext;
import org.tbm.common.bean.vo.BizData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/7/10.
 */
public class Shaper {

    public List<BizData> shaping(final LoggingEvent event) {
        List<BizData> result = new ArrayList<>();
        if (null == event) {
            return null;
        }

        BizData bizData = new BizData();
        bizData.setBindingId(ClientContext.getBindingId());
        bizData.setTime(event.getTimeStamp());
        bizData.setLevel(event.getLevel().toInt());
        bizData.safeSetTrace(event.getMDC("traceId"));

        LocationInfo location = event.getLocationInformation();
        bizData.safeSetClazz(location.getClassName());
        bizData.safeSetMethod(location.getMethodName());
        bizData.setLine(Integer.valueOf(location.getLineNumber()));

        bizData.safeSetContent(event.getMessage().toString().trim());
        result.add(bizData);

        if (null != event.getThrowableInformation()) {
            String[] strRep = event.getThrowableInformation().getThrowableStrRep();
            List<StringBuilder> list = new ArrayList<>();
            StringBuilder s = new StringBuilder();
            for (String item : strRep) {
                if (s.length() + item.length() <= BizData.MAX_LEN_CONTENT) {
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

                    BizData clone = bizData.clone();
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
