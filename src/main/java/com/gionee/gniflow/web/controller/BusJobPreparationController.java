package com.gionee.gniflow.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.gionee.auth.OrganizationService;
import com.gionee.auth.model.Organization;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.web.util.ResponseFactory;
import com.gionee.gniflow.biz.model.BusJobPreparation;
import com.gionee.gniflow.biz.service.BusJobPreparationService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.response.BusJobPreparationResponse;
import com.gionee.gniflow.web.util.GnifStringUtils;
import com.gionee.gniflow.web.util.MimeUtil;
import com.gionee.gniflow.web.util.PoiUtil;

@RequestMapping("/busJobPrepara")
@Controller
public class BusJobPreparationController {
	
	private static final Logger logger = LoggerFactory.getLogger(BusJobPreparationController.class);
	
	@Autowired
	private BusJobPreparationService busJobbusJobPreparationService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private CommonsMultipartResolver multipartResolver;
	
    /** 加载岗位编制信息 */
    @RequestMapping("/loadBusJobPreparation.json")
    @ResponseBody
    public GridPageData<BusJobPreparationResponse> loadBusJobPreparation(QueryMap queryMap){
    	PageResult<BusJobPreparation> pageResult = busJobbusJobPreparationService.queryPage(queryMap);
    	GridPageData<BusJobPreparationResponse> gridPageData = 
    			new GridPageData<BusJobPreparationResponse>(ResponseFactory.convertList(pageResult.getRows(), BusJobPreparationResponse.class),pageResult.getTotal().longValue());
    	return gridPageData;
    }
    
    /** 保存岗位编制 */
    @RequestMapping(value="/saveBusJobPrepare.json", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveDictionary(BusJobPreparation busJobPreparation){
    	AjaxJson ajaxJson = null;
    	try {
    		busJobbusJobPreparationService.save(busJobPreparation);
    		ajaxJson = new AjaxJson(true, "保存岗位编制成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存岗位编制失败!");
		}
    	return ajaxJson;
    }
    
    /** 删除岗位编制*/
    @RequestMapping(value="/deleteJobPrepara.json", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson deleteJobPrepara(@RequestParam("id") int id) {
    	AjaxJson ajaxJson = null;
    	try {
    		busJobbusJobPreparationService.delete(id);
    		ajaxJson = new AjaxJson(true, "删除岗位编制成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除岗位编制失败!");
		}
    	return  ajaxJson;
    }
    
    /**
     * 导入人员配置
     * @param file
     * @return
     */
    @RequestMapping(value = "/importBusJobPrepara.html")
    @ResponseBody
    public AjaxJson importUserAssignment(@RequestParam(value = "file") MultipartFile file) {
    	AjaxJson ajaxJson = null;
		String fileName = file.getOriginalFilename();
		
		String[] excleTitle = new String[]{"部门名称","岗位名称","职级","编制",/*"现有人数",*/"所属公司","薪资范围"};
		//存储错误信息
		Map<Integer,String> errors = new TreeMap<Integer,String>();
		StringBuffer sb = null;
		if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx") && file.getSize() < 10*1024*1024) {
			try {
				int i= 0;
				List<Map<String,String>> lists = PoiUtil.importExcelToMap(file.getInputStream(), fileName,excleTitle.length, 1);
				//循环验证格式，为空，数字
				if (!CollectionUtils.isEmpty(lists)) {
					for(Map<String, String> element : lists){
						sb = new StringBuffer();
						String orgName = element.get("0");
						String positions = element.get("1");
						String grade = element.get("2");
						String plait = element.get("3");
//						String existNum = element.get("4");
						String rootOrgName = element.get("4");
						String salaryRange = element.get("5");
						if(StringUtils.isEmpty(orgName)){
							sb.append("部门名称为空！");
						}
						if(StringUtils.isEmpty(positions)){
							sb.append("岗位名称为空！");
						}
						if(StringUtils.isEmpty(grade)){
							sb.append("职级为空！");
						}
						if(StringUtils.isEmpty(plait)){
							sb.append("编制为空！");
						}
						if(!GnifStringUtils.isNumer(plait)){
							sb.append("编制必须是数字！");
						}
//						if(StringUtils.isEmpty(existNum)){
//							sb.append("现有人数为空！");
//						}
//						if(!GnifStringUtils.isNumer(existNum)){
//							sb.append("现有人数必须是数字！");
//						}
//						if (Integer.parseInt(existNum) > Integer.parseInt(plait)) {
//							sb.append("现有人数大于编制人数！");
//						}
						if(StringUtils.isEmpty(rootOrgName)){
							sb.append("所属公司为空！");
						}
						if(StringUtils.isEmpty(salaryRange)){
							sb.append("薪资范围为空！");
						}
						if(sb.toString().length()>0){
							errors.put(i+1, sb.toString());
						}
						i++;
					}
				} else {
					errors.put(2, "Excel文件中未包含导入数据!");
				}
				//有错，则输出相关信息
				if(errors.size() > 0){
					sb = new StringBuffer();
					for(Object o: errors.keySet()){
						sb.append("第" + o + "行").append(errors.get(o)).append("<br/>");
			        }  
					ajaxJson = new AjaxJson(false, sb.toString());
				} else {
					try {
						busJobbusJobPreparationService.handleBusJobPrepara(lists);
						ajaxJson = new AjaxJson(true, "导入成功！");
					} catch (Exception e) {
						ajaxJson = new AjaxJson(false, e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.error("BpmRoleController Exception", e);
				ajaxJson = new AjaxJson(false, "导入失败,请详细检查导入文件格式是否正确!");
			} 
		} else {
			ajaxJson = new AjaxJson(false, "导入失败,请导入Excel文件!");
		}
		
		return ajaxJson;
    }
    
    /**
     * 导出岗位编制
     * @param exportOrgId
     */
    @RequestMapping(value = "/exportBusJobPrepara.html")
    public void exportBusJobPrepara(@RequestParam("exportOrgId") Integer exportOrgId, HttpServletRequest request, 
    		HttpServletResponse response){
    	List<BusJobPreparation> datas = busJobbusJobPreparationService.statBusJobPreparation(exportOrgId);
    	try {
	    	if (!CollectionUtils.isEmpty(datas)) {
	    		//画Excel
	    		HSSFWorkbook workbook = new HSSFWorkbook();
	    	    HSSFSheet sheet = workbook.createSheet("岗位编制信息");
	    	    // 设置表格默认列宽度为15个字节
	            sheet.setDefaultColumnWidth(15);
	            // 生成一个样式
	            CellStyle titleStyle = PoiUtil.defaultCellStyle(workbook, PoiUtil.createFount(workbook, (short)14));
	            CellStyle bodyStyle = PoiUtil.defaultCellStyle(workbook, PoiUtil.createFount(workbook, (short)12));
	            
	            String[] headers = {"序号","部门/科室","岗位名称","职级","编制",/*"现有人数",*/"薪资范围"};
	            
	            String excelTitle = "";
	            Organization org = organizationService.getOrganization(exportOrgId);
	            if (org != null) {
	            	excelTitle = datas.get(0).getRootOrgName() + org.getName() + "岗位统计表";
	            }
	            
	            PoiUtil.createExcelHeader(excelTitle, titleStyle, sheet, 
	            		new CellRangeAddress(0,0,0,headers.length - 1), 40, workbook);
	           
	            // 产生表格标题行
	            PoiUtil.createExcelHeaders(headers, bodyStyle, sheet, 1, 20);
	            int rowSeq = 1;
	            int startRow = 2;
	            int mergerStartRow = 2;
            	for (int i = 0; i < datas.size(); i++) {
	        		HSSFRow row = sheet.createRow(startRow);
	        		row.setHeightInPoints(20);
	        		
	        		HSSFCell cell = row.createCell(0);
	                cell.setCellStyle(bodyStyle);
	                cell.setCellValue(new HSSFRichTextString(String.valueOf(rowSeq)));
					
	                PoiUtil.drawCell4Object(bodyStyle, row, 
							new String[]{"orgName","positions","grade","plait",/*"existNum",*/"salaryRange"}, datas.get(i));
	                
	                if (i != 0 && (datas.get(i).getOrgId().intValue() == datas.get(i-1).getOrgId().intValue())) {
	                	PoiUtil.megerCell(sheet, new CellRangeAddress(mergerStartRow,startRow,1,1), workbook);
	                	mergerStartRow = startRow + 1;
	                } else {
	                	mergerStartRow = startRow;
	                }
	        		rowSeq++;
	        		startRow++;
				}
            	 
            	response.setContentType("application/ms-excel; charset=utf-8");
            	response.setHeader("Content-disposition", "attachment; filename="
            			+ MimeUtil.encodeFilename(request, excelTitle+".xls"));
                
                OutputStream ouputStream = response.getOutputStream();     
                workbook.write(ouputStream);  
                ouputStream.flush();     
                ouputStream.close();
	    	} else {
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
    
    /**
     * 获取岗位编制信息
     * @return
     */
    @RequestMapping("/getPositionInfo.json")
	@ResponseBody
    public AjaxJson getPositionInfo(@RequestParam("positionId") String positionId){
    	AjaxJson ajaxJosn = null;
    	BusJobPreparation busJobPreparation = null;
    	int posiId = Integer.parseInt(positionId);
		try {
			busJobPreparation = busJobbusJobPreparationService.getBusJobPreparation(posiId);
        	ajaxJosn = new AjaxJson();
 			ajaxJosn.setSuccess(true);
 			ajaxJosn.setObj(busJobPreparation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ajaxJosn;
    }
    
    /**
     * 根据部门ID获取部门下岗位
     */
    @RequestMapping("/jonInfoFromDept.json")
	@ResponseBody
    public List<ComboBoxData> jonInfoFromDept(@RequestParam("deptId") String deptId) {
    	return busJobbusJobPreparationService.getJobInfoFromDept(deptId);
    }
}
