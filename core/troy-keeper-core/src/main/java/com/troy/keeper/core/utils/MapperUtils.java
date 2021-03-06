package com.troy.keeper.core.utils;

import com.troy.keeper.core.base.service.RedisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * Created by yg on 2017/5/24.
 */
@Component
public class MapperUtils<T> {

    public static final Logger log = Logger.getLogger(MapperUtils.class);

    @Autowired
    private RedisService redisService;
    /**
     * 转换对象为map格式
     * @param mapperParam                 参数封装对象
     * @return
     * @throws Exception
     */
    public Map<String, Object> convert(MapperParam mapperParam) throws Exception{
        Object obj = mapperParam.getObj();
        List<String> ignoreColumnList = mapperParam.getIgnoreColumnList();
        List<String> keyList = mapperParam.getKeyList();
        List<String> sourceColumnList = mapperParam.getSourceColumnList();
        List<String> userIdColumnList = mapperParam.getUserIdColumnList();

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if(obj == null){
            return resultMap;
        }
        if(keyList != null && sourceColumnList == null){
            throw new Exception("keyList或者sourceColumnList参数错误!");
        }
        if(keyList == null && sourceColumnList != null){
            throw new Exception("keyList或者sourceColumnList参数错误!");
        }
        if(keyList != null && sourceColumnList != null){
            if(keyList.size() != sourceColumnList.size()){
                throw new Exception("keyList或者sourceColumnList参数错误!");
            }
        }
        Class clazz = obj.getClass();
        objectConvert(obj, ignoreColumnList, keyList, sourceColumnList, userIdColumnList, resultMap, clazz);
        return resultMap;
    }

    /**
     * Object的递归转换方法
     * @param obj
     * @param ignoreColumnList
     * @param keyList
     * @param sourceColumnList
     * @param userIdColumnList
     * @param resultMap
     * @param clazz
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void objectConvert(Object obj, List<String> ignoreColumnList, List<String> keyList, List<String> sourceColumnList, List<String> userIdColumnList, Map<String, Object> resultMap, Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            boolean isStatic = Modifier.isStatic(fields[i].getModifiers());
            if(isStatic){
                continue;
            }
            String fieldName = fields[i].getName();
            String fieldType = fields[i].getType().toString();
            if(ignoreColumnList == null || (ignoreColumnList != null && !ignoreColumnList.contains(fieldName))){
                Method m = null;
                if("class java.lang.Boolean".equals(fieldType)){
                    m = clazz.getMethod("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                }
                else{
                    m = clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                }
                Object value = m.invoke(obj);
                if(value != null){
                    if("class java.util.Date".equals(fieldType)){
                        Date valueD = (Date)value;
                        resultMap.put(fieldName, valueD.getTime());
                    }
                    else if("class java.sql.Timestamp".equals(fieldType)){
                        Timestamp valueT = (Timestamp)value;
                        resultMap.put(fieldName, valueT.getTime());
                    }
                    else if("class java.time.Instant".equals(fieldType)){
                        Instant valueI = (Instant)value;
                        resultMap.put(fieldName, valueI.getEpochSecond());
                    }
                    else{
                        resultMap.put(fieldName, value);
                    }

                    if(sourceColumnList != null && sourceColumnList.contains(fieldName)){
                        String key = keyList.get(sourceColumnList.indexOf(fieldName));
                        String columnKey = key + "." + value;
                        String columnKeyValue = null;
                        try{
                            columnKeyValue = redisService.get(columnKey);
                        }catch (Exception e){
                            log.error(e.getMessage() + "从redis获取" + columnKey + "的值失败!");
                        }
                        resultMap.put(fieldName + "Value", columnKeyValue);
                    }

                    if(userIdColumnList != null && userIdColumnList.contains(fieldName)){
                        String userIdKey = "user_" + value;
                        String columnKeyValue = null;
                        try{
                            columnKeyValue = redisService.get(userIdKey);
                        }catch (Exception e){
                            log.error(e.getMessage() + "从redis获取" + userIdKey + "的值失败!");
                        }
                        resultMap.put(fieldName + "Name", columnKeyValue);
                    }
                }
                else{
                    resultMap.put(fieldName, null);
                }
            }
        }
        Class superClazz = clazz.getSuperclass();
        if(superClazz != Object.class){
            objectConvert(obj, ignoreColumnList, keyList, sourceColumnList, userIdColumnList, resultMap, superClazz);
        }
    }

    /**
     * 转换对象list为List<Map<String, Object>>
     * @param mapperParam                 参数封装对象
     * @return
     * @throws Exception
     */
    public  List<Map<String, Object>> convertList(MapperParam mapperParam) throws Exception{
        List<Object> list = mapperParam.getList();
        List<String> ignoreColumnList = mapperParam.getIgnoreColumnList();
        List<String> keyList = mapperParam.getKeyList();
        List<String> sourceColumnList = mapperParam.getSourceColumnList();
        List<String> userIdColumnList = mapperParam.getUserIdColumnList();

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if(list == null || list.isEmpty()){
            return resultList;
        }
        if(keyList != null && sourceColumnList == null){
            throw new Exception("keyList或者sourceColumnList参数错误!");
        }
        if(keyList == null && sourceColumnList != null){
            throw new Exception("keyList或者sourceColumnList参数错误!");
        }
        if(keyList != null && sourceColumnList != null){
            if(keyList.size() != sourceColumnList.size()){
                throw new Exception("keyList或者sourceColumnList参数错误!");
            }
        }

        Map<String, Map<String, String>> keyMap = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Class clazz = obj.getClass();
            Map<String, Object> map = listConvert(ignoreColumnList, keyList, sourceColumnList, userIdColumnList, keyMap, obj, clazz);
            resultList.add(map);
        }
        return resultList;
    }

    /**
     * list的递归转换方法
     * @param ignoreColumnList
     * @param keyList
     * @param sourceColumnList
     * @param userIdColumnList
     * @param keyMap
     * @param obj
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Map<String, Object> listConvert(List<String> ignoreColumnList, List<String> keyList, List<String> sourceColumnList, List<String> userIdColumnList, Map<String, Map<String, String>> keyMap, Object obj, Class clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Field[] fields = clazz.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        Map<String, Object> map = new HashMap();
        Map<String, String> userIdKeyMap = new HashMap();
        for (int j = 0; j < fields.length; j++) {
            Map<String, String> valueMap = null;
            boolean isStatic = Modifier.isStatic(fields[j].getModifiers());
            if(isStatic){
                continue;
            }
            String fieldName = fields[j].getName();
            String fieldType = fields[j].getType().toString();
            if(ignoreColumnList == null || (ignoreColumnList != null && !ignoreColumnList.contains(fieldName))){
                Method m = null;
                if("class java.lang.Boolean".equals(fieldType)){
                    m = clazz.getMethod("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                }
                else{
                    m = clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                }
                Object value = m.invoke(obj);
                if(value != null){
                    if("class java.util.Date".equals(fieldType)){
                        Date valueD = (Date)value;
                        map.put(fieldName, valueD.getTime());
                    }
                    else if("class java.sql.Timestamp".equals(fieldType)){
                        Timestamp valueT = (Timestamp)value;
                        map.put(fieldName, valueT.getTime());
                    }
                    else if("class java.time.Instant".equals(fieldType)){
                        Instant valueI = (Instant)value;
                        map.put(fieldName, valueI.getEpochSecond());
                    }
                    else{
                        map.put(fieldName, value);
                    }

                    if(sourceColumnList != null && sourceColumnList.contains(fieldName)){
                        if(sourceColumnList != null && sourceColumnList.contains(fieldName)){
                            String key = keyList.get(sourceColumnList.indexOf(fieldName));
                            if(keyMap.containsKey(key)){
                                valueMap = keyMap.get(key);
                            }
                            else{
                                try{
                                    valueMap = redisService.hMGet(key);
                                    keyMap.put(key, valueMap);
                                }catch (Exception e){
                                    log.error(e.getMessage() + "从redis获取" + key + "的值失败!");
                                }
                            }
                            String columnKeyValue = null;
                            if(valueMap != null){
                                columnKeyValue = valueMap.get(String.valueOf(value));
                            }
                            map.put(fieldName + "Value", columnKeyValue);
                        }

                        if(userIdColumnList != null && userIdColumnList.contains(fieldName)){
                            String userIdKey = "user_" + value;
                            String columnKeyValue = null;
                            if(userIdKeyMap.containsKey(userIdKey)){
                                columnKeyValue = userIdKeyMap.get(userIdKey);
                            }
                            else{
                                try{
                                    columnKeyValue = redisService.get(userIdKey);
                                    userIdKeyMap.put(userIdKey, columnKeyValue);
                                }catch (Exception e){
                                    log.error(e.getMessage() + "从redis获取" + userIdKey + "的值失败!");
                                }
                            }
                            map.put(fieldName + "Name", columnKeyValue);
                        }
                    }
                }
                else{
                    map.put(fieldName, null);
                }
            }
        }
        Class superClazz = clazz.getSuperclass();
        if(superClazz != Object.class){
            Map<String, Object> superMap = listConvert(ignoreColumnList, keyList, sourceColumnList, userIdColumnList, keyMap, obj, superClazz);
            map.putAll(superMap);
        }
        return map;
    }

    /**
     * 转换对象page为Page<Map<String, Object>>
     * @param mapperParam                 参数封装对象
     * @return
     * @throws Exception
     */
    public Page<Map<String, Object>> convertPage(MapperParam mapperParam) throws Exception{
        Page<T> page = mapperParam.getPage();
        if(page == null){
            return null;
        }
        List<String> ignoreColumnList = mapperParam.getIgnoreColumnList();
        List<String> keyList = mapperParam.getKeyList();
        List<String> sourceColumnList = mapperParam.getSourceColumnList();
        List<String> userIdColumnList = mapperParam.getUserIdColumnList();

        if(keyList != null && sourceColumnList == null){
            throw new Exception("keyList或者sourceColumnList参数错误!");
        }
        if(keyList == null && sourceColumnList != null){
            throw new Exception("keyList或者sourceColumnList参数错误!");
        }
        if(keyList != null && sourceColumnList != null){
            if(keyList.size() != sourceColumnList.size()){
                throw new Exception("keyList或者sourceColumnList参数错误!");
            }
        }

        Page<Map<String, Object>> maps = page.map(new Converter<T, Map<String, Object>>() {
            @Override
            public Map<String, Object> convert(T t) {
                Map<String, Object> resultMap = new HashMap();
                Class clazz = t.getClass();
                pageConvert(t, ignoreColumnList, keyList, sourceColumnList, userIdColumnList, resultMap, clazz);
                return resultMap;
            }

            /**
             * page的递归转换方法
             * @param t
             * @param ignoreColumnList
             * @param keyList
             * @param sourceColumnList
             * @param userIdColumnList
             * @param resultMap
             * @param clazz
             */
            private void pageConvert(T t, List<String> ignoreColumnList, List<String> keyList, List<String> sourceColumnList, List<String> userIdColumnList, Map<String, Object> resultMap, Class clazz) {
                Field[] fields = clazz.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
                Map<String, String> userIdKeyMap = new HashMap();
                for (int i = 0; i < fields.length; i++) {
                    boolean isStatic = Modifier.isStatic(fields[i].getModifiers());
                    if(isStatic){
                        continue;
                    }
                    String fieldName = fields[i].getName();
                    String fieldType = fields[i].getType().toString();
                    if(ignoreColumnList == null || (ignoreColumnList != null && !ignoreColumnList.contains(fieldName))){
                        Method m = null;
                        try {
                            if("class java.lang.Boolean".equals(fieldType)){
                                m = clazz.getMethod("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                            }
                            else{
                                m = clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                            }
                            Object value = m.invoke(t);
                            if(value != null){
                                if("class java.util.Date".equals(fieldType)){
                                    Date valueD = (Date)value;
                                    resultMap.put(fieldName, valueD.getTime());
                                }
                                else if("class java.sql.Timestamp".equals(fieldType)){
                                    Timestamp valueT = (Timestamp)value;
                                    resultMap.put(fieldName, valueT.getTime());
                                }
                                else if("class java.time.Instant".equals(fieldType)){
                                    Instant valueI = (Instant)value;
                                    resultMap.put(fieldName, valueI.getEpochSecond());
                                }
                                else{
                                    resultMap.put(fieldName, value);
                                }

                                if(sourceColumnList != null && sourceColumnList.contains(fieldName)){
                                    String key = keyList.get(sourceColumnList.indexOf(fieldName));
                                    String columnKey = key + "." + value;
                                    String columnKeyValue = null;
                                    try{
                                        columnKeyValue = redisService.get(columnKey);
                                    }catch (Exception e){
                                        log.error(e.getMessage() + "从redis获取" + columnKey + "的值失败!");
                                    }
                                    resultMap.put(fieldName + "Value", columnKeyValue);
                                }

                                if(userIdColumnList != null && userIdColumnList.contains(fieldName)){
                                    String userIdKey = "user_" + value;
                                    String columnKeyValue = null;
                                    if(userIdKeyMap.containsKey(userIdKey)){
                                        columnKeyValue = userIdKeyMap.get(userIdKey);
                                    }
                                    else{
                                        try{
                                            columnKeyValue = redisService.get(userIdKey);
                                            userIdKeyMap.put(userIdKey, columnKeyValue);
                                        }catch (Exception e){
                                            log.error(e.getMessage() + "从redis获取" + userIdKey + "的值失败!");
                                        }
                                    }
                                    resultMap.put(fieldName + "Name", columnKeyValue);
                                }
                            }
                            else{
                                resultMap.put(fieldName, null);
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }
                Class superClazz = clazz.getSuperclass();
                if(superClazz != Object.class){
                    pageConvert(t, ignoreColumnList, keyList, sourceColumnList, userIdColumnList, resultMap, superClazz);
                }
            }
        });
        return maps;
    }

    /**
     * 转换对象为map格式(默认去掉审计字段的方法)
     * @param mapperParam                 参数封装对象
     * @return
     * @throws Exception
     */
    public Map<String, Object> convertDefault(MapperParam mapperParam) throws Exception{
        List<String> ignoreColumnList = getDefaultIgnoreColumnList();
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        return convert(mapperParam);
    }

    /**
     * 转换对象list为List<Map<String, Object>> (默认去掉审计字段的方法)
     * @param mapperParam                 参数封装对象
     * @return
     * @throws Exception
     */
    public  List<Map<String, Object>> convertListDefault(MapperParam mapperParam) throws Exception{
        List<String> ignoreColumnList = getDefaultIgnoreColumnList();
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        return convertList(mapperParam);
    }

    /**
     * 转换对象page为Page<Map<String, Object>> (默认去掉审计字段的方法)
     * @param mapperParam                 参数封装对象
     * @return
     * @throws Exception
     */
    public Page<Map<String, Object>> convertPageDefault(MapperParam mapperParam) throws Exception{
        List<String> ignoreColumnList = getDefaultIgnoreColumnList();
        mapperParam.setIgnoreColumnList(ignoreColumnList);
        return convertPage(mapperParam);
    }

    /**
     * 设置默认过滤字段list
     * @return
     */
    private List<String> getDefaultIgnoreColumnList() {
        List<String> ignoreColumnList = new ArrayList<String>();
        ignoreColumnList.add("createdBy");
        ignoreColumnList.add("createdDate");
        ignoreColumnList.add("lastUpdatedBy");
        ignoreColumnList.add("lastUpdatedDate");
        return ignoreColumnList;
    }
}