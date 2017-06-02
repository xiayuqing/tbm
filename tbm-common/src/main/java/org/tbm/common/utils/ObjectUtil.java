package org.tbm.common.utils;

import java.util.List;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class ObjectUtil {
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
}
