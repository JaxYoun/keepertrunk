package com.troy.keeper.excelimport.paramter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by SimonChu on 2017/6/29.
 */
public class ExcelConfigParamter {

    // excel读取参数
    private ExcelReadParamter excelReadParamter;

    // 工作薄
    private HSSFWorkbook hssfWorkbook;

    // 工作表
    private HSSFSheet sheet;

    // 表名
    private String tableName;

    // 实体名
    private String domainName;

    // 实体类
    private Class domainClazz;

    // excel读取结果
    private List<Map<String, String>> excelList;

    // excel处理结果（数据）
    private List<Map<String, String>> excelHandleList;

    // excel处理结果（实体）
    private List<Object> excelDomain;

    // 编码
    private String code;

    // 消息
    private String msg;

    // excel配置
    private List<JSONObject> excelReadConfList;

    // 当前行数据
    private Map<String, String> currentRowData;

    public ExcelReadParamter getExcelReadParamter() {
        return excelReadParamter;
    }

    public void setExcelReadParamter(ExcelReadParamter excelReadParamter) {
        this.excelReadParamter = excelReadParamter;
    }

    public HSSFWorkbook getHssfWorkbook() {
        return hssfWorkbook;
    }

    public void setHssfWorkbook(HSSFWorkbook hssfWorkbook) {
        this.hssfWorkbook = hssfWorkbook;
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCodeAndMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public List<JSONObject> getExcelReadConfList() {
        return excelReadConfList;
    }

    public void setExcelReadConfList(List<JSONObject> excelReadConfList) {
        this.excelReadConfList = excelReadConfList;
    }

    public Map<String, String> getCurrentRowData() {
        return currentRowData;
    }

    public void setCurrentRowData(Map<String, String> currentRowData) {
        this.currentRowData = currentRowData;
    }

    public void clearCurrentRowData() {
        if (this.currentRowData != null) {
            this.currentRowData.clear();
        }
    }

    public void putCurrentRowData(String key, String value) {
        this.currentRowData.put(key, value);
    }

    public void addExcelList(Map<String, String> map) {
        this.excelList.add(map);
    }

    public List<Map<String, String>> getExcelList() {
        return excelList;
    }

    public void setExcelList(List<Map<String, String>> excelList) {
        this.excelList = excelList;
    }

    public List<Map<String, String>> getExcelHandleList() {
        return excelHandleList;
    }

    public void setExcelHandleList(List<Map<String, String>> excelHandleList) {
        this.excelHandleList = excelHandleList;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Class getDomainClazz() {
        return domainClazz;
    }

    public void setDomainClazz(Class domainClazz) {
        this.domainClazz = domainClazz;
    }

    public List<Object> getExcelDomain() {
        return excelDomain;
    }

    public void setExcelDomain(List<Object> excelDomain) {
        this.excelDomain = excelDomain;
    }

    public void addExcelDomain(Object obj){
        this.excelDomain.add(obj);
    }

    public void closeExcelStream() throws IOException {
        this.excelReadParamter.closeInputStream();
    }
}
