package com.troy.uaa.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Created by yjm on 2017/4/12.
 */
@NoRepositoryBean
public interface BaseRepository<T, I extends Serializable> extends PagingAndSortingRepository<T, I>, JpaSpecificationExecutor<T> {
}
