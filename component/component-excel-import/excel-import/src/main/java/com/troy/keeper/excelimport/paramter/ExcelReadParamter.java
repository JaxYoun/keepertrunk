package com.troy.keeper.excelimport.paramter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * Created by SimonChu on 2017/6/28.
 */
public class ExcelReadParamter {

    /**
     * Excel 文件流
     */
    private InputStream excelFile;

    /**
     * 工作表编号
     */
    private Integer sheetNum;

    /**
     * 数据库名称所在行号
     */
    private Integer dataBaseNameRow;

    /**
     * 数据字段名称所在行号
     */
    private Integer dataColumnNameRow;

    /**
     * 数据类型所在行号
     * 1 为String类型
     * 2 为Date类型
     * 3 为Number类型
     */
    private Integer dataTypeRow;

    /**
     * 数据必填配置所在行号
     */
    private Integer dataRequiredRow;

    /**
     * 业务数据起始行号
     */
    private Integer dataStartRow;

    /**
     * 时间转换参数
     * 格式yyyy-mm-dd
     */
    private SimpleDateFormat dateFormat;

    /**
     * 字段驼峰转换模式
     * 1 首字母大写
     * 2 首字母小写
     */
    private Integer humpMode;

    public BufferedInputStream getExcelFile() {
        return new BufferedInputStream(excelFile, 16 * 1024);
    }

    public void setExcelFile(InputStream excelFile) {
        this.excelFile = excelFile;
    }

    public Integer getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(Integer sheetNum) {
        this.sheetNum = sheetNum;
    }

    public Integer getDataBaseNameRow() {
        return dataBaseNameRow;
    }

    public void setDataBaseNameRow(Integer dataBaseNameRow) {
        this.dataBaseNameRow = dataBaseNameRow;
    }

    public Integer getDataColumnNameRow() {
        return dataColumnNameRow;
    }

    public void setDataColumnNameRow(Integer dataColumnNameRow) {
        this.dataColumnNameRow = dataColumnNameRow;
    }

    public Integer getDataTypeRow() {
        return dataTypeRow;
    }

    public void setDataTypeRow(Integer dataTypeRow) {
        this.dataTypeRow = dataTypeRow;
    }

    public Integer getDataRequiredRow() {
        return dataRequiredRow;
    }

    public void setDataRequiredRow(Integer dataRequiredRow) {
        this.dataRequiredRow = dataRequiredRow;
    }

    public Integer getDataStartRow() {
        return dataStartRow;
    }

    public void setDataStartRow(Integer dataStartRow) {
        this.dataStartRow = dataStartRow;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Integer getHumpMode() {
        return humpMode;
    }

    public void setHumpMode(Integer humpMode) {
        this.humpMode = humpMode;
    }

    public void closeInputStream() throws IOException {
        this.excelFile.close();
        this.excelFile = null;
    }
}
