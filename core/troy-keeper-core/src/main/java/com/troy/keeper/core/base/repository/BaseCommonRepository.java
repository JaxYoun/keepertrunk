package com.troy.keeper.core.base.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yg on 2017/7/11.
 */
public class BaseCommonRepository {
    @PersistenceContext
    protected EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    /**
     * 添加
     * @param entity
     * @return
     */
    @Transactional
    public Object add(Object entity){
        this.em.persist(entity);
        return entity;
    }

    /**
     * 添加多个
     * @param entities
     * @return
     */
    @Transactional
    public Object add(Iterable entities){
        ArrayList result = new ArrayList();
        if(entities == null) {
            return result;
        } else {
            Iterator var3 = entities.iterator();
            while(var3.hasNext()) {
                Object entity = var3.next();
                result.add(this.add(entity));
            }
            return result;
        }
    }

    @Transactional
    public void customAddBatch(Iterable entities, int batchSize){
        if(entities == null || batchSize == 0) {
            return;
        } else {
            Iterator var3 = entities.iterator();
            int count = 0;
            while(var3.hasNext()) {
                Object entity = var3.next();
                em.persist(entity);
                count++;
                if (count % batchSize == 0) {
                    em.flush();
                    em.close();
                }
            }
            em.flush();
            em.close();
        }
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    @Transactional
    public Object update(Object entity){
        this.em.merge(entity);
        return entity;
    }

    /**
     * 修改多个
     * @param entities
     * @return
     */
    @Transactional
    public Object update(Iterable entities){
        ArrayList result = new ArrayList();
        if(entities == null) {
            return result;
        } else {
            Iterator var3 = entities.iterator();
            while(var3.hasNext()) {
                Object entity = var3.next();
                result.add(this.update(entity));
            }
            return result;
        }
    }

    /**
     * 根据hql更新
     * @param hql
     * @return
     */
    @Transactional
    public int updateByHql(String hql){
        Query query = this.em.createQuery(hql);
        return query.executeUpdate();
    }

    /**
     * 删除
     * @param entity
     * @return
     */
    @Transactional
    public void delete(Object entity){
        this.em.remove(this.em.contains(entity)?entity:this.em.merge(entity));
    }

    /**
     * 通过id删除
     * @param id
     * @param domainType
     */
    @Transactional
    public void delete(Serializable id, Class domainType){
        Object entity = this.findOne(id, domainType);
        if(entity == null) {
            throw new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!", new Object[]{domainType, id}), 1);
        } else {
            this.delete(entity);
        }
    }

    @Transactional
    public void deleteInBatch(Iterable entities){
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        if(entities.iterator().hasNext()) {
            String entityName = entities.iterator().next().getClass().getSimpleName();
            QueryUtils.applyAndBind(QueryUtils.getQueryString("delete from %s x", entityName), entities, this.em).executeUpdate();
        }
    }



    /**
     * 通过id查询
     * @param id
     * @param domainType
     * @return
     */
    public Object findOne(Serializable id, Class domainType){
        return this.em.find(domainType, id);
    }

    /**
     * 查询一条(通过条件)
     * @param spec
     * @return
     */
    public Object findOne(Class domainType, Specification spec) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(domainType);
        Root root = query.from(domainType);
        if(spec != null){
            Predicate predicate = spec.toPredicate(root, query, builder);
            if(predicate != null) {
                query.where(predicate);
            }
        }
        query.select(root);
        try {
            return this.em.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 查询所有
     * @param domainType
     * @return
     */
    public List findAll(Class domainType){
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(domainType);
        Root root = query.from(domainType);
        query.select(root);
        return this.em.createQuery(query).getResultList();
    }

    /**
     * 查询所有(分页)
     * @param pageable
     * @param domainType
     * @return
     */
    public Page findAll(Pageable pageable, Class domainType) {
        if(pageable == null){
            return new PageImpl(this.findAll(domainType));
        }
        else{
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            CriteriaQuery cquery = builder.createQuery(domainType);
            Root root = cquery.from(domainType);
            cquery.select(root);
            Sort sort = pageable.getSort();
            if(sort != null) {
                cquery.orderBy(QueryUtils.toOrders(sort, root, builder));
            }
            TypedQuery query = this.em.createQuery(cquery);
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> BaseCommonRepository.executeCountQuery(BaseCommonRepository.this.getCountQuery(null, domainType)).longValue());
        }
    }

    /**
     * 条件查询所有(分页)
     * @param pageable
     * @param domainType
     * @param spec
     * @return
     */
    public Page findAll(Pageable pageable, Class domainType, Specification spec) {
        if(pageable == null){
            return new PageImpl(this.findAll(domainType, spec));
        }
        else{
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            CriteriaQuery cquery = builder.createQuery(domainType);
            Root root = cquery.from(domainType);
            if(spec != null){
                Predicate predicate = spec.toPredicate(root, cquery, builder);
                if(predicate != null) {
                    cquery.where(predicate);
                }
            }
            cquery.select(root);
            Sort sort = pageable.getSort();
            if(sort != null) {
                cquery.orderBy(QueryUtils.toOrders(sort, root, builder));
            }
            TypedQuery query = this.em.createQuery(cquery);
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> BaseCommonRepository.executeCountQuery(BaseCommonRepository.this.getCountQuery(spec, domainType)).longValue());
        }
    }

    /**
     * 条件查询所有(分页)  自己传count
     * @param pageable
     * @param domainType
     * @param spec
     * @param count
     * @return
     */
    public Page findAll(Pageable pageable, Class domainType, Specification spec, Long count) {
        if(pageable == null){
            return new PageImpl(this.findAll(domainType, spec));
        }
        else{
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            CriteriaQuery cquery = builder.createQuery(domainType);
            Root root = cquery.from(domainType);
            if(spec != null){
                Predicate predicate = spec.toPredicate(root, cquery, builder);
                if(predicate != null) {
                    cquery.where(predicate);
                }
            }
            cquery.select(root);
            Sort sort = pageable.getSort();
            if(sort != null) {
                cquery.orderBy(QueryUtils.toOrders(sort, root, builder));
            }
            TypedQuery query = this.em.createQuery(cquery);
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> count);
        }
    }

    /**
     * 条件查询所有(分页)  自己传count
     * @param pageable
     * @param domainType
     * @param spec   查询条件(可以包含fetch)
     * @param countSpec  查询count的条件(不包含fetch)
     * @return
     */
    public Page findAll(Pageable pageable, Class domainType, Specification spec, Specification countSpec) {
        if(pageable == null){
            return new PageImpl(this.findAll(domainType, spec));
        }
        else{
            CriteriaBuilder builder = this.em.getCriteriaBuilder();
            CriteriaQuery cquery = builder.createQuery(domainType);
            Root root = cquery.from(domainType);
            if(spec != null){
                Predicate predicate = spec.toPredicate(root, cquery, builder);
                if(predicate != null) {
                    cquery.where(predicate);
                }
            }
            cquery.select(root);
            Sort sort = pageable.getSort();
            if(sort != null) {
                cquery.orderBy(QueryUtils.toOrders(sort, root, builder));
            }
            TypedQuery query = this.em.createQuery(cquery);
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
            return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> BaseCommonRepository.executeCountQuery(BaseCommonRepository.this.getCountQuery(countSpec, domainType)).longValue());
        }
    }

    /**
     * 获取查询条数的hql
     * @param spec
     * @param domainClass
     * @return
     */
    protected TypedQuery<Long> getCountQuery(Specification spec, Class domainClass) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Long.class);
        Root root = query.from(domainClass);
        if(spec != null){
            Predicate predicate = spec.toPredicate(root, query, builder);
            if(predicate != null) {
                query.where(predicate);
            }
        }
        if(query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }
        query.orderBy(Collections.emptyList());
        return this.em.createQuery(query);
    }

    /**
     * 执行查询条数的语句，返回条数
     * @param query
     * @return
     */
    private static Long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List totals = query.getResultList();
        Long total = Long.valueOf(0L);

        Long element;
        for(Iterator var3 = totals.iterator(); var3.hasNext(); total = Long.valueOf(total.longValue() + (element == null?0L:element.longValue()))) {
            element = (Long)var3.next();
        }

        return total;
    }

    /**
     * 条件查询所有
     * @param domainType
     * @param spec
     * @return
     */
    public List findAll(Class domainType, Specification spec){
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(domainType);
        Root root = query.from(domainType);
        if(spec != null){
            Predicate predicate = spec.toPredicate(root, query, builder);
            if(predicate != null) {
                query.where(predicate);
            }
        }
        query.select(root);
        return this.em.createQuery(query).getResultList();
    }

    /**
     * 条件查询所有并排序
     * @param domainType
     * @param spec
     * @param sort
     * @return
     */
    public List findAll(Class domainType, Specification spec, Sort sort){
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(domainType);
        Root root = query.from(domainType);
        if(spec != null){
            Predicate predicate = spec.toPredicate(root, query, builder);
            if(predicate != null) {
                query.where(predicate);
            }
        }
        query.select(root);
        if(sort != null) {
            query.orderBy(QueryUtils.toOrders(sort, root, builder));
        }
        return this.em.createQuery(query).getResultList();
    }

    /**
     * 根据hql查询list
     * @param hql
     * @param params
     * @return
     */
    public List findListByHql(String hql, Object[] params){
        Query query = this.em.createQuery(hql);
        if(params == null){
            return query.getResultList();
        }
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i , params[i]);
        }
        return query.getResultList();
    }

    /**
     * 根据hql查询list(限制条数)
     * @param hql
     * @param params
     * @param start
     * @param size
     * @return
     */
    public List findListByHql(String hql, Object[] params, int start, int size){
        Query query = this.em.createQuery(hql);
        query.setFirstResult(start);
        query.setMaxResults(size);
        if(params == null){
            return query.getResultList();
        }
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i , params[i]);
        }
        return query.getResultList();
    }

    /**
     * 根据hql查询单体
     * @param hql
     * @param params
     * @return
     */
    public Object findOneByHql(String hql, Object[] params){
        Query query = this.em.createQuery(hql);
        if(params == null){
            return query.getSingleResult();
        }
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i , params[i]);
        }
        return query.getSingleResult();
    }

    /**
     * 根据sql查询单体
     * @param sql
     * @param domainType
     * @return
     */
    public Object findOneBySql(String sql, Class domainType){
        Query query = this.em.createNativeQuery(sql, domainType);
        return query.getSingleResult();
    }

    /**
     * 根据sql查询list
     * @param sql
     * @param domainType
     * @return
     */
    public List findListBySql(String sql, Class domainType){
        Query query = this.em.createNativeQuery(sql, domainType);
        return query.getResultList();
    }

    /**
     * 根据sql查询list
     * @param sql
     * @return
     */
    public List findListBySql(String sql){
        Query query = this.em.createNativeQuery(sql);
        return query.getResultList();
    }
    /**
     * 执行sql
     * @param sql
     * @return
     */
    public int executeSql(String sql){
        Query query = this.em.createNativeQuery(sql);
        return query.executeUpdate();
    }
}
