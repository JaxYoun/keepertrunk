//~ Formatted by Jindent --- http://www.jindent.com


/**
 * @Title: BaseDaoImpl.java
 * @author guohongjin
 * @date 2015/04/23 
 * @Copyright:  www.yineng.com.cn Inc. All rights reserved.
 * @version v1.0
 */

package com.troy.keeper.core.base.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @ClassName BaseDaoImpl
 * @Description 基础Dao实现
 * @Date 2015/04/23
 */
public class BaseRepositoryImpl {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    protected EntityManager em;
}
