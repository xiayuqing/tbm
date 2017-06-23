package org.tbm.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JasonX on 16/10/27.
 */
public class DateUtils {

    public static Date getTargetDateWith(int after) {
        return getTargetDateWith(null, after);
    }

    public static Date getTargetDateWith(Date start, int after) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(null == start ? new Date() : start);
        instance.add(Calendar.DAY_OF_MONTH, after);
        try {
            return parseDate(instance.getTime(), "yyyy-MM-dd 00:00:00");
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Date parseDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return date == null ? null : sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static String getDateString(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static void setHoursTime(Date date, Integer hours, Integer minutes, Integer seconds) {
        if (null == date) {
            return;
        }

        if (null != hours) {
            date.setHours(hours);
        }

        if (null != minutes) {
            date.setMinutes(minutes);
        }

        if (null != seconds) {
            date.setSeconds(seconds);
        }
    }
}
