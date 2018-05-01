package com.troy.keeper.excelimport.service.impl;

import com.troy.keeper.core.base.dto.ResponseDTO;
import com.troy.keeper.excelimport.config.Const;
import com.troy.keeper.excelimport.paramter.ExcelConfigParamter;
import com.troy.keeper.excelimport.paramter.ExcelReadParamter;
import com.troy.keeper.excelimport.service.BaseExcelImportBatchService;
import com.troy.keeper.excelimport.service.ExcelDataHandle;
import com.troy.keeper.excelimport.service.ExcelImportService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SimonChu on 2017/6/28.
 * <p>
 * Excel 数据导入数据库
 * <p>
 * <p>
 * importExcelBy2003 有参数类传入则直接进行校验和解析
 * importExcelNotParamterBy2003 无参数类传入则创建默认参数类进行校验和解析
 */
@Service
@Transactional
public class BaseExcelImportServiceImpl implements ExcelImportService {

    private final Logger log = LoggerFactory.getLogger(BaseExcelImportServiceImpl.class);

    @Autowired
    private BaseExcelImportBatchService baseExcelImportBatchService;

    /**
     * Default Excel Read Config Start
     */
    // 工作表编号
    private static final Integer DEFAULT_SHEET_NUM = 0;
    // 数据库名称所在行号
    private static final Integer DEFAULT_DATA_BASE_NAME_ROW = 0;
    // 数据字段名称所在行号
    private static final Integer DEFAULT_DATA_COLUMN_NAME_ROW = 1;
    // 数据类型所在行号
    private static final Integer DEFAULT_DATA_TYPE_ROW = 2;
    // 数据必填配置所在行号
    private static final Integer DEFAULT_DATA_REQUIRED_ROW = 3;
    // 业务数据起始行号
    private static final Integer DEFAULT_DATA_START_ROW = 4;
    // 时间格式参数
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    // 驼峰转换模式
    private static final Integer DEFAULT_HUMP_MODE = 0;
    // 驼峰转换的依赖字符
    public static final char UNDERLINE = '_';
    public static final char SPOT = '.';

    // 必填字段状态
    private static final String REQUIRED_TYPE_START = "1";
    // 单元格数据类型
    private static final String FIELD_TYPE_STRING = "1";
    private static final String FIELD_TYPE_DATE = "2";
    private static final String FIELD_TYPE_DATE_LONG = "3";
    private static final String FIELD_TYPE_NUMBER_INT = "4";
    private static final String FIELD_TYPE_NUMBER_DOUBLE = "5";

    /**
     * 导入Excel数据2003
     *
     * @param excelReadParamter
     * @return
     */
    public ResponseDTO<Object> importExcelBy2003(ExcelReadParamter excelReadParamter, ExcelDataHandle excelDataHandle) throws Exception {
        ExcelConfigParamter excelConfigParamter = new ExcelConfigParamter();
        excelConfigParamter.setExcelReadParamter(excelReadParamter);
        // 检查文件流及处理导入对象
        if (!checkReadExcelConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 读取工作表配置
        if (!readSheetConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 处理Excel业务数据
        if (!handleExcelBusinessData(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        excelDataHandle.importExcelDataHandle(excelConfigParamter);
        // 解析完成数据转换为Map
        mapToObject(excelConfigParamter);
        if (!excelConfigParamter.getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 插入数据库
        insertDataBase(excelConfigParamter);
        if (!excelConfigParamter.getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        return returnMsg(Const.CODE_200, Const.MSG_SUCCESSFUL_OPERATION, null);
    }

    /**
     * 导入Excel数据2003（默认参数）
     *
     * @param excelFile
     * @return
     */
    public ResponseDTO<Object> importExcelNotParamterBy2003(InputStream excelFile, ExcelDataHandle excelDataHandle) throws Exception {
        ExcelConfigParamter excelConfigParamter = new ExcelConfigParamter();
        excelConfigParamter.setExcelReadParamter(getDefaultConfig(excelFile));
        // 检查文件流及处理导入对象
        if (!checkReadExcelConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 读取工作表配置
        if (!readSheetConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 处理Excel业务数据
        if (!handleExcelBusinessData(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        excelDataHandle.importExcelDataHandle(excelConfigParamter);
        // 解析完成数据转换为Map
        mapToObject(excelConfigParamter);
        if (!excelConfigParamter.getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 插入数据库
        insertDataBase(excelConfigParamter);
        if (!excelConfigParamter.getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        return returnMsg(Const.CODE_200, Const.MSG_SUCCESSFUL_OPERATION, null);
    }

    /**
     * 获取Excel处理数据
     *
     * @param excelReadParamter
     * @return
     * @throws Exception
     */
    public ResponseDTO<Object> getExcelDataBy2003(ExcelReadParamter excelReadParamter) throws Exception {
        ExcelConfigParamter excelConfigParamter = new ExcelConfigParamter();
        excelConfigParamter.setExcelReadParamter(excelReadParamter);
        // 检查文件流及处理导入对象
        if (!checkReadExcelConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 读取工作表配置
        if (!readSheetConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 处理Excel业务数据
        if (!handleExcelBusinessData(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        return returnMsg(Const.CODE_200, Const.MSG_SUCCESSFUL_OPERATION, excelConfigParamter);
    }

    /**
     * 获取Excel处理数据 无参数
     *
     * @param excelFile
     * @return
     * @throws Exception
     */
    public ResponseDTO<Object> getExcelDataNotParamterBy2003(InputStream excelFile) throws Exception {
        ExcelConfigParamter excelConfigParamter = new ExcelConfigParamter();
        excelConfigParamter.setExcelReadParamter(getDefaultConfig(excelFile));
        // 检查文件流及处理导入对象
        if (!checkReadExcelConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 读取工作表配置
        if (!readSheetConfig(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        // 处理Excel业务数据
        if (!handleExcelBusinessData(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return returnMsg(excelConfigParamter.getCode(), excelConfigParamter.getMsg(), null);
        }
        return returnMsg(Const.CODE_200, Const.MSG_SUCCESSFUL_OPERATION, excelConfigParamter);
    }


    /*****  私有方法  *****/

    /**
     * 检查文件流及处理导入对象
     *
     * @param excelConfigParamter
     * @return
     */
    private ExcelConfigParamter checkReadExcelConfig(ExcelConfigParamter excelConfigParamter) throws IOException {
        // 检查工作薄
        if (!checkExcelReadBook(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return excelConfigParamter;
        }
        // 检查工作表
        if (!checkExcelReadSheet(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return excelConfigParamter;
        }
        return excelConfigParamter;
    }

    /**
     * 获取默认Excel参数默认配置 生成参数对象
     *
     * @param excelFile
     * @return
     */
    private ExcelReadParamter getDefaultConfig(InputStream excelFile) {
        ExcelReadParamter excelReadParamter = new ExcelReadParamter();
        excelReadParamter.setExcelFile(excelFile);
        excelReadParamter.setSheetNum(DEFAULT_SHEET_NUM);
        excelReadParamter.setDataBaseNameRow(DEFAULT_DATA_BASE_NAME_ROW);
        excelReadParamter.setDataColumnNameRow(DEFAULT_DATA_COLUMN_NAME_ROW);
        excelReadParamter.setDataTypeRow(DEFAULT_DATA_TYPE_ROW);
        excelReadParamter.setDataRequiredRow(DEFAULT_DATA_REQUIRED_ROW);
        excelReadParamter.setDataStartRow(DEFAULT_DATA_START_ROW);
        excelReadParamter.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        excelReadParamter.setHumpMode(DEFAULT_HUMP_MODE);
        return excelReadParamter;
    }


    /**
     * 检查Excel文件流读取工作薄
     *
     * @param excelConfigParamter
     * @return
     */
    private ExcelConfigParamter checkExcelReadBook(ExcelConfigParamter excelConfigParamter) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("----------------------开始读取文件及工作薄---------------------");
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(excelConfigParamter.getExcelReadParamter().getExcelFile());
            excelConfigParamter.setHssfWorkbook(workbook);
            excelConfigParamter.setCodeAndMsg(Const.CODE_200, Const.MSG_SUCCESSFUL_OPERATION);
            long endTime = System.currentTimeMillis();
            log.info("程序运行时间： " + (endTime - startTime) + "ms");
            log.info("----------------------结束读取文件及工作薄---------------------");
            return excelConfigParamter;
        } catch (IOException e) {
            log.error("读取Excel文件失败", e);
            excelConfigParamter.setCodeAndMsg(Const.CODE_500, "读取Excel工作薄失败");
            return excelConfigParamter;
        } catch (Exception e1) {
            log.error(Const.MSG_UNKNOWN_SYS_ERROR, e1);
            excelConfigParamter.setCodeAndMsg(Const.CODE_201, Const.MSG_UNKNOWN_SYS_ERROR);
            return excelConfigParamter;
        } finally {
            excelConfigParamter.closeExcelStream();
        }
    }

    /**
     * 检查Excel工作薄读取工作表
     *
     * @param excelConfigParamter
     * @return
     */
    private ExcelConfigParamter checkExcelReadSheet(ExcelConfigParamter excelConfigParamter) {
        log.info("----------------------开始读取工作表---------------------");
        try {
            HSSFSheet sheet = excelConfigParamter.getHssfWorkbook().getSheetAt(excelConfigParamter.getExcelReadParamter().getSheetNum());
            excelConfigParamter.setSheet(sheet);
            excelConfigParamter.setCodeAndMsg(Const.CODE_200, Const.MSG_SUCCESSFUL_OPERATION);
            log.info("----------------------结束读取工作表---------------------");
            return excelConfigParamter;
        } catch (NullPointerException e) {
            log.error("读取Excel工作表失败", e);
            excelConfigParamter.setCodeAndMsg(Const.CODE_202, "读取Excel工作表失败");
            return excelConfigParamter;
        } catch (Exception e1) {
            log.error(Const.MSG_UNKNOWN_SYS_ERROR, e1);
            excelConfigParamter.setCodeAndMsg(Const.CODE_201, Const.MSG_UNKNOWN_SYS_ERROR);
            return excelConfigParamter;
        }
    }

    /**
     * 读取表配置（总）
     *
     * @param excelConfigParamter
     * @return
     */
    private ExcelConfigParamter readSheetConfig(ExcelConfigParamter excelConfigParamter) throws Exception {
        log.info("----------------------开始读取工作表导入配置信息---------------------");
        if (!readSheetConfigByDBName(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return excelConfigParamter;
        }
        if (!readSheetConfigByField(excelConfigParamter).getCode().equals(Const.CODE_200)) {
            return excelConfigParamter;
        }
        log.info("----------------------结束读取工作表导入配置信息---------------------");
        return excelConfigParamter;
    }

    /**
     * 读取配置的数据库表名称或实体名
     *
     * @param excelConfigParamter
     * @return
     */
    private ExcelConfigParamter readSheetConfigByDBName(ExcelConfigParamter excelConfigParamter) throws NestedServletException {
        HSSFRow tableNameRow = excelConfigParamter.getSheet().getRow(excelConfigParamter.getExcelReadParamter().getDataBaseNameRow());
        // 默认读取第0格的数据
        HSSFCell tableNameData = tableNameRow.getCell(0);
        if (tableNameData.getStringCellValue().isEmpty()) {
            excelConfigParamter.setCodeAndMsg(Const.CODE_202, "读取数据库表名失败！");
            log.error("读取数据库表名失败!");
            return excelConfigParamter;
        }
        // 判断是表名还是实体名
//        if (tableNameData.getStringCellValue().indexOf(UNDERLINE) != -1) {
        if (tableNameData.getStringCellValue().indexOf(SPOT) == -1) {
            excelConfigParamter.setTableName(tableNameData.getStringCellValue());
        } else {
            try {
                Class clazz = Class.forName(tableNameData.getStringCellValue());
                excelConfigParamter.setDomainClazz(clazz);
            } catch (ClassNotFoundException e) {
                log.error("实体获取失败", e);
                excelConfigParamter.setCodeAndMsg(Const.CODE_202, "获取实体失败");
                return excelConfigParamter;
            } catch (NoClassDefFoundError e2) {
                log.error("从内存装载实体失败", e2);
                excelConfigParamter.setCodeAndMsg(Const.CODE_202, "装载实体失败");
                return excelConfigParamter;
            }
            excelConfigParamter.setDomainName(tableNameData.getStringCellValue());
        }
        return excelConfigParamter;
    }

    /**
     * 读取配置的字段配置信息
     *
     * @param excelConfigParamter
     * @return
     */
    private ExcelConfigParamter readSheetConfigByField(ExcelConfigParamter excelConfigParamter) throws Exception {
        List<JSONObject> excelReadConf = new ArrayList<>();
        HSSFRow columnNameRow = excelConfigParamter.getSheet().getRow(excelConfigParamter.getExcelReadParamter().getDataColumnNameRow());
        HSSFRow columnTypeRow = excelConfigParamter.getSheet().getRow(excelConfigParamter.getExcelReadParamter().getDataTypeRow());
        HSSFRow columnRequiredRow = excelConfigParamter.getSheet().getRow(excelConfigParamter.getExcelReadParamter().getDataRequiredRow());
        for (int i = 0; i < columnNameRow.getLastCellNum(); i++) {
            HSSFCell fieldName = columnNameRow.getCell(i);
            if (fieldName != null) {
                // 获取字段名对应的类型配置数据
                HSSFCell fieldType = columnTypeRow.getCell(i);
                // 获取字段名对应的必填配置数据
                HSSFCell fieldReq = columnRequiredRow.getCell(i);
                fieldName.setCellType(CellType.STRING);
                fieldType.setCellType(CellType.STRING);
                fieldReq.setCellType(CellType.STRING);
                JSONObject jo = new JSONObject();
                jo.put("fieldName", fieldName.getStringCellValue());
                if (isInteger(fieldType.getStringCellValue())) {
                    jo.put("fieldType", fieldType.getStringCellValue());
                } else {
                    excelConfigParamter.setCodeAndMsg(Const.CODE_202, "读取Excel字段配置是发生错误，第【" + excelConfigParamter.getExcelReadParamter().getDataTypeRow() + "】行，第【" + i + "】配置错误！");
                    return excelConfigParamter;
                }
                if (isInteger(fieldReq.getStringCellValue())) {
                    jo.put("fieldReq", fieldReq.getStringCellValue());
                } else {
                    excelConfigParamter.setCodeAndMsg(Const.CODE_202, "读取Excel字段配置是发生错误，第【" + excelConfigParamter.getExcelReadParamter().getDataRequiredRow() + "】行，第【" + i + "】配置错误！");
                    return excelConfigParamter;
                }
                jo.put("cellNum", String.valueOf(i));
                excelReadConf.add(jo);
            }
        }
        excelConfigParamter.setExcelReadConfList(excelReadConf);
        return changeHump(excelConfigParamter);
    }

    /**
     * 处理Excel业务数据:主方法
     *
     * @param excelConfigParamter
     * @return
     */
    private ExcelConfigParamter handleExcelBusinessData(ExcelConfigParamter excelConfigParamter) throws JSONException {
        excelConfigParamter.setExcelList(new ArrayList<>());
        excelConfigParamter.setExcelDomain(new ArrayList<>());
        log.info("---------------------开始读取Excel业务数据---------------------");
        for (int i = excelConfigParamter.getExcelReadParamter().getDataStartRow(); i < (excelConfigParamter.getSheet().getLastRowNum() + 1); i++) {
            // 清空当前行数据
            excelConfigParamter.setCurrentRowData(new HashMap<>());
            // 读取行数据
            HSSFRow row = excelConfigParamter.getSheet().getRow(i);
            // 按配置组装业务数据
            for (int j = 0; j < excelConfigParamter.getExcelReadConfList().size(); j++) {
                // 获取当前列的配置
                JSONObject cellConfig = excelConfigParamter.getExcelReadConfList().get(j);
                // 获取当前行列的Cell数据
                HSSFCell cell = row.getCell(cellConfig.getInt("cellNum"));
                // 判断是否为必填字段
                if (!checkCellRequired(cell, cellConfig, excelConfigParamter)) {
                    excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(i, j, "为必填数据，请检查！"));
                    return excelConfigParamter;
                }
                // 判断配置的单元格格式，并处理数据
                if (cell != null) {
                    try {
                        judgeCellType(cell, cellConfig, excelConfigParamter, i, j);
                    } catch (Exception e) {
                        log.error(getReqMsg(i, j, "数据处理失败"), e);
                        excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(i, j, "数据处理失败"));
                        return excelConfigParamter;
                    }
                }
            }
            excelConfigParamter.addExcelList(excelConfigParamter.getCurrentRowData());
        }
        log.info("---------------------完成读取Excel业务数据---------------------");
        return excelConfigParamter;
    }

    /**
     * 检查必填字段方法
     *
     * @param cell
     * @param cellConfig
     * @param excelConfigParamter
     * @return
     * @throws JSONException
     */
    private boolean checkCellRequired(HSSFCell cell, JSONObject cellConfig, ExcelConfigParamter excelConfigParamter) throws JSONException {
        if (cellConfig.get("fieldReq").equals(REQUIRED_TYPE_START)) {
            if (cellConfig.get("fieldType").equals(FIELD_TYPE_STRING)) {
                cell.setCellType(CellType.STRING);
                if (cell == null || cell.getStringCellValue().equals("")) {
                    return false;
                } else {
                    return true;
                }
            } else if (cellConfig.get("fieldType").equals(FIELD_TYPE_NUMBER_INT) || cellConfig.get("fieldType").equals(FIELD_TYPE_NUMBER_DOUBLE)) {
                if (cell == null) {
                    return false;
                } else {
                    return true;
                }
            } else if (cellConfig.get("fieldType").equals(FIELD_TYPE_DATE) || cellConfig.get("fieldType").equals(FIELD_TYPE_DATE_LONG)) {
                if (("").equals(changeDate(cell, excelConfigParamter))) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 判断设置单元格类型
     *
     * @param cell
     * @param cellConfig
     * @param excelConfigParamter
     * @return
     * @throws JSONException
     */
    private ExcelConfigParamter judgeCellType(HSSFCell cell, JSONObject cellConfig, ExcelConfigParamter excelConfigParamter, int rowNum, int cellNum) throws JSONException {
        // 如果是String
        if (cellConfig.get("fieldType").equals(FIELD_TYPE_STRING)) {
            cell.setCellType(CellType.STRING);
            try {
                excelConfigParamter.putCurrentRowData(cellConfig.getString("fieldName"), cell.getStringCellValue());
            } catch (Exception e) {
                log.warn("String类型读取失败", e);
                excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(rowNum, cellNum, "String类型读取失败！"));
                return excelConfigParamter;
            }
            // 如果是Date
        } else if (cellConfig.get("fieldType").equals(FIELD_TYPE_DATE)) {
            if (cell.getCellTypeEnum() != CellType.NUMERIC) {
                excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(rowNum, cellNum, "日期类型错误，请修正后重试！"));
                return excelConfigParamter;
            } else {
                try {
                    excelConfigParamter.putCurrentRowData(cellConfig.getString("fieldName"), changeDate(cell, excelConfigParamter));
                } catch (Exception e) {
                    log.warn("日期类型转String失败", e);
                    excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(rowNum, cellNum, "日期类型转String失败！"));
                    return excelConfigParamter;
                }
            }
            // 如果是数字
        } else if (cellConfig.get("fieldType").equals(FIELD_TYPE_NUMBER_INT) || cellConfig.get("fieldType").equals(FIELD_TYPE_NUMBER_DOUBLE)) {
            cell.setCellType(CellType.NUMERIC);
            // 判断是小数还是整数
            if (cellConfig.get("fieldType").equals(FIELD_TYPE_NUMBER_DOUBLE)) {
                try {
                    excelConfigParamter.putCurrentRowData(cellConfig.getString("fieldName"), String.valueOf(cell.getNumericCellValue()));
                } catch (Exception e) {
                    log.warn("数字类型读取失败", e);
                    excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(rowNum, cellNum, "数字类型读取失败！"));
                    return excelConfigParamter;
                }
            } else {
                try {
                    int num = (int) cell.getNumericCellValue();
                    excelConfigParamter.putCurrentRowData(cellConfig.getString("fieldName"), String.valueOf(num));
                } catch (Exception e) {
                    log.warn("数字类型读取失败", e);
                    excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(rowNum, cellNum, "数字类型读取失败！"));
                    return excelConfigParamter;
                }
            }
            // 如果是Date并需要转换为Long
        } else if (cellConfig.get("fieldType").equals(FIELD_TYPE_DATE_LONG)) {
            if (cell.getCellTypeEnum() != CellType.NUMERIC) {
                excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(rowNum, cellNum, "日期类型错误，请修正后重试！"));
                return excelConfigParamter;
            } else {
                try {
                    excelConfigParamter.putCurrentRowData(cellConfig.getString("fieldName"), dateToLongDate(cell, excelConfigParamter).toString());
                } catch (Exception e) {
                    log.warn("日期类型转Long失败", e);
                    excelConfigParamter.setCodeAndMsg(Const.CODE_202, getReqMsg(rowNum, cellNum, "日期类型转Long失败！"));
                    return excelConfigParamter;
                }

            }
        }
        return excelConfigParamter;
    }

    /**
     * 日期转Long
     *
     * @param hssfCell
     * @param excelConfigParamter
     * @return
     */
    private Long dateToLongDate(HSSFCell hssfCell, ExcelConfigParamter excelConfigParamter) {
        hssfCell.setCellType(CellType.NUMERIC);
        return HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue()).getTime() / 1000;
    }

    /**
     * 获取数据错误信息
     *
     * @param rowNum
     * @param cellNum
     * @return
     */
    private String getReqMsg(int rowNum, int cellNum, String errorMsg) {
        StringBuilder msg = new StringBuilder();
        msg.append("第" + (rowNum + 1) + "行，");
        msg.append("第" + (cellNum + 1) + "列");
        msg.append(errorMsg);
        return msg.toString();
    }

    /**
     * 返回信息包装
     *
     * @param code
     * @param msg
     * @param obj
     * @return
     */
    private ResponseDTO<Object> returnMsg(String code, String msg, Object obj) {
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setCode(code);
        responseDTO.setMsg(msg);
        responseDTO.setData(obj);
        return responseDTO;
    }

    /**
     * 判断是否为数字
     *
     * @param value
     * @return
     */
    private static boolean isInteger(String value) {
        Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
        Matcher isNum = pattern.matcher(value);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 转换日期
     *
     * @param hssfCell
     * @param excelConfigParamter
     * @return
     */
    private static String changeDate(HSSFCell hssfCell, ExcelConfigParamter excelConfigParamter) {
        hssfCell.setCellType(CellType.NUMERIC);
        return excelConfigParamter.getExcelReadParamter().getDateFormat().format(HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue()));
    }

    /**
     * 驼峰转换方法
     *
     * @param excelConfigParamter
     * @return
     */
    public static ExcelConfigParamter changeHump(ExcelConfigParamter excelConfigParamter) throws JSONException {
        for (int g = 0; g < excelConfigParamter.getExcelReadConfList().size(); g++) {
            String fileName = excelConfigParamter.getExcelReadConfList().get(g).getString("fieldName");
            if (fileName == null || "".equals(fileName.trim())) {
                excelConfigParamter.setCodeAndMsg(Const.CODE_202, "驼峰转换失败");
                return excelConfigParamter;
            }
            fileName = fileName.toLowerCase();
            int len = fileName.length();
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                char c = fileName.charAt(i);
                if (i == 0 && excelConfigParamter.getExcelReadParamter().getHumpMode() == 1) {
                    c = String.valueOf(c).toUpperCase().charAt(0);
                }
                if (c == UNDERLINE) {
                    if (++i < len) {
                        sb.append(Character.toUpperCase(fileName.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }
            excelConfigParamter.getExcelReadConfList().get(g).put("fieldName", sb.toString());
        }
        return excelConfigParamter;
    }

    /**
     * Map转对象
     * 业务说明：将处理完成后的结果数据转换为对象
     *
     * @param excelConfigParamter
     */
    public void mapToObject(ExcelConfigParamter excelConfigParamter) {
        try {
            for (int i = 0; i < excelConfigParamter.getExcelHandleList().size(); i++) {
                Object obj = excelConfigParamter.getDomainClazz().newInstance();
                BeanUtils.populate(obj, excelConfigParamter.getExcelHandleList().get(i));
                excelConfigParamter.addExcelDomain(obj);
            }
        } catch (InstantiationException e) {
            log.error(Const.MSG_MAP_TO_OBJECT_ERROR, e);
            excelConfigParamter.setCodeAndMsg(Const.CODE_202, Const.MSG_MAP_TO_OBJECT_ERROR);
        } catch (IllegalAccessException e) {
            log.error(Const.MSG_MAP_TO_OBJECT_ERROR, e);
            excelConfigParamter.setCodeAndMsg(Const.CODE_202, Const.MSG_MAP_TO_OBJECT_ERROR);
        } catch (InvocationTargetException e) {
            log.error(Const.MSG_MAP_TO_OBJECT_ERROR, e);
            excelConfigParamter.setCodeAndMsg(Const.CODE_202, Const.MSG_MAP_TO_OBJECT_ERROR);
        }
    }

    /**
     * 导入数据库方法
     *
     * @param excelConfigParamter
     */
    private void insertDataBase(ExcelConfigParamter excelConfigParamter) {
        try {
            baseExcelImportBatchService.batch(excelConfigParamter);
        } catch (Exception e) {
            log.error(Const.MSG_MAP_TO_OBJECT_ERROR, e);
            excelConfigParamter.setCodeAndMsg(Const.CODE_202, "数据插入失败！");
        }
    }
}
