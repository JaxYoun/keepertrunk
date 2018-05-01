package com.troy.keeper.monomer.demo.test;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Method;

/**
 * Created by yjm on 2017/6/7.
 */
//@Component
public class HibernateEvent {

//
//    @PersistenceContext
//    protected EntityManager em;
//
//    @Autowired
//    private OperListener operListener;
//
//
//
//    @PostConstruct
//    public void registerListeners() {
//        EntityManagerFactory emf = em.getEntityManagerFactory();
//        Assert.state(emf != null, "EntityManagerFactory must not be null");
//
//        try {
//            Method ex = emf.getClass().getMethod("getSessionFactory", new Class[0]);
//            SessionFactory sessionFactory =  (SessionFactory) ReflectionUtils.invokeMethod(ex, emf);
//            EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry()
//                    .getService(EventListenerRegistry.class);
////            registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(operListener);//对实体保存的监听
////            registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(operListener);//对实体修改的监听
////            registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(operListener);//对实体删除的监听
//        } catch (NoSuchMethodException var3) {
//            throw new IllegalStateException("No compatible Hibernate EntityManagerFactory found: " + var3);
//        }
//    }
}
