package com.gionee.gniflow.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class ExcelReadUtil {
	

	public static Object selectType(Cell cell) throws Exception{
		switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString();
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
            	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                return df.parse(df.format(cell.getDateCellValue()));
            } else {
            	String rex = Double.toString(cell.getNumericCellValue());
            	if(rex.matches("[1-9]\\d*\\.[0]*")){
            	 return Integer.parseInt(rex.substring(0,rex.indexOf('.'))); 
            	 }else{
            		 return cell.getNumericCellValue();
            	 }
            } 
        case Cell.CELL_TYPE_BOOLEAN:
           return cell.getBooleanCellValue();
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        default:
            return " ";
    }
	}
	
	public static Object selectTypeTo(Cell cell) throws Exception{
		switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString();
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
            	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                return df.parse(df.format(cell.getDateCellValue()));
            } else {
            	String rex = Double.toString(cell.getNumericCellValue());
            	if(rex.matches("[0-9]\\d*\\.[0]*")){
            	 return Integer.parseInt(rex.substring(0,rex.indexOf('.'))); 
            	 }else{
            		 return cell.getNumericCellValue();
            	 }
            } 
        case Cell.CELL_TYPE_BOOLEAN:
           return cell.getBooleanCellValue();
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        default:
            return " ";
    }
	}
	
	public static Object selectTypeForString(Cell cell) throws Exception{
		if(cell.getCellType()==Cell.CELL_TYPE_STRING){
			cell.setCellType(Cell.CELL_TYPE_STRING);	
			return cell.getStringCellValue();
		}
		else if (DateUtil.isCellDateFormatted(cell)) {
        	DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
            return df.parse(df.format(cell.getDateCellValue()));
        }
		else if (cell.getCellStyle().getDataFormat() == 58||cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd")||cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("yyyy/MM/dd")) {
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");  
			 double value = cell.getNumericCellValue();  
			 Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);  
			 return sdf.format(date);  
	
		}else{
			cell.setCellType(Cell.CELL_TYPE_STRING);	
			return cell.getStringCellValue();
		}
	  
	}
	
	
	/*
	 * @param TableHead 表头所占行数。
	 * @param fileds Excel表的连续列对应实体类的名字
	 * 
	 * */
	public static <T> List<T> ReadExcel(InputStream is,String[] fileds,Class clszz,int tableHead) throws Exception {
		int count=0;
		Workbook wbook = WorkbookFactory.create(is);
		Sheet sheet = wbook.getSheetAt(0);
		List<T> list = new ArrayList<T>();
		int index=0;
		for(Row row : sheet){
			if(index++>(tableHead-1)){
				Object obj = clszz.newInstance();
				for(int i=0;i<fileds.length;i++){
					if(org.springframework.util.StringUtils.isEmpty(row.getCell(0))){
						count++;
						continue;
					}
					try {
						ReflectionUtils.invokeSetterMethod(obj,fileds[i],selectTypeTo((row.getCell(i)==null?row.createCell(0):row.getCell(i))));
					} catch (IllegalArgumentException e) {
						StringBuilder sb = new StringBuilder();
						sb.append("上传失败:原因：第"+index+"行"+"第"+(i+1)+"列");
						sb.append(e.getMessage());
						sb.append(",请修改Excel");
						throw new IllegalArgumentException(sb.toString());
					}catch(NullPointerException e){
						StringBuilder sb = new StringBuilder();
						sb.append("上传失败:原因：第"+index+"行"+"第"+(i+1)+"列");
						sb.append("的值为空,请修改Excel");
						throw new NullPointerException(sb.toString());
					} 
					catch (InvocationTargetException e) {
						
					} catch (IllegalAccessException e) {
					}
				}
				if(count==0){
				list.add((T) obj);
				}
			}
		}
		return list;
	}
	
	
	/*
	 * @param TableHead 表头所占行数。
	 * @param fileds Excel表的连续列对应实体类的名字
	 * 
	 * */
	public static <T> List<T> ReadExcelForForeign(InputStream is,String[] fileds,Class clszz,int tableHead) throws Exception {
		int count=0;
		Workbook wbook = WorkbookFactory.create(is);
		Sheet sheet = wbook.getSheetAt(0);
		List<T> list = new ArrayList<T>();
		int index=0;
		for(Row row : sheet){
			if(index++>(tableHead-1)){
				Object obj = clszz.newInstance();
				for(int i=0;i<fileds.length;i++){
					if(org.springframework.util.StringUtils.isEmpty(row.getCell(0))){
						count++;
						continue;
					}
					try {
						ReflectionUtils.invokeSetterMethod(obj,fileds[i],selectTypeTo((row.getCell(i)==null?row.createCell(0):row.getCell(i))));
					} catch (IllegalArgumentException e) {
						StringBuilder sb = new StringBuilder();
						sb.append("上传失败:原因：第"+index+"行"+"第"+(i+1)+"列");
						sb.append(e.getMessage());
						sb.append(",请修改Excel");
						throw new IllegalArgumentException(sb.toString());
					}catch(NullPointerException e){
						StringBuilder sb = new StringBuilder();
						sb.append("上传失败:原因：第"+index+"行"+"第"+(i+1)+"列");
						sb.append("的值为空,请修改Excel");
						throw new NullPointerException(sb.toString());
					} 
					catch (InvocationTargetException e) {
						
					} catch (IllegalAccessException e) {
					}
				}
				if(count==0){
				list.add((T) obj);
				}
			}
		}
		return list;
	}

	
	public static <T> List<T> ReadExcelForTraining(InputStream is,String[] fileds,Class clszz,int tableHead) throws Exception {
		Workbook wbook = WorkbookFactory.create(is);
		Sheet sheet = wbook.getSheetAt(0);
		List<T> list = new ArrayList<T>();
		int index=0;
		for(Row row : sheet){
			if(index++>(tableHead-1)){
				Object obj = clszz.newInstance();
				String text=null;
				row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				text=row.getCell(0).getStringCellValue();
				if(text!=null&&!"".equals(text.trim())){
					for(int i=0;i<fileds.length;i++){
						try {
							ReflectionUtils.invokeSetterMethod(obj,fileds[i].toString(),selectTypeForString(row.getCell(i)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else{
					continue;	
				}
				
				list.add((T) obj);
				}
		}
		return list;
	}
	
	public static <T> List<T> newCompileReadExcel(InputStream is,String[] fileds,Class clszz,int tableHead) throws Exception {
		int count=0;
		Workbook wbook = WorkbookFactory.create(is);
		Sheet sheet = wbook.getSheetAt(0);
		List<T> list = new ArrayList<T>();
		int index=0;
		for(Row row : sheet){
			if(index++>(tableHead-1)){
				Object obj = clszz.newInstance();
				for(int i=0;i<fileds.length;i++){
					if(org.springframework.util.StringUtils.isEmpty(row.getCell(0))){
						count++;
						continue;
					}
					if((index==4||index==8||index==10)&&StringUtils.isEmpty(row.getCell(0).toString())){
						throw new IllegalArgumentException("请完善内容");
					}
					try {
						ReflectionUtils.invokeSetterMethod(obj,fileds[i],selectType((row.getCell(i)==null?row.createCell(0):row.getCell(i))));
					} catch (IllegalArgumentException e) {
						StringBuilder sb = new StringBuilder();
						sb.append("上传失败:原因：第"+index+"行"+"第"+(i+1)+"列");
						sb.append(e.getMessage());
						sb.append(",请修改Excel");
						throw new IllegalArgumentException(sb.toString());
					}catch(NullPointerException e){
						StringBuilder sb = new StringBuilder();
						sb.append("上传失败:原因：第"+index+"行"+"第"+(i+1)+"列");
						sb.append("的值为空,请修改Excel");
						throw new NullPointerException(sb.toString());
					} 
					catch (InvocationTargetException e) {
						
					} catch (IllegalAccessException e) {
					}
				}
				if(count==0){
				list.add((T) obj);
				}
			}
		}
		return list;
	}
}
