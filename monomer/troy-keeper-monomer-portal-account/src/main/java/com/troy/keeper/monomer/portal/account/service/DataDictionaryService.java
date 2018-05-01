package com.troy.keeper.monomer.portal.account.service;

import com.troy.keeper.monomer.api.dto.DataDictionaryDTO;
import com.troy.keeper.monomer.portal.account.domain.DataDictionary;
import com.troy.keeper.core.base.service.BaseService;

import java.util.List;

/**
 * Created by yg on 2017/4/17.
 */
public interface DataDictionaryService extends BaseService<DataDictionary, DataDictionaryDTO> {
    public void addDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo);
    public void editDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo);
    public void deleteDataDictionary(String dicCode);
    public DataDictionaryDTO queryDataDictionaryByDicCode(String dicCode);
    public List<DataDictionaryDTO> queryDataDictionaries();
    public List<DataDictionaryDTO> queryDataDictionariesByKey(String key);

    public DataDictionary save(DataDictionary dataDictionary);
}
