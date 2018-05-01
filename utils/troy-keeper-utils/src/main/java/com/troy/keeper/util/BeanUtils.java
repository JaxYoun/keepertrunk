package com.troy.keeper.util;

import org.dozer.DozerBeanMapper;

/**
 * Created by yjm on 2017/4/9.
 */
public class BeanUtils {
    private static DozerBeanMapper dozerMapper = new DozerBeanMapper();

    public BeanUtils() {
    }

    public static <T> T copyProperties(Class<T> destClass, Object source) {
        Object target = null;

        try {
            target = destClass.newInstance();
            copyProperties(source, target);
            return (T) target;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static void copyProperties(Object source, Object dest) {
        dozerMapper.map(source, dest);
    }
}
