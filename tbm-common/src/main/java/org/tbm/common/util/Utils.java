package org.tbm.common.util;

import org.tbm.common.bean.ValuePair;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jason.Xia on 17/6/5.
 */
public class Utils {

    public static void main(String[] args) {
        Date dateAfter = Utils.getDateAfter(new Date(), -29);
        String date = Utils.getDateString(dateAfter, "yyyyMMddHH");
        System.out.println(Long.valueOf(date));
    }

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static InetAddress getLocalAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new IllegalStateException(e);
        }
    }

    /***
     * key:ip
     * value:port
     * @param socketAddress
     * @return
     */
    public static ValuePair<String, Integer> convertHostInfo(SocketAddress socketAddress) {
        String substring = socketAddress.toString().substring(1);
        int indexOf = substring.indexOf(":");
        if (-1 != indexOf) {
            String[] split = substring.split(":");
            return new ValuePair<>(split[0], Integer.valueOf(split[1]));
        } else {
            return new ValuePair<>(substring, 0);
        }
    }

    public static String getUUIDWithoutStrike() {
        return UUID.randomUUID().toString().replace("-", "");
    }

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

    @SuppressWarnings("unchecked")
    public static <T> List<T> convertObject(List<Object> param, List<T> target) {
        if (null == param) {
            return null;
        }

        for (Object item : param) {
            target.add((T) item);
        }

        return target;
    }

    /**
     * @param objects
     * @param <T>
     * @return
     */
    public static <T> List<T> multiObjectConvertToList(T... objects) {
        if (null == objects) {
            return null;
        }

        List<T> result = new ArrayList<>();
        for (int i = 0; i < objects.length; i++) {
            result.add(objects[i]);
        }

        return result;
    }

    /**
     * @param object
     * @param <T>
     * @return
     */
    public static <T> List<T> singleObjectConvertToList(T object) {
        if (null == object) {
            return null;
        }

        List<T> result = new ArrayList<>();
        result.add(object);
        return result;
    }
}
