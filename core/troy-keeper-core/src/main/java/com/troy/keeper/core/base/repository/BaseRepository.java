//~ Formatted by Jindent --- http://www.jindent.com


/**
 * @Title: BaseDao.java
 * @author guohongjin
 * @date 2015/04/23 
 * @Copyright:  www.yineng.com.cn Inc. All rights reserved.
 * @version v1.0
 */

package com.troy.keeper.core.base.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * @interface BaseDao
 * @Description 基础dao接口
 * @Date 2015/04/23
 */
@NoRepositoryBean
public interface BaseRepository<T, I extends Serializable> extends PagingAndSortingRepository<T, I>,JpaSpecificationExecutor<T> {


}
