package com.troy.keeper.monomer.demo.domain;

import com.troy.keeper.monomer.demo.repository.DataDictionaryRepository;
import com.troy.keeper.monomer.demo.service.impl.DataDictionaryServiceImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 测试用的  没有建表  只是一个测试实体
 * Created by yg on 2017/5/25.
 */
public class Test<Long> extends ArrayList<String> {
	/**
    public static void main(String[] args){
//        DataDictionaryServiceImpl t=new DataDictionaryServiceImpl();
        System.out.println("sssss:"+getActualType(DataDictionaryRepository.class));
    } */
	
    public static Class getActualType(Class target){
        ParameterizedType parameterizedType = (ParameterizedType)target.getGenericSuperclass();
        Type[] types =  parameterizedType.getActualTypeArguments();
        Type type = types[0];
//        type.getClass();
        type.getTypeName();
        return parameterizedType.getActualTypeArguments()[0].getClass();
    }
}
