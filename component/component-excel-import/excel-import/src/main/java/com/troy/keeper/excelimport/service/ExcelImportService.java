package com.troy.keeper.excelimport.service;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;
import com.troy.keeper.excelimport.paramter.ExcelReadParamter;

import java.io.InputStream;

/**
 * Created by SimonChu on 2017/6/28.
 */
public interface ExcelImportService {

    /**
     * 导入Excel数据2003
     *
     * @param excelReadParamter
     * @return
     */
    public ResponseDTO<Object> importExcelBy2003(ExcelReadParamter excelReadParamter, ExcelDataHandle excelDataHandle) throws Exception;

    /**
     * 导入Excel数据2003
     *
     * @param excelFile
     * @return
     */
    public ResponseDTO<Object> importExcelNotParamterBy2003(InputStream excelFile, ExcelDataHandle excelDataHandle) throws Exception;

    /**
     * 获取Excel处理数据
     *
     * @param excelReadParamter
     * @return
     * @throws Exception
     */
    public ResponseDTO<Object> getExcelDataBy2003(ExcelReadParamter excelReadParamter) throws Exception;

    /**
     * 获取Excel处理数据 （默认参数）
     *
     * @param excelFile
     * @return
     * @throws Exception
     */
    public ResponseDTO<Object> getExcelDataNotParamterBy2003(InputStream excelFile) throws Exception;

    /**
     * 处理结果转对象功能
     *
     * @param excelConfigParamter
     */
    public void mapToObject(ExcelConfigParamter excelConfigParamter);
}
