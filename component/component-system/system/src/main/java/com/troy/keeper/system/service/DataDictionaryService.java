package com.troy.keeper.system.service;


import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseService;
import com.troy.keeper.system.domain.DataDictionary;
import com.troy.keeper.system.dto.DataDictionaryDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by yg on 2017/4/17.
 */
public interface DataDictionaryService extends BaseService<DataDictionary, BaseDTO> {

    /**
     * 前端接口：查询列表（分页）
     *
     * @param dataDictionaryDTO
     * @return
     * @throws Exception
     */
    public Page<Map<String, Object>> getPage(DataDictionaryDTO dataDictionaryDTO) throws Exception;

    /**
     * 前端接口：查询列表
     *
     * @param dataDictionaryDTO
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getList(DataDictionaryDTO dataDictionaryDTO) throws Exception;

    /**
     * 后端接口：查询pageObj
     *
     * @param dataDictionaryDTO
     * @return
     */
    public Page<DataDictionary> pageObj(DataDictionaryDTO dataDictionaryDTO);

    /**
     * 后端接口：查询listObj
     *
     * @param dataDictionaryDTO
     * @return
     */
    public List<DataDictionary> listObj(DataDictionaryDTO dataDictionaryDTO);

    /**
     * 前端接口：检查重复
     *
     * @param dataDictionaryDTO
     * @return
     */
    public boolean checkReport(DataDictionaryDTO dataDictionaryDTO);

    public void addDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo);

    public void editDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo);

    public void deleteDataDictionary(String dicCode);

    public DataDictionary queryDataDictionaryByDicCode(String dicCode);

    public List<DataDictionary> queryDataDictionaries();

    public List<DataDictionary> queryDataDictionariesByKey(String key);
}
