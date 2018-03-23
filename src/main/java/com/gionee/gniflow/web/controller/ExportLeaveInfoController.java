package com.gionee.gniflow.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.service.ExportLeaveInfoService;
import com.gionee.gniflow.biz.service.ProcessHelpService;



@RequestMapping("/export")
@Controller
public class ExportLeaveInfoController {
	
    @Autowired
	private ExportLeaveInfoService exportLeaveInfoService;
	
    @Autowired
    private ProcessHelpService processHelpService;
    
	@RequestMapping("/exportLeaveInfo.html")
	@ResponseBody
	public void exportDetail(QueryMap critera,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		List<Map> results = exportLeaveInfoService.queryLeaveInfo(critera.getMap());
		try {
	    	if (!CollectionUtils.isEmpty(results)) {
	    		toExcel(response, results);
	    	}else {
	            response.setContentType("text/plain; charset=UTF-8");
				try {
					response.getWriter().println("数据不存在,导出Excel失败!");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
	    	}
    	} catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain; charset=UTF-8");
			try {
				response.getWriter().println("导出Excel文件失败!");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
        } 
	}
	void toExcel(HttpServletResponse response, List<Map> results) throws Exception{
		//List<Map> llist=exportLeaveInfoService.query(new HashMap<String, Object>());
		// 获取总列数
        // 创建Excel文档
        HSSFWorkbook hwb = new HSSFWorkbook();
        // sheet 对应一个工作页
        HSSFSheet sheet = hwb.createSheet("离职信息明细表");
        HSSFRow firstrow = sheet.createRow(0); // 下标为0的行开始
        //流程实例编号		发起人编号		发起人姓名			离职人员编号			离职人员姓名	离职人员部门	离职操作类型	 			离职日期
        //procInstId	applyUserId	applyUserName	applyUserAccount	userName	orgName	 	operationReasonText		leaveDate
        String[] names = new String[]{"流程实例编号","发起人编号","发起人姓名","离职人员编号","离职人员姓名","离职人员部门","离职操作类型","离职日期"};
        String[] values = new String[]{"procInstId","applyUserId","applyUserName","applyUserAccount","userName","orgName","operationReasonText","leaveDate"};
        
        //设置excel宽度
        int flag = 0;
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        sheet.setColumnWidth(flag++, 30 * 255);
        sheet.setColumnWidth(flag++, 25 * 255);
        sheet.setColumnWidth(flag++, 20 * 255);
        int CountColumnNum = names.length;
        for (int i = 0; i < CountColumnNum; i++) {
            firstrow.createCell(i).setCellValue(new HSSFRichTextString(names[i]));
        }
        HSSFCellStyle cellStyleStr = hwb.createCellStyle();  
        cellStyleStr.setDataFormat(hwb.createDataFormat().getFormat("@"));  
        cellStyleStr.setWrapText(true);
		Map<Object,List<Map>> llm = new HashMap();
		//根据流程实例编号区分导出数据条数
        for(Map m:results){
        	if(!llm.containsKey(m.get("ID"))){
        		llm.put( m.get("ID"), new ArrayList<Map>());
        	}
        	List<Map> lm=llm.get(m.get("ID"));
        	lm.add(m);
        }
        int rown=1;
        //流程实例编号
        for(Object id:llm.keySet()){
        	HashMap m = new HashMap<>();
    		m.put("NAME_", "procInstId");//procInstId  流程实例编号
    		m.put("TEXT_", id);
    		llm.get(id).add(m);
        }
        //根据发起人编号获得发起人姓名
        for(List<Map> rowMap:llm.values()){
	        for (int i = 0; i < values.length; i++) {
	            for(Map m:rowMap){
	            	if(m.get("NAME_").equals("applyUserId")){
	            		//String name=processHelpService.getUserName(clobToString((Clob)m.get("TEXT_")));
	            		String name=processHelpService.getUserName((String)m.get("TEXT_"));
	            		HashMap<String, Object> mm = new HashMap<>();
	            		mm.put("NAME_", "applyUserName");
	            		mm.put("TEXT_", name);
	            		rowMap.add(mm);
	            		break;
	            	}
	            }
	        }
	        HSSFRow row = sheet.createRow(rown++);
	        for (int i = 0; i < values.length; i++) {
	            HSSFCell cell=row.createCell(i);
	            cell.setCellStyle(cellStyleStr);
	            for(Map m:rowMap){
	            	if(m.get("NAME_").equals(values[i])){
	                    cell.setCellValue(conver(m.get("TEXT_")));
	            	}
	            }
	        }
        }
        // 创建文件输出流，准备输出电子表格
        response.setContentType("application/vnd.ms-excel");   
        String encodedFileName = "leaveInfoDetailExcel.xls" ; 
		response.setHeader("Content-disposition", "attachment;filename=" + encodedFileName);
		OutputStream out = response.getOutputStream();
        hwb.write(out);
        out.flush();
        out.close();
        //System.out.println("数据库导出成功");
	}
	//行列转换
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private String conver(Object obj){
		if(obj==null)
			return "";
		if(obj instanceof Date){
			return df.format((Date)obj);
		}
		return obj.toString();
	}
	//将clob类型的数据，转换为string类型
	public static String clobToString(Clob clob) { 

		String reString = ""; 
	    Reader is = null; 
	    try{ 
           is = clob.getCharacterStream(); 
	     }catch(SQLException e) { 
	    	 e.printStackTrace(); 
	     } 
	    // 得到流
	    BufferedReader br = new BufferedReader(is); 
        String s = null; 
        try{ 
        	s = br.readLine(); 
        }catch(IOException e) { 
        	e.printStackTrace(); 
        } 
        StringBuffer sb = new StringBuffer(); 
        while(s != null) { 
        // 执行循环将字符串全部取出付值给 StringBuffer由StringBuffer转成STRING 
        		sb.append(s); 
		        try{ 
		        		s = br.readLine(); 
		        }catch(IOException e) { 
			             e.printStackTrace(); 
			     } 
        	} 
	        reString = sb.toString(); 
	        return reString; 
	    } 

	  
}
