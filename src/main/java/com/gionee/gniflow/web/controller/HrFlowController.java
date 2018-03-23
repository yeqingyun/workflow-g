/**
 * @(#) WorkspaceController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.auth.OrganizationService;
import com.gionee.auth.UserService;
import com.gionee.auth.dao.OrganizationDao;
import com.gionee.auth.model.Organization;
import com.gionee.auth.model.User;
import com.gionee.devtool.web.response.auth.OrganizationResponse;
import com.gionee.devtool.web.response.auth.UserResponse;
import com.gionee.devtool.web.util.DevResponseFactory;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.CostCenter;
import com.gionee.gniflow.biz.model.JTeamMonth;
import com.gionee.gniflow.biz.model.LTeamActivitiesDetail;
import com.gionee.gniflow.biz.service.LTeamActivitiesDetailService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.SysCategoryService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.dto.UserDto;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.integration.dao.OrganizationHelpDao;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.bmp.SendOrderApplyEntity;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.support.AudiException;
import com.sap.conn.jco.JCoException;

/**
 * The class <code>WorkspaceController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/hrFlow")
@Controller
public class HrFlowController {
	private static final String OPEN = "open";
	private static final String CLOSED = "closed";
	
	private static final Logger logger = LoggerFactory.getLogger(HrFlowController.class);
	
	@Autowired
	private ProcessHelpService processHelpService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private OrganizationHelpDao organizationHelpDao;
	
	@Autowired
	private LTeamActivitiesDetailService  lTeamActivitiesDetailService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private SysCategoryService categoryService;
	
	@Autowired
	private KeepRunTaskDao keepRunTaskDao;
	
	@RequestMapping("/auth/orgs.json")
	@ResponseBody
	public List<OrganizationResponse> orgs(Integer id) {
		if (id == null) {
			OrganizationResponse rootOrg = new OrganizationResponse();
			rootOrg.setChildren(DevResponseFactory.convertOrganization(organizationService.getRoots()));
			List<OrganizationResponse> ret = new ArrayList<OrganizationResponse>();
			ret.add(rootOrg);
			return ret;
		}
		else if (id == 0) {
			return DevResponseFactory.convertOrganization(organizationService.getRoots());
		}
		else {
			return DevResponseFactory.convertOrganization(organizationService.getOrganization(id).getSubOrganizations());
		}
	}
	
	@RequestMapping("/auth/xinxibuOrgs.json")
	@ResponseBody
	public List<OrganizationResponse> xinxibuOrgs() {
			
			List<OrganizationResponse> ret = new ArrayList<OrganizationResponse>();
			OrganizationResponse rootOrg = new OrganizationResponse();
			rootOrg.setId(57682);
			rootOrg.setName("项目部（信息中心）");
			rootOrg.setPid(57681);
			ret.add(rootOrg);
			
			rootOrg = new OrganizationResponse();
			rootOrg.setId(57707);
			rootOrg.setName("开发部（信息中心）");
			rootOrg.setPid(57681);
			ret.add(rootOrg);
			
			rootOrg = new OrganizationResponse();
			rootOrg.setId(57742);
			rootOrg.setName("运维部（信息中心）");
			rootOrg.setPid(57681);
			ret.add(rootOrg);
			
			rootOrg = new OrganizationResponse();
			rootOrg.setId(57751);
			rootOrg.setName("硬件部（信息中心）");
			rootOrg.setPid(57681);
			ret.add(rootOrg);
			
			return ret;
	}
	
	@RequestMapping("/auth/getCom.json")
	@ResponseBody
	public List<OrganizationResponse> getCompany(Integer id) {
		
			Organization organ = new Organization();
			organ.setId(56876);
			return DevResponseFactory.convertOrganization(organ.getSubOrganizations());
		
	}
	
	@RequestMapping("/auth/filterCom.json")
	@ResponseBody
	public List<OrganizationResponse> filterCom(Integer id) {
		
			Organization organ = new Organization();
			organ.setId(56876);
			 List<OrganizationResponse>list=DevResponseFactory.convertOrganization(organ.getSubOrganizations());
			 for (int i = list.size()-1; i >=0; i--) {
				if (list.get(i).getId()!=56877) {
				   list.remove(i);
				}
			 }
			return list;
		
	}
	@RequestMapping("/auth/filterFactory.json")
	@ResponseBody
	public List<OrganizationResponse> filterFactory(){
		Organization organ = new Organization();
		organ.setId(56876);
		 List<OrganizationResponse>list=DevResponseFactory.convertOrganization(organ.getSubOrganizations());
		 for (int i = list.size()-1; i >=0; i--) {
			if (list.get(i).getId()!=59513 && list.get(i).getId()!=66111) {
			   list.remove(i);
			}
		 }
		return list;
	}
	/**从SAP获取金铭、金卓下的所有成本中心
	 * @throws Exception */
	/*@RequestMapping("/auth/filterDepartment.json")
	@ResponseBody
	public List<OrganizationResponse> filterDepartment() throws Exception{
		Organization organ = new Organization();
		organ.setId(56876);
		 List<OrganizationResponse> list=DevResponseFactory.convertOrganization(organ.getSubOrganizations());
		 for (int i = list.size()-1; i >=0; i--) {
			if (list.get(i).getId()!=59513 && list.get(i).getId()!=66111) {
			   list.remove(i);
			}
		 }
		 for(OrganizationResponse orgs:list){
			 List<OrganizationResponse> children=new ArrayList<OrganizationResponse>();
			 //List<ComboBoxData> coms=null;
			 List<Organization> coms=null;
			 if(orgs.getId()==59513){
				 coms=organizationDao.getAllByPid(59513);
				 //coms=getSapCostCenter("ZHR_OA44","I_TYPE_VALUE_C_NEXT_I_BUKRS_VALUE_2000","OT_CSKS","KOSTL","KTEXT");
				 
			 }else{
				 coms=organizationDao.getAllByPid(66111);
				 //coms=getSapCostCenter("ZHR_OA44","I_TYPE_VALUE_C_NEXT_I_BUKRS_VALUE_3000","OT_CSKS","KOSTL","KTEXT");
			 }
			for(ComboBoxData com:coms){
				 OrganizationResponse org=new OrganizationResponse();
				 org.setId(Integer.parseInt(com.getId()));
				 org.setName(com.getText());
				 children.add(org);
			 }
			 for(Organization com:coms){
				 OrganizationResponse org=new OrganizationResponse(com);
				 if (com.isLeaf()) {
						org.setState(OPEN);
					}
					else {
						org.setState(CLOSED);
					}
				 children.add(org);
			 }
			 orgs.setState(CLOSED);
			 orgs.setChildren(children);
		 }
		 return list;
	}*/
	@RequestMapping("/auth/users.json")
	@ResponseBody
	public List<UserResponse> users(Integer id) {
		List<UserResponse> ret = new ArrayList<UserResponse>();
		List<User> users = userService.getUsersByOrgId(id);
		for (User user:users) {
			UserResponse userResponse = new UserResponse(user);
			userResponse.setGroupEntities(userService.getGroups(user.getId()));
			ret.add(userResponse);
		}
		return ret;
	}

	/** 获取用户个人信息根据登录编号 */
	@RequestMapping("/getHrUserInfo.json")
	@ResponseBody
	public AjaxJson getHrUserInfo(@RequestParam("account") String account) {
		AjaxJson ajaxJosn = null;
		try {
			HrUserDto userDto = processHelpService.getHrUser4Account(account);
	        
	        ajaxJosn = new AjaxJson();
			ajaxJosn.setSuccess(true);
			ajaxJosn.setObj(userDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ajaxJosn;
	}
	
	/** 获取用户个人信息根据员工编号 */
	@RequestMapping("/getHrUserInfoById.json")
	@ResponseBody
	public AjaxJson getHrUserInfoById(@RequestParam("account") String account) {
		AjaxJson ajaxJosn = null;
		try {
			List<HrUserDto> userDto = processHelpService.getEmpInfoById(account);
	        
	        ajaxJosn = new AjaxJson();
			ajaxJosn.setSuccess(true);
			ajaxJosn.setObj(userDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ajaxJosn;
	}
	/** 设置顶层部门 */
	@RequestMapping("/setRootOrgInfo.json")
	@ResponseBody
	public AjaxJson setRootOrgInfo(@RequestParam("deptId") String deptId) {
		AjaxJson ajaxJosn = null;
		try {
			Organization rootOrg = organizationHelpDao.queryRootNode(Integer.parseInt(deptId));
			
			ajaxJosn = new AjaxJson();
			ajaxJosn.setSuccess(true);
			ajaxJosn.setObj(rootOrg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ajaxJosn;
	}
	
	@RequestMapping("/isDeptManager.json")
	@ResponseBody
	public AjaxJson isDeptManager(@RequestParam("account") String account) {
		AjaxJson ajaxJosn = new AjaxJson();
		try {
			String result = processHelpService.getOrgLeader(account, BPMConstant.LEADER_TYPE_MANAGER);
	        if (result.equals(account)) {
	        	ajaxJosn = new AjaxJson(true, "true");
	        } else {
	        	ajaxJosn = new AjaxJson(false, "false");
	        }
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJosn = new AjaxJson(false, "false");
		}
		return ajaxJosn;
	}
	/**
	 * 判断是否转正
	 */
	@RequestMapping("/isRegularEmployee.json")
	@ResponseBody
	public Boolean isRegularEmployee(@RequestParam("account") String account) {
			return processHelpService.isRegularEmployee(account);
	}
	
	@RequestMapping("/isOfficeWorker.json")
	@ResponseBody
	public Boolean isOfficeWorker(@RequestParam("account") String account) {
		return processHelpService.isOfficeWorker(account);
	}
	
	@RequestMapping("/isOfficeWorkerByEmpId.json")
	@ResponseBody
	public Boolean isOfficeWorkerByEmpId(@RequestParam("account") String account) {
		return processHelpService.isOfficeWorkerByEmpId(account);
	}
	@RequestMapping("/isThreeMonth.json")
	@ResponseBody
	public Boolean isThreeMonth(@RequestParam("account") String account) {
		return processHelpService.isThreeMonth(account);
	}
	
	@RequestMapping("/allCompanyInfo.json")
	@ResponseBody
	public List<ComboBoxData> allCompanyInfo() {
		List<ComboBoxData> comboData = new ArrayList<ComboBoxData>();
		try {
			List<Organization> orgs = organizationService.getRoots();
			for (Organization org : orgs) {
				comboData.add(new ComboBoxData(String.valueOf(org.getId()), org.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comboData;
	}
	
	@RequestMapping("/systemInfoFromCompany.json")
	@ResponseBody
	public List<ComboBoxData> systemInfoFromCompany(@RequestParam("companyId") String companyId) {
		return processHelpService.getSystemInfoFromCompany(companyId);
	}
	
	@RequestMapping("/deptInfoFromSystem.json")
	@ResponseBody
	public List<ComboBoxData> deptInfoFromSystem(@RequestParam("companyId") String companyId,
			@RequestParam("sysNo") String sysNo) {
		return processHelpService.getDeptInfoFromSystem(companyId, sysNo);
	}
	
	@RequestMapping("/jonInfoFromDept.json")
	@ResponseBody
	public List<ComboBoxData> jonInfoFromDept(@RequestParam("deptId") String deptId) {
		return processHelpService.getJobInfoFromDept(deptId);
	}
	
	@RequestMapping("/queryUserByOrgId.json")
	@ResponseBody
	public PageResult<UserDto> queryUserByOrgId(@RequestParam("taskId") String taskId, QueryMap queryMap) {
		try {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			 if (task != null) {
				User user = userService.getUserByAccount(task.getAssignee());
				if (user != null && queryMap.getMap().get("account") == null && queryMap.getMap().get("name") == null) {
					queryMap.put("orgId", user.getOrgId());
				}
			 }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		if (StringUtils.isEmpty(taskId)) {
			PageResult<UserDto> result = new PageResult<UserDto>();
			result.setRows(new ArrayList<UserDto>());
			result.setTotal(0);
			return result;
		}
		return processHelpService.queryPageUsers(queryMap);
	}
	/** 转换部门id为SAP所需的部门NUM **/
	@RequestMapping("/transOrgIdToSap.json")
	@ResponseBody
	public String transOrgIdToSap(@RequestParam("deptId") String deptId) {
		return processHelpService.getSapOrgCode4DeptId(deptId);
	}
	/** 获取SAP数据 */
	@RequestMapping("/getSapOptions.json")
	@ResponseBody
	public List<ComboBoxData> getSapOptions(String functionName,String vars,String exportTableName,String code,String describe) throws Exception{
		/*System.out.println("functionName---"+functionName);
		System.out.println("vars---"+vars);
		System.out.println("exportTableName---"+exportTableName);
		System.out.println("code---"+code);
		System.out.println("describe---"+describe);*/
		
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
					inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		return  processHelpService.getSapOptions(functionName,inputMap,exportTableName,code,describe);
	}
	
	/** 获取SAP数据 */
	@RequestMapping("/getSapOptions4Job.json")
	@ResponseBody
	public List<ComboBoxData> getSapOptions4Job(String functionName,String vars,String exportTableName,String code,String describe) throws Exception{
		//System.out.println("functionName---"+functionName);
		//System.out.println("vars---"+vars);
		//System.out.println("exportTableName---"+exportTableName);
		//System.out.println("code---"+code);
		//System.out.println("describe---"+describe);
		
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
					inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		return  processHelpService.getSapOptions4Job(functionName,inputMap,exportTableName,code,describe);
	}
	@RequestMapping("/getSapCostCenter.json")
	@ResponseBody
	public List<ComboBoxData> getSapCostCenter(String factory) throws Exception{
		List<ComboBoxData> comlist=new ArrayList<ComboBoxData>();
		List<CostCenter> costs=keepRunTaskDao.getCostCenterByFactory(factory);
		if(costs!=null&&costs.size()>0){
			ComboBoxData coms=new ComboBoxData();
			coms.setId("");
			coms.setText("");
			comlist.add(coms);
		}
		for(CostCenter cost:costs){
			ComboBoxData com=new ComboBoxData();
			com.setId(cost.getCode());
			com.setText(cost.getCode()+"-"+cost.getName());
			comlist.add(com);
		}
		return comlist;
		/*Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
						inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		return  processHelpService.getSapOptions4Job(functionName,inputMap,exportTableName,code,describe);*/
	}
	/** 获取SAP数据 */
	@RequestMapping("/getSapInfoArray.json")
	@ResponseBody
	public List<ComboBoxData> getSapInfoArray(String functionName,String vars,String exportTableName,String exportlist) throws Exception{
		/*System.out.println("functionName---"+functionName);
		System.out.println("vars---"+vars);
		System.out.println("exportTableName---"+exportTableName);
		System.out.println("exportlist---"+exportlist);*/
		
		String[] exportVars=null;
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
					inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		if(exportlist!=null&&!"".equals(exportlist.trim())){
			exportVars=exportlist.split("_NEXT_");
		}
		
		return  processHelpService.getSapInfoArray(functionName,inputMap,exportTableName,exportVars);
	}
	
	/** 获取SAP数据 */
	@RequestMapping("/getSapPersonInfo.json")
	@ResponseBody
	public String getSapPersonInfo(String functionName,String vars,String exportTableName,String exportlist) throws Exception{
		/*System.out.println("functionName---"+functionName);
		System.out.println("vars---"+vars);
		System.out.println("exportTableName---"+exportTableName);
		System.out.println("exportlist---"+exportlist);*/
		
		String[] exportVars=null;
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
					inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		if(exportlist!=null&&!"".equals(exportlist.trim())){
			exportVars=exportlist.split("_NEXT_");
		}
		
		return  processHelpService.getSapPersonInfo(functionName,inputMap,exportTableName,exportVars);
	}

	
	/** 获取SAP数据 */
	@RequestMapping("/getSapEmpInfoForJson.json")
	@ResponseBody
	public String getSapEmpInfoForJson(String functionName,String vars,String exportTableName,String exportlist) throws Exception{
		/*System.out.println("functionName---"+functionName);
		System.out.println("vars---"+vars);
		System.out.println("exportTableName---"+exportTableName);
		System.out.println("exportlist---"+exportlist);*/
		
		String[] exportVars=null;
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
					inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		if(exportlist!=null&&!"".equals(exportlist.trim())){
			exportVars=exportlist.split("_NEXT_");
		}
		
		return  processHelpService.getSapPersonInfoForJson(functionName,inputMap,exportTableName,exportVars);
	}
	/**
	 * 从SAP获取固定资产信息
	 * @param functionName
	 * @param vars
	 * @param exportTableName
	 * @param exportlist
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getAssetInfo.json")
	@ResponseBody
	public String getAssetInfo(String functionName,String vars,String exportTableName,String exportlist) throws Exception{
		String[] exportVars=null;
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
					inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		if(exportlist!=null&&!"".equals(exportlist.trim())){
			exportVars=exportlist.split("_NEXT_");
		}
		
		return  processHelpService.getSapAssetsInfoForJson(functionName,inputMap,exportTableName,exportVars);
	}
	/**
	 * 从SAP获取非生产物资信息
	 * @throws Exception 
	 */
	@RequestMapping("/getStockInfo.json")
	@ResponseBody
	public String getStockInfo(String functionName,String vars,String exportTableName,String exportlist) throws Exception{
		String[] exportVars=null;
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
						inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		if(exportlist!=null&&!"".equals(exportlist.trim())){
			exportVars=exportlist.split("_NEXT_");
		}
		
		return  processHelpService.getSapStockInfoForJson(functionName,inputMap,exportTableName,exportVars);
	}
	/** 获取SAP数据 */
	@RequestMapping("/getSapOptionsForJson.json")
	@ResponseBody
	public  List<ComboBoxData>  getSapOptionsForJson(String functionName,String vars,String exportTableName,String exportlist) throws Exception{
		/*System.out.println("functionName---"+functionName);
		System.out.println("vars---"+vars);
		System.out.println("exportTableName---"+exportTableName);
		System.out.println("exportlist---"+exportlist);*/
		
		String[] exportVars=null;
		Map<String,String> inputMap=null;
		if(vars!=null&&!"".equals(vars.trim())){
			inputMap= new   HashMap<String, String>(); 
			String strs[]=vars.split("_NEXT_");
				if(strs!=null&&strs.length>0){
					for(int i=0;i<strs.length;i++){
					String substrs[]=strs[i].split("_VALUE_");
					if(substrs.length==1){
						inputMap.put(substrs[0],"");	
					}else{
					inputMap.put(substrs[0], substrs[1]);
					}
				   }
				}
			
		}
		
		if(exportlist!=null&&!"".equals(exportlist.trim())){
			exportVars=exportlist.split("_NEXT_");
		}
		
		return  processHelpService.getSapOptionsForJson(functionName,inputMap,exportTableName,exportVars);
	}
	
	
	@RequestMapping("/trainers.json")
	@ResponseBody
	public List<UserResponse> trainers() {
		List<UserResponse> ret = new ArrayList<UserResponse>();
		List<User> users = new ArrayList<User>();
		User u=new User();
		u.setAccount("01036314");
		u.setName("杨永秀");
		u.setId(17035);
		users.add(u);
		
		u=new User();
		u.setAccount("01010167");
		u.setName("朱军");
		u.setId(16898);
		users.add(u);
		
//		u=new User();
//		u.setAccount("01000165");
//		u.setName("于彬");
//		u.setId(16074);
//		users.add(u);
		
		for (User user:users) {
			UserResponse userResponse = new UserResponse(user);
			userResponse.setGroupEntities(userService.getGroups(user.getId()));
			ret.add(userResponse);
		}
		return ret;
	}
	
	@RequestMapping("/hardwareEngineer.json")
	@ResponseBody
	public List<UserResponse> hardwareEngineer() {
		List<UserResponse> ret = new ArrayList<UserResponse>();
		List<User> users = new ArrayList<User>();
		User u=new User();
		u.setAccount("00000068");
		u.setName("刘登科");
		u.setId(14976);
		users.add(u);
		
		u=new User();
		u.setAccount("tanyc");
		u.setName("谭业成");
		u.setId(15124);
		users.add(u);
		
		for (User user:users) {
			UserResponse userResponse = new UserResponse(user);
			userResponse.setGroupEntities(userService.getGroups(user.getId()));
			ret.add(userResponse);
		}
		return ret;
	}
	/**
	 * 根据部门已经在职日期获取员工，并剔除掉已经申请了部门经费的员工
	 * @param orgId
	 * @param zaizhiMonth
	 * @return
	 */
	@RequestMapping("/getOrgInfoByMonthAndOrg.json")
	@ResponseBody
	public AjaxJson getOrgInfoByMonthAndOrg(String orgId,String zaizhiMonth){
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setSuccess(false);
		Map<String,Object> result = processHelpService.getOrgInfoByMonthAndOrg(orgId, zaizhiMonth);
		Integer total = (Integer)result.get("total");
		Integer costTotal = (Integer)result.get("costTotal");
		JSONArray emps = (JSONArray)result.get("emps");
		JSONArray newEmps = new JSONArray();
		if(emps!= null && emps.size()>0){
			int length = emps.size();
			for(int i=0;i<length;i++){
				QueryMap queryMap = new QueryMap();
				JSONObject emp = (JSONObject)emps.get(i);
				queryMap.put("empId", emp.get("empId"));
				queryMap.put("activityMonth", zaizhiMonth);
				//查询并剔除已经申请的员工
				List<LTeamActivitiesDetail> list  = lTeamActivitiesDetailService.query(queryMap);
				if(list != null && list.size()>0){
					costTotal-=costTotal/total;
					total--;
				}else{
					newEmps.add(emp);
				}
			}
			if(total > 0){
				result.put("total", total);
				result.put("costTotal", costTotal);
				result.put("emps", newEmps);
				ajaxJson.setSuccess(true);
				ajaxJson.setAttributes(result);
			}
		}
		return ajaxJson;
	}
	
	
	@RequestMapping("/getActiDays.json")
	@ResponseBody
	public String getActiDays(String startTime,String endTime){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date end=sdf.parse(endTime);
			Date start=sdf.parse(startTime);
			Long mistiming=end.getTime()-start.getTime();
			Long dayCount=mistiming/1000/60/60/24;
			String days=dayCount.toString();
			return days;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping("/checkHasChild.json")
	@ResponseBody
	public List<Organization> checkHasChild(String id){
		return organizationHelpDao.checkHasChild(id);
	}
	/*@RequestMapping("/findById.json")
	@ResponseBody
	public List<OrganizationResponse> findById(String ids){
		String arr[]=ids.split(",");
		List<List<OrganizationResponse>>lists=new ArrayList<List<OrganizationResponse>>();
		
		List<OrganizationResponse> list=new ArrayList<OrganizationResponse>();
		if(arr.length>1){
			for (int i = 0; i <=arr.length-1; i++) {

				Organization o=organizationService.getOrganization(Integer.valueOf(arr[i]));
				if(o==null){
					continue;
				}
				OrganizationResponse organizationResponse = new OrganizationResponse(o);
				if (o.isLeaf()) {
					organizationResponse.setState("open");
				}else {
					organizationResponse.setState("close");
				}
				list.add(organizationResponse);
				 List<OrganizationResponse> list=DevResponseFactory.convertOrganization(o.getSubOrganizations());
				 
				if(list.size()>0){
						 lists.add(list);
				}else{
					list.add(new OrganizationResponse(o));
					lists.add(list);
				}
		   }
		}
		List<OrganizationResponse> list=DevResponseFactory.convertOrganization(orgList);
		/*List<OrganizationResponse> list=new ArrayList<OrganizationResponse>();
		for(List<OrganizationResponse> s:lists ){
			for(OrganizationResponse o: s){
				list.add(o);
			}
		}
		return list;
	}*/
	@RequestMapping("/findById.json")
	@ResponseBody
	public List<OrganizationResponse> findById2(Integer pid){
		List<Integer>list=categoryService.getAllByPid(pid);
		List<OrganizationResponse> ret = new ArrayList<OrganizationResponse>();
		for(Integer id: list){
			Organization organization =organizationService.getOrganization(id);
				if(organization==null){
					continue;
				}
				OrganizationResponse organizationResponse = new OrganizationResponse(organization);
				if (organization.isLeaf()) {
					organizationResponse.setState(OPEN);
				}
				else {
					organizationResponse.setState(CLOSED);
				}
				ret.add(organizationResponse);
			}
			return ret;
	}
	
	//检查所选日期是否合要求
	@RequestMapping("/checkSelectDate.json")
	@ResponseBody
	public boolean checkSelectDate(String date,Integer flag){
		Date date1=new Date();
		long time1=date1.getTime();
		long time2=0L;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date2=sdf.parse(date);
			time2=date2.getTime();
			if(flag==1){
				time1=time1+1000*60*60*24*29L;
			}else{
				time1=time1+1000*60*60*24*2L;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (time2-time1)>=0L;
	}
	
	//根据职员编号获取部门ID
	@RequestMapping("/getOrgIdByAccount.json")
	@ResponseBody
	public String getOrgIdByAccount(String account){
		return processHelpService.findById(account);
	}
	//根据账号获取职员的培训、工作经历
	@RequestMapping("/getUserWorkInfo.json")
	@ResponseBody
	public AjaxJson getUserWorkInfo(String account){
		AjaxJson ajaxJosn = new AjaxJson();
		Map<String,Object>map=processHelpService.getUserWorkInfo(account);
		ajaxJosn.setAttributes(map);
		return ajaxJosn;
	}
	
	//根据部门ID判断部门是否是三级以下的部门
	@RequestMapping("/isThirdAndLeaf.json")
	@ResponseBody
	public AjaxJson isThirdAndLeaf(String orgId){
		AjaxJson ajaxJosn = new AjaxJson();
		String orgIds="";
		Organization org=new Organization();
		org.setId(Integer.parseInt(orgId));
		if(org.isLeaf()){
			orgIds=orgId;
			ajaxJosn.setSuccess(true);
		}else if(org.getParents().size()>2){
			StringBuilder builder=new StringBuilder();
			ajaxJosn.setSuccess(true);
			getLeafDepartId(Integer.parseInt(orgId),builder);
			orgIds=builder.toString();
			orgIds=orgIds.substring(0,orgIds.length()-1);
		}else{
			ajaxJosn.setSuccess(false);
		}
		ajaxJosn.setMsg(orgIds);
		return ajaxJosn;
	}
	
	public void getLeafDepartId(Integer orgId,StringBuilder orgIds){
		Organization org=new Organization();
		org.setId(orgId);
		if(org.isLeaf()){
			orgIds.append(orgId+",");
		}else{
			orgIds.append(orgId+",");
			List<Organization> orgs=org.getSubOrganizations();
			for(Organization organization:orgs){
				getLeafDepartId(organization.getId(),orgIds);
			}
		}
	}
	    //获取服务器时间
		@RequestMapping("/loadSysDate.json")
		@ResponseBody
		public AjaxJson loadSysDate(){
			
			AjaxJson ajaxJson=new AjaxJson();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			   try {
				String now = sdf.format(new Date());//当前时间  
				if(Integer.parseInt(now.split("-")[2])<=10){
					 //获取当前月第一天：
					       Calendar c = Calendar.getInstance();    
					        c.add(Calendar.MONTH, -1);
					       c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
					        String first = sdf.format(c.getTime());
				       //获取当前月最后一天
					      Calendar ca = Calendar.getInstance();    
					        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
					        String last = sdf.format(ca.getTime());
					 String str=first+"@"+last;
					 
					 
					 ajaxJson.setSuccess(true);
					 ajaxJson.setMsg(str);
				   return ajaxJson;
				}else {
					 //获取当前月第一天：
				       Calendar c = Calendar.getInstance();    
				        c.add(Calendar.MONTH, 0);
				       c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
				        String first = sdf.format(c.getTime());
				         
			       //获取当前月最后一天
				      Calendar ca = Calendar.getInstance();    
				        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
				        String last = sdf.format(ca.getTime());
				 String str=first+"@"+last;
				 ajaxJson.setSuccess(true);
				 ajaxJson.setMsg(str);
			   return ajaxJson;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			return new AjaxJson(true,sdf.format(new Date()));
		}
		//查询成本中心
		@RequestMapping("/getAllCostCenter.json")
		@ResponseBody
		public GridPageData<CostCenter> getAllCostCenter(QueryMap queryMap){
			List<CostCenter> costs=keepRunTaskDao.getAllCostCenter(queryMap.getMap());
			Integer count=keepRunTaskDao.getTotalCount();
			return new GridPageData<CostCenter>(costs,count.longValue());
		}
		
	  //保存成本中心
	  @RequestMapping("/saveCostCenter.json")
	  @ResponseBody
	  public String saveCostCenter(String factory,String code,String name){
		  Map<String,Object> map=new HashMap<String,Object>();
		  map.put("factory",factory);
		  map.put("code",code);
		  map.put("name",name);
		  try{
			  keepRunTaskDao.saveCostCenter(map);
			  return "保存成功!";
		  }catch(Exception e){
			  return e.getMessage();
		  }
	  }
	  
	  //编辑成本中心
	  @RequestMapping("/updateCostCenter.json")
	  @ResponseBody
	  public String updateCostCenter(QueryMap queryMap){
		  try{
			  keepRunTaskDao.updateCostCenter(queryMap.getMap());
			  return "编辑成功!";
		  }catch(Exception e){
			  return e.getMessage();
		  }
	  }
	  
	  //根据成本中心号删除成本中心
	  @RequestMapping("/deleteCostCenter.json")
	  @ResponseBody
	  public String deleteCostCenter(String codes){
		  Map<String,Object> map=new HashMap<String,Object>();
		  map.put("codes", codes.split(","));
		  try{
			  keepRunTaskDao.deleteCostCenter(map);
			  return "删除成功!";
		  }catch(Exception e){
			  return e.getMessage();
		  }
	  }
	  
	  //根据员工编号判断调岗人员是否是部门的主管或经理
	  @RequestMapping("/isOffDireOrManager.json")
	  @ResponseBody
	  public String isOffDireOrManager(String account){
		  String direNo=processHelpService.getOrgLeader(account, "4");
		  //String managerNo=processHelpService.getOrgLeader(account, "1");
		  try {
			String result="0";
			String applyNo=processHelpService.getEmpAccount(account);
			if(applyNo!=null&&applyNo.equals(direNo)){
				result="1";
			}
			/*if(applyNo.equals(managerNo)){
				result="2";
			}*/
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	  }
	  
	  //检查接收部门审核人是否是经理或总监
	  @RequestMapping("/isManagerOrGeneral.json")
	  @ResponseBody
	  public String isManagerOrGeneral(String orgId,String account){
		  String managerNo=processHelpService.getOtherOrgLeader(orgId,"1");
		  String generalNo=processHelpService.getOtherOrgLeader(orgId,"2");
		  String result="0";
		  if(account.equals(managerNo)){
			  result="1";
		  }
		  if(account.equals(generalNo)){
			  result="2";
		  }
		  return result;
	  }
	  
	  //检查物料信息是否SAP的要求
	  @RequestMapping("/checkNonProdMaterInfoToSap.json")
	  @ResponseBody
	  public AjaxJson checkNonProdMaterInfoToSap(String proName,String proofType,String noProducs) throws JCoException{
		  Map<String,Object> map=processHelpService.parseJsonToMap(noProducs);
			
			List<Map<String,Object>> assets=(List<Map<String,Object>>)map.get("accessoryData");
			Map<String,String> param=new HashMap<String,String>();
			String factory=null;
			param.put("I_SUBMIT","");
			if("M-Equipment-Maintain".equals(proName)||"logisticProcurementAudiTask".equals(proName)||"updateEquipmentApplyTask".equals(proName)){
				param.put("I_BSART",proofType.split("#")[0]);
				factory=proofType.split("#")[1];
			}else{
				param.put("I_BSART",proofType);
			}
			
			SapUtil sap=new SapUtil();
			
			AjaxJson ajaxJson = new AjaxJson();
			ajaxJson.setSuccess(true);
			List<String> procurs=Arrays.asList("001,002,003,004,005,006,101,102,103,104,201,202,301,501".split(","));
			//List<Map<String,String>> list=new ArrayList<Map<String,String>>();
			//写入清单
			int count=0;
			for(Map<String,Object> asset:assets){
				count++;
				String courseAllot=(String)asset.get("courseAllot");
				if(courseAllot==null){
					courseAllot="";
				}
				String stockNumber=(String)asset.get("stockNumber");
				if(stockNumber==null||"".equals(stockNumber.trim())){
					stockNumber="";
				}else{
					stockNumber="000000000"+stockNumber;
				}
				String stockNameAndStandard=(String)asset.get("stockNameAndStandard");
				if(stockNameAndStandard==null){
					stockNameAndStandard="";
				}
				String applyNumber=(String)asset.get("applyNumber");
				String monad=(String)asset.get("monad");
				if(monad==null){
					monad="";
				}
				String companyName="";
				if("M-NonProduction-Materials".equals(proName)||"updateNonProdutionMaterialsApply".equals(proName)){
					companyName=(String)asset.get("factory");
					if(companyName.equals("东莞市金铭电子有限公司")||companyName.equals("东莞金卓通信科技有限公司")){
						if(companyName.equals("东莞市金铭电子有限公司")){
							companyName="2000";
						}else{
							companyName="3000";
						}
					}
				}
				String reckoning=(String)asset.get("reckoning");
				if(reckoning==null){
					reckoning="";
				}
				String costCenter=(String)asset.get("costCenter");
				if(costCenter!=null && !"".equals(costCenter.trim())){
					if(costCenter.indexOf("-")==-1){
						costCenter="00"+costCenter;
					}else{
						costCenter="00"+costCenter.substring(0,costCenter.indexOf("-"));
					}
				}
				String unitPrice=(String)asset.get("unitPrice");
				if(unitPrice==null||"".equals(unitPrice.trim())){
					unitPrice="";
				}else{
					unitPrice=String.valueOf(Double.parseDouble(unitPrice)*1000);
					unitPrice=unitPrice.substring(0,unitPrice.indexOf("."));
				}
				String deliveryDate=(String)asset.get("deliveryDate");
				deliveryDate=deliveryDate.replaceAll("-","");
				String procurementSection=(String)asset.get("procurementSection");
				if(procurementSection==null){
					procurementSection="";
				}
				String itemGroup=(String)asset.get("itemGroup");
				if(itemGroup==null){
					itemGroup="";
				}
				String applyName=(String)asset.get("applyName");
				
				//检查成本中心是否符合SAP的要求
				if("K".equals(courseAllot.trim())&&costCenter.indexOf("-")==-1){
					boolean flag=true;
					if("M-Equipment-Maintain".equals(proName)||"logisticProcurementAudiTask".equals(proName)||"updateEquipmentApplyTask".equals(proName)){
						List<CostCenter> costs=keepRunTaskDao.getCostCenterByFactory(factory);
						for(CostCenter center:costs){
							if(costCenter.equals("00"+center.getCode())){
								flag=false;
								break;
							}
						}
					}else{
						List<CostCenter> costs=keepRunTaskDao.getCostCenterByFactory(companyName);
						for(CostCenter center:costs){
							if(costCenter.equals("00"+center.getCode())){
								flag=false;
								break;
							}
						}
					}
					if(flag){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,成本中心"+costCenter.substring(2)+"不符合SAP的要求，请重新填写!");
						break;
					}
				}
				
				//如果有物料编号，判断工厂、物料编号是否与物料名称或规格、单位、物料组、采购组相对应
				if(!"".equals(stockNumber.trim())){
					Map<String,String> inputMap=new HashMap<String,String>();
					inputMap.put("I_MATNR",stockNumber.substring(9));
					if("M-Equipment-Maintain".equals(proName)||"logisticProcurementAudiTask".equals(proName)||"updateEquipmentApplyTask".equals(proName)){
						inputMap.put("I_WERKS",factory);
					}else{
						inputMap.put("I_WERKS",companyName);
					}
					String[] exportVars={"MAKTX","MEINS","EKGRP","MATKL"};
					String info = sap.readSapStockInfoForJson("ZHR_OA46", inputMap,"E_MATNR", exportVars);
					Map<String,Object> sapInfo=processHelpService.parseJsonToMap(info);
					if("".equals(String.valueOf(sapInfo.get("MAKTX")).trim())&&"".equals(String.valueOf(sapInfo.get("MEINS")).trim())&&"".equals(String.valueOf(sapInfo.get("EKGRP")).trim())&&"".equals(String.valueOf(sapInfo.get("MATKL")).trim())){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,编号为"+stockNumber.substring(9)+"的物资在SAP不存在!");
						break;
					}
					if(!stockNameAndStandard.equals(sapInfo.get("MAKTX"))){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,物料名称及规格"+stockNameAndStandard+"与"+stockNumber.substring(9)+"不对应,应为："+sapInfo.get("MAKTX"));
						break;
					}
					if(!monad.equals(sapInfo.get("MEINS"))){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,单位"+monad+"与"+stockNumber.substring(9)+"不对应,应为："+sapInfo.get("MEINS"));
						break;
					}
					/*if(!procurementSection.equals(sapInfo.get("EKGRP"))){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("SAP检查数据反馈信息：采购组"+procurementSection+"与"+stockNumber.substring(9)+"不对应,应为："+sapInfo.get("EKGRP"));
						break;
					}*/
					if(!itemGroup.equals(sapInfo.get("MATKL"))){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,物料组"+itemGroup+"与"+stockNumber.substring(9)+"不对应,应为："+sapInfo.get("MATKL"));
						break;
					}
					if("7001".equals(sapInfo.get("MATKL"))||"7010".equals(sapInfo.get("MATKL"))){
						if(!"6598000059".equals(reckoning.trim())){
							ajaxJson.setSuccess(false);
							ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与编号"+stockNumber.substring(9)+"不对应,应为：6598000059");
							break;
						}
					}else if("7011".equals(sapInfo.get("MATKL"))||"7014".equals(sapInfo.get("MATKL"))){
						if(!"6598000022".equals(reckoning.trim())){
							ajaxJson.setSuccess(false);
							ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与编号"+stockNumber.substring(9)+"不对应,应为：6598000022");
							break;
						}
					}else if("7509".equals(sapInfo.get("MATKL"))){
						if(!"6598000086".equals(reckoning.trim())){
							ajaxJson.setSuccess(false);
							ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与编号"+stockNumber.substring(9)+"不对应,应为：6598000086");
							break;
						}
					}else if("7510".equals(sapInfo.get("MATKL"))){
						if(!"6598000087".equals(reckoning.trim())){
							ajaxJson.setSuccess(false);
							ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与编号"+stockNumber.substring(9)+"不对应,应为：6598000087");
							break;
						}
					}else if("7511".equals(sapInfo.get("MATKL"))){
						if(!"6598000088".equals(reckoning.trim())){
							ajaxJson.setSuccess(false);
							ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与编号"+stockNumber.substring(9)+"不对应,应为：6598000088");
							break;
						}
					}else{
						if(!"6598000033".equals(reckoning.trim())){
							ajaxJson.setSuccess(false);
							ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与编号"+stockNumber.substring(9)+"不对应,应为：6598000033");
							break;
						}
					}
				}
				//检查采购组是否符合SAP的要求
				if(!procurs.contains(procurementSection)){
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,采购组"+procurementSection+"不符合SAP的要求，请重新输入!");
					break;
				}
				
				Map<String,String> param1 = new HashMap<String,String>();
				//param1.put("BSART",proofType);
				param1.put("KNTTP",courseAllot);
				param1.put("MATNR",stockNumber);
				param1.put("TXZ01",stockNameAndStandard);
				param1.put("MENGE",applyNumber);
				param1.put("MEINS",monad);
				//param1.put("MEINS","PC");
				param1.put("LFDAT",deliveryDate);
				param1.put("MATKL",itemGroup);
				if("M-Equipment-Maintain".equals(proName)||"logisticProcurementAudiTask".equals(proName)||"updateEquipmentApplyTask".equals(proName)){
					param1.put("WERKS",factory);
				}else{
					param1.put("WERKS",companyName);
				}
				param1.put("EKGRP",procurementSection);
				param1.put("SAKTO",reckoning);
				param1.put("KOSTL",costCenter);
				param1.put("PREIS",unitPrice);
				param1.put("PEINH","1000");
				param1.put("AFNAM",applyName);
				//param1.put("BEDNR",execution.getId().substring(execution.getId().length()-9,execution.getId().length()));
				
				//list.add(param1);
				String message;
				try {
					message = sap.checkSapWithInputTable("ZHR_OA45", param, "OT_EBAN", param1);//ZHR_OA45为写入非生产物资的接口名
					if(!"".equals(message.trim())){
						ajaxJson.setSuccess(false);
						ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,"+message);
						break;
					}
					if("K".equals(courseAllot.trim())&&"".equals(stockNumber.trim())){
						if("7001".equals(itemGroup.trim())||"7010".equals(itemGroup.trim())){
							if(!"6598000059".equals(reckoning.trim())){
								ajaxJson.setSuccess(false);
								ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与物料组"+itemGroup+"不对应,应为：6598000059");
								break;
							}
						}else if("7011".equals(itemGroup.trim())||"7014".equals(itemGroup.trim())){
							if(!"6598000022".equals(reckoning.trim())){
								ajaxJson.setSuccess(false);
								ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与物料组"+itemGroup+"不对应,应为：6598000022");
								break;
							}
						}else if("7509".equals(itemGroup.trim())){
							if(!"6598000086".equals(reckoning.trim())){
								ajaxJson.setSuccess(false);
								ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与物料组"+itemGroup+"不对应,应为：6598000086");
								break;
							}
						}else if("7510".equals(itemGroup.trim())){
							if(!"6598000087".equals(reckoning.trim())){
								ajaxJson.setSuccess(false);
								ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与物料组"+itemGroup+"不对应,应为：6598000087");
								break;
							}
						}else if("7511".equals(itemGroup.trim())){
							if(!"6598000088".equals(reckoning.trim())){
								ajaxJson.setSuccess(false);
								ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与物料组"+itemGroup+"不对应,应为：6598000088");
								break;
							}
						}else{
							if(!"6598000033".equals(reckoning.trim())){
								ajaxJson.setSuccess(false);
								ajaxJson.setMsg("SAP检查数据反馈信息：第"+count+"行,总账科目"+reckoning+"与物料组"+itemGroup+"不对应,应为：6598000033");
								break;
							}
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					ajaxJson.setMsg(e.getMessage());
					ajaxJson.setSuccess(false);
				}
			}
			return ajaxJson;
	  }
	  
	  //获取服务器时间
    @SuppressWarnings({ "deprecation" })
	@RequestMapping("/getServerDate.json")
	  @ResponseBody
	  public AjaxJson getServerDate(){
		  AjaxJson ajaxJson=new AjaxJson();
		  ajaxJson.setSuccess(false);
		  List<JTeamMonth> jTeamMonths =keepRunTaskDao.getAllJTeamMonth();
		  Date date=new Date();
		  Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  Integer currentMonth = calendar.get(Calendar.MONTH)+1;
		  for(JTeamMonth jTeamMonth:jTeamMonths){
			  Integer applyMonth = jTeamMonth.getApply_month();
			  if(applyMonth == currentMonth || applyMonth+1 ==currentMonth){
				  ajaxJson.setSuccess(true);
				  Map<String,Object> quarterMap = new HashMap<String,Object>();
				  quarterMap.put("quarterName",jTeamMonth.getName());
				  quarterMap.put("quarter", jTeamMonth.getId());
				  if(jTeamMonth.getId() == 1){
					  quarterMap.put("month", "03,04,05");
				  }
				  if(jTeamMonth.getId() == 2){
					  quarterMap.put("month", "06,07,08");
				  }
				  if(jTeamMonth.getId() == 3){
					  quarterMap.put("month", "09,10,11");
				  }
				  if(jTeamMonth.getId() == 4){
					  quarterMap.put("month", "12,01,02");
				  }
				  if(jTeamMonth.getId() == 4){
					  Integer year = calendar.get(Calendar.YEAR)-1;
					  quarterMap.put("year", year);
				  }else{
					  Integer year = calendar.get(Calendar.YEAR);
					  quarterMap.put("year", year);
				  }
				  ajaxJson.setAttributes(quarterMap);
			  }
		  }
		  return ajaxJson;
	  }
	  
	 //判断是否是os产品部门内的
	  @RequestMapping("/getIfOffThreeDepartById.json")
	  @ResponseBody
	  public AjaxJson getIfOffThreeDepartById(String orgId,String parentId){
		  AjaxJson ajaxJson=new AjaxJson();
		  Boolean boolean1=processHelpService.ifOffThreeDepartById(orgId, parentId);
		  if(boolean1){
			  ajaxJson.setSuccess(true);
			  ajaxJson.setMsg("00002089");
		  }else{
			  ajaxJson.setSuccess(true);
			  ajaxJson.setMsg("00002388");
		  }
		  return ajaxJson;
	  }
	  //判断是否是研发编制内还是编制外 true 编制内
	  @RequestMapping("/getIfOffThreeDepartByIds.json")
	  @ResponseBody
	  public AjaxJson getIfOffThreeDepartByIds(String orgId,String parentIds){
		  AjaxJson ajaxJson=new AjaxJson();
		  Boolean boolean1=processHelpService.ifOffThreeDepartById(orgId, parentIds);
		  if(boolean1){
			  ajaxJson.setSuccess(true);
		  }else{
			  ajaxJson.setSuccess(false);
		  }
		  return ajaxJson;
	  }
	  
	  //判断职员入职是否满3个月
	  @RequestMapping("/isOffThreeMonth.json")
	  @ResponseBody
	  public boolean isOffThreeMonth(String inDate){
		  Date date=new Date();
		  Long time1=date.getTime();
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		  try {
			Date date2=sdf.parse(inDate);
			Calendar cal=Calendar.getInstance();  
		    cal.setTime(date2);
		    cal.add(Calendar.MONTH,+3);
		    date2=cal.getTime();
			Long time2=date2.getTime();
			if(time1<time2){
				return true;
			}else{
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	  }
	  /**
	   * 获取员工的合同结束日期
	   * @param orgId
	   * @param parentId
	   * @return
	   */
	  @SuppressWarnings("finally")
	  @RequestMapping("/getEmpLaborContractExpire.json")
	  @ResponseBody
	  public AjaxJson getEmpLaborContractExpire(String empId){
		  AjaxJson ajaxJson=new AjaxJson();
		  Map<String,Object> attributes = new HashMap<String,Object>();
		  ajaxJson.setSuccess(false);
		  try {
			  String contractInfo=processHelpService.getEmpLaborContractInfo(empId);
			  if(StringUtils.isNotBlank(contractInfo)){
				  Map<String,Object> contractInfoMap = processHelpService.parseJsonToMap(contractInfo);
				  List<Map<String,Object>> contracts = (List<Map<String,Object>>)contractInfoMap.get("Rows");
				  Map<String,Object> contract = contracts.get(contracts.size()-1);
				  String expire = (String)contract.get("oveDate1");
				  String cntrType = (String)contract.get("cntrTyp");
				  if(cntrType.equals(BPMConstant.LABOR_CONTRACT_TYPE_FIXED)){
					  ajaxJson.setSuccess(true);
					  ajaxJson.setMsg("查询合同信息成功");
					  attributes.put("principalContractCount", contracts.size());
					  attributes.put("contractExpire", expire);
					  ajaxJson.setAttributes(attributes);
				  }else{
					  ajaxJson.setSuccess(false);
					  ajaxJson.setMsg("当前员工为无固定期限合同员工，不能续签");
				  }
			  }else{
				  ajaxJson.setSuccess(false);
				  ajaxJson.setMsg("查询合同信息失败");
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  ajaxJson.setMsg("查询合同信息失败");
		  }finally {
			  return ajaxJson;
		}
	  }
	  /**
	   * 获取部门领导
	   * @param orgId
	   * @param wfRule
	   * @return
	   */
	  @RequestMapping("/getOtherOrgLeader.json")
	  @ResponseBody
	  public AjaxJson getOtherOrgLeader(String orgId,String wfRule){
		  AjaxJson ajaxJson=new AjaxJson();
		  ajaxJson.setSuccess(false);
		  String leaderAccount=processHelpService.getOtherOrgLeader(orgId, wfRule);
		  if(StringUtils.isNotBlank(leaderAccount)){
			  HrUserDto user = processHelpService.getHrUser4Account(leaderAccount);
			  Map<String,Object> map = new HashMap<String,Object>();
			  String empId = "00000000"+user.getEmpId();
			  map.put("empId", empId.substring(empId.length()-8));
			  map.put("empNm", user.getName());
			  ajaxJson.setSuccess(true);
			  ajaxJson.setObj(map);
		  }else{
			  ajaxJson.setSuccess(false);
			  ajaxJson.setMsg("无部门副总裁");
		  }
		  return ajaxJson;
	  }
}
