package com.troy.keeper.excelimport.service;

import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;

/**
 * Created by SimonChu on 2017/7/6.
 */
public interface BaseExcelImportBatchService {

    public void batch(ExcelConfigParamter excelConfigParamter) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;

}
