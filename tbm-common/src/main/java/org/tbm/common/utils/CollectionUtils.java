package org.tbm.common.utils;

import java.util.Collection;

/**
 * Created by Jason.Xia on 17/6/5.
 */
public class CollectionUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }
}
