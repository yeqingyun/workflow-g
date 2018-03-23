/**
 * @(#) ProcessHelpServiceImpl.java Created on 2014年8月13日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.NativeHistoricDetailQuery;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.auth.dao.UserDao;
import com.gionee.auth.model.Organization;
import com.gionee.auth.model.User;
import com.gionee.gnif.GnifException;
import com.gionee.gnif.biz.domain.AccountManager;
import com.gionee.gnif.biz.model.AccountDetail;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.BpmProcessConf;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.dto.UserDto;
import com.gionee.gniflow.integration.dao.ActivitiHelpDao;
import com.gionee.gniflow.integration.dao.BpmProcessConfDao;
import com.gionee.gniflow.integration.dao.BpmProcessRunDao;
import com.gionee.gniflow.integration.dao.GnifUserDao;
import com.gionee.gniflow.integration.dao.KeepRunTaskDao;
import com.gionee.gniflow.integration.dao.OrganizationHelpDao;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.bmp.BpmHisActivityEntity;
import com.gionee.gniflow.web.bmp.BpmIdentityLinkEntity;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailTool;
import com.sap.conn.jco.JCoException;

/**
 * The class <code>ProcessHelpServiceImpl</code>
 * 
 * @author lipw
 * @version 1.0
 */
@Service("processHelpService")
public class ProcessHelpServiceImpl implements ProcessHelpService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessHelpServiceImpl.class);
	
	@Autowired
	private AccountManager accountManager;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private GnifUserDao gnifUserDao;

	@Autowired
	private ActivitiHelpDao activitiHelpDao;
	
	@Autowired
	private BpmProcessRunDao bpmProcessRunDao;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private BpmProcessConfDao bpmProcessConfDao;
	
	@Autowired
	private OrganizationHelpDao organizationHelpDao;
	
	@Autowired
	private KeepRunTaskDao keepRunTaskDao;
	
	/*@Autowired
	private com.gionee.gnif.mail.biz.model.MailSender mailSender;*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gionee.gniflow.biz.service.ProcessHelpService#getUserName(java.lang
	 * .String)
	 */
	@Override
	public String getUserName(String account) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("account", account);
		List<User> users = userDao.getAll(param);
		if (!CollectionUtils.isEmpty(users)) {
			return users.get(0).getName();
		}
		return null;
	}

	@Override
	public String getUserEmailAddress(String account) {
		if(StringUtils.isNotBlank(account)){
			List<AccountDetail> accountDetails = accountManager.getDetail(Arrays
					.asList(new String[] { account }));
			if (!CollectionUtils.isEmpty(accountDetails)) {
				return accountDetails.get(0).getEmail();
			}
		}
		return null;
	}
		
	@Override
	public String getUserMobileNumber(String account) {
		if(StringUtils.isNotBlank(account)){
			List<AccountDetail> accountDetails = accountManager.getDetail(Arrays
					.asList(new String[] { account }));
			if (!CollectionUtils.isEmpty(accountDetails)) {
				return accountDetails.get(0).getMobile();
			}
		}
		return null;
	}
	
	@Override
	public String getUserMobileNumberNoLogin(String account) {
		List<HrUserDto> list = new ArrayList<HrUserDto>();
		try{
			String url = PropertyHolder.getContextProperty(WebReqConstant.HR_ALL_EMPINFONOLOGIN_KEY);
			Map<String,String> reqData = new HashMap<String,String>();
			reqData.put("fields", "EmpId,EmpNm,ComNm,DeptNm,InDate,JobNm,Email,Age,PosiNm,Major,School,Gender,SchoNm,GpNm,GpNo,tel,DeptSysNm,Birthday,GpChrNo,GpChrNm");
			reqData.put("logins", account);
			
			String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
			JSONArray jsonArray = JSONArray.parseArray(result);
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObj=jsonArray.getJSONObject(i);
				HrUserDto userDto = new HrUserDto();
				userDto.setEmpId(jsonObj.getInteger("empId"));//员工编号
				userDto.setName(jsonObj.getString("empNm"));//姓名
				String sex = jsonObj.getString("gender");//性别
				if (sex.equals("1")) {
					userDto.setSex("男");
				} else if (sex.equals("2")) {
					userDto.setSex("女");
				}
				userDto.setCompanyName(jsonObj.getString("comNm"));//公司
				userDto.setOrgName(jsonObj.getString("deptNm"));//部门
				userDto.setJob(jsonObj.getString("jobNm"));//职位
				//userDto.setBirthdayDate(DateUtil.parse(jsonObj.getString("Birthday"), "yyyy-MM-dd"));//出生日期
		        //userDto.setNativePlace(jsonObj.getString("BrithAdd"));//籍贯
				userDto.setSystemName(jsonObj.getString("deptSysNm"));//所属系统
				userDto.setMajor(jsonObj.getString("major"));//主修专业
				userDto.setPosition(jsonObj.getString("posiNm"));//职位名称
				//userDto.setOrgName(jsonObj.getString("ComId"));//公司对应组织表的ID
				//userDto.setOrgName(jsonObj.getString("ComNm"));//公司描述
				//userDto.setOrgName(jsonObj.getString("ScoChrNo"));//人事子范围编码
				userDto.setSchool(jsonObj.getString("schoNm"));//毕业学校
				userDto.setEmpType(jsonObj.getString("gpNm"));//所属员工组
				userDto.setEmpGroup(jsonObj.getString("gpNo"));//所属员工组编码
				userDto.setAge(jsonObj.getInteger("age"));//年龄
				userDto.setEducation(jsonObj.getString("school"));//学历
				userDto.setEmpChildGroup(jsonObj.getString("gpChrNo"));//所属员工子组编码
				userDto.setEmpChildGroupName(jsonObj.getString("gpChrNm"));//所属员工子组名称
				
				if(jsonObj.getString("inDate")!=null)
				userDto.setEntryDate(DateUtil.parse(jsonObj.getString("inDate"), "yyyy-MM-dd"));//入职日期
				userDto.setEmail(jsonObj.getString("email"));//邮箱
			    userDto.setTel(jsonObj.getString("tel"));//联系电话
				list.add(userDto);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		
		if(list != null && list.size() > 0) {
			return list.get(0).getTel();
		} else {
			return null;
		}
	}
	
	@Override
	public List<String> getMultiInstanceTaskUsers(String assigneeList) {
		if (StringUtils.isEmpty(assigneeList)) {
			return null;
		}
		return Arrays.asList(assigneeList.split(","));
	}

	@Override
	public boolean judgeMultiInstanceTaskUsers(String assigneeList) {
		//System.out.println("judgeMultiInstanceTaskUsers running.......");
		if (!StringUtils.isEmpty(assigneeList)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean judgeMultiInstanceTaskAudi(Execution execution) {

		//System.out.println("[judgeMultiInstanceTaskAudi begin....]");

		String proInstId = execution.getProcessInstanceId();

		List<HistoricDetail> resuktDetails = null;
		StringBuffer queryRecordSql = new StringBuffer(100);
		queryRecordSql
				.append("SELECT t.* FROM ACT_HI_DETAIL t WHERE ")
				.append("t.name_ LIKE #{name} ")
				.append("AND t.PROC_INST_ID_ = #{proInstId} ")
				.append("AND t.REV_ = ( ")
				.append("SELECT MAX (a.REV_) FROM ")
				.append("ACT_HI_DETAIL a where a.name_ LIKE #{name} and a.PROC_INST_ID_ = #{proInstId} ")
				.append(")");

		NativeHistoricDetailQuery nativeHisQuery = historyService
				.createNativeHistoricDetailQuery();
		resuktDetails = nativeHisQuery.sql(queryRecordSql.toString())
				.parameter("name", "%relevantdepartmentsSignAudi")
				.parameter("proInstId", proInstId).list();

		for (HistoricDetail detail : resuktDetails) {
			HistoricDetailVariableInstanceUpdateEntity entity = (HistoricDetailVariableInstanceUpdateEntity) detail;
			//System.out.println(entity.getTextValue()
				//	+ "recod already excute...........");
			if (Integer.parseInt(entity.getTextValue()) == 1) {
				return false;
			}

		}

		return true;
	}

	@Override
	public boolean muliInstanceIsCompleted(Execution execution,
			String assigneeList) {

			boolean temp=true;
			String[] assigneeArr = assigneeList.split(",");
			for (String assignee : assigneeArr) {
				String obj = (String) runtimeService.getVariable(
						execution.getId(), assignee
								+ "relevantdepartmentsSignAudi");
				if (obj.equals("1")) {
					temp=false; //如果有人不同意设置临时变量为false
					break;//跳出循环
				}
			}
			if(temp){
			return true;
			}
		
		return false;
	}

	/*@Override
	public boolean muliInstanceIsCompletedOnlyOne(Execution execution,
			String assigneeList) {

			boolean temp=true;
			String[] assigneeArr = assigneeList.split(",");
			for (String assignee : assigneeArr) {
				String obj = (String) runtimeService.getVariable(
						execution.getId(), assignee
								+ "relevantdepartmentsSignAudi");
				if (obj.equals("1")) {
					//temp=false; //如果有人不同意设置临时变量为false
					return false;
					//break;//跳出循环
				}
			}
			if(temp){
			return true;
			}
		
		return false;
	}*/
	@Override
	public void sendEmail4Orgs(List<String> orgIds, String subject,
			String content) {
		if (!CollectionUtils.isEmpty(orgIds)) {
			List<User> users = null;
			for (String ordId : orgIds) {
				users = userDao.getUsersByOrgId(Integer.parseInt(ordId));
				if (!CollectionUtils.isEmpty(users)) {
					for (User user : users) {
						// 正式环境在取消注释
						if (!StringUtils.isEmpty(this.getUserEmailAddress(user.getAccount()))) {
							//mailSender.sendMail(this.getUserEmailAddress(user.getAccount()), subject, content);
							SendMailTool.sendMailByAddress(subject, content, this.getUserEmailAddress(user.getAccount()));
						}
						
					}
				}
			}
		} else {
			throw new GnifException("邮件接收部门为空!");
		}
	}

	@Override
	public List<String> getSapProcessDef() {
		List<ProcessDefinition> processDefs = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionCategory(BPMConstant.SAP_PROCESS_REQ_CATAGORY)
				.latestVersion().list();
		List<String> proDefIds = new ArrayList<String>();
		for (ProcessDefinition proDef : processDefs) {
			proDefIds.add(proDef.getKey());
		}
		return proDefIds;
	}

	@Override
	public void sendEmail4UserAccount(List<String> userAccounts,
			String subject, String content) {
		if (!CollectionUtils.isEmpty(userAccounts)) {
			for (String account : userAccounts) {
				// 正式环境在取消注释
				//mailSender.sendMail(account, subject, content);
				SendMailTool.sendMailByAddress(subject, content, this.getUserEmailAddress(account));
			}
		}
	}

	@Override
	public String getOrgLeader(String userAccount, String type) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_ORGLEADER_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		reqData.put("usr.wfRule", type);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	
	@Override
	public String getOtherOrgLeader(String orgId, String type) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACCOUNT_OTHER_ORGLEADER_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("orgId", orgId);
		reqData.put("usr.wfRule", type);
		
		String result = HttpClientUtil
				.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	
	@Override
	public Boolean isRegularEmployee(String userAccount) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACOUNT_IS_REGULAREMPLOYEE);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean isExistLeader(String userAccount, String type) {
		String str=getOrgLeader(userAccount, type);
		if (StringUtils.isNotEmpty(str)) {
			return true ;
		} else {
			return false ;
		}
	}
	
	@Override
	public Boolean isExistLeaderByOrgId(String orgId, String type) {
		String str=getOtherOrgLeader(orgId, type);
		if (StringUtils.isNotEmpty(str)) {
			return true ;
		} else {
			return false ;
		}
	}

	@Override
	public Boolean isMiddleManager(String userAccount) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACOUNT_IS_MIDDLEMANAGER);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			return false;
			//throw new GnifException("Don't find organization information in SAP!");
		}
	}
	
	@Override
	public Boolean isOnWork(String userAccount) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACCOUNT_IS_ONWORK);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("empId", userAccount);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			throw new GnifException("Don't find organization information in SAP!");
		}
	}
	
	@Override
	public Boolean isOfficeWorker(String userAccount) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACCOUNT_IS_OFFICEWORKER);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			throw new GnifException("Don't find information in SAP!");
		}
	}

	@Override
	public Boolean isOfficeWorkerByEmpId(String empId) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACCOUNT_IS_OFFICEWORKERBYEMPID);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.empId", empId);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			throw new GnifException("Don't find information in SAP!");
		}
	}

	
	@Override
	public String getSigndemandTimeToIso(String signdemandTime) {
		Date date = DateUtil.parse(signdemandTime, "yyyy-MM-dd HH:mm:ss");
		return DateUtil.format(date, "yyyy-MM-dd'T'HH:mm:ss");
	}

	@Override
	public String getBeforSigndemandTime(String dateString,int day) {
		Date date = DateUtil.parse(dateString, "yyyy-MM-dd HH:mm:ss");
		return DateUtil.format(DateUtil.addDay(date, day),
				"yyyy-MM-dd'T'HH:mm:ss");
	}

	@Override
	public AjaxJson getNextTaskAssignee(String procInstId, boolean isProcess) {
		String resultPerfix = isProcess ? "流程启动成功!" : "审批任务成功!";
		AjaxJson ajaxJson = new AjaxJson(true, resultPerfix);
		
		List<Task> taskList = taskService.createTaskQuery()
				.processInstanceId(procInstId).active().list();

		if (!CollectionUtils.isEmpty(taskList)) {
			// 如果只有一个
			if (taskList.size() == 1) {
				Task nextTask = taskList.get(0);
				if (StringUtils.isEmpty(nextTask.getAssignee())) {
					// 如果是任务未分配，需要查询任务候选人
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("type", "candidate");
					params.put("procInstId", nextTask.getProcessInstanceId());

					List<BpmIdentityLinkEntity> bpmIdentityLinks = activitiHelpDao.queryTaskCandidates(params);
					if (!CollectionUtils.isEmpty(bpmIdentityLinks)) {
						StringBuffer sb = new StringBuffer();
						sb.append("{");
						for (BpmIdentityLinkEntity linkEntity : bpmIdentityLinks) {
							sb.append("[").append(linkEntity.getUserId())
									.append("-").append(linkEntity.getUserName()).append("],");
						}
						sb.deleteCharAt(sb.length() - 1);
						sb.append("}");
						ajaxJson.setMsg(resultPerfix + "下一步处理人是" + sb.toString()+ "其中一人.");
					}
				} else {
					User nextUser = userDao.getByAccount(nextTask.getAssignee());
					if (nextUser != null) {
						ajaxJson.setMsg(resultPerfix + "下一步处理人是[" + nextTask.getAssignee() + "-" + nextUser.getName() + "].");
					}
				}
			} else {
				// 启动后下一个节点是并发的任务，暂不考虑
			}
		}
		return ajaxJson;
	}

	@Override
	public boolean judgeIsPreAgreement(String userAccount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getChairmanOrCEO(String userAccount) {
		String url = PropertyHolder.getContextProperty(WebReqConstant.HR_EMP_EMPINFO_KEY);
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("emp.empId", userAccount);//必须
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		
		JSONObject jsonObj = JSONArray.parseObject(result);
		String userCompany = jsonObj.getString("scoNm");
		String userSystem = jsonObj.getString("deptSysNm");
		if (StringUtils.isNotEmpty(userCompany)) {
			if (userCompany.equals("金立") && userSystem.equals("财务信息系统")) {
				return "00000001";//刘董
			} else {
				return "02000001";//卢总
			}
		} else if (userCompany.equals("金铭")){
			return "01000004";//李总
		} else {
			throw new GnifException("Don't find user company information from HR System!");
		}
	}

	@Override
	public List<ComboBoxData> getSystemInfoFromCompany(String companyId) {
		String url = PropertyHolder.getContextProperty(WebReqConstant.HR_INFO_COMPANY_SYSTEMINFO);
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("comId", companyId);//必须
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		
		List<ComboBoxData> dataList = new ArrayList<ComboBoxData>();
		try {
			JSONArray jsonArray = JSONArray.parseArray(result);
			 for (int i = 0; i < jsonArray.size(); i++) {
		        JSONObject curJsonObj = (JSONObject) jsonArray.get(i);
		        dataList.add(new ComboBoxData(curJsonObj.getString("sysNo"), curJsonObj.getString("sysNm")));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with HR System!");
		}
		return dataList;
	}

	@Override
	public List<ComboBoxData> getDeptInfoFromSystem(String companyId, String sysNo) {
		String url = PropertyHolder.getContextProperty(WebReqConstant.HR_INFO_SYSTEM_DEPTINFO);
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("comId", companyId);
		reqData.put("sysNo", sysNo);
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		
		List<ComboBoxData> dataList = new ArrayList<ComboBoxData>();
		try {
			JSONArray jsonArray = JSONArray.parseArray(result);
			 for (int i = 0; i < jsonArray.size(); i++) {
		        JSONObject curJsonObj = (JSONObject) jsonArray.get(i);
		        dataList.add(new ComboBoxData(curJsonObj.getString("orgId"), curJsonObj.getString("orgNm")));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with HR System!");
		}
		return dataList;
	}

	@Override
	public List<ComboBoxData> getJobInfoFromDept(String deptId) {
		String url = PropertyHolder.getContextProperty(WebReqConstant.HR_INFO_DEPT_JOBINFO);
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("deptId", deptId);
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		
		List<ComboBoxData> dataList = new ArrayList<ComboBoxData>();
		try {
			JSONArray jsonArray = JSONArray.parseArray(result);
			 for (int i = 0; i < jsonArray.size(); i++) {
		        JSONObject curJsonObj = (JSONObject) jsonArray.get(i);
		        dataList.add(new ComboBoxData(curJsonObj.getString("orgId"), curJsonObj.getString("orgId")+'-'+curJsonObj.getString("orgNm")));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with HR System!");
		}
		return dataList;
	}

	@Override
	public List<HrUserDto> getAllEmpInfo(String empIds){
		List<HrUserDto> list = new ArrayList<HrUserDto>();
		try{
			String url = PropertyHolder.getContextProperty(WebReqConstant.HR_ALL_EMPINFO_KEY);
			Map<String,String> reqData = new HashMap<String,String>();
			reqData.put("fields", "EmpId,EmpNm,ComNm,DeptNm,InDate,ScoNm");
			reqData.put("empIds",empIds);

			String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
			JSONArray jsonArray = JSONArray.parseArray(result);
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObj=jsonArray.getJSONObject(i);
				HrUserDto userDto = new HrUserDto();
				userDto.setEmpId(jsonObj.getInteger("empId"));//员工编号
				userDto.setName(jsonObj.getString("empNm"));//姓名
				userDto.setCompanyName(jsonObj.getString("comNm"));//公司
				userDto.setOrgName(jsonObj.getString("deptNm"));//部门
				userDto.setCompanyDomain(jsonObj.getString("scoNm"));
				if(jsonObj.getString("inDate")!=null)
				userDto.setEntryDate(DateUtil.parse(jsonObj.getString("inDate"), "yyyy-MM-dd"));//入职日期
				list.add(userDto);
			}
			return list;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<HrUserDto> getEmpInfoById(String account){
		List<HrUserDto> list = new ArrayList<HrUserDto>();
		try{
			String url = PropertyHolder.getContextProperty(WebReqConstant.HR_ALL_EMPINFO_KEY);
			Map<String,String> reqData = new HashMap<String,String>();
			reqData.put("fields", "EmpId,EmpNm,ComNm,DeptNm,InDate,JobNm,Email,Age,PosiNm,Major,School,Gender,SchoNm,GpNm,GpNo,tel,DeptSysNm,Birthday,GpChrNo,GpChrNm");
			reqData.put("empIds", account);
			
			String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
			JSONArray jsonArray = JSONArray.parseArray(result);
			for(int i=0;i<jsonArray.size();i++){
				JSONObject jsonObj=jsonArray.getJSONObject(i);
				HrUserDto userDto = new HrUserDto();
				userDto.setEmpId(jsonObj.getInteger("empId"));//员工编号
				userDto.setName(jsonObj.getString("empNm"));//姓名
				String sex = jsonObj.getString("gender");//性别
				if (sex.equals("1")) {
					userDto.setSex("男");
				} else if (sex.equals("2")) {
					userDto.setSex("女");
				}
				userDto.setCompanyName(jsonObj.getString("comNm"));//公司
				userDto.setOrgName(jsonObj.getString("deptNm"));//部门
				userDto.setJob(jsonObj.getString("jobNm"));//职位
				//userDto.setBirthdayDate(DateUtil.parse(jsonObj.getString("Birthday"), "yyyy-MM-dd"));//出生日期
		        //userDto.setNativePlace(jsonObj.getString("BrithAdd"));//籍贯
				userDto.setSystemName(jsonObj.getString("deptSysNm"));//所属系统
				userDto.setMajor(jsonObj.getString("major"));//主修专业
				userDto.setPosition(jsonObj.getString("posiNm"));//职位名称
				//userDto.setOrgName(jsonObj.getString("ComId"));//公司对应组织表的ID
				//userDto.setOrgName(jsonObj.getString("ComNm"));//公司描述
				//userDto.setOrgName(jsonObj.getString("ScoChrNo"));//人事子范围编码
				userDto.setSchool(jsonObj.getString("schoNm"));//毕业学校
				userDto.setEmpType(jsonObj.getString("gpNm"));//所属员工组
				userDto.setEmpGroup(jsonObj.getString("gpNo"));//所属员工组编码
				userDto.setAge(jsonObj.getInteger("age"));//年龄
				userDto.setEducation(jsonObj.getString("school"));//学历
				userDto.setEmpChildGroup(jsonObj.getString("gpChrNo"));//所属员工子组编码
				userDto.setEmpChildGroupName(jsonObj.getString("gpChrNm"));//所属员工子组名称
				
				if(jsonObj.getString("inDate")!=null)
				userDto.setEntryDate(DateUtil.parse(jsonObj.getString("inDate"), "yyyy-MM-dd"));//入职日期
				userDto.setEmail(jsonObj.getString("email"));//邮箱
			    userDto.setTel(jsonObj.getString("tel"));//联系电话
				list.add(userDto);
			}
			return list;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public HrUserDto getHrUser4Account(String account) {
		HrUserDto userDto = null;
		try {
			String url = PropertyHolder.getContextProperty(WebReqConstant.HR_EMP_EMPINFO_KEY);
			Map<String,String> reqData = new HashMap<String,String>();
			reqData.put("emp.empId", account);//必须
			
			String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
			
			JSONObject jsonObj = JSONArray.parseObject(result);
			userDto = new HrUserDto();
			userDto.setEmpId(jsonObj.getInteger("empId"));
			userDto.setAge(jsonObj.getInteger("age"));//年龄
			userDto.setName(jsonObj.getString("empNm"));//姓名
			userDto.setOrgName(jsonObj.getString("deptNm"));//部门
			String sex = jsonObj.getString("gender");//性别
			if (sex.equals("1")) {
				userDto.setSex("男");
			} else if (sex.equals("2")) {
				userDto.setSex("女");
			}
			userDto.setPosition(jsonObj.getString("posiNm"));//岗位性质
	        userDto.setJob(jsonObj.getString("jobNm"));//岗位
	        userDto.setSystemName(jsonObj.getString("deptSysNm"));//系统名称
	        
	        userDto.setEntryDate(DateUtil.parse(jsonObj.getString("inDate"), "yyyy-MM-dd"));//入职日期
	        
	        userDto.setEducation(jsonObj.getString("school"));//学历
	        userDto.setSchool(jsonObj.getString("schoNm"));//毕业学校
	        userDto.setNational(jsonObj.getString("natioNm"));//民族
	        userDto.setMajor(jsonObj.getString("major"));//专业
	        userDto.setMaritalStatus(jsonObj.getString("maritalNm"));//婚姻状况
	        
	        userDto.setBirthdayDate(DateUtil.parse(jsonObj.getString("birthday"), "yyyy-MM-dd"));
	        userDto.setNativePlace(jsonObj.getString("brithAdd"));//籍贯
	        
	        Integer isAttend = jsonObj.getInteger("isAttend");//是否考勤人员
	        if (isAttend != null) {
	        	if (isAttend == 1) {
	        		userDto.setIsAttend("是");
	        	}
	        	if (isAttend == 2) {
	        		userDto.setIsAttend("否");
	        	}
	        }
	        userDto.setEmail(jsonObj.getString("email"));//邮箱
	        userDto.setCompanyId(jsonObj.getInteger("comId"));//comId
	        userDto.setCompanyName(jsonObj.getString("comNm"));//公司
	        userDto.setTel(jsonObj.getString("tel"));//联系电话
	        userDto.setEmpType(jsonObj.getString("gpNm"));//员工类型
	        
	        userDto.setEmpGroup(jsonObj.getString("gpNo"));//员工组
	        
	        Integer status = jsonObj.getInteger("status");//状态-0-离职，1-不活动，2-退休，3-在职
	        if(status != null){
	        	switch (status) {
				case 0:
					userDto.setStatus("离职");
			        userDto.setDepartureDate(DateUtil.parse(jsonObj.getString("leftDate"), "yyyy-MM-dd"));//离职日期
					break;
				case 1:
					userDto.setStatus("不活动");
					break;
				case 2:
					userDto.setStatus("退休");
					break;
				case 3:
					userDto.setStatus("在职");
					break;
				default:
					break;
				}
	        }
	        //身份证
	        JSONArray jsonArray =  jsonObj.getJSONArray("empCreds");
	        for (int i = 0; i < jsonArray.size(); i++) {
	        	JSONObject curJsonObj = (JSONObject) jsonArray.get(i);
	        	if (curJsonObj != null && curJsonObj.getString("credTypNm").equals("身份证")) {
	        		userDto.setIdCard(curJsonObj.getString("credNo"));
	        	}
			}
	        
	        //人员工作地点
	        Integer value = jsonObj.getInteger("scoChrNo");
	        if (value == 1000) {
	        	userDto.setWorkPlace("深圳");
	        } else if (value == 2000){
	        	userDto.setWorkPlace("东莞");
	        } else if (value == 3000) {
	        	userDto.setWorkPlace("北京");
	        }
	        
	        //不常用
//	        userDto.setHouseholdRegAddress(jsonObj.getString("regAddr"));//户籍地址
//	        userDto.setResidenceAddress(jsonObj.getString("regNatureNm"));//户口类型-如：外地非农户口regNatureNm
//	        userDto.setEnLevel(jsonObj.getString("englishLevel"));//英语等级
//	        userDto.setComputerLevel(jsonObj.getString("computer"));//计算机等级
//	        userDto.setCompanyDomain(jsonObj.getString("scoNm"));//所属公司域
	        
	        return userDto;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return null;
		}
		
	}

	@Override
	public PageResult<UserDto> queryPageUsers(QueryMap critera) {
		PageResult<UserDto> result = new PageResult<UserDto>();
		try {
			List<UserDto> rows = gnifUserDao.queryPageUsers(critera.getMap());
			Integer count = gnifUserDao.countUsers(critera.getMap());
			if (CollectionUtils.isEmpty(rows)) {
				result.setRows(new ArrayList<UserDto>());
				result.setTotal(0);
			} else {
				result.setRows(rows);
				result.setTotal(count);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public void changeProcessState(Integer status, String processInstId) {
		//先终止流程
		runtimeService.deleteProcessInstance(processInstId, BPMConstant.OBSOLETE_PROCESS_REASON);
		
		//操作bpm_process_run表
		BpmProcessRun bpmProcessRun = bpmProcessRunDao.get(processInstId);
		if (bpmProcessRun != null) {
			bpmProcessRun.setStatus(status);
			bpmProcessRun.setEndTime(new Date());
			bpmProcessRunDao.update(bpmProcessRun);
		} else {
			throw new GnifException("Could not find the BpmProcessRun Object, update failure!");
		}
		//将ACT_HI_ACTINST对应的endTime为空的Activity删除
		//查找废止流程的废止任务
		List<HistoricTaskInstance> hisTaskInstList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstId)
				.finished().taskDeleteReason(BPMConstant.OBSOLETE_PROCESS_REASON).list();
		if (!CollectionUtils.isEmpty(hisTaskInstList)) {
			for (HistoricTaskInstance taskInst : hisTaskInstList) {
				activitiHelpDao.deleteHisActivityByTaskId(taskInst.getId());
			}
		}
	}

	@Override
	public Boolean firstTaskIsHandledWithStartEvent(String processInstId) {
		List<HistoricTaskInstance> hisTaskIns = historyService.createHistoricTaskInstanceQuery().finished()
			.processInstanceId(processInstId).list();
		if (CollectionUtils.isEmpty(hisTaskIns)) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean validateRepeatStartProcess(String processDefKey,
			Integer processStatus, String startUser) {
		String defKey = processDefKey.split(":")[0];
		Integer count = activitiHelpDao.validateRepeatStartProcess(defKey, processStatus, startUser);
		//查询bpm_process_conf表中的can_repeat的值是不是可以重复发起
		BpmProcessConf proConf = bpmProcessConfDao.getByDefKey(defKey);

		if ((count != null && count == 0) || 
				(proConf != null && proConf.getCanRepeat() == BPMConstant.PROCESS_CAN_REPEAT_YES)) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean isThreeMonth(String userAccount) {
		Date entryDate = getHrUser4Account(userAccount).getEntryDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(entryDate);
		calendar.add(Calendar.MONTH, 3);
		Date compDate = calendar.getTime();
		Date now = new Date();
		if(!now.after(compDate)){
			return false;
		}
		return true;
	}

	@Override
	public String getProcessStartUser(String processInstId) {
		HistoricProcessInstance hisProInst = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstId).singleResult();
		if (hisProInst != null) {
			return hisProInst.getStartUserId();
		}
		return null;
	}

	@Override
	public String getRemindersTaskEmailContent(String hisActId, String userAccount) {
		BpmHisActivityEntity hisActivityEntity = activitiHelpDao.queryHisActivity(hisActId);
		//员工姓名
		String userName = this.getUserName(userAccount);
		//任务ID
		String taskId = null;
		String taskName = null;
		String proDefName = null;
		String content = "";
		TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().processInstanceId(hisActivityEntity.getProcessInstanceId())
			.taskDefinitionKey(hisActivityEntity.getActivityId()).taskAssignee(userAccount).singleResult();
		if (taskEntity != null) {
			taskId = taskEntity.getId();
			taskName = taskEntity.getName();
			
			//流程名称
			ProcessDefinition proDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(hisActivityEntity.getProcessDefinitionId()).singleResult();
			if (proDefinition != null) {
				proDefName = proDefinition.getName();
			}
		}
		if (StringUtils.isNotEmpty(userName) && taskId != null && taskName != null && proDefName != null) {
			content = PropertyHolder.getContextProperty(WebReqConstant.REMINDERS_TASK_EMAIL_CONTENT);
			content = content.replace("#assignee", userName);
			content = content.replace("#taskId", taskId);
			content = content.replace("#processDefName", proDefName);
			content = content.replace("#taskName", taskName);
			content = content.replace("#taskAddreess", PropertyHolder.getContextProperty(WebReqConstant.FLOW_TASK_HAND_URL));
			content = content.replace("#reminder", AppContext.getCurrentAppUser().getName());
		}
		return content;
	}

	@Override
	public boolean isDeptPass(Execution execution,
			String assigneeList) {
		float avrragescore=0.0f;
			String[] assigneeArr = assigneeList.split(",");
			for (String assignee : assigneeArr) {
				String obj = (String) runtimeService.getVariable(
						execution.getId(), assignee
								+ "totalscore");
				avrragescore+=Float.parseFloat(obj);
		}
			avrragescore=Math.round((avrragescore/3.0)*10)/10;
			//System.out.println("avrragescore++++++"+avrragescore);
			if(avrragescore>=80.0f){
				return true;
			}else{
				return false;
			}
	}

	@Override
	public boolean isRelateDeptPass(Execution execution,
			String assigneeList) {

		float avrragescore=0.0f;
			String[] assigneeArr = assigneeList.split(",");
			for (String assignee : assigneeArr) {
				String obj = (String) runtimeService.getVariable(
						execution.getId(), assignee
								+ "relatetotalscore");
				avrragescore+=Float.parseFloat(obj);
		}
			avrragescore=Math.round((avrragescore/3.0)*10)/10;
			if(avrragescore>=80.0f){
				return true;
			}else{
				return false;
			}
	}
	
	
	@Override
	public String findOrgLeaderForSpec(String userAccount, String type) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_ORGLEADER_SPECIAL_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		reqData.put("usr.wfRule", type);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	
	@Override
	public String findOrgLeaderByOrgNoForSpec(String orgId, String type) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_ACCOUNT_OTHER_ORGLEADER_SPECIAL_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("orgId", orgId);
		reqData.put("usr.wfRule", type);
		
		String result = HttpClientUtil
				.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	
	@Override
	public List<ComboBoxData> getSapOptions(String functionName,Map<String,String> inputMap,String exportTableName,String code,String describe){
		List<ComboBoxData> dataList = null;
		try {
			SapUtil sap=new SapUtil();
			dataList = sap.readSapOptionsForList(functionName, inputMap, exportTableName, code, describe);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with SAP!");
		}
		return dataList;
	}
	@Override
	public List<ComboBoxData> getSapOptions4Job(String functionName,Map<String,String> inputMap,String exportTableName,String code,String describe){
		List<ComboBoxData> dataList = null;
		try {
			SapUtil sap=new SapUtil();
			dataList = sap.readSapOptionsForList4Job(functionName, inputMap, exportTableName, code, describe);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with SAP!");
		}
		return dataList;
	}
	
	
	@Override
	public List<ComboBoxData> getSapInfoArray(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars){
		List<ComboBoxData> dataList = null;
		try {
			SapUtil sap=new SapUtil();
			dataList = sap.readSapInfoArray(functionName, inputMap, exportTableName, exportVars);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with SAP!");
		}
		return dataList;
	}
	
	//从SAP读取单行数据
	@Override
		public String getSapPersonInfo(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
			String info="";
			try {
				SapUtil sap=new SapUtil();
				info = sap.readSapPersonInfo(functionName, inputMap, exportTableName, exportVars);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new GnifException("Failure of communication with SAP!");
			}
			return info;
		}
	
	//从SAP读取单行数据-JSON格式
	@Override
		public String getSapPersonInfoForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
			String info="";
			try {
				SapUtil sap=new SapUtil();
				info = sap.readSapEmpInfoForJson(functionName, inputMap, exportTableName, exportVars);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new GnifException("Failure of communication with SAP!");
			}
			return info;
		}
	
	
	@Override
	public boolean isHrPass(Execution execution) {

		float composite=0.0f;
		float relatecomposite=0.0f;
		float wrritenScore=0.0f;
		float finalScore=0.0f;
		
		String compositeobj = (String) runtimeService.getVariable(execution.getId(), "composite");
		composite+=Float.parseFloat(compositeobj);
		
		String relatecompositeobj = (String) runtimeService.getVariable(execution.getId(), "relatecomposite");
		relatecomposite+=Float.parseFloat(relatecompositeobj);
		
		String wrritenScoreobj = (String) runtimeService.getVariable(execution.getId(), "wrritenScore");
		wrritenScore+=Float.parseFloat(wrritenScoreobj);
		
		String finalScoreobj = (String) runtimeService.getVariable(execution.getId(), "finalScore");
		finalScore+=Float.parseFloat(finalScoreobj);
		
			if(composite>=80.0f&&(relatecomposite>=80.0f||relatecomposite==0.0f)&&wrritenScore>=80.0f&&finalScore>=80.0f){
				return true;
			}else{
				return false;
			}
	}
	
	//从SAP读取多行数据-JSON格式
	public    List<ComboBoxData>    getSapOptionsForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception{
		List<ComboBoxData> dataList = null;
		try {
			SapUtil sap=new SapUtil();
			dataList = sap.readSapOptionsForJson(functionName, inputMap, exportTableName, exportVars);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with SAP!");
		}
		return dataList;
	}

	@Override
	public String getSapOrgCode4DeptId(String deptId) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_INFO_DEPT_SAPCODEINFO_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("deptId", deptId);
		reqData.put("Otype", "O");

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			JSONObject jsonObj = JSONObject.parseObject(result);
			return (String) jsonObj.get("orgNo");
		}
		return null;
	}
	
	@Override
	//工作流平台取得员工编号对应的用户账号
	public String getEmpAccount(String empNo)  throws Exception{
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_Emp_UsrACCOUNT);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.empId", empNo);
		//防止接口调用超时返回通信失败的值
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result) && !"通信失败".equals(result)) {
			return result;
		}
		return null;
	}
	
	@Override
	public String getOrgMaster(String userAccount) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_ORGMASTER_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	
	@Override
	//流程文档会签流程多并发多可选多人审核判断是否同意(兼容以前的流程图)
		public String getProjHumanAccountIsAgree(Execution execution,String TestPlatformAudit,String BigDataAudit,String TestProjectAudit,String UserInteractAudit,String SoftArchDeptAudit,String BaseRomProductAudit,
				String TechnicalResearchAudit,String SoftPlatToolAudit,String FirstProductAmigoAudit,String SystemFirstAudit,String FirstProductSubjectAudit) {
			
			try {
				String SystemPlatformResult = "";
				if(("1".equals(TestPlatformAudit) || "1".equals(BigDataAudit) || "1".equals(TestProjectAudit) 
						|| "1".equals(UserInteractAudit) || "1".equals(SoftArchDeptAudit) || "1".equals(BaseRomProductAudit) || "1".equals(TechnicalResearchAudit)) 
						&& runtimeService.getVariable(execution.getId(),"SystemPlatformIsAgree") != null) {
					String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemPlatformIsAgree");
					if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
						if(ThreeCheckIsAgree.equals("0")){
							SystemPlatformResult="no";
						}
					}
				}
				
				String ImageTeResult = "";
				if(("1".equals(SoftPlatToolAudit) || "1".equals(FirstProductAmigoAudit)) && runtimeService.getVariable(execution.getId(),"ImageTeIsAgree") != null) {
					String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageTeIsAgree");
					if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
						if(ThreeCheckIsAgree.equals("0")){
							ImageTeResult="no";
						}
					}
				}
				
				String ProjManageResult = "";
				if(("1".equals(SystemFirstAudit) || "1".equals(FirstProductSubjectAudit)) && runtimeService.getVariable(execution.getId(),"ProjManageIsAgree") != null) {
					String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjManageIsAgree");
					if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
						if(ThreeCheckIsAgree.equals("0")){
							ProjManageResult="no";
						}
					}
				}
				
				if(SystemPlatformResult.equals("no") || ImageTeResult.equals("no") || ProjManageResult.equals("no")){
					return "no";
				} else {
					return "yes";
				}
			} catch(Exception e) {
				e.printStackTrace();
				return "yes";
			}
		}
	public String getProjHumanAccountIsAgree(Execution execution,String level,String SecondProductServerAuditor,String BaseRomSoftSecondAuditor,String OtherDeptAuditor,String TestPlatformAudit,String BigDataAudit,String TestProjectAudit,String UserInteractAudit,String SoftArchDeptAudit,
			String BaseRomProductAudit,String TechnicalResearchAudit,String SoftPlatToolAudit,String FirstProductAmigoAudit,String SystemFirstAudit,String FirstProductSubjectAudit,
			String BaseRomSoftFirstAudit,String SoftPlatDriveAudit,String SoftPlatModemAudit,String FirstProductLifeAudit,String SystemSecondAudit,String SystemSoftAudit,String SecondProductLockAudit,
			String ProjMageSoftProjAudit,String ProjMageSoftQualityAudit,String VadeoSoftwareAudit,String ProductPlanBraAudit,
			String ThirdProductAdAudit,String VadeoEffectAudit,String ExpertTechRfAudit,String ThirdProductPayAudit,String ExpertTechPropAudit,
			String ProductPlanUserAudit,String ProductPlanOperAudit){
		try {
			String FirstPlatformResult = "";
			if("1".equals(TestPlatformAudit) && runtimeService.getVariable(execution.getId(),"FirstPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"FirstPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						FirstPlatformResult="no";
					}
				}
			}
			
			String SecondPlatformResult = "";
			if("1".equals(BigDataAudit) && runtimeService.getVariable(execution.getId(),"SecondPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SecondPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SecondPlatformResult="no";
					}
				}
			}
			
			String ThreePlatformResult = "";
			if("1".equals(TestProjectAudit) && runtimeService.getVariable(execution.getId(),"ThreePlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ThreePlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ThreePlatformResult="no";
					}
				}
			}
			
			String DrivePlatformResult = "";
			if("1".equals(UserInteractAudit) && runtimeService.getVariable(execution.getId(),"DrivePlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"DrivePlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						DrivePlatformResult="no";
					}
				}
			}
			
			String ModemPlatformResult = "";
			if("1".equals(SoftArchDeptAudit) && runtimeService.getVariable(execution.getId(),"ModemPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ModemPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ModemPlatformResult="no";
					}
				}
			}
			
			String ToolPlatformResult = "";
			if("1".equals(BaseRomProductAudit) && runtimeService.getVariable(execution.getId(),"ToolPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ToolPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ToolPlatformResult="no";
					}
				}
			}
			
			String SystemSoftResult = "";
			if("1".equals(TechnicalResearchAudit) && runtimeService.getVariable(execution.getId(),"SystemSoftIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemSoftIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SystemSoftResult="no";
					}
				}
			}
			
			String SystemStewardResult = "";
			if("1".equals(BaseRomSoftFirstAudit) && runtimeService.getVariable(execution.getId(),"SystemStewardIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemStewardIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SystemStewardResult="no";
					}
				}
			}
			
			String ScienceFirstResult = "";
			if("1".equals(SoftPlatDriveAudit) && runtimeService.getVariable(execution.getId(),"ScienceFirstIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ScienceFirstIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ScienceFirstResult="no";
					}
				}
			}
			
			String ImageEffectResult = "";
			if("1".equals(SoftPlatToolAudit) && runtimeService.getVariable(execution.getId(),"ImageEffectIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageEffectIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ImageEffectResult="no";
					}
				}
			}
			
			String ImageSoftResult = "";
			if("1".equals(FirstProductAmigoAudit) && runtimeService.getVariable(execution.getId(),"ImageSoftIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageSoftIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ImageSoftResult="no";
					}
				}
			}
			
			String TestProjectResult = "";
			if("1".equals(SoftPlatModemAudit) && runtimeService.getVariable(execution.getId(),"TestProjectIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"TestProjectIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						TestProjectResult="no";
					}
				}
			}
			
			String TestPlatResult = "";
			if("1".equals(FirstProductLifeAudit) && runtimeService.getVariable(execution.getId(),"TestPlatIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"TestPlatIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						TestPlatResult="no";
					}
				}
			}
			
			String ProjMageSoftProjResult = "";
			if("1".equals(SystemFirstAudit) && runtimeService.getVariable(execution.getId(),"ProjMageSoftProjIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjMageSoftProjIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProjMageSoftProjResult="no";
					}
				}
			}
			
			String ProjMageSoftQualityResult = "";
			if("1".equals(FirstProductSubjectAudit) && runtimeService.getVariable(execution.getId(),"ProjMageSoftQualityIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjMageSoftQualityIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProjMageSoftQualityResult="no";
					}
				}
			}
			
			String ClientDeveResult = "";
			if("1".equals(SystemSecondAudit) && runtimeService.getVariable(execution.getId(),"ClientDeveIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ClientDeveIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ClientDeveResult="no";
					}
				}
			}
			
			String SecondProductServerResult = "";
			if(SecondProductServerAuditor != null && !SecondProductServerAuditor.equals("")) {
				String[] SecondProductServerArr = SecondProductServerAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ProductDeveThreeIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ProductDeveThreeIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							SecondProductServerResult = "no";
							break;
						}
					}
				}
			}
			
			String ScienceSecondResult = "";
			if(BaseRomSoftSecondAuditor != null && !BaseRomSoftSecondAuditor.equals("")) {
				String[] SecondProductServerArr = BaseRomSoftSecondAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ScienceSecondIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ScienceSecondIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							ScienceSecondResult = "no";
							break;
						}
					}
				}
			}
			
			String OtherDeptResult = "";
			if(OtherDeptAuditor != null && !OtherDeptAuditor.equals("")) {
				String[] SecondProductServerArr = OtherDeptAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "OtherDeptIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "OtherDeptIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							OtherDeptResult = "no";
							break;
						}
					}
				}
			}
			
			String ProductDeveTwoResult = "";
			if("1".equals(SystemSoftAudit) && runtimeService.getVariable(execution.getId(),"ProductDeveTwoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductDeveTwoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductDeveTwoResult="no";
					}
				}
			}
			
			String ProductDeveOneResult = "";
			if("1".equals(SecondProductLockAudit) && runtimeService.getVariable(execution.getId(),"ProductDeveOneIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductDeveOneIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductDeveOneResult="no";
					}
				}
			}
			
			String BaseRomSoftFirstResult = "";
			if("1".equals(ProjMageSoftProjAudit) && runtimeService.getVariable(execution.getId(),"BaseRomSoftFirstIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomSoftFirstIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomSoftFirstResult="no";
					}
				}
			}
			
			String BaseRomProductResult = "";
			if("1".equals(ProjMageSoftQualityAudit) && runtimeService.getVariable(execution.getId(),"BaseRomProductIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomProductIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomProductResult="no";
					}
				}
			}
			
			
			String ProductAmigoResult = "";
			if("1".equals(VadeoSoftwareAudit) && runtimeService.getVariable(execution.getId(),"ProductAmigoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductAmigoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductAmigoResult="no";
					}
				}
			}
			
			String VisualProductResult = "";
			if("1".equals(ProductPlanBraAudit) && runtimeService.getVariable(execution.getId(),"VisualProductIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"VisualProductIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						VisualProductResult="no";
					}
				}
			}
			
			String ProductGameResult = "";
			if("1".equals(ThirdProductAdAudit) && runtimeService.getVariable(execution.getId(),"ProductGameIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductGameIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductGameResult="no";
					}
				}
			}
			
			String ProductPlanLockResult = "";
			if("1".equals(VadeoEffectAudit) && runtimeService.getVariable(execution.getId(),"ProductPlanLockIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductPlanLockIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductPlanLockResult="no";
					}
				}
			}
			
			String UserInteractResult = "";
			if("1".equals(ExpertTechRfAudit) && runtimeService.getVariable(execution.getId(),"UserInteractIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"UserInteractIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						UserInteractResult="no";
					}
				}
			}
			
			String UserExperienceResult = "";
			if("1".equals(ThirdProductPayAudit) && runtimeService.getVariable(execution.getId(),"UserExperienceIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"UserExperienceIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						UserExperienceResult="no";
					}
				}
			}
			
			String ComCenterResult = "";
			if("1".equals(ExpertTechPropAudit) && runtimeService.getVariable(execution.getId(),"ComCenterIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ComCenterIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ComCenterResult="no";
					}
				}
			}
			
			
			String DataPayAndSetResult = "";
			if("1".equals(ProductPlanUserAudit) && runtimeService.getVariable(execution.getId(),"DataPayAndSetIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"DataPayAndSetIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						DataPayAndSetResult="no";
					}
				}
			}
			
			String IPRTResult = "";
			if("1".equals(ProductPlanOperAudit) && runtimeService.getVariable(execution.getId(),"IPRTIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"IPRTIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						IPRTResult="no";
					}
				}
			}
					
			if(FirstPlatformResult.equals("no") || SecondPlatformResult.equals("no") || ThreePlatformResult.equals("no")
					|| DrivePlatformResult.equals("no") || ModemPlatformResult.equals("no") || ToolPlatformResult.equals("no") || SystemSoftResult.equals("no") 
					 || SystemStewardResult.equals("no")  || ScienceFirstResult.equals("no") || ScienceSecondResult.equals("no") || ImageEffectResult.equals("no")
					|| ImageSoftResult.equals("no") || TestProjectResult.equals("no") || TestPlatResult.equals("no")
					|| ProjMageSoftProjResult.equals("no") || ProjMageSoftQualityResult.equals("no") || ClientDeveResult.equals("no")
					|| SecondProductServerResult.equals("no") || ProductDeveTwoResult.equals("no") || ProductDeveOneResult.equals("no") || BaseRomSoftFirstResult.equals("no")
					|| BaseRomProductResult.equals("no") 
					|| ProductAmigoResult.equals("no") || VisualProductResult.equals("no") || ProductGameResult.equals("no") || ProductPlanLockResult.equals("no") 
					|| UserInteractResult.equals("no") || UserExperienceResult.equals("no") || ComCenterResult.equals("no") 
					|| DataPayAndSetResult.equals("no") || IPRTResult.equals("no") || OtherDeptResult.equals("no")) {
				return "no";
			} else {
				String result="";
				if("dept".equals(level)){
					if("1".equals(TestPlatformAudit)||"1".equals(BigDataAudit)||"1".equals(TestProjectAudit)||"1".equals(UserInteractAudit)||
							"1".equals(SoftArchDeptAudit)||"1".equals(BaseRomProductAudit)||"1".equals(TechnicalResearchAudit)||
							"1".equals(SoftPlatToolAudit)||"1".equals(FirstProductAmigoAudit)){
						result="1";
					}else{
						result="0";
					}
				}else{
					if("1".equals(TestPlatformAudit)||"1".equals(BigDataAudit)||"1".equals(TestProjectAudit)||"1".equals(UserInteractAudit)||
							"1".equals(SoftArchDeptAudit)||"1".equals(BaseRomProductAudit)||"1".equals(TechnicalResearchAudit)||
							"1".equals(SoftPlatToolAudit)||"1".equals(FirstProductAmigoAudit)||"1".equals(SystemFirstAudit)||"1".equals(FirstProductSubjectAudit)){
						result="1";
					}else{
						result="0";
					}
				}
				return result;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "yes";
		}
	}
	//方法重载兼容以前的流程
	public String getProjHumanAccountIsAgree(Execution execution,String level,String SecondProductServerAuditor,String BaseRomSoftSecondAuditor,String OtherDeptAuditor,String TestPlatformAudit,String BigDataAudit,String TestProjectAudit,String UserInteractAudit,String SoftArchDeptAudit,
			String BaseRomProductAudit,String TechnicalResearchAudit,String SoftPlatToolAudit,String FirstProductAmigoAudit,String SystemFirstAudit,String FirstProductSubjectAudit,
			String BaseRomSoftFirstAudit,String SoftPlatDriveAudit,String SoftPlatModemAudit,String FirstProductLifeAudit,String SystemSecondAudit,String SystemSoftAudit,String SecondProductLockAudit,
			String ProjMageSoftProjAudit,String SecondProductVadeoAudit,String ProjMageSoftQualityAudit,String ThirdProductBuyAudit,String VadeoSoftwareAudit,String ProductPlanBraAudit,
			String ThirdProductAdAudit,String VadeoEffectAudit,String ThirdProductGameAudit,String ExpertTechRfAudit,String ThirdProductPayAudit,String ExpertTechPropAudit,
			String ProductPlanLockAudit,String ProductPlanUserAudit,String ProductPlanOperAudit){
		try {
			String FirstPlatformResult = "";
			if("1".equals(TestPlatformAudit) && runtimeService.getVariable(execution.getId(),"FirstPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"FirstPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						FirstPlatformResult="no";
					}
				}
			}
			
			String SecondPlatformResult = "";
			if("1".equals(BigDataAudit) && runtimeService.getVariable(execution.getId(),"SecondPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SecondPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SecondPlatformResult="no";
					}
				}
			}
			
			String ThreePlatformResult = "";
			if("1".equals(TestProjectAudit) && runtimeService.getVariable(execution.getId(),"ThreePlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ThreePlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ThreePlatformResult="no";
					}
				}
			}
			
			String DrivePlatformResult = "";
			if("1".equals(UserInteractAudit) && runtimeService.getVariable(execution.getId(),"DrivePlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"DrivePlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						DrivePlatformResult="no";
					}
				}
			}
			
			String ModemPlatformResult = "";
			if("1".equals(SoftArchDeptAudit) && runtimeService.getVariable(execution.getId(),"ModemPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ModemPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ModemPlatformResult="no";
					}
				}
			}
			
			String ToolPlatformResult = "";
			if("1".equals(BaseRomProductAudit) && runtimeService.getVariable(execution.getId(),"ToolPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ToolPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ToolPlatformResult="no";
					}
				}
			}
			
			String SystemSoftResult = "";
			if("1".equals(TechnicalResearchAudit) && runtimeService.getVariable(execution.getId(),"SystemSoftIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemSoftIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SystemSoftResult="no";
					}
				}
			}
			
			String SystemStewardResult = "";
			if("1".equals(BaseRomSoftFirstAudit) && runtimeService.getVariable(execution.getId(),"SystemStewardIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemStewardIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SystemStewardResult="no";
					}
				}
			}
			
			String ScienceFirstResult = "";
			if("1".equals(SoftPlatDriveAudit) && runtimeService.getVariable(execution.getId(),"ScienceFirstIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ScienceFirstIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ScienceFirstResult="no";
					}
				}
			}
			
			String ImageEffectResult = "";
			if("1".equals(SoftPlatToolAudit) && runtimeService.getVariable(execution.getId(),"ImageEffectIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageEffectIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ImageEffectResult="no";
					}
				}
			}
			
			String ImageSoftResult = "";
			if("1".equals(FirstProductAmigoAudit) && runtimeService.getVariable(execution.getId(),"ImageSoftIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageSoftIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ImageSoftResult="no";
					}
				}
			}
			
			String TestProjectResult = "";
			if("1".equals(SoftPlatModemAudit) && runtimeService.getVariable(execution.getId(),"TestProjectIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"TestProjectIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						TestProjectResult="no";
					}
				}
			}
			
			String TestPlatResult = "";
			if("1".equals(FirstProductLifeAudit) && runtimeService.getVariable(execution.getId(),"TestPlatIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"TestPlatIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						TestPlatResult="no";
					}
				}
			}
			
			String ProjMageSoftProjResult = "";
			if("1".equals(SystemFirstAudit) && runtimeService.getVariable(execution.getId(),"ProjMageSoftProjIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjMageSoftProjIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProjMageSoftProjResult="no";
					}
				}
			}
			
			String ProjMageSoftQualityResult = "";
			if("1".equals(FirstProductSubjectAudit) && runtimeService.getVariable(execution.getId(),"ProjMageSoftQualityIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjMageSoftQualityIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProjMageSoftQualityResult="no";
					}
				}
			}
			
			String ClientDeveResult = "";
			if("1".equals(SystemSecondAudit) && runtimeService.getVariable(execution.getId(),"ClientDeveIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ClientDeveIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ClientDeveResult="no";
					}
				}
			}
			
			String SecondProductServerResult = "";
			if(SecondProductServerAuditor != null && !SecondProductServerAuditor.equals("")) {
				String[] SecondProductServerArr = SecondProductServerAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ProductDeveThreeIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ProductDeveThreeIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							SecondProductServerResult = "no";
							break;
						}
					}
				}
			}
			
			String ScienceSecondResult = "";
			if(BaseRomSoftSecondAuditor != null && !BaseRomSoftSecondAuditor.equals("")) {
				String[] SecondProductServerArr = BaseRomSoftSecondAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ScienceSecondIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ScienceSecondIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							ScienceSecondResult = "no";
							break;
						}
					}
				}
			}
			
			String OtherDeptResult = "";
			if(OtherDeptAuditor != null && !OtherDeptAuditor.equals("")) {
				String[] SecondProductServerArr = OtherDeptAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "OtherDeptIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "OtherDeptIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							OtherDeptResult = "no";
							break;
						}
					}
				}
			}
			
			String ProductDeveTwoResult = "";
			if("1".equals(SystemSoftAudit) && runtimeService.getVariable(execution.getId(),"ProductDeveTwoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductDeveTwoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductDeveTwoResult="no";
					}
				}
			}
			
			String ProductDeveOneResult = "";
			if("1".equals(SecondProductLockAudit) && runtimeService.getVariable(execution.getId(),"ProductDeveOneIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductDeveOneIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductDeveOneResult="no";
					}
				}
			}
			
			String BaseRomSoftFirstResult = "";
			if("1".equals(ProjMageSoftProjAudit) && runtimeService.getVariable(execution.getId(),"BaseRomSoftFirstIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomSoftFirstIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomSoftFirstResult="no";
					}
				}
			}
			
			String BaseRomSoftSecondResult = "";
			if("1".equals(SecondProductVadeoAudit) && runtimeService.getVariable(execution.getId(),"BaseRomSoftSecondIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomSoftSecondIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomSoftSecondResult="no";
					}
				}
			}
			
			String BaseRomProductResult = "";
			if("1".equals(ProjMageSoftQualityAudit) && runtimeService.getVariable(execution.getId(),"BaseRomProductIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomProductIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomProductResult="no";
					}
				}
			}
			
			String ProductLifeResult = "";
			if("1".equals(ThirdProductBuyAudit) && runtimeService.getVariable(execution.getId(),"ProductLifeIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductLifeIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductLifeResult="no";
					}
				}
			}
			
			String ProductAmigoResult = "";
			if("1".equals(VadeoSoftwareAudit) && runtimeService.getVariable(execution.getId(),"ProductAmigoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductAmigoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductAmigoResult="no";
					}
				}
			}
			
			String VisualProductResult = "";
			if("1".equals(ProductPlanBraAudit) && runtimeService.getVariable(execution.getId(),"VisualProductIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"VisualProductIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						VisualProductResult="no";
					}
				}
			}
			
			String ProductGameResult = "";
			if("1".equals(ThirdProductAdAudit) && runtimeService.getVariable(execution.getId(),"ProductGameIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductGameIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductGameResult="no";
					}
				}
			}
			
			String ProductPlanLockResult = "";
			if("1".equals(VadeoEffectAudit) && runtimeService.getVariable(execution.getId(),"ProductPlanLockIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductPlanLockIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductPlanLockResult="no";
					}
				}
			}
			
			String ProductVadeoResult = "";
			if("1".equals(ThirdProductGameAudit) && runtimeService.getVariable(execution.getId(),"ProductVadeoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductVadeoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductVadeoResult="no";
					}
				}
			}
			
			String UserInteractResult = "";
			if("1".equals(ExpertTechRfAudit) && runtimeService.getVariable(execution.getId(),"UserInteractIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"UserInteractIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						UserInteractResult="no";
					}
				}
			}
			
			String UserExperienceResult = "";
			if("1".equals(ThirdProductPayAudit) && runtimeService.getVariable(execution.getId(),"UserExperienceIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"UserExperienceIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						UserExperienceResult="no";
					}
				}
			}
			
			String ComCenterResult = "";
			if("1".equals(ExpertTechPropAudit) && runtimeService.getVariable(execution.getId(),"ComCenterIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ComCenterIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ComCenterResult="no";
					}
				}
			}
			
			String BigDataStateResult = "";
			if("1".equals(ProductPlanLockAudit) && runtimeService.getVariable(execution.getId(),"BigDataStateIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BigDataStateIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BigDataStateResult="no";
					}
				}
			}
			
			String DataPayAndSetResult = "";
			if("1".equals(ProductPlanUserAudit) && runtimeService.getVariable(execution.getId(),"DataPayAndSetIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"DataPayAndSetIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						DataPayAndSetResult="no";
					}
				}
			}
			
			String IPRTResult = "";
			if("1".equals(ProductPlanOperAudit) && runtimeService.getVariable(execution.getId(),"IPRTIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"IPRTIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						IPRTResult="no";
					}
				}
			}
					
			if(FirstPlatformResult.equals("no") || SecondPlatformResult.equals("no") || ThreePlatformResult.equals("no")
					|| DrivePlatformResult.equals("no") || ModemPlatformResult.equals("no") || ToolPlatformResult.equals("no") || SystemSoftResult.equals("no") 
					 || SystemStewardResult.equals("no")  || ScienceFirstResult.equals("no") || ScienceSecondResult.equals("no") || ImageEffectResult.equals("no")
					|| ImageSoftResult.equals("no") || TestProjectResult.equals("no") || TestPlatResult.equals("no")
					|| ProjMageSoftProjResult.equals("no") || ProjMageSoftQualityResult.equals("no") || ClientDeveResult.equals("no")
					|| SecondProductServerResult.equals("no") || ProductDeveTwoResult.equals("no") || ProductDeveOneResult.equals("no") || BaseRomSoftFirstResult.equals("no")
					|| BaseRomSoftSecondResult.equals("no") || BaseRomProductResult.equals("no") || ProductLifeResult.equals("no")
					|| ProductAmigoResult.equals("no") || VisualProductResult.equals("no") || ProductGameResult.equals("no") || ProductPlanLockResult.equals("no") || ProductVadeoResult.equals("no")
					|| UserInteractResult.equals("no") || UserExperienceResult.equals("no") || ComCenterResult.equals("no") || BigDataStateResult.equals("no") 
					|| DataPayAndSetResult.equals("no") || IPRTResult.equals("no") || OtherDeptResult.equals("no")) {
				return "no";
			} else {
				String result="";
				if("dept".equals(level)){
					if("1".equals(TestPlatformAudit)||"1".equals(BigDataAudit)||"1".equals(TestProjectAudit)||"1".equals(UserInteractAudit)||
							"1".equals(SoftArchDeptAudit)||"1".equals(BaseRomProductAudit)||"1".equals(TechnicalResearchAudit)||
							"1".equals(SoftPlatToolAudit)||"1".equals(FirstProductAmigoAudit)){
						result="1";
					}else{
						result="0";
					}
				}else{
					if("1".equals(TestPlatformAudit)||"1".equals(BigDataAudit)||"1".equals(TestProjectAudit)||"1".equals(UserInteractAudit)||
							"1".equals(SoftArchDeptAudit)||"1".equals(BaseRomProductAudit)||"1".equals(TechnicalResearchAudit)||
							"1".equals(SoftPlatToolAudit)||"1".equals(FirstProductAmigoAudit)||"1".equals(SystemFirstAudit)||"1".equals(FirstProductSubjectAudit)){
						result="1";
					}else{
						result="0";
					}
				}
				return result;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "yes";
		}
	}
	@Override
	//流程文档会签流程多并发多可选多人审核判断是否同意(兼容以前的流程图)
	public String getProjHumanAccountIsAgree(Execution execution) {
		
		try {
			String SystemPlatformResult = "";
			if(runtimeService.getVariable(execution.getId(),"SystemPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SystemPlatformResult="no";
					}
				}
			}
			
			String ImageTeResult = "";
			if(runtimeService.getVariable(execution.getId(),"ImageTeIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageTeIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ImageTeResult="no";
					}
				}
			}
			
			String ProjManageResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProjManageIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjManageIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProjManageResult="no";
					}
				}
			}
			
			if(SystemPlatformResult.equals("no") || ImageTeResult.equals("no") || ProjManageResult.equals("no")){
				return "no";
			} else {
				return "yes";
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "yes";
		}
	}
	@Override
	public String getProjHumanAccountIsAgree(Execution execution,String level,String SecondProductServerAuditor,String BaseRomSoftSecondAuditor,String OtherDeptAuditor,String TestPlatformAudit,String BigDataAudit,String TestProjectAudit,String UserInteractAudit,String SoftArchDeptAudit,
			String BaseRomProductAudit,String TechnicalResearchAudit,String SoftPlatToolAudit,String FirstProductAmigoAudit,String SystemFirstAudit,String FirstProductSubjectAudit){
		try {
			String FirstPlatformResult = "";
			if(runtimeService.getVariable(execution.getId(),"FirstPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"FirstPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						FirstPlatformResult="no";
					}
				}
			}
			
			String SecondPlatformResult = "";
			if(runtimeService.getVariable(execution.getId(),"SecondPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SecondPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SecondPlatformResult="no";
					}
				}
			}
			
			String ThreePlatformResult = "";
			if(runtimeService.getVariable(execution.getId(),"ThreePlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ThreePlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ThreePlatformResult="no";
					}
				}
			}
			
			String DrivePlatformResult = "";
			if(runtimeService.getVariable(execution.getId(),"DrivePlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"DrivePlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						DrivePlatformResult="no";
					}
				}
			}
			
			String ModemPlatformResult = "";
			if(runtimeService.getVariable(execution.getId(),"ModemPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ModemPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ModemPlatformResult="no";
					}
				}
			}
			
			String ToolPlatformResult = "";
			if(runtimeService.getVariable(execution.getId(),"ToolPlatformIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ToolPlatformIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ToolPlatformResult="no";
					}
				}
			}
			
			String SystemSoftResult = "";
			if(runtimeService.getVariable(execution.getId(),"SystemSoftIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemSoftIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SystemSoftResult="no";
					}
				}
			}
			
			String SystemStewardResult = "";
			if(runtimeService.getVariable(execution.getId(),"SystemStewardIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"SystemStewardIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						SystemStewardResult="no";
					}
				}
			}
			
			String ScienceFirstResult = "";
			if(runtimeService.getVariable(execution.getId(),"ScienceFirstIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ScienceFirstIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ScienceFirstResult="no";
					}
				}
			}
			
			String ImageEffectResult = "";
			if(runtimeService.getVariable(execution.getId(),"ImageEffectIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageEffectIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ImageEffectResult="no";
					}
				}
			}
			
			String ImageSoftResult = "";
			if(runtimeService.getVariable(execution.getId(),"ImageSoftIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ImageSoftIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ImageSoftResult="no";
					}
				}
			}
			
			String TestProjectResult = "";
			if(runtimeService.getVariable(execution.getId(),"TestProjectIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"TestProjectIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						TestProjectResult="no";
					}
				}
			}
			
			String TestPlatResult = "";
			if(runtimeService.getVariable(execution.getId(),"TestPlatIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"TestPlatIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						TestPlatResult="no";
					}
				}
			}
			
			String ProjMageSoftProjResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProjMageSoftProjIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjMageSoftProjIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProjMageSoftProjResult="no";
					}
				}
			}
			
			String ProjMageSoftQualityResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProjMageSoftQualityIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProjMageSoftQualityIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProjMageSoftQualityResult="no";
					}
				}
			}
			
			String ClientDeveResult = "";
			if(runtimeService.getVariable(execution.getId(),"ClientDeveIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ClientDeveIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ClientDeveResult="no";
					}
				}
			}
			
			String SecondProductServerResult = "";
			if(SecondProductServerAuditor != null && !SecondProductServerAuditor.equals("")) {
				String[] SecondProductServerArr = SecondProductServerAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ProductDeveThreeIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ProductDeveThreeIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							SecondProductServerResult = "no";
							break;
						}
					}
				}
			}
			
			String ScienceSecondResult = "";
			if(BaseRomSoftSecondAuditor != null && !BaseRomSoftSecondAuditor.equals("")) {
				String[] SecondProductServerArr = BaseRomSoftSecondAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ScienceSecondIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "ScienceSecondIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							ScienceSecondResult = "no";
							break;
						}
					}
				}
			}
			
			String OtherDeptResult = "";
			if(OtherDeptAuditor != null && !OtherDeptAuditor.equals("")) {
				String[] SecondProductServerArr = OtherDeptAuditor.split(",");
				for (String SecondProductServerStr : SecondProductServerArr) {
					if(runtimeService.getVariable(execution.getId(),SecondProductServerStr + "OtherDeptIsAgree") != null) {
						String SecondProductServerIsAgree = (String) runtimeService.getVariable(execution.getId(),SecondProductServerStr + "OtherDeptIsAgree");
						if(SecondProductServerIsAgree.equals("0")) {
							OtherDeptResult = "no";
							break;
						}
					}
				}
			}
			
			String ProductDeveTwoResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProductDeveTwoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductDeveTwoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductDeveTwoResult="no";
					}
				}
			}
			
			String ProductDeveOneResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProductDeveOneIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductDeveOneIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductDeveOneResult="no";
					}
				}
			}
			
			String BaseRomSoftFirstResult = "";
			if(runtimeService.getVariable(execution.getId(),"BaseRomSoftFirstIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomSoftFirstIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomSoftFirstResult="no";
					}
				}
			}
			
			String BaseRomSoftSecondResult = "";
			if(runtimeService.getVariable(execution.getId(),"BaseRomSoftSecondIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomSoftSecondIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomSoftSecondResult="no";
					}
				}
			}
			
			String BaseRomProductResult = "";
			if(runtimeService.getVariable(execution.getId(),"BaseRomProductIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BaseRomProductIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BaseRomProductResult="no";
					}
				}
			}
			
			String ProductLifeResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProductLifeIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductLifeIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductLifeResult="no";
					}
				}
			}
			
			String ProductAmigoResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProductAmigoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductAmigoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductAmigoResult="no";
					}
				}
			}
			
			String VisualProductResult = "";
			if(runtimeService.getVariable(execution.getId(),"VisualProductIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"VisualProductIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						VisualProductResult="no";
					}
				}
			}
			
			String ProductGameResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProductGameIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductGameIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductGameResult="no";
					}
				}
			}
			
			String ProductPlanLockResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProductPlanLockIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductPlanLockIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductPlanLockResult="no";
					}
				}
			}
			
			String ProductVadeoResult = "";
			if(runtimeService.getVariable(execution.getId(),"ProductVadeoIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ProductVadeoIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ProductVadeoResult="no";
					}
				}
			}
			
			String UserInteractResult = "";
			if(runtimeService.getVariable(execution.getId(),"UserInteractIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"UserInteractIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						UserInteractResult="no";
					}
				}
			}
			
			String UserExperienceResult = "";
			if(runtimeService.getVariable(execution.getId(),"UserExperienceIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"UserExperienceIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						UserExperienceResult="no";
					}
				}
			}
			
			String ComCenterResult = "";
			if(runtimeService.getVariable(execution.getId(),"ComCenterIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"ComCenterIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						ComCenterResult="no";
					}
				}
			}
			
			String BigDataStateResult = "";
			if(runtimeService.getVariable(execution.getId(),"BigDataStateIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"BigDataStateIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						BigDataStateResult="no";
					}
				}
			}
			
			String DataPayAndSetResult = "";
			if(runtimeService.getVariable(execution.getId(),"DataPayAndSetIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"DataPayAndSetIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						DataPayAndSetResult="no";
					}
				}
			}
			
			String IPRTResult = "";
			if(runtimeService.getVariable(execution.getId(),"IPRTIsAgree") != null) {
				String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),"IPRTIsAgree");
				if(ThreeCheckIsAgree!=null && !"".equals(ThreeCheckIsAgree)){
					if(ThreeCheckIsAgree.equals("0")){
						IPRTResult="no";
					}
				}
			}
					
			if(FirstPlatformResult.equals("no") || SecondPlatformResult.equals("no") || ThreePlatformResult.equals("no")
					|| DrivePlatformResult.equals("no") || ModemPlatformResult.equals("no") || ToolPlatformResult.equals("no") || SystemSoftResult.equals("no") 
					 || SystemStewardResult.equals("no")  || ScienceFirstResult.equals("no") || ScienceSecondResult.equals("no") || ImageEffectResult.equals("no")
					|| ImageSoftResult.equals("no") || TestProjectResult.equals("no") || TestPlatResult.equals("no")
					|| ProjMageSoftProjResult.equals("no") || ProjMageSoftQualityResult.equals("no") || ClientDeveResult.equals("no")
					|| SecondProductServerResult.equals("no") || ProductDeveTwoResult.equals("no") || ProductDeveOneResult.equals("no") || BaseRomSoftFirstResult.equals("no")
					|| BaseRomSoftSecondResult.equals("no") || BaseRomProductResult.equals("no") || ProductLifeResult.equals("no")
					|| ProductAmigoResult.equals("no") || VisualProductResult.equals("no") || ProductGameResult.equals("no") || ProductPlanLockResult.equals("no") || ProductVadeoResult.equals("no")
					|| UserInteractResult.equals("no") || UserExperienceResult.equals("no") || ComCenterResult.equals("no") || BigDataStateResult.equals("no") 
					|| DataPayAndSetResult.equals("no") || IPRTResult.equals("no") || OtherDeptResult.equals("no")) {
				return "no";
			} else {
				String result="";
				if("dept".equals(level)){
					if("1".equals(TestPlatformAudit)||"1".equals(BigDataAudit)||"1".equals(TestProjectAudit)||"1".equals(UserInteractAudit)||
							"1".equals(SoftArchDeptAudit)||"1".equals(BaseRomProductAudit)||"1".equals(TechnicalResearchAudit)||
							"1".equals(SoftPlatToolAudit)||"1".equals(FirstProductAmigoAudit)){
						result="1";
					}else{
						result="0";
					}
				}else{
					if("1".equals(TestPlatformAudit)||"1".equals(BigDataAudit)||"1".equals(TestProjectAudit)||"1".equals(UserInteractAudit)||
							"1".equals(SoftArchDeptAudit)||"1".equals(BaseRomProductAudit)||"1".equals(TechnicalResearchAudit)||
							"1".equals(SoftPlatToolAudit)||"1".equals(FirstProductAmigoAudit)||"1".equals(SystemFirstAudit)||"1".equals(FirstProductSubjectAudit)){
						result="1";
					}else{
						result="0";
					}
				}
				return result;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "yes";
		}
	}
	
	@Override
	//流程文档会签流程三方会审判断是否同意
	//流程文档会签流程三方会审判断是否同意
		public String getThreeCheckIsAgree(Execution execution,String ThreeCheckAuditor) {
			try {
				String ThreeCheckResult = "";
				if(ThreeCheckAuditor != null && !ThreeCheckAuditor.equals("")) {
					String[] ThreeCheckArr = ThreeCheckAuditor.split(",");
					for (String ThreeCheckStr : ThreeCheckArr) {
						if(runtimeService.getVariable(execution.getId(),ThreeCheckStr + "ThreeCheckIsAgree") != null) {
							String ThreeCheckIsAgree = (String) runtimeService.getVariable(execution.getId(),ThreeCheckStr + "ThreeCheckIsAgree");
							if(ThreeCheckIsAgree.equals("0")) {
								ThreeCheckResult = "no";
								break;
							}
						}
					}
				}
				
				if(ThreeCheckResult.equals("no")) {
					return "no";
				} else {
					return "yes";
				}
			} catch(Exception e) {
				e.printStackTrace();
				return "yes";
			}
		}
	
	
	///---------------------------------------------------------------------
		//以下三个接口用于传员工编号查找对应的部门领导
	
	//工作流平台查找部门负责人（适用于一级审批）
	@Override
	public String getOrgMasterByEmpId(String userAccount) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_ORGMASTER_ByEmpId_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	
	//工作流平台查找部门负责人（适用于二级审批）
	@Override
	public String findOrgLeaderForSpecByEmpId(String userAccount, String type) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_ORGLEADER_SPECIAL_ByEmpId_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		reqData.put("usr.wfRule", type);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	
	//工作流平台查找部门负责人（适用于三级审批）
	@Override
	public String getOrgLeaderByEmpId(String userAccount, String type) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_ORGLEADER_ByEmpId_KEY);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		reqData.put("usr.wfRule", type);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}

	@Override
	public Map<String,Object> getOrgInfoByMonthAndOrg(String orgId,String zaizhiMonth) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.QUARTER_FUND_APPLY_EMPS);
		String[] orgIds=orgId.split(",");
		Map<String,Object> results= new HashMap<String,Object>();
		int allTotal=0;
		int allCostTotal=0;
		JSONArray empsAllJson = new JSONArray();
		for(String org:orgIds){
			Map<String, String> reqData = new HashMap<String, String>();
			reqData.put("orgId",org);
			reqData.put("zaizhiMonth",zaizhiMonth);

			String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
			if(result!=null&&!result.equals("")){
				String emps=result.split("@")[0];
				JSONArray empsJson = JSONArray.parseArray(emps);
				for(int i=0;i<empsJson.size();i++){
					empsAllJson.add(empsJson.get(i));
				}
				int total=Integer.parseInt(result.split("@")[1]);
				int costTotal=Integer.parseInt(result.split("@")[2]);
				allTotal+=total;
				allCostTotal+=costTotal;
			}
			
		}
		results.put("total", allTotal);
		results.put("costTotal", allCostTotal);
		results.put("emps", empsAllJson);
		return results;
	}

	@Override
	public String findById(String account) {
		// TODO Auto-generated method stub
		return gnifUserDao.findById(account);
	}

	@Override
	public String findOrgMaster(String userAccount) {
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ORGID_FINDORGMASTER_ByEmpId_KEY);
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
		// TODO Auto-generated method stub
	}
	///---------------------------------------------------------------------	

	@Override
	public String findOrgLeaderByOrgNo(String orgId, String type) {
		// TODO Auto-generated method stub
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ORGID_ORGLEADER_ByEmpId_KEY);
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("orgId", orgId);
		reqData.put("usr.wfRule", type);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}
	@Override
	public Map<String,Object> getUserWorkInfo(String account){
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_EMP_WORKIFO_BYLOGIN);
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("login", account);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if(result==null){
			return null;
		}
		return parseJsonToMap(result);
	}
	@Override
	public Map<String,Object> parseJsonToMap(String result){
		Map<String,Object> map=new HashMap<String,Object>();
		JSONObject json=JSONObject.parseObject(result);
		for(Object k : json.keySet()){  
	          Object v = json.get(k);   
	           //如果内层还是数组的话，继续解析  
	          if(v instanceof JSONArray){
	               List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
	                Iterator<Object> it = ((JSONArray)v).iterator();  
	               while(it.hasNext()){  
	                   JSONObject json2 = (JSONObject)it.next();  
	                    list.add(parseJsonToMap(json2.toString()));  
	                }  
	               map.put(k.toString(), list);  
	            } else {  
	              map.put(k.toString(), v);  
	            }  
	       }
	       return map;  
	}

	@Override
	public String getRelevantOrgLeader(String userAccount) {
		boolean flag=isMiddleManager(userAccount);
		if(flag){
			return getOrgLeader(userAccount,"2")==null?getOrgLeader(userAccount,"3"):getOrgLeader(userAccount,"2");
		}else{
			return getOrgLeader(userAccount,"1")==null?(getOrgLeader(userAccount,"2")==null?getOrgLeader(userAccount,"3"):getOrgLeader(userAccount,"2")):getOrgLeader(userAccount,"1");
		}
	}

	@Override
	public boolean isOffSpecRole(String loginNo, String type) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.HR_LOGINNO_ISOFF_ROLE);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login",loginNo);
		reqData.put("usr.wfRule", type);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			throw new GnifException("Don't find organization information in SAP!");
		}
	}

	@Override
	public String findOrgLeaderByEmpId(String userAccount, String type) {
		// TODO Auto-generated method stub
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_FINDORGLEADER_ByEmpId_KEY);
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", userAccount);
		reqData.put("usr.wfRule", type);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.split("_")[0];
		}
		return null;
	}

	@Override
	public String getSapStockInfoForJson(String functionName, Map<String,String> inputMap,String exportTableName, String[] exportVars) throws JCoException {
		String info="";
		SapUtil sap=new SapUtil();
		info = sap.readSapStockInfoForJson(functionName, inputMap,exportTableName, exportVars);
		return info;
	}

	@Override
	public String getSapAssetsInfoForJson(String functionName, Map<String, String> inputMap, String exportTableName,
			String[] exportVars) throws Exception {
		String info="";
		try {
			SapUtil sap=new SapUtil();
			info = sap.readSapAssetsInfoForJson(functionName, inputMap, exportTableName, exportVars);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new GnifException("Failure of communication with SAP!");
		}
		return info;
	}

	@Override
	public String loadEmpByUsrAccount(String account) {
		// TODO Auto-generated method stub
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.LOAD_EMPBY_USERACCOUNT);
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", account);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		JSONObject json = JSONObject.parseObject(result);
		if(!org.springframework.util.StringUtils.isEmpty(json)){
			for(Object k : json.keySet()){  
				if(k.toString().equals("empId")){
					return json.get(k).toString();
				}
				continue;
		       }
		}
		return null;
	}

	@Override
	public String isManager(String account) {
		// TODO Auto-generated method stub
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_IS_MANAGER);
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", account);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if(StringUtils.isNotEmpty(result)){
			return result;
		}
		return null;
	}

	@Override
	public String getRelevantLeaderByEmpId(String account, String orgId) throws Exception{
		String direNo=getOrgLeader(account, "4");
		try {
			String leaderNo="";
			if(account.equals(direNo)||getOtherOrgLeader(orgId,"4")==null){
				leaderNo=findOrgLeaderByOrgNoForSpec(orgId,"1");
			}else{
				leaderNo=findOrgLeaderByOrgNoForSpec(orgId,"4");
			}
			if(leaderNo == null){
				throw new Exception("您好!接收部门没配置主管或经理!");
			}
			return leaderNo;
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	public void sendEmail4Orgs(Integer orgId, String titel, String content) {
		// TODO Auto-generated method stub
		List<User>users = userDao.getUsersByOrgId(orgId);
		if (!CollectionUtils.isEmpty(users)) {
			for (User user : users) {
				// 正式环境在取消注释
				String email=this.getUserEmailAddress(user.getAccount());
				if (!StringUtils.isEmpty(email)) {
					//mailSender.sendMail(this.getUserEmailAddress(user.getAccount()), subject, content);
					SendMailTool.sendMailByAddress(titel, content, email);
					//TestEmail.sendMailByAddress(titel, content, "zhongyanyun@gionee.com");
				}
				
			}
		}
	}

	@Override
	public Integer getOrgId(String account) {
		// TODO Auto-generated method stub
		return activitiHelpDao.getOrgId(account);
	}

	@Override
	public String isBuMenWenYuan(String account) {
		// TODO Auto-generated method stub
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_IS_BUMENWENYUAN);
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("usr.login", account);

		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if(StringUtils.isNotEmpty(result)){
			return result;
		}
		return null;
	}

	@Override
	public String isProductEmployee(String account) {
		// TODO Auto-generated method stub
				String reqUrl = PropertyHolder
						.getContextProperty(WebReqConstant.IS_PRODUCT_EMPLOYEE);
				Map<String, String> reqData = new HashMap<String, String>();
				reqData.put("usr.empId", account);

				String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
				if(StringUtils.isNotEmpty(result)){
					return result;
				}
		return null;
	}

	@Override
	public String getDepartRelevantLeader(String userAccount) {
		String result=null;
		String manager=getOrgLeader(userAccount,"1");
		String deptDirector=getOrgLeader(userAccount,"2");
		String business=getOrgLeader(userAccount,"3");
			if(userAccount.equals(manager)){
				if(deptDirector != null && !"".equals(deptDirector)){
					result=deptDirector;
				}else{
					result=business;
				}
			}
			else if(userAccount.equals(deptDirector)){
				result=business;
			}
			else if((manager==null||"".equals(manager.trim()))){
				if(deptDirector != null && !"".equals(deptDirector)){
					result=deptDirector;
				}else{
					result=business;
				}
			}else{
				result=manager;
			}
		return result;
	}

	@Override
	public String getDepartRecruiterAccount(String departName) throws Exception {
		String recruiters = PropertyHolder
				.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=parseJsonToMap(recruiters);
		Set<Entry<String,Object>> entrys=map.entrySet();
		String empId="";
		for(Entry<String,Object> entry:entrys){
			empId=entry.getKey();
			List<String> departNames=Arrays.asList(entry.getValue().toString().split(","));
			if(departNames.contains(departName)){
				break;
			}else{
				empId="";
			}
		}
		return getEmpAccount(empId);
	}

	@Override
	public String getAccountById(String id) {
		return gnifUserDao.getAccountById(id);
	}

	@Override
	public boolean ifOffThreeDepartById(String orgId, String orgListNo) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.DEPART_ISOFF_TREEDEPART);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("orgId",orgId);
		reqData.put("orgnolist", orgListNo);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			throw new GnifException("Don't find organization information in SAP!");
		}
	}

	@Override
	public String getThreeDepartByOrgId(String orgId) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.THREE_DEPART_BYORGID);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("orgId",orgId);
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		return result;
	}

	@Override
	public List<String> getThreeDepartByEmpAccount(String account) {
		String recruiters = PropertyHolder
				.getContextProperty(WebReqConstant.DEPART_RECRUITER_EMPID);
		Map<String,Object> map=parseJsonToMap(recruiters);
		String empId=loadEmpByUsrAccount(account);
		List<String> departNames=null;
		if(StringUtils.isNotEmpty(empId)){
		  departNames=Arrays.asList(map.get(empId).toString().split(","));
		}
		return departNames;
	}

	@Override
	public String getProbationFlow(String account) {
		String result=getDepartRelevantLeader(account);
		String business=getOrgLeader(account,"3");
		String data="0";
		if(result != null && result.equals(business)){
			data="1";
		}
		return data;
	}

	@Override
	public boolean isBeijingEmpByEmpId(String empId) {
		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.LEAVE_ISBEIJINGEMP_BYEMPID);

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("empId",empId);
		
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		if (StringUtils.isNotEmpty(result)) {
			return result.trim().equalsIgnoreCase("Y") ? true : false;
		} else {
			throw new GnifException("Don't find organization information in SAP!");
		}
	}
	@Override
	public List<Organization> getSubOrganization(String orgId){
		List<Organization> orgs = organizationHelpDao.checkHasChild(orgId);
		if(orgs != null && orgs.size() >0 ){
			for(int i=0;i<orgs.size();i++){
				orgs.addAll(getSubOrganization(String.valueOf(orgs.get(i).getId())));
			}
		}
		return orgs;
	}

	@Override
	public boolean isDirectManageDep(String orgId) {
		// TODO Auto-generated method stub
		String[] directManageDeps = PropertyHolder.getContextProperty(WebReqConstant.DIRECT_MANAGEMENT_ORGANIZATION).split(",");
		String thirdDepName = getThreeDepartByOrgId(orgId);
		for(int i=0;i<directManageDeps.length;i++){
			String directManageDep = directManageDeps[i];
			if(directManageDep.equals(thirdDepName)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getEmpLaborContractInfo(String empId) {
		// TODO Auto-generated method stub
		String reqUrl = PropertyHolder
				.getContextProperty(WebReqConstant.HR_ACCOUNT_LABOR_CONTRACTINFO);
		reqUrl+="?empId="+empId;

		String result = HttpClientUtil.sendGetRequest(reqUrl);
		if (StringUtils.isNotEmpty(result)) {
			return result;
		}
		return "";
	}

	@Override
	public String getgetCandidateAccounts(String procDefId, String type) {
		StringBuilder builder=new StringBuilder();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("procDefId", procDefId);
		map.put("type", type);
		String[] empIds=keepRunTaskDao.getCandidateEmpIds(map).split(",");
		
		try {
			for(String empId:empIds){
				builder.append(getEmpAccount(empId)+",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result=builder.toString();
		return result.substring(0, result.length()-1);
	}
	

}
