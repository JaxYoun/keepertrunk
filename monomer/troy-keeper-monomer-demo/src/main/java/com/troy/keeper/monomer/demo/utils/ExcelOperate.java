package com.troy.keeper.monomer.demo.utils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * Excel操作类
 * 
 * @author 杨岗
 * 
 */
public class ExcelOperate {

	/**
	 * 判断是否是真正的EXCEL文件
	 * @param file
	 */
	@SuppressWarnings("unused")
	public static boolean checkIsRealExcel(File file){
		boolean result = false;
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		// excel2003
		if ("xls".equals(extension)) {
			try {
				FileInputStream f = new FileInputStream(file);
				BufferedInputStream in = new BufferedInputStream(f);
				//能正常new表示是真正的EXCEL文件，否则会报异常
				HSSFWorkbook wb = new HSSFWorkbook(in);
				in.close();
				f.close();
				result = true;
			} catch (Exception e) {
				//e.printStackTrace();
				result = false;
			}
		}
		// excel2007
		else if ("xlsx".equals(extension)) {
			try {
				FileInputStream f = new FileInputStream(file);
				BufferedInputStream in = new BufferedInputStream(f);
				//能正常new表示是真正的EXCEL文件，否则会报异常
				XSSFWorkbook wb = new XSSFWorkbook(in);
				in.close();
				f.close();
				result = true;
			} catch (Exception e) {
				//e.printStackTrace();
				result = false;
			}
		} else {
			result = false;
		}	
		return result;
	}
	
	/**
	 * 
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @param mergeCellsFlag
	 *            是否解析合并单元格
	 * @param sheetIndex 读取的EXCEL表单的序号
	 * @return 读出的Excel中数据的内容
	 * @throws Exception
	 */
	public static String[][] getData(MultipartFile file, int ignoreRows,
									 boolean mergeCellsFlag, int sheetIndex) throws Exception {
		String fileName = file.getOriginalFilename();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		// excel2003
		if ("xls".equals(extension)) {
			return read2003Excel(file, ignoreRows, mergeCellsFlag, sheetIndex);
		}
		// excel2007
		else if ("xlsx".equals(extension)) {
			return read2007Excel(file, ignoreRows, mergeCellsFlag, sheetIndex);
		} else {
			throw new Exception("不支持的文件类型");
		}
	}

	/**
	 * 
	 * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行(此方法主要是上传文件生成的文件是临时文件的时候使用)
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * @param fileName
	 *            读取数据的源Excel的文件名
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @param mergeCellsFlag
	 *            是否解析合并单元格
	 * @param sheetIndex 读取的EXCEL表单的序号
	 * @return 读出的Excel中数据的内容
	 * @throws Exception
	 */
	public static String[][] getData(MultipartFile file, String fileName,
			int ignoreRows, boolean mergeCellsFlag, int sheetIndex) throws Exception {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		// excel2003
		if ("xls".equals(extension)) {
			return read2003Excel(file, ignoreRows, mergeCellsFlag, sheetIndex);
		}
		// excel2007
		else if ("xlsx".equals(extension)) {
			return read2007Excel(file, ignoreRows, mergeCellsFlag, sheetIndex);
		} else {
			throw new Exception("不支持的文件类型");
		}
	}

	/**
	 * 
	 * 读取 office 2003 excel
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @param mergeCellsFlag
	 *            是否解析合并单元格
	 * @param sheetIndex 读取的EXCEL表单的序号
	 * @return 读出的Excel中数据的内容
	 * @throws Exception
	 */
	public static String[][] read2003Excel(MultipartFile file, int ignoreRows,
			boolean mergeCellsFlag, int sheetIndex) throws Exception {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		InputStream f = file.getInputStream();
		BufferedInputStream in = new BufferedInputStream(f);
		
		try {
			HSSFWorkbook wb = new HSSFWorkbook(in);
			List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
			// 默认只解析第一个excel表单
			getCombineCell(wb.getSheetAt(sheetIndex), list);

			HSSFCell cell = null;
			HSSFSheet st = wb.getSheetAt(sheetIndex);
			// 第一行为标题，不取
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = row.getLastCellNum() + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasValue = false;
				for (int columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
					String value = "";
					cell = row.getCell(columnIndex);
					if (cell != null) {
						value = getValue2003(cell, value);
					}
					values[columnIndex] = value.trim();
					hasValue = true;
				}
				if (hasValue) {
					result.add(values);
				}
			}
			in.close();
			f.close();
			if(result != null && !result.isEmpty()){
				String[][] returnArray = new String[result.size()][rowSize];
				for (int i = 0; i < returnArray.length; i++) {
					returnArray[i] = (String[]) result.get(i);
				}
				if(mergeCellsFlag == true){
					return changeArray(returnArray, list, ignoreRows);
				}
				return returnArray;
			}
			else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(in != null){
				in.close();
			}
			if(f != null){
				f.close();
			}
		}
		
		
	}

	/**
	 * excel2003获取单元格值
	 * 
	 * @param cell
	 * @param value
	 * @return
	 */
	private static String getValue2003(HSSFCell cell, String value) {
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if (date != null) {
					value = new SimpleDateFormat("yyyy-MM-dd").format(date);
				} else {
					value = "";
				}
			} else {
				// value =
				// String.valueOf(cell.getNumericCellValue());
				value = new DecimalFormat("0.0000").format(cell
						.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			// 导入时如果为公式生成的数据则无值
			if (!cell.getStringCellValue().equals("")) {
				value = cell.getStringCellValue();
			} else {
				value = cell.getNumericCellValue() + "";
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = (cell.getBooleanCellValue() == true ? "Y" : "N");
			break;
		default:
			value = "";
		}
		return value;
	}

	/**
	 * 
	 * 读取 office 2007 excel
	 * 
	 * @param file
	 *            读取数据的源Excel
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @param mergeCellsFlag
	 *            是否解析合并单元格
	 * @param sheetIndex 读取的EXCEL表单的序号
	 * @return 读出的Excel中数据的内容
	 * @throws Exception
	 */
	public static String[][] read2007Excel(MultipartFile file, int ignoreRows,
			boolean mergeCellsFlag, int sheetIndex) throws Exception {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		InputStream f = file.getInputStream();
		BufferedInputStream in = new BufferedInputStream(f);
		try {
			XSSFWorkbook wb = new XSSFWorkbook(in);

			List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
			// 默认只解析第一个excel表单
			getCombineCell(wb.getSheetAt(sheetIndex), list);

			XSSFCell cell = null;
			XSSFSheet st = wb.getSheetAt(sheetIndex);
			// 第一行为标题，不取
			for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
				XSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = row.getLastCellNum() + 1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasValue = false;
				for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
					String value = "";
					cell = row.getCell(columnIndex);
					if (cell != null) {
						value = getValue2007(cell, value);
					}
					values[columnIndex] = value.trim();
					hasValue = true;
				}
				if (hasValue) {
					result.add(values);
				}
			}
			in.close();
			f.close();
			if(result != null && !result.isEmpty()){
				String[][] returnArray = new String[result.size()][rowSize];
				for (int i = 0; i < returnArray.length; i++) {
					returnArray[i] = (String[]) result.get(i);
				}
				if(mergeCellsFlag == true){
					return changeArray(returnArray, list, ignoreRows);
				}
				return returnArray;
			}
			else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally{
			if(in != null){
				in.close();
			}
			if(f != null){
				f.close();
			}
		}
	}

	/**
	 * excel2007获取单元格值
	 * 
	 * @param cell
	 * @param value
	 * @return
	 */
	private static String getValue2007(XSSFCell cell, String value) {
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if (date != null) {
					value = new SimpleDateFormat("yyyy-MM-dd").format(date);
				} else {
					value = "";
				}
			} else {
				// value =
				// String.valueOf(cell.getNumericCellValue());
				value = new DecimalFormat("0.0000").format(cell
						.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			// 导入时如果为公式生成的数据则无值
			if (!cell.getStringCellValue().equals("")) {
				value = cell.getStringCellValue();
			} else {
				value = cell.getNumericCellValue() + "";
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = (cell.getBooleanCellValue() == true ? "Y" : "N");
			break;
		default:
			value = "";
		}
		return value;
	}

	/**
	 * 合并单元格处理--加入list
	 * 
	 * @param sheet
	 * @return
	 */
	public static void getCombineCell(Sheet sheet, List<CellRangeAddress> list) {
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		// 遍历合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格加入list中
			CellRangeAddress ca = sheet.getMergedRegion(i);
			list.add(ca);
		}
	}

	/**
	 * 替换合并单元格的值
	 * 
	 * @param arr
	 * @param list
	 * @param ignoreRows
	 *            读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
	 * @return
	 */
	public static String[][] changeArray(String[][] arr,
			List<CellRangeAddress> list, int ignoreRows) {
		String[][] result = new String[arr.length][arr[0].length];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				try {
					result[i][j] = arr[i][j];
				} catch (Exception e) {
					
				}			
			}
		}
		if (list != null && !list.isEmpty()) {
			for (CellRangeAddress ca : list) {
				int firstC = ca.getFirstColumn();
				int lastC = ca.getLastColumn();
				int firstR = ca.getFirstRow();
				int lastR = ca.getLastRow();

				if (firstR < ignoreRows) {
					continue;
				} else {
					String cellValue = result[firstR - ignoreRows][firstC];
					// System.out.println("cellValue:"+cellValue);
					for (int i = firstR; i < lastR + 1; i++) {
						for (int j = firstC; j < lastC + 1; j++) {
							result[i - ignoreRows][j] = cellValue;
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * list导出
	 * 
	 * @param headers
	 *            头信息
	 * @param list
	 *            vluelist
	 * @param out
	 *            字节流
	 * @param colNames
	 * @param pattern
	 * @return byte[]
	 */
	public static byte[] exportExcel(String[] headers, List<Object> list,
			ByteArrayOutputStream out, String[] colNames, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		List<String []> ls = new ArrayList<String []>();

		Iterator<Object> it = list.iterator();
		while(it.hasNext()){
			String cols[] = new String[colNames.length];
			Object obj = it.next();
			try {
				for (int i = 0; i < headers.length; i++) {
					Object value = PropertyUtils.getProperty(obj, colNames[i]);
					if (value == null) {
						value = "";
					}
					cols[i] = value.toString();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			ls.add(cols);
		}

		Iterator<String []> it2 = ls.iterator();
		int index = 0;
		while (it2.hasNext()) {
			index++;
			row = sheet.createRow(index);
			String[] strs = it2.next();
			for (int i = 0; i < strs.length; i++) {
				HSSFCell cell = row.createCell(i);
				String textValue = strs[i];
				cell.setCellStyle(style2);
				HSSFRichTextString richString = new HSSFRichTextString(
						textValue);
				cell.setCellValue(richString);
			}
		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}
