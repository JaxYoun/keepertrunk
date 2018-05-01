package com.troy.keeper.system.repository;

import com.troy.keeper.core.base.repository.BaseRepository;
import com.troy.keeper.system.domain.DataDictionary;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yg on 2017/4/17.
 */
public interface DataDictionaryRepository extends BaseRepository<DataDictionary, Serializable> {
    @Query("select dataDictionary from DataDictionary dataDictionary where dataDictionary.dicCode = ?1 ")
    public DataDictionary queryDataDictionaryByDicCode(String dicCode);

    @Query("select dataDictionary from DataDictionary dataDictionary where dataDictionary.dicCode like CONCAT(?1,'%') order by dataDictionary.orderCode asc")
    public List<DataDictionary> queryDataDictionariesByKey(String key);
}
