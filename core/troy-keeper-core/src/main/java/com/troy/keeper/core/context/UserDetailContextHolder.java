package com.troy.keeper.core.context;

import com.troy.keeper.core.utils.string.StringUtils;

/**
 * Created by Harry on 2017/9/28.
 */
public abstract class UserDetailContextHolder {

    private static final ThreadLocal<String> typeHolder = new  ThreadLocal<>();

    public static String getType() {
        return typeHolder.get();
    }

    public static void setType(String type) {
        if (StringUtils.isNoneEmpty(type)) {
            typeHolder.set(type);
        }
    }
}
