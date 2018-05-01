package com.troy.keeper.monomer.demo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.troy.keeper.monomer.demo.domain.LogType;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * Created by yjm on 2017/6/7.
 */
@Component
public class OperListener implements PostCommitDeleteEventListener,PostCommitInsertEventListener,PostCommitUpdateEventListener {

    @Autowired
    private SaveLog saveLog;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onPostDeleteCommitFailed(PostDeleteEvent event) {

    }

    @Override
    public void onPostInsertCommitFailed(PostInsertEvent event) {

    }

    @Override
    public void onPostUpdateCommitFailed(PostUpdateEvent event) {
        System.out.println("update lis start..........");
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        System.out.println("delete entity log..........");
        Object entity = event.getEntity();
        LogInfo(entity,LogType.DELETE);
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        System.out.println("insert entity log..........");
        Object entity = event.getEntity();
        LogInfo(entity,LogType.ADD);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        System.out.println("update entity log..........");
        Object entity = event.getEntity();
        LogInfo(entity,LogType.UPDATE);
    }

    private void LogInfo(Object entity,LogType logType){
        Class<?> clazz = entity.getClass();

        Field[] fields = clazz.getDeclaredFields();

        AccessibleObject.setAccessible(fields, true);

        Comment field = clazz.getAnnotation(Comment.class);
        if (field != null){
            try {
                Class classRepository = Class.forName(field.saveFor().getName());
                Object object  = classRepository.newInstance();//实例化对象
                saveLog.saveInfo(objSetValue(object,classRepository,entity,logType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private Object objSetValue(Object clazzObj, Class clazz, Object entity, LogType logType) throws Exception{
        Field[] fields = clazz.getDeclaredFields();
        String entityJson =  mapper.writeValueAsString(entity);
        for (Field field : fields) {
                field.setAccessible(true);
                String fileName = field.getName();
                if (fileName.equals("context")){
                    field.set(clazzObj,entityJson);
                }else if(fileName.equals("tableName")){
                    field.set(clazzObj,"test");
                }else if (fileName.equals("type")){
                    field.set(clazzObj,logType);
                }
        }
        return clazzObj;
    }



    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }
}
