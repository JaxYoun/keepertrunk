package com.troy.keeper.monomer.portal.account.repository;

import com.troy.keeper.monomer.portal.account.domain.DataDictionary;
import com.troy.keeper.core.base.repository.BaseRepository;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yg on 2017/4/17.
 */
public interface DataDictionaryRepository extends BaseRepository<DataDictionary, Serializable> {

    @Query("select dataDictionary from DataDictionary dataDictionary where dataDictionary.dicCode = ?1")
    public DataDictionary queryDataDictionaryByDicCode(String dicCode);

    @Query("select dataDictionary from DataDictionary dataDictionary where dataDictionary.dicCode like CONCAT(?1,'%') order by dataDictionary.orderCode asc")
    public List<DataDictionary> queryDataDictionariesByKey(String key);

//    public DataDictionary queryTest(List listQuery);
//    public void  test();
}
