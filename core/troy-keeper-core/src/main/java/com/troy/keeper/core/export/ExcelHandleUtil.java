package com.troy.keeper.core.export;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.troy.keeper.core.error.KeeperException;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by yjm on 2017/7/9.
 */
public class ExcelHandleUtil {
    public ExcelHandleUtil() {
    }

    public static void exportExcel(JSONArray fieldsJsonArray, Collection<?> dataSet, HttpServletResponse response,String excelName){
        Validate.notBlank(excelName);
        if (excelName.indexOf("xls")<0){
            throw new KeeperException("导出名格式不正确");
        }
        response.setCharacterEncoding("utf-8");
        byte[] bytes = new byte[0];
        try {
            bytes = ExcelHandleUtil.exportExcel(fieldsJsonArray, dataSet);
            response.setContentType("application/octet-stream");
            response.setContentLength(bytes.length);
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(excelName.getBytes("gb2312"), "iso-8859-1"));
            response.getOutputStream().write(bytes, 0, bytes.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] exportExcel(JSONArray fieldsJsonArray, Collection<?> dataSet) throws Exception {
        ByteArrayOutputStream os = null;

        byte[] var4;
        try {
            os = new ByteArrayOutputStream(1024);
            HSSFWorkbook e = buildExportExcel((String)null, fieldsJsonArray, dataSet);
            e.write(os);
            var4 = os.toByteArray();
        } catch (Exception var8) {
            throw var8;
        } finally {
            IOUtils.closeQuietly(os);
        }

        return var4;
    }

    private static HSSFWorkbook buildExportExcel(String titleName, JSONArray fieldsJsonArray, Collection<?> dataSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("导出数据");
        sheet.setDefaultColumnWidth(30);
        HSSFCellStyle headStyleTitle = workbook.createCellStyle();
        headStyleTitle.setFillForegroundColor((short)23);
        headStyleTitle.setBorderBottom((short)1);
        headStyleTitle.setBorderLeft((short)1);
        headStyleTitle.setBorderRight((short)1);
        headStyleTitle.setBorderTop((short)1);
        headStyleTitle.setWrapText(true);
        headStyleTitle.setAlignment((short)2);
        headStyleTitle.setLocked(false);
        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setFillForegroundColor((short)23);
        headStyle.setBorderBottom((short)1);
        headStyle.setBorderLeft((short)1);
        headStyle.setBorderRight((short)1);
        headStyle.setBorderTop((short)1);
        headStyle.setWrapText(true);
        headStyle.setAlignment((short)2);
        headStyle.setLocked(false);
        HSSFCellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setLocked(false);
        HSSFFont font = workbook.createFont();
        font = workbook.createFont();
        font.setColor((short)8);
        font.setFontHeightInPoints((short)12);
        headStyle.setFont(font);
        headStyle.setFillPattern((short)1);
        int rowIndex = 0;
        HSSFCell cell = null;
        HSSFRow row = null;
        HSSFRichTextString text = null;
        if(titleName != null && !"".equals(titleName)) {
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,5));
            font = workbook.createFont();
            font.setColor((short)8);
            font.setFontHeightInPoints((short)20);
            headStyleTitle.setFont(font);
            row = sheet.createRow(rowIndex);
            row.setHeightInPoints(31.0F);
            text = new HSSFRichTextString(titleName);
            cell = row.createCell(rowIndex);
            cell.setCellValue(text);
            cell.setCellStyle(headStyleTitle);
            ++rowIndex;
        }

        row = sheet.createRow(rowIndex);
        ++rowIndex;
        ArrayList fieldsGetList = Lists.newArrayList();

        String getAttribute;
        for(int i = 0; i < fieldsJsonArray.size(); ++i) {
            JSONObject obj = fieldsJsonArray.getJSONObject(i);
            String i1 = obj.getString("key");
            getAttribute = obj.getString("value");
            int value = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, value < 3000?3000:value);
            text = new HSSFRichTextString(getAttribute);
            cell = row.createCell(i);
            cell.setCellValue(text);
            cell.setCellStyle(headStyle);
            fieldsGetList.add(beforeAppendAttribute(i1, "get"));
        }

        for(Iterator var19 = dataSet.iterator(); var19.hasNext(); ++rowIndex) {
            Object var20 = var19.next();
            row = sheet.createRow(rowIndex);

            for(int var21 = 0; var21 < fieldsGetList.size(); ++var21) {
                getAttribute = (String)fieldsGetList.get(var21);
                String var22 = formatValue(MethodUtils.invokeMethod(var20, getAttribute, (Object[])null));
                text = new HSSFRichTextString(var22);
                cell = row.createCell(var21);
                cell.setCellValue(text);
            }
        }

        return workbook;
    }

    private static String beforeAppendAttribute(String attribute, String beforeStr) {
        StringBuffer sb = new StringBuffer(attribute);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, beforeStr);
        return sb.toString();
    }

    private static String formatValue(Object obj) {
        return obj instanceof Date ? DateFormatUtils.format((Date)obj, "yyyy-MM-dd HH:mm:ss"):(obj == null?"":obj.toString());
    }
}
