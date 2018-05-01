package com.troy.keeper.monomer.demo.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Method;

/**
 * Created by yjm on 2017/6/7.
 */
@Component
public class SaveLog<T> {

    @PersistenceContext
    protected EntityManager em;

    public void saveInfo(T t){
        EntityManagerFactory emf = em.getEntityManagerFactory();
        Assert.state(emf != null, "EntityManagerFactory must not be null");

        Method ex = null;
        Transaction transaction = null;
        Session session = null;
        try {
            ex = emf.getClass().getMethod("getSessionFactory", new Class[0]);
            SessionFactory sessionFactory =  (SessionFactory) ReflectionUtils.invokeMethod(ex, emf);
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();//开始事物
            session.save(t);
            transaction.commit();
        } catch (NoSuchMethodException e) {
            if(transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            session.close();
        }

    }
}
