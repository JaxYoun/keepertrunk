package com.troy.keeper.core.utils.db;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.entity.BaseAuditingEntity;
import com.troy.keeper.core.utils.string.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class DbUtils {
    private static Logger logger = LoggerFactory.getLogger(DbUtils.class);

    public class Constant {
        public static final String STATUS = "1";
        public static final String KEY_STATUS = "status";
        public   static  final String  METHOD_GET = "get";
    }


    /**
     * 获取getPredicateList
     */
    public  static  <D extends BaseDTO,T extends BaseAuditingEntity> List<Predicate> getPredicateList(Root root, CriteriaQuery query, CriteriaBuilder cb, D d, Class<T> clazz, String exclude){
        List<Predicate> list = new ArrayList<>();
        list.add(cb.equal(root.get(Constant.KEY_STATUS).as(String.class), Constant.STATUS));
        for( Method m :d.getClass().getMethods()){
            if(m.getName().startsWith(Constant.METHOD_GET)){
                try {
                    Method method = clazz.getMethod(m.getName());
                    if(method != null){
                        String key = StringUtils.toLowerCaseFirstOne(m.getName().replace(Constant.METHOD_GET,""));
                        if(exclude.indexOf(key)>-1)
                            continue;
                        Object o = m.invoke(d);
                        if(o == null )
                            continue;
                        Predicate predicate = cb.equal(root.get(key).as(m.getReturnType()),o);
                        list.add(predicate);
                    }
                } catch (Exception e) {
                    logger.info("获取方法执行失败！！");
                }
            }
        }
        return list;
    }
    /**
     * 获取getPredicateList
     */
    public  static  <D extends BaseDTO,T extends BaseAuditingEntity> List<Predicate> getPredicateList(Root root, CriteriaQuery query, CriteriaBuilder cb, D d, Class<T> clazz){
        List<Predicate> list = new ArrayList<>();
        list.add(cb.equal(root.get(Constant.KEY_STATUS).as(String.class), Constant.STATUS));
       for( Method m :d.getClass().getMethods()){
           if(m.getName().startsWith(Constant.METHOD_GET)){
               try {
                   Method method = clazz.getMethod(m.getName());
                   if(method != null){
                       String key = StringUtils.toLowerCaseFirstOne(m.getName().replace(Constant.METHOD_GET,""));
                       Object o = m.invoke(d);
                       if(o == null )
                           continue;
                       Predicate predicate = cb.equal(root.get(key).as(m.getReturnType()),o);
                       list.add(predicate);
                   }
               } catch (Exception e) {
                   logger.info("获取方法执行失败！！");
               }
           }
       }
        return list;
    }
}
