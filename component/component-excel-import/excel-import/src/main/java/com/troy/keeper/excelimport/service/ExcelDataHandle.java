package com.troy.keeper.excelimport.service;

import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;

/**
 * Created by SimonChu on 2017/7/4.
 */
public interface ExcelDataHandle {
    /**
     * 导入Excel数据处理接口
     *
     * @param excelConfigParamter
     * @return
     */
    public ExcelConfigParamter importExcelDataHandle(ExcelConfigParamter excelConfigParamter);


}
