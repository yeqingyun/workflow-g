/**
 * @(#) HttpClientUtil.java Created on 2014年5月26日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The class <code>PoiUtil</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class PoiUtil {
	
	/**
	 * 创建字体样式
	 * @param workbook
	 * @param fontHeight
	 * 			字体
	 * @return
	 */
	public static Font createFount(Workbook workbook, short fontHeight){
		Font font = (HSSFFont) workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints(fontHeight);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        return font;
	}
	
	/**
	 * 初始化Cell的样式
	 */
	public static CellStyle defaultCellStyle(Workbook workbook, Font font) {
		CellStyle cellStyle = workbook.createCellStyle();
		// 单元格横向对齐方式
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 单元格纵向对齐方式
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置边框
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setFont(font);
		return cellStyle;
	}
	
	/**
	 * 画Excel的表头
	 * @param headers
	 * @param cellStyle
	 * @param sheet
	 */
	public static void createExcelHeaders(String[] headers,CellStyle cellStyle, HSSFSheet sheet, int rowIndex, int rowHieght){
		HSSFRow row = sheet.createRow(rowIndex);
		row.setHeightInPoints(rowHieght);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
	}
	
	/**
	 * 根据提供的区间画表格头
	 * @param title
	 * 			名称
	 * @param cellStyle
	 * 			样式
	 * @param sheet
	 * 			sheet
	 * @param cellRangeAddress
	 * 			合并区间
	 * @param rowHeight
	 * 			行高
	 * @param workbook
	 * 			workbook
	 */
	public static void createExcelHeader(String title, CellStyle cellStyle, HSSFSheet sheet, 
			CellRangeAddress cellRangeAddress, int rowHeight, Workbook workbook){
		HSSFRow row = sheet.createRow(cellRangeAddress.getFirstRow());
		row.setHeightInPoints(rowHeight);
		for (int i = 0; i < cellRangeAddress.getLastColumn(); i++) {
	           HSSFCell cell = row.createCell(i);
	           cell.setCellStyle(cellStyle);
	           if (i == 0) {
		           HSSFRichTextString text = new HSSFRichTextString(title);
		           cell.setCellValue(text);
	           }
	     }
		sheet.addMergedRegion(cellRangeAddress); 
		//合并后，设置边框样式
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);  
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);  
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);  
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook); 
        
	}
	
	/**
	 * 合并单元格
	 * @param sheet
	 * @param cellRangeAddress
	 * @param workbook
	 */
	public static void megerCell(HSSFSheet sheet, CellRangeAddress cellRangeAddress, Workbook workbook){
		sheet.addMergedRegion(cellRangeAddress); 
		//合并后，设置边框样式
		RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);  
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);  
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);  
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook); 
	}
	
	public static void drawCell4Object(CellStyle cellStyle,HSSFRow row,
			String[] fields, Object obj) throws Exception {
		Method method = null;
		String methodName = null;
		StringBuffer sb = null;
		int m = 1;
		for (String field : fields) {
			sb = new StringBuffer();
			sb.append("get");
			sb.append(field.substring(0, 1).toUpperCase());
			sb.append(field.substring(1));
			//获取getXX方法名
			methodName = sb.toString();
			method = obj.getClass().getMethod(methodName);
			Object object = method.invoke(obj, new Object[0]);
			
			HSSFCell cell = row.createCell(m);
			cell.setCellStyle(cellStyle);
			if (object != null) {
		        cell.setCellValue(new HSSFRichTextString(String.valueOf(object)));
			} else {
				cell.setCellValue(new HSSFRichTextString(""));
			}
			m++;
		}
	}

	/**
	 * 导入Excel文件 内容以List<Map<String K,String V>>的方式存放
	 * 
	 * @param excelFile
	 *            Excel文件对象
	 * @param fieldNames
	 *            Map的Key列表，Value为相应的sheet一行中各列的值
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Map<String, String>> importExcelToMap(InputStream fileInputStream,
			String fileName, int fieldLength, int rowIndex) {
		Workbook workbook = null;
		// String[] strKey = fieldNames.split(",");
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		try {
			workbook = initWorkbook(fileInputStream, fileName);
			Sheet sheet = workbook.getSheetAt(0);
			while (true) {
				Row row = sheet.getRow(rowIndex);
				if (row == null)
					break;
				Map<String, String> map = new LinkedHashMap<String, String>();
				// map.put("rowid", String.valueOf(row.getRowNum()));
				for (int keyIndex = 0; keyIndex < fieldLength; keyIndex++) {
					Cell hSSFCell = null;
					hSSFCell = row.getCell(keyIndex);
					String cellvalue = "";
					if (hSSFCell != null) {
						switch (hSSFCell.getCellType()) {
						// 如果当前Cell的Type为NUMERIC
						case HSSFCell.CELL_TYPE_NUMERIC: {
							// 判断当前的cell是否为Date
							if (HSSFDateUtil.isCellDateFormatted(hSSFCell)) {
								// 如果是Date类型则，取得该Cell的Date值
								SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
								cellvalue = sdf.format(HSSFDateUtil
										.getJavaDate(hSSFCell
												.getNumericCellValue()));
							}
							// 如果是纯数字
							else {
								double value = hSSFCell.getNumericCellValue();
								//转成了科学计数
								if(String.valueOf(value).indexOf("E") != -1){
									DecimalFormat df = new DecimalFormat("#");  
									cellvalue = df.format(value);  
								} else if(String.valueOf(value).indexOf("-") != -1) {
									// 取得当前Cell的数值
									String num = String.valueOf(hSSFCell
											.getNumericCellValue());
									cellvalue = num;
								} else if (String.valueOf(value).indexOf(".") != -1){
									BigDecimal bd = new BigDecimal(hSSFCell
											.getNumericCellValue());
									cellvalue = String.valueOf(bd.floatValue());
								} else {
									BigDecimal bd = new BigDecimal(hSSFCell
											.getNumericCellValue());
									cellvalue = String.valueOf(bd.intValue());
								}
							}
							break;
						}
						// 如果当前Cell的Type为STRIN
						case HSSFCell.CELL_TYPE_STRING:
							// 取得当前的Cell字符串
							cellvalue = hSSFCell.getStringCellValue().toString();
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							//如果是公式生成的值
							try {
								if (HSSFDateUtil.isCellDateFormatted(hSSFCell)) {  
					                Date date = hSSFCell.getDateCellValue();  
					                cellvalue = (date.getMonth() + 1) +"-" + date.getDate() + "-" + (date.getYear() + 1900); 
					                break;
								} else {
									BigDecimal bd = new BigDecimal(hSSFCell.getNumericCellValue());
									cellvalue = String.valueOf(bd.floatValue());
								}
							} catch (IllegalStateException e) {
								cellvalue = String.valueOf(hSSFCell.getRichStringCellValue());  
							}
							break;
						// 默认的Cell值
						default:
							cellvalue = " ";
						}
					}
					map.put(String.valueOf(keyIndex), cellvalue);
				}
				listMap.add(map);

				rowIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}
	/**
	 * 导入Excel文件 内容以List<Map<String K,String V>>的方式存放
	 * 
	 * @param excelFile
	 *            Excel文件对象
	 * @param fieldNames
	 *            Map的Key列表，Value为相应的sheet一行中各列的值
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Map<String, String>> importExcelToMapRepair(InputStream fileInputStream,
			String fileName, int fieldLength, int rowIndex) {
		Workbook workbook = null;
		// String[] strKey = fieldNames.split(",");
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		try {
			workbook = initWorkbook(fileInputStream, fileName);
			Sheet sheet = workbook.getSheetAt(0);
			while (true) {
				Row row = sheet.getRow(rowIndex);
				if (row == null)
					break;
				int j = 0;
				for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
					Cell hSSFCell = row.getCell(i);
					if(hSSFCell == null ||hSSFCell.toString().trim().equals("")){
						j++;
					}
				}
				if(j == row.getPhysicalNumberOfCells()){
					break;
				}
				Map<String, String> map = new LinkedHashMap<String, String>();
				// map.put("rowid", String.valueOf(row.getRowNum()));
				for (int keyIndex = 0; keyIndex < fieldLength; keyIndex++) {
					Cell hSSFCell = null;
					hSSFCell = row.getCell(keyIndex);
					String cellvalue = "";
					if (hSSFCell != null) {
						switch (hSSFCell.getCellType()) {
						// 如果当前Cell的Type为NUMERIC
						case HSSFCell.CELL_TYPE_NUMERIC: {
							// 判断当前的cell是否为Date
							if (HSSFDateUtil.isCellDateFormatted(hSSFCell)) {
								// 如果是Date类型则，取得该Cell的Date值
								SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
								cellvalue = sdf.format(HSSFDateUtil
										.getJavaDate(hSSFCell
												.getNumericCellValue()));
							}
							// 如果是纯数字
							else {
								double value = hSSFCell.getNumericCellValue();
								//转成了科学计数
								if(String.valueOf(value).indexOf("E") != -1){
									DecimalFormat df = new DecimalFormat("#");  
									cellvalue = df.format(value);  
								} else if(String.valueOf(value).indexOf("-") != -1) {
									// 取得当前Cell的数值
									String num = String.valueOf(hSSFCell
											.getNumericCellValue());
									cellvalue = num;
								} else if (String.valueOf(value).indexOf(".") != -1){
									BigDecimal bd = new BigDecimal(hSSFCell
											.getNumericCellValue());
									cellvalue = String.valueOf(bd.floatValue());
								} else {
									BigDecimal bd = new BigDecimal(hSSFCell
											.getNumericCellValue());
									cellvalue = String.valueOf(bd.intValue());
								}
							}
							break;
						}
						// 如果当前Cell的Type为STRIN
						case HSSFCell.CELL_TYPE_STRING:
							// 取得当前的Cell字符串
							cellvalue = hSSFCell.getStringCellValue()
									.toString();
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							//如果是公式生成的值
							try {
								if (HSSFDateUtil.isCellDateFormatted(hSSFCell)) {  
					                Date date = hSSFCell.getDateCellValue();  
					                cellvalue = (date.getMonth() + 1) +"-" + date.getDate() + "-" + (date.getYear() + 1900); 
					                break;
								} else {
									BigDecimal bd = new BigDecimal(hSSFCell.getNumericCellValue());
									cellvalue = String.valueOf(bd.floatValue());
								}
							} catch (IllegalStateException e) {
								cellvalue = String.valueOf(hSSFCell.getRichStringCellValue());  
							}
							break;
						// 默认的Cell值
						default:
							cellvalue = " ";
						}
					}
					map.put(String.valueOf(keyIndex), cellvalue);
				}
				listMap.add(map);

				rowIndex++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMap;
	}

	/**
	 * 导入Excel文件 内容以List<Map<String K,String V>>的方式存放
	 * 变更说明：增加了sheetname属性，用于指定所需读取数据的表。
	 * 
	 * 
	 * @param excelFile
	 *            Excel文件对象
	 * 
	 * @param fieldNames
	 *            Map的Key列表，Value为相应的sheet一行中各列的值
	 * 
	 * @return
	 */
	public static List<Map<String, String>> importExcelToMap(InputStream fileInputStream,
			String fileName, String sheetName, String fieldNames, int rowIndex) {
		Workbook workbook = null;
		String[] strKey = fieldNames.split(",");
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		try {
			workbook = initWorkbook(fileInputStream, fileName);
			Sheet sheet = workbook.getSheet(sheetName);
			while (true) {
				Row row = sheet.getRow(rowIndex);
				if (row == null)
					break;
				Map<String, String> map = new LinkedHashMap<String, String>();
				// map.put("rowid", String.valueOf(row.getRowNum()));
				for (int keyIndex = 0; keyIndex < strKey.length; keyIndex++) {
					Cell hSSFCell = null;
					hSSFCell = row.getCell(keyIndex);
					String cellvalue = "";
					if (hSSFCell != null) {
						switch (hSSFCell.getCellType()) {
						// 如果当前Cell的Type为NUMERIC
						case HSSFCell.CELL_TYPE_NUMERIC: {
							// 判断当前的cell是否为Date
							if (HSSFDateUtil.isCellDateFormatted(hSSFCell)) {
								// 如果是Date类型则，取得该Cell的Date值
								SimpleDateFormat sdf = new SimpleDateFormat(
										"MM-dd-yyyy HH:mm:ss");
								cellvalue = sdf.format(HSSFDateUtil
										.getJavaDate(hSSFCell
												.getNumericCellValue()));
							}
							// 如果是纯数字
							else {
								// 取得当前Cell的数值
								Integer num = new Integer((int) hSSFCell
										.getNumericCellValue());
								cellvalue = String.valueOf(num);
							}
							break;
						}
							// 如果当前Cell的Type为STRIN
						case HSSFCell.CELL_TYPE_STRING:
							// 取得当前的Cell字符串
							cellvalue = hSSFCell.getStringCellValue()
									.toString();
							break;
						// 默认的Cell值
						default:
							cellvalue = " ";
						}
					}
					map.put(strKey[keyIndex], cellvalue);
				}
				listMap.add(map);
				rowIndex++;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return listMap;
	}

	/**
	 * @param sheetTitle
	 *            Sheet的名称
	 * 
	 * @param fieldTitles
	 *            各列的标题（第一行各列的名称）
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Workbook initWorkbook(InputStream fileInputStream, String filepath)
			throws Exception {
		// 创建工作簿（Excel文件）
		Workbook workbook = null;
		if (isExcel2003(filepath)) {
			workbook = new HSSFWorkbook(fileInputStream);
		} else if (isExcel2007(filepath)) {
			workbook = new XSSFWorkbook(fileInputStream);
		} else {
			throw new Exception("创建文件格式错误！");
		}
		return workbook;
	}

	/**
	 * 判断是不是excel2003
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 判断是不是2007
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
	
	public static void main(String[] args) {
		String fileName = "aa.xls";
		//System.out.println(isExcel2003(fileName));
	}

}