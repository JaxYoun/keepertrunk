package com.troy.keeper.system.service.impl;

import com.troy.keeper.core.base.dto.BaseDTO;
import com.troy.keeper.core.base.service.BaseServiceImpl;
import com.troy.keeper.core.utils.MapperParam;
import com.troy.keeper.core.utils.MapperUtils;
import com.troy.keeper.system.config.Const;
import com.troy.keeper.system.domain.DataDictionary;
import com.troy.keeper.system.dto.DataDictionaryDTO;
import com.troy.keeper.system.repository.DataDictionaryRepository;
import com.troy.keeper.system.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yg on 2017/4/17.
 */
@Service
public class DataDictionaryServiceImpl extends BaseServiceImpl<DataDictionary, BaseDTO> implements DataDictionaryService {
    @Autowired
    private DataDictionaryRepository dataDictionaryRepository;

    @Autowired
    private MapperUtils mapperUtils;

    /**
     * 前端接口：查询列表（分页）
     *
     * @param dataDictionaryDTO
     * @return
     * @throws Exception
     */
    public Page<Map<String, Object>> getPage(DataDictionaryDTO dataDictionaryDTO) throws Exception {
        MapperParam<DataDictionary> mapperParam = new MapperParam<>();
        mapperParam.setPage(pageObj(dataDictionaryDTO));
        return mapperUtils.convertPageDefault(mapperParam);
    }

    /**
     * 前端接口：查询列表
     *
     * @param dataDictionaryDTO
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getList(DataDictionaryDTO dataDictionaryDTO) throws Exception {
        MapperParam<DataDictionary> mapperParam = new MapperParam<>();
        mapperParam.setList(listObj(dataDictionaryDTO));
        return mapperUtils.convertListDefault(mapperParam);
    }

    /**
     * 前端接口：检查重复
     *
     * @param dataDictionaryDTO
     * @return
     */
    public boolean checkReport(DataDictionaryDTO dataDictionaryDTO) {
        Specification<DataDictionary> spec = (root, query, cb) -> query.where(cb.equal(root.get("dicCode").as(String.class), dataDictionaryDTO.getDicCode())).getRestriction();
        List<DataDictionary> result = dataDictionaryRepository.findAll(spec);
        return result.isEmpty();
    }

    /**
     * 后端接口：查询pageObj
     *
     * @param dataDictionaryDTO
     * @return
     */
    public Page<DataDictionary> pageObj(DataDictionaryDTO dataDictionaryDTO) {
        PageRequest pageRequest = new PageRequest(dataDictionaryDTO.getPage(), dataDictionaryDTO.getSize());
        return dataDictionaryRepository.findAll(getNormalSpec(dataDictionaryDTO), pageRequest);
    }

    /**
     * 后端接口：查询listObj
     *
     * @param dataDictionaryDTO
     * @return
     */
    public List<DataDictionary> listObj(DataDictionaryDTO dataDictionaryDTO) {
        return dataDictionaryRepository.findAll(getNormalSpec(dataDictionaryDTO));
    }

    /**
     * 获取默认条件
     *
     * @param dataDictionaryDTO
     * @return
     */
    public Specification<DataDictionary> getNormalSpec(DataDictionaryDTO dataDictionaryDTO) {
        return (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(dataDictionaryDTO.getDicCode())) {
                list.add(cb.like(root.get("dicCode").as(String.class), "%" + dataDictionaryDTO.getDicCode() + "%"));
            }
            Predicate[] predicates = new Predicate[list.size()];
            return cb.and(list.toArray(predicates));
        };
    }

    /**
     * 前端方法：新增数据字典
     *
     * @param dicCode
     * @param dicValue
     * @param orderCode
     * @param memo
     */
    @Override
    @Transactional
    public void addDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo) {
        DataDictionary dataDictionary = new DataDictionary();
        dataDictionary.setDicCode(dicCode);
        dataDictionary.setDicValue(dicValue);
        dataDictionary.setOrderCode(orderCode);
        dataDictionary.setMemo(memo);
        dataDictionary.setStatus(Const.VALID_DATA_STATUS);
        dataDictionaryRepository.save(dataDictionary);
    }

    @Override
    @Transactional
    public void editDataDictionary(String dicCode, String dicValue, Integer orderCode, String memo) {
        DataDictionary dataDictionary = dataDictionaryRepository.queryDataDictionaryByDicCode(dicCode);
        if (dataDictionary != null) {
            if (dicValue != null) {
                dataDictionary.setDicValue(dicValue);
            }
            if (orderCode != null) {
                dataDictionary.setOrderCode(orderCode);
            }
            if (memo != null) {
                dataDictionary.setMemo(memo);
            }
            dataDictionaryRepository.save(dataDictionary);
        }
    }

    @Override
    @Transactional
    public void deleteDataDictionary(String dicCode) {
        DataDictionary dataDictionary = dataDictionaryRepository.queryDataDictionaryByDicCode(dicCode);
        if (dataDictionary != null) {
            dataDictionaryRepository.delete(dataDictionary);
        }
    }

    @Override
    public DataDictionary queryDataDictionaryByDicCode(String dicCode) {
        return dataDictionaryRepository.queryDataDictionaryByDicCode(dicCode);
    }

    @Override
    public List<DataDictionary> queryDataDictionaries() {
        Sort sort = new Sort(Sort.Direction.ASC, "orderCode");
        Iterable<DataDictionary> itDD = dataDictionaryRepository.findAll(sort);
        List<DataDictionary> list = new ArrayList<>();
        if (itDD != null) {
            Iterator<DataDictionary> it = itDD.iterator();
            while (it.hasNext()) {
                DataDictionary dataDictionary = it.next();
                list.add(dataDictionary);
            }
        }
        return list;
    }

    @Override
    public List<DataDictionary> queryDataDictionariesByKey(String key) {
        return dataDictionaryRepository.queryDataDictionariesByKey(key);
    }
}
