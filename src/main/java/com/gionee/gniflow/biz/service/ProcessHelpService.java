/**
 * @(#) ProcessHelpService.java Created on 2014年8月13日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.Execution;

import com.gionee.auth.model.Organization;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.dto.UserDto;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.support.AudiException;
import com.sap.conn.jco.JCoException;
/**
 * The class <code>ProcessHelpService</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface ProcessHelpService {
	/**
	 * 根据用户的账号获取姓名
	 * @param account
	 * @return
	 */
	String getUserName(String account);
	/**
	 * 获取用户的Email地址
	 * @param account
	 * @return
	 */
	String getUserEmailAddress(String account);
	/**
	 * 获取用户的手机号码
	 * @param account
	 * @return
	 */
	String getUserMobileNumber(String account);
	/**
	 * 获取用户的手机号码(定时任务中调用,无需登录系统)
	 * @param account
	 * @return
	 */
	String getUserMobileNumberNoLogin(String account);
	/**
	 * 获取多实例任务的责任人
	 * @return
	 */
	List<String> getMultiInstanceTaskUsers(String assigneeList);
	
	/**
	 * 判断多实例任务是否有用户
	 * @return
	 */
	boolean judgeMultiInstanceTaskUsers(String assigneeList);
	/**
	 * 判断多会签部门
	 * @return
	 */
	boolean judgeMultiInstanceTaskAudi(Execution execution);
	/**
	 * 判断会签部门是否完成
	 * @return
	 */
	boolean muliInstanceIsCompleted(Execution execution, String assigneeList);
	/**
	 * 根据选取的部门发送邮件
	 * @param orgIds
	 * @param content
	 */
	void sendEmail4Orgs(List<String> orgIds, String subject, String content);
	/**
	 * 查找Sap的流程定义，以便平台流程查询时方便过滤
	 * @return
	 */
	List<String> getSapProcessDef();
	/**
	 * 根据用户账户列表发送邮件
	 * @param orgIds
	 * @param content
	 */
	void sendEmail4UserAccount(List<String> userAccounts, String subject, String content);

	/**
	 * 获取部门领导  1：部门经理，2：总监，3：主管副总，4：主管，5:部门文员，6：总裁
	 * @param userAccount
	 * @param type
	 * @return
	 */
	String getOrgLeader(String userAccount, String type);
	/**
	 * 获取其他部门领导  1：部门经理，2：总监，3：主管副总，4：主管，5:部门文员，6：总裁 
	 * @param userAccount
	 * @param type
	 * @return
	 */
	String getOtherOrgLeader(String orgId, String type);
	/**
	 * 根据职员等级查找相应负责人(普通职员就找部门经理，经理找总监)
	 * @param userAccount
	 * @return
	 */
	String getRelevantOrgLeader(String userAccount);
	/**
	 * 负责查找上一级领导
	 * @param userAccount
	 * @return
	 */
	String getDepartRelevantLeader(String userAccount);
	/**
	 * 获取部门领导  1：部门经理，2：总监，3：主管副总，4：主管，5:部门文员，6：总裁
	 * 此方法适用于如果未配置部门经理，则发给部门总监
	 * @param userAccount
	 * @param type
	 * @return
	 */
	String findOrgLeaderForSpec(String userAccount, String type);
	/**
	 * 获取其他部门领导  1：部门经理，2：总监，3：主管副总，4：主管，5:部门文员，6：总裁
	 * 此方法适用于如果未配置部门经理，则发给部门总监
	 * @param userAccount
	 * @return
	 */
	String findOrgLeaderByOrgNoForSpec(String orgId, String type);
	
	/**
	 * 判断员工是否转正，true-已转，false-未转
	 * @param userAccount
	 * @return
	 */
	Boolean isRegularEmployee(String userAccount);
	/**
	 * 判断员工是不是中层管理员，true-是，false-否
	 * @param userAccount
	 * @return
	 */
	Boolean isMiddleManager(String userAccount);
	
	/**
	 * 判断员工当天是否在岗，y-是，n-否
	 * @param userAccount
	 * @return
	 */
	Boolean isOnWork(String userAccount);
	/**
	 * 判断员工入职是否满三个月 ，true-是，false-否
	 * @param userAccount
	 * @return
	 */
	Boolean isThreeMonth(String userAccount);
	/**
	 * 判断员工是否职员
	 * @param userAccount
	 * @return
	 */
	Boolean isOfficeWorker(String userAccount);
	/**
	 * 根据员工编号判断是否是职员
	 * @param userAccount
	 * @return
	 */
	
	Boolean isOfficeWorkerByEmpId(String empId);
	

	
	String getChairmanOrCEO(String userAccount);
	/**
	 * 把日期转换为IOS 8601格式
	 * @param execution
	 * 
	 */
	String getSigndemandTimeToIso(String signdemandTime);
	/**
	 * 获取当前时间的前两天时间，并转化成ISO 8601格式
	 * @param execution
	 * 
	 */
	String getBeforSigndemandTime(String date,int day);
	/**
	 * 获取下一个任务的处理人
	 * @return
	 */
	AjaxJson getNextTaskAssignee(String procInstId, boolean isProcess);
	
	/**
	 * 判断是否执行竞业协议
	 *
	 * @return
	 */
	boolean judgeIsPreAgreement(String userAccount);
	/**
	 * 获取公司下的系统名称
	 * @param companyId
	 * @return
	 */
	List<ComboBoxData> getSystemInfoFromCompany(String companyId);
	/**
	 * 获取系统下的部门
	 * @param companyId
	 * @param sysNo
	 * @return
	 */
	List<ComboBoxData> getDeptInfoFromSystem(String companyId, String sysNo);
	/**
	 * 获取部门下的空岗位
	 * @param deptId
	 * @return
	 */
	List<ComboBoxData> getJobInfoFromDept(String deptId);
	/**
	 * 通过账户获取OA员工信息
	 * @param account
	 * @return
	 */
	HrUserDto getHrUser4Account(String account);
	/**
	 * 通过用户编码获取公司所有员工OA信息(旅游申请使用)
	 * @param account
	 * @return
	 */
	List<HrUserDto> getAllEmpInfo(String empIds);
	/**
	 * 通过用户编码获取公司所有员工OA信息
	 * @param account
	 * @return
	 */
	List<HrUserDto> getEmpInfoById(String account);
	/**
	 * 查询用户
	 * @param critera
	 * @return
	 */
	PageResult<UserDto> queryPageUsers(QueryMap critera);
	/**
	 * 改变流程的状态
	 * @param status
	 * @param processInstId
	 */
	void changeProcessState(Integer status, String processInstId);
	/**
	 * 开始节点后的第一个任务是否被办理
	 * @param processInstId
	 * @return
	 */
	Boolean firstTaskIsHandledWithStartEvent(String processInstId);
	/**
	 * 验证重复发起流程
	 * @return
	 */
	Boolean validateRepeatStartProcess(String processDefKey, Integer processStatus, String startUser);
	/**
	 * 获取流程发起人的账户
	 * @param processInstId
	 * @return
	 */
	String getProcessStartUser(String processInstId);
	/**
	 * 获取催办任务的邮件内容
	 * @param actId
	 * @param userAccount
	 * @return
	 */
	String getRemindersTaskEmailContent(String actId, String userAccount);
	/**
	 * 根据DeptId获取SAP中的组织代码
	 * @param deptId
	 * @return
	 */
	String getSapOrgCode4DeptId(String deptId);
	/**
	 * 根据人员编号判断是否有type类型的领导
	 * @param userAccount
	 * @param type
	 * @return
	 */
	Boolean isExistLeader(String userAccount, String type);
	
	/**
	 * 根据部门编号判断是否有type类型的领导
	 * @param orgId
	 * @param type
	 * @return
	 */
	Boolean isExistLeaderByOrgId(String orgId, String type);
	 
	/**
	 * 根据 account查到部门id
	 * @param account
	 * @return id
	 */
	String findById(String account);
	//计算试用期转正流程部门评分是否超过80分
	boolean isDeptPass(Execution execution,String assigneeList) ;
	//计算试用期转正流程关联部门评分是否超过80分
	boolean isRelateDeptPass(Execution execution,String assigneeList) ;
	
	//计算试用期转正流程培训专员评分是否超过80分
	boolean isHrPass(Execution execution) ;
		
	//从SAP读取数据列表(键值对格式)
	public List<ComboBoxData> getSapOptions(String functionName,Map<String,String> inputMap,String exportTableName,String code,String describe) throws Exception;
	//从SAP读取数据列表(键值对格式) 专为新职位的显示而设置
	public List<ComboBoxData> getSapOptions4Job(String functionName,Map<String,String> inputMap,String exportTableName,String code,String describe) throws Exception;
	//从SAP读取数据列表(多个属性格式)
	public List<ComboBoxData> getSapInfoArray(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception;

	//从SAP读取单行数据
	public String  getSapPersonInfo(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception;
	
	//从SAP读取单行数据-JSON格式
	public String  getSapPersonInfoForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception;
	//从SAP读取多行数据-JSON格式
	public    List<ComboBoxData>    getSapOptionsForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception;

	//工作流平台取得员工编号对应的用户账号
	public  String getEmpAccount(String empNo) throws Exception;
	
	//工作流平台查找部门负责人（适用于一级审批）
	public  String getOrgMaster(String userAccount);
	
	//流程文档会签流程多并发多可选多人审核判断是否同意(兼容以前的流程图)
	public String getProjHumanAccountIsAgree(Execution execution);
	
	public String getProjHumanAccountIsAgree(Execution execution,String level,String SecondProductServerAuditor,String BaseRomSoftSecondAuditor,String OtherDeptAuditor,String TestPlatformAudit,String BigDataAudit,String TestProjectAudit,String UserInteractAudit,String SoftArchDeptAudit,
			String BaseRomProductAudit,String TechnicalResearchAudit,String SoftPlatToolAudit,String FirstProductAmigoAudit,String SystemFirstAudit,String FirstProductSubjectAudit);
	
	
	//流程文档会签流程多并发多可选多人审核判断是否同意(兼容以前的流程图)
		public String getProjHumanAccountIsAgree(Execution execution,String TestPlatformAudit,String BigDataAudit,String TestProjectAudit,String UserInteractAudit,String SoftArchDeptAudit,String BaseRomProductAudit,
				String TechnicalResearchAudit,String SoftPlatToolAudit,String FirstProductAmigoAudit,String SystemFirstAudit,String FirstProductSubjectAudit);
		
		public String getProjHumanAccountIsAgree(Execution execution,String level,String SecondProductServerAuditor,String BaseRomSoftSecondAuditor,String OtherDeptAuditor,String TestPlatformAudit,String BigDataAudit,String TestProjectAudit,String UserInteractAudit,String SoftArchDeptAudit,
				String BaseRomProductAudit,String TechnicalResearchAudit,String SoftPlatToolAudit,String FirstProductAmigoAudit,String SystemFirstAudit,String FirstProductSubjectAudit,
				String BaseRomSoftFirstAudit,String SoftPlatDriveAudit,String SoftPlatModemAudit,String FirstProductLifeAudit,String SystemSecondAudit,String SystemSoftAudit,String SecondProductLockAudit,
				String ProjMageSoftProjAudit,String SecondProductVadeoAudit,String ProjMageSoftQualityAudit,String ThirdProductBuyAudit,String VadeoSoftwareAudit,String ProductPlanBraAudit,
				String ThirdProductAdAudit,String VadeoEffectAudit,String ThirdProductGameAudit,String ExpertTechRfAudit,String ThirdProductPayAudit,String ExpertTechPropAudit,
				String ProductPlanLockAudit,String ProductPlanUserAudit,String ProductPlanOperAudit);
		
		//流程文档会签流程三方会审判断是否同意
		public String getThreeCheckIsAgree(Execution execution,String ThreeCheckAuditor);
	
	
///---------------------------------------------------------------------
	//以下三个接口用于传员工编号查找对应的部门领导
	//工作流平台查找部门负责人（适用于一级审批）
	public  String getOrgMasterByEmpId(String userAccount);
	
	//工作流平台查找部门负责人（适用于三级审批）
	String getOrgLeaderByEmpId(String userAccount, String type);
	/**
	 * 获取其他部门领导  1：部门经理，2：总监，3：主管副总，4：主管，5:部门文员，6：总裁
	 * @param userAccount
	 * @param type
	 * @return
	 */
	//工作流平台查找部门负责人（适用于二级审批）
	String findOrgLeaderForSpecByEmpId(String userAccount, String type);
	/**
	 * 获取其他部门领导  1：部门经理，2：总监，3：主管副总，4：主管，5:部门文员，6：总裁
	 * 此方法适用于如果未配置部门经理，则发给部门总监
	 * @param userAccount
	 * @return
	 */
	///---------------------------------------------------------------------
	/**
	 * 根据部门ID、月份获取某部门某个在职人员的名字、人数及活动经费
	 */
	Map<String,Object> getOrgInfoByMonthAndOrg(String orgId,String zaizhiMonth);
	
	/**
	 * 根据登陆编号 获取最高领导人
	 */
	String findOrgMaster (String userAccount);
	
	/**
	 * 根据部门编号找领导   1：部门经理，2：总监，3：主管副总，4：主管
	 * @param orgId
	 * @param type
	 * @return
	 */
	String findOrgLeaderByOrgNo(String orgId,String type);
	
	/**根据账号获取职员的培训、工作经历*/
	Map<String,Object> getUserWorkInfo(String account);
	
	/**
	 * 1：部门经理，2：总监，3：主管副总，4：主管,5:部门文员,6:总裁,7:副经理
	 * @param userAccount
	 * @param type
	 * @return
	 */
	String findOrgLeaderByEmpId(String userAccount,String type);
	/**把json格式数据转成map*/
	Map<String,Object> parseJsonToMap(String result);
	/**
	 * 判断某个员工是否是指定的角色   绩效专员:1招聘专员:2 培训专员:3薪资专员:4人事专员:5行政专员:6考勤专员(金立):7 考勤专员(金铭):8人力经理:9公司总经理:10
	 * @param loginNo
	 * @param type
	 * @return
	 */
	boolean isOffSpecRole(String loginNo,String type);
	/**
	 * 从SAP获取非生产物料信息
	 * @param functionName
	 * @param inputMap
	 * @param exportVars
	 * @return
	 */
	String getSapStockInfoForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws JCoException;
	/**
	 * 从SAP获取固定资产信息
	 * @param functionName
	 * @param inputMap
	 * @param exportTableName
	 * @param exportVars
	 * @return
	 * @throws Exception
	 */
	String getSapAssetsInfoForJson(String functionName,Map<String,String> inputMap,String exportTableName,String[] exportVars) throws Exception;
	
    /**
     *  根据账号获取编号
     * @param account
     * @return
     */
	 String loadEmpByUsrAccount(String account);
	 
	 /**
	  * 根据账号判断是否是主管以上
     * @param account
     * @return
	  */
	 String isManager(String account);
	 
	 /**
	  * 根据职员编号及所在部门获取相应的审核人
	  * @param account
	  * @param orgId
	  * @return
	  */
	 String getRelevantLeaderByEmpId(String account,String orgId) throws Exception;
	 
	 /**
	  * 发送邮件给部门所用人
	  * @param orgId
	  * @param titel
	  * @param content
	  */
	 void sendEmail4Orgs(Integer orgId,String titel, String content);
	 
	 Integer getOrgId(String account);
	 
	 /**
	  * 根据登陆账号判断是否是部门文员 n代表不是 
	  * @param account
	  * @return
	  */
	 String isBuMenWenYuan(String account);
	 
	 /**
	  * 根据账号判断是否是生产部员工
	  * @param account
	  * @return
	  */
	 String  isProductEmployee(String account);
	 
	 /**
	  * 根据职员账号查找相应的第三层部门
	  * @param account
	  * @return
	  */
	 List<String> getThreeDepartByEmpAccount(String account);
	 
	 /**
	  * 根据三层部门查找相应的招聘专员
	  * @param departName
	  * @return
	  */
	 String getDepartRecruiterAccount(String departName) throws Exception;
	 
	 /**
	  * 根据用户id查找账号
	  * @param id
	  * @return
	  */
	 String getAccountById(String id);
	 
	 /**
	  * 判断某个部门是否在指定的第三层部门下
	  * @param orgId
	  * @param orgListNo
	  * @return
	  */
	 boolean ifOffThreeDepartById(String orgId,String orgListNo);
	 
	 /**
	  * 根据部门id获取顶层部门名称
	  * @param orgId
	  * @return
	  */
	 String getThreeDepartByOrgId(String orgId);
	 
	 /**
	  * 根据员工编号判断试用期是否要跳过主管副总审核
	  * @param account
	  * @return
	  */
	 String getProbationFlow(String account);
	 
	 /**
	  * 根据职员编号判断是否是北京金立职员
	  * @param empId
	  * @return
	  */
	 boolean isBeijingEmpByEmpId(String empId);
	 /**
	  * 获取刘董直管部门：监察部、产品规划中心、供应链&流程管理部及其下属部门
	  * @param orgId
	  * @return
	  */
	 public List<Organization> getSubOrganization(String orgId);
	 /**
	  * 是否为刘董直管部门：监察部、产品规划中心、供应链&流程管理部及其下属部门
	  * @param orgId
	  * @return
	  */
	 boolean isDirectManageDep(String orgId);
	 /**
	  * 获取员工本次合同信息中的合同信息
	  * @param account
	  * @return
	  */
	 String getEmpLaborContractInfo(String empId);
	 /**
	  * 根据流程定义ID及类型查找相应的节点候选人
	  * @param procDefId
	  * @param type
	  * @return
	  */
	 String getgetCandidateAccounts(String procDefId,String type);
}
