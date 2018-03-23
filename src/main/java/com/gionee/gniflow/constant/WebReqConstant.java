/**
 * @(#) WebReqConstant.java Created on 2014年6月11日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.constant;

import java.io.File;

/**
 * The class <code>WebReqConstant</code>
 *
 * @author lipw
 * @version 1.0
 */
public final class WebReqConstant {
	
	//日期默认格式
	public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_CHAR_SET = "UTF-8";
	
	//获取PMS的数据
	public static final String  PMS_URL_PROJECT_TREE = "pms.url.projectTree";
	
	public static final String  PMS_URL_PROJECT_LEADER = "pms.url.projectLeader";
	
	/***************************************************************************/
	//获取PPMM的数据
	public static final String PPMM_URL_GET_ADMINS = "ppmm.url.getAdmins";
	
	/***************************************************************************/
	//HR相关接口URL的key
	public static final String HR_EMP_EMPINFO_KEY = "hr.emp.empinfo";
	public static final String HR_ALL_EMPINFO_KEY="hr.all.empinfo";
	public static final String HR_ALL_EMPINFONOLOGIN_KEY="hr.all.empinfonologin";
	public static final String HR_DOCUEMNT_DOCINFO_KEY = "hr.document.docinfo";
	public static final String HR_DOCUEMNT_SYNCDATA_KEY = "hr.document.syncdata";
	public static final String HR_DOCUEMNT_EMAIL_NOTIFY_KEY = "hr.document.email.notify";
	public static final String HR_ORG_APPLY_INFO="hr.emp.applyInfo";
	
	public static final String HR_ACCOUNT_ORGLEADER_KEY = "hr.account.orgleader";
	public static final String HR_ACCOUNT_OTHER_ORGLEADER_KEY = "hr.account.other.orgleader";
	public static final String HR_LOGINNO_ISOFF_ROLE="hr.loginNo.isOff.role";
	
	public static final String HR_ACOUNT_IS_REGULAREMPLOYEE = "hr.account.is.regularemployee";
	public static final String HR_ACOUNT_IS_MIDDLEMANAGER = "hr.account.is.middlemanager";
	public static final String HR_ACCOUNT_IS_ONWORK="hr.account.is.onWork";
	public static final String HR_ACCOUNT_IS_OFFICEWORKER = "hr.account.is.officeworker";
	public static final String HR_ACCOUNT_IS_OFFICEWORKERBYEMPID = "hr.account.is.officeworkerbyempid";
	public static final String HR_ACCOUNT_LABOR_CONTRACTINFO = "hr.account.labor.contractinfo";
	
	public static final String HR_INFO_COMPANY_SYSTEMINFO = "hr.info.company.systeminfo";
	public static final String HR_INFO_SYSTEM_DEPTINFO = "hr.info.system.deptinfo";
	public static final String HR_INFO_DEPT_JOBINFO = "hr.info.dept.jobinfo";
	public static final String HR_ACCOUNT_ORGLEADER_SPECIAL_KEY = "hr.account.orgleaderspecial";
	public static final String HR_ACCOUNT_OTHER_ORGLEADER_SPECIAL_KEY = "hr.account.other.orgleaderspecial";
	
	public static final String HR_INFO_DEPT_SAPCODEINFO_KEY = "hr.info.dept.sapcodeinfo";
	public static final String HR_Emp_UsrACCOUNT = "hr.account.empAccount";
	public static final String HR_ACCOUNT_ORGMASTER_KEY = "hr.account.orgmaster";
	
	
	public static final String HR_ACCOUNT_ORGMASTER_ByEmpId_KEY = "hr.account.empid.orgmaster";
	public static final String HR_ACCOUNT_ORGLEADER_SPECIAL_ByEmpId_KEY = "hr.account.empid.orgleaderspecial";
	public static final String HR_ACCOUNT_ORGLEADER_ByEmpId_KEY = "hr.account.empid.orgleader";
	
	public static final String HR_ORGID_FINDORGMASTER_ByEmpId_KEY = "hr.orgid.findorgmaster.orgleader";
	
	public static final String HR_ORGID_ORGLEADER_ByEmpId_KEY = "hr.orgid.empid.orgleader";
	
	public static final String HR_ACCOUNT_FINDORGLEADER_ByEmpId_KEY = "hr.account.findorglead.orgleader";
	public static final String HR_EMP_WORKIFO_BYLOGIN="hr.emp.work_info";
	
	/***************************************************************************/
	//部门领导权限组的key
	public static final String DEPT_LEADER_KEY = "DeptLeader";
	public static final String DEPT_LEADER_VALUE = "1";
	
	//催办任务邮件内容模板KEY
	public static final String REMINDERS_TASK_EMAIL_TITLE = "reminders.task.email.title";
	public static final String REMINDERS_TASK_EMAIL_CONTENT = "reminders.task.email.content";
	
	//SAP采购流程-删除标识
	public static final char DEL_NO = '0';
	public static final char DEL_YES = '1';
	
	//采购单状态
	public static final Integer PROCURE_SHEET_TOBEAUDI = 0;//待审核
	public static final Integer PROCURE_SHEET_AUDIING = 1;//审核中
	public static final Integer PROCURE_SHEET_AUDIED = 2;//已审核
	public static final Integer PROCURE_SHEET_TOBEMODEFY = 3;//待修改
	
	//文档会签流程
	public static final Integer SIGN_FILE_PRO_AUDIING = 1;//审批中
	public static final Integer SIGN_FILE_PRO_AUDIED = 2;//审批结束
	
	//文档会签 文件 操作类型
	public static final Integer SIGN_FILE_OPERATE_TYPE_ADD = 0;//新增
	public static final Integer SIGN_FILE_OPERATE_TYPE_MODIFY = 1;//修订
	public static final Integer SIGN_FILE_OPERATE_TYPE_ABOLISH = 2;//废止
	
	//是否同意
	public static final Integer AGREE_YES = 0;
	public static final Integer AGREE_NO = 1;
	
	//空串
	public static final String EMPTY_STRING = "";
	
	//模板的位置
	public static final String TEMPLATE_PATH = "WEB-INF" + File.separator + "template" + File.separator;
	
	//打印插件的位置
	public static final String COMMON_PATH = "WEB-INF" + File.separator + "common" + File.separator;
	
	//旅游活动审核是否通过
	public static final Integer NOT_PASSED = 0;
	public static final Integer HAVE_PASSED = 1;
	public static final String LOAD_EMPBY_USERACCOUNT= "load.empby.useraccount";
	//根据员工账号判断是否是主管以上
	public static final String HR_IS_MANAGER= "hr.is.Manager";
	
	//根据员工账号判断是否是部门文员
	public static final String HR_IS_BUMENWENYUAN= "hr.is.BuMenWenYuan";
	
	//根据员工账号判断是否是生产部员工 
	public static final String IS_PRODUCT_EMPLOYEE="is.product.employee";
	
	//部门对应的招聘专员
	public static final String DEPART_RECRUITER_EMPID="depart.recruiter.empId";
	
	//判断某个部门是否在指定的第三层部门下
	public static final String DEPART_ISOFF_TREEDEPART="depart.isOff.threeDepart";
	
	//根据部门id获取顶层部门名称
	public static final String THREE_DEPART_BYORGID="three.depart.byOrgId";
	
	//根据职员编号判断是否是北京金立职员
	public static final String LEAVE_ISBEIJINGEMP_BYEMPID="leave.isBeijingEmp.byEmpId";
	
	//刘董直管部门
	public static final String DIRECT_MANAGEMENT_ORGANIZATION = "direct.management.organization";
	
	//劳动合同续签通知邮件图片
	public static final String REMINDER_LABOR_CONTRACT_EMAIL_IMAGE = "reminder.labor.contract.email.image";
	//劳动合同续签通知邮件信封
	public static final String REMINDER_LABOR_CONTRACT_EMAIL_BGIMAGE = "reminder.labor.contract.email.bgimage";
	
	public static final String REMINDER_LABOR_CONTRACT_EMAIL_QUERY = "reminder.labor.contract.email.query";
	
	public static final String SURE_LABOR_CONTRACT_EMAIL_QUERY = "sure.labor.contract.email.query";
	
	public static final String FLOW_TASK_HAND_URL = "flow.task.hand.url";
	
	public static final String  QUARTER_FUND_APPLY_EMPS= "quarter.fund.apply.emps";
	
	//户口办理引导图片地址
	public static final String ACCOUNT_GUIDE_EMAIL_BGIMAGE = "account.guide.email.bgimage";
	
	//离职流程指引图片地址
	public static final String LEAVE_GUIDE_EMAIL_IMAGE = "leave.guide.email.image";
}
