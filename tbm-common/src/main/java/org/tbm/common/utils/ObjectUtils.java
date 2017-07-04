package org.tbm.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason.Xia on 17/6/2.
 */
public class ObjectUtils {
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
     *
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
     *
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
