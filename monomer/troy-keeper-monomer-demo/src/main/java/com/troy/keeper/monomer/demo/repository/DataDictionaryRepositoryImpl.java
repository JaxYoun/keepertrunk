//package com.troy.keeper.monomer.demo.repository;
//
//import com.troy.keeper.core.base.repository.BaseRepositoryImpl;
//import com.troy.keeper.monomer.demo.domain.DataDictionary;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.envers.AuditReader;
//import org.hibernate.envers.AuditReaderFactory;
//import org.hibernate.envers.DefaultRevisionEntity;
//import org.hibernate.envers.query.AuditEntity;
//import org.hibernate.envers.query.AuditQuery;
//import org.springframework.util.Assert;
//import org.springframework.util.ReflectionUtils;
//
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Query;
//import java.lang.reflect.Method;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by yjm on 2017/5/25.
// */
//public class DataDictionaryRepositoryImpl extends BaseRepositoryImpl {
//
//    public DataDictionary queryTest(List listQuery) {
//        String hql = "select dataDictionary from DataDictionary dataDictionary  ";
////        String hql = "select dataDictionary from DataDictionary dataDictionary where dataDictionary.dateLongTest = ?1 ";
//
////        String hql = "select dataDictionary from DataDictionary dataDictionary " +
////                "where dataDictionary.dateLongTest BETWEEN  ?1 and ?2 and dataDictionary.datelong = ?3" +
////                " and dataDictionary.instantLongTest = ?4";
////        String hql = "select dataDictionary from DataDictionary dataDictionary LEFT JOIN  dataDictionary.dateDictionaryChildList child" +
////                " where dataDictionary.dateLongTest BETWEEN  ?1 and ?2 and child.dateOne = ?3";
//
//        Query query = this.em.createQuery(hql);
////        DateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        Date start = null;
////        Date end = null;
////        try {
////            start = da.parse("2017-05-26 11:34:21");
////            end = da.parse("2017-05-26 11:35:00");
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
////
////        query.setParameter(1,start);
////        query.setParameter(2,end);
////        query.setParameter(3,new Date(1495789306000l));
//////
////        query.setParameter(3,new Date(1495713075729l));
////        query.setParameter(4,Instant.ofEpochSecond(1495713075l,124));
//        List<DataDictionary> o = query.getResultList();
//        return null;
//    }
//
//    public void test(){
//        EntityManagerFactory emf = em.getEntityManagerFactory();
//        Assert.state(emf != null, "EntityManagerFactory must not be null");
//
//        Method ex = null;
//        Transaction transaction = null;
//        Session session = null;
//        try {
//            ex = emf.getClass().getMethod("getSessionFactory", new Class[0]);
//            SessionFactory sessionFactory = (SessionFactory) ReflectionUtils.invokeMethod(ex, emf);
//            session = sessionFactory.openSession();
//
//            AuditQuery query = AuditReaderFactory.get(em).createQuery()
//                    .forRevisionsOfEntity(DataDictionary.class, false, true);
//
////        query.add(AuditEntity.revisionProperty("createdDate").gt(minDate))
////                .add(AuditEntity.revisionProperty("createdDate").lt(maxDate))
////                .add(AuditEntity.revisionProperty("createdBy").eq(userId));
//            query.add(AuditEntity.property("dicCode").eq("01"));//查询条件
//
//
////the documentation shows getSingleResult returns a number
////so i'm guessing a resultList also contains numbers
//            List<Object[]> resultList = query.getResultList();
////            Object object = query.getSingleResult();
//            System.out.println(resultList);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            session.close();
//        }
//    }
//
//
////    public static void main(String[] args) {
////        DateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        try {
////            Date noes = da.parse("2017-05-26 11:30:10");
////            Long s = noes.getTime();
////            Date ns = new Date(1495769662000l);
//////            Instant instant = Instant.ofEpochSecond(1495809222l);
////            System.out.println("1111:"+s);
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
////    }
//}
