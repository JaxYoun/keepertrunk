package com.troy.keeper.monomer.demo.service.impl;

import com.troy.keeper.monomer.api.dto.DataDictionaryDTO;
import com.troy.keeper.monomer.demo.domain.DataDictionary;
import com.troy.keeper.monomer.demo.repository.DataDictionaryRepository;
import com.troy.keeper.monomer.demo.service.DataDictionaryService;
import com.troy.keeper.monomer.demo.service.mapper.DataDictionaryMapper;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yg on 2017/4/17.
 */
@Service
public class DataDictionaryServiceImpl extends BaseServiceImpl<DataDictionary, DataDictionaryDTO> implements DataDictionaryService {
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;
    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;


    @Override
    @Transactional
    public void addDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo) {
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setDicCode(dicCode);
        dataDictionary.setDicValue(dicValue);
        dataDictionary.setOrderCode(orderCode);
        dataDictionary.setMemo(memo);
        dataDictionaryRepository.save(dataDictionary);
    }

    @Override
    @Transactional
    public void editDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo) {
        DataDictionary dataDictionary = dataDictionaryRepository.queryDataDictionaryByDicCode(dicCode);
        if(dataDictionary != null){
            if(dicValue != null){
                dataDictionary.setDicValue(dicValue);
            }
            if(orderCode != null){
                dataDictionary.setOrderCode(orderCode);
            }
            if(memo != null){
                dataDictionary.setMemo(memo);
            }
            dataDictionaryRepository.save(dataDictionary);
        }
    }

    @Override
    @Transactional
    public void deleteDataDictionary(String dicCode) {
        DataDictionary dataDictionary = dataDictionaryRepository.queryDataDictionaryByDicCode(dicCode);
        if(dataDictionary != null){
            dataDictionaryRepository.delete(dataDictionary);
        }
    }

    @Override
    public DataDictionaryDTO queryDataDictionaryByDicCode(String dicCode) {
        DataDictionary dataDictionary = dataDictionaryRepository.queryDataDictionaryByDicCode(dicCode);
        if(dataDictionary != null){
            return dataDictionaryMapper.dataDictionaryToDataDictionaryDTO(dataDictionary);
        }
        return null;
    }

    @Override
    public List<DataDictionaryDTO> queryDataDictionaries() {
        Sort sort = new Sort(Sort.Direction.ASC, "orderCode");
        Iterable<DataDictionary> itDD = dataDictionaryRepository.findAll(sort);
        List<DataDictionary> list = new ArrayList<DataDictionary>();
        if(itDD != null){
            Iterator<DataDictionary> it = itDD.iterator();
            for (;it.hasNext();){
                DataDictionary dataDictionary = it.next();
                list.add(dataDictionary);
            }
        }
        return dataDictionaryMapper.dataDictionariesToDataDictionaryDTOs(list);
    }

    @Override
    public List<DataDictionaryDTO> queryDataDictionariesByKey(String key) {
        List<DataDictionary> list = dataDictionaryRepository.queryDataDictionariesByKey(key);
        if(list != null && !list.isEmpty()){
            return dataDictionaryMapper.dataDictionariesToDataDictionaryDTOs(list);
        }
        return null;
    }

    @Override
    @Transactional
    public DataDictionary save(DataDictionary dataDictionary) {
         return dataDictionaryRepository.save(dataDictionary);
    }
}
