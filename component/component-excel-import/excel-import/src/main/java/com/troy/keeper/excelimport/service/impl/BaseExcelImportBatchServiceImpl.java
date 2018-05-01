package com.troy.keeper.excelimport.service.impl;

import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;
import com.troy.keeper.excelimport.service.BaseExcelImportBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

/**
 * Created by SimonChu on 2017/7/6.
 */
@Service
@Transactional
public class BaseExcelImportBatchServiceImpl implements BaseExcelImportBatchService {

    @Autowired
    private EntityManager entityManager;


    public void batch(ExcelConfigParamter excelConfigParamter) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException {
        for (int i = 0; i < excelConfigParamter.getExcelDomain().size(); i++) {
            entityManager.persist(excelConfigParamter.getExcelDomain().get(i));
            if (i % 10 == 0) {
                entityManager.flush();
                entityManager.close();
            }
        }
        entityManager.flush();
        entityManager.close();
    }
}
