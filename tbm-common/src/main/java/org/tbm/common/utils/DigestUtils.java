package org.tbm.common.utils;

import java.util.UUID;

/**
 * Created by Jason.Xia on 17/6/7.
 */
public class DigestUtils {
    public static String getUUIDWithoutStrike() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
