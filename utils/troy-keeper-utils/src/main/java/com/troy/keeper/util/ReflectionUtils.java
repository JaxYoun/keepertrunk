package com.troy.keeper.util;

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ReflectionUtils {
    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(),e);
        }
        return result;
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(),e);
        }
    }

    protected static Field getDeclaredField(Object object, String fieldName) {
        Validate.notNull(object, "object can't be null.");
        return getDeclaredField(object.getClass(), fieldName);
    }

    protected static Field getDeclaredField(Class clazz, String fieldName) {
        Validate.notNull(clazz, "clazz can't be null.");
        Validate.notEmpty(fieldName, "fieldName");
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass())
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        return null;
    }

    protected static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers())))
            field.setAccessible(true);
    }

    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if ((index >= params.length) || (index < 0)) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);

            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    @SuppressWarnings("unchecked")
    public static List fetchElementPropertyToList(Collection collection, String propertyName) throws Exception {
        List list = new ArrayList();

        for (Iterator i$ = collection.iterator(); i$.hasNext();) {
            Object obj = i$.next();
            list.add(PropertyUtils.getProperty(obj, propertyName));
        }

        return list;
    }

    public static String fetchElementPropertyToString(Collection collection, String propertyName, String separator) throws Exception {
        List list = fetchElementPropertyToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }
    /**
     * 获取一些列属性值
     * @param bean
     * @param properties
     * @return
     * @throws Exception
     */
    public static String[] getSimpleProperties(Object bean,String... properties) throws Exception{
        String[] values=new String[properties.length];
        for (int i=0; i<values.length;i++){
            values[i]= BeanUtils.getSimpleProperty(bean, properties[i]);
        }
        return values;
    }

    /**
     * 返回属性toString值
     * @param object
     * @param fieldNames
     * @return
     */
    public static Map<String,String> getFieldStringValues(Object object, String[] fieldNames) {
        Validate.notEmpty(fieldNames,"fieldNames can't be null");
        Map<String,String> result= Maps.newHashMap();
        for (String attr:fieldNames){
            Object obj=getFieldValue(object,attr);
            result.put(attr,obj==null?"":obj.toString());
        }
        return result;
    }
}