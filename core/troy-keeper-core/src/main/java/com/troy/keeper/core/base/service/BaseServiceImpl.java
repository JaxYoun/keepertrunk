package com.troy.keeper.core.base.service;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;


public abstract class BaseServiceImpl<T,K extends BaseDTO> implements BaseService<T,K>   {

    private static Logger LOGGER= LoggerFactory.getLogger(BaseServiceImpl.class);


    @Autowired
    protected EntityManager entityManager;



    /**
     * 数据库实体bean
     */
    protected Class<T> entityClass;
    /**
     * 服务层实体bean
     */
    protected Class<K> voEntityClass;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass(), 0);
        this.voEntityClass = ReflectionUtils.getSuperClassGenricType(getClass(), 1);
    }


}
