package org.tbm.common.utils;

/**
 * Created by Jason.Xia on 17/6/13.
 */
public class StringUtils {
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }
}
