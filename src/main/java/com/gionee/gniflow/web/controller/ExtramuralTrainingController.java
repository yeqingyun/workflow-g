package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.gionee.auth.UserService;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.BpmRoleProcdefService;
import com.gionee.gniflow.biz.service.BpmRoleService;
import com.gionee.gniflow.biz.service.BpmUserRoleService;
import com.gionee.gniflow.biz.service.CategoryService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.util.PoiUtil;


@RequestMapping("/extramuralTraining")
@Controller
public class ExtramuralTrainingController {
	
	private static final Logger logger = LoggerFactory.getLogger(ExtramuralTrainingController.class);
	
	@Autowired
	private ProcessHelpService processHelpService;
	
    /**
     * 导入人员配置
     * @param file
     * @return
     */
    @RequestMapping(value = "/importTrainingEmp.html")
    @ResponseBody
    public AjaxJson importUserAssignment(@RequestParam(value = "file") MultipartFile file) {
    	AjaxJson ajaxJson = new AjaxJson();
		String fileName = file.getOriginalFilename();
		
		String[] excleTitle = new String[]{"empNo","empName"};
		//存储错误信息
		Map<Integer,String> errors = new TreeMap<Integer,String>();
		StringBuffer sb = null;
		if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx") && file.getSize() < 10*1024*1024) {
			try {
				int i= 0;
				List<Map<String,String>> excelData = PoiUtil.importExcelToMap(file.getInputStream(), fileName,excleTitle.length, 1);
				List<Map<String,String>>  lists = new ArrayList<Map<String,String>>();
				//循环验证格式，为空，数字
				if (!CollectionUtils.isEmpty(excelData)) {
					for(Map<String, String> element : excelData){
						sb = new StringBuffer();
						String empNo = element.get("0");
						String empName = element.get("1");
						if(StringUtils.isEmpty(empNo)){
							sb.append("员工编号为空！");
						}
						if(StringUtils.isEmpty(empName)){
							sb.append("员工姓名为空！");
						}
						if(sb.toString().length()>0){
							errors.put(i+1, sb.toString());
						}
						Map<String,String> empInfo = new HashMap<String,String>();
						empInfo.put("empNo", empNo);
						empInfo.put("empName", empName);
						lists.add(empInfo);
						i++;
					}
				} else {
					errors.put(2, "Excel文件中未包含导入数据!");
				}
				//判断是否有重复项
				if(lists.size()>1){
					String str = IsExistAlike(lists);
					if (!"success".equals(str)) {
						ajaxJson.setMsg(str);
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
				}
				//根据员工编号获取信息,判断是否有效，名字是否冲突
				List<Map<String,String>> empInfos = new ArrayList<Map<String,String>>();
				for(Map<String,String> map:lists){
					String empNo = map.get("empNo").trim();
					String empName = map.get("empName").trim();
					List<HrUserDto> list = processHelpService.getEmpInfoById(empNo);
					if(list.size()>0){
						HrUserDto user = list.get(0);
						if(!user.getName().equals(empName)){
							ajaxJson.setMsg("员工编号[ "+empNo+"]的员工的名字["+ empName + "]与系统中的名字["+user.getName()+"]有冲突！");
							ajaxJson.setSuccess(false);
							return ajaxJson;
						}else{
							Map<String,String> empInfo = new HashMap<String,String>();
							empInfo.put("empId", list.get(0).getEmpId().toString());
							empInfo.put("name", list.get(0).getName());
							empInfo.put("orgName", list.get(0).getOrgName());
							empInfo.put("job", list.get(0).getJob());
							empInfos.add(empInfo);
						}
					}else{
						ajaxJson.setMsg("员工 ["+empNo+","+ empName + "]不存在！");
						ajaxJson.setSuccess(false);
						return ajaxJson;
					}
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
						ajaxJson = new AjaxJson(true, "导入成功！");
						ajaxJson.setObj(empInfos);
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
    
	//判断导入的数据中是否有重复数据
	 public String IsExistAlike(List<Map<String, String>> list){
		  String str ="success";
		  label:for(int index=0;index<list.size();index++){
				for(int y=index+1;y<list.size();y++){
					Map<String, String> current = list.get(index);
					Map<String, String> next = list.get(y);
					if(StringUtils.equals(current.get("empNo").trim(), next.get("empNo").trim())){
						str="员工编号["+current.get("empNo")+"]重复导入!";
						break label;
					}
				}
			}
		  return str;
	  }
    
}
