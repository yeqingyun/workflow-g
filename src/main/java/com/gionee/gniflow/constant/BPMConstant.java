/**
 * @(#) BPMConstant.java Created on 2014年6月26日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.constant;

/**
 * The class <code>BPMConstant</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface BPMConstant {

	public static final String PROCESS_STEPS = "processSteps";			//Process Task
	public static final String BPMN_MULTIINSTTASK_SUFFIX = "MultiInstTask";//会签节点后缀
	public static final String SKIP_TASK_IGNOR_CONDTION_KEY = "ignor_condion";//是否跳过任务属性KEY
	public static final String OBSOLETE_PROCESS_REASON = "用户废止流程";
	
	//是否能够重复发起的标识
	public static final Integer PROCESS_CAN_REPEAT_YES = 1;
	public static final Integer PROCESS_CAN_REPEAT_NO = 2;
	
	//SAP 采购流程部署分类ID
	public static final String SAP_PROCESS_REQ_CATAGORY = "0";
	//SAP 采购流程定义的Key
	public static final String SAP_REQ_PRO_DEF_KEY = "SAP-Purchase-Requisition";
	//SAP 审批值
	public static final String SAP_AUDI_AGREE = "0";
	public static final String SAP_AUDI_NOAGREE = "1";
	
	//子系统调用接口启动流程人员的参数的key,必须以此值配置
	public static final String SUB_SYSTEM_START_PROCESS_USER_ACCOUNT = "currentUserId";
	public static final String SUB_SYSTEM_START_PROCESS_USER_NAME = "currentUserName";
	
	//GnFS
	public static final String FILE_UPLOAD_URL = "file_upload_url";
	public static final String FILE_DOWNLOAD_URL =  "file_download_url";
	public static final String FILE_PARAM_IFRAMEURL = "file_param_iframeUrl";
	public static final String FILE_PARAM_SYID = "file_param_syId";
	public static final String FILE_PARAM_SYNM = "file_param_syNm";
	public static final String FILE_PARAM_DIYFOLDER = "file_param_diyFolder";
	public static final String FILE_PARAM_UPLOADTYPE = "file_parma_uploadType";
	public static final String FILE_PARAM_FILEVERSION = "file_param_fileVersion";
	
	//日期标识
	public static final String CURRENT_PARAM_DATE = "current_param_date";
	//删除标识
	public final static Integer STATUS_NORMAL = 0;
	public final static Integer STATUS_DELETED = -1;
	
	//流程状态
	public static final Integer PROCESS_STATUS_RUNNING = 1;//运行中
	public static final Integer PROCESS_STATUS_COMPLETED = 2;//已完成
	
	//文档会签的任务Key
	public static final String FILE_SIGN_TASK_KEY = "relevantdepartmentsMultiInstTask";
	
	//领导类型
	public static final String LEADER_TYPE_MANAGER = "1";//部门经理
	public static final String LEADER_TYPE_DIRECTOR = "2";//部门总监
	public static final String LEADER_TYPE_DEPUTY_DIRECTOR = "3";//主管副总
	public static final String LEADER_TYPE_CHARGE = "4";//主管
	public static final String LEADER_TYPE_CLERK = "5";//部门文员
	public static final String LEADER_TYPE_PRESIDENT = "6";//总裁
	
	
	//任务分类类型
	public static final Integer TASK_ASSIGN_TYPE_TODO = 1;
	public static final Integer TASK_ASSIGN_TYPE_PROXY = 2;
	
	//邮件发送类型
	public static final Integer MAIL_SEND_TYPE_TASK_ARRIVAL_TRANSACTOR = 1;//发给流程办理人
	public static final Integer MAIL_SEND_TYPE_TASK_ARRIVAL_STARTER = 2;//发给流程发起人
	public static final Integer MAIL_SEND_TYPE_TASK_COMPLETED = 3;//任务完成时，提醒流程发起人
	public static final Integer MAIL_SEND_TYPE_TASK_TIMEOUT = 4;//任务超时时，提醒任务处理人
	
	//是否发送邮件标识
	public static final Integer MAIL_SEND_FLAG_YES = 1;//发送
	public static final Integer MAIL_SEND_FLAG_NO = 2;//不发送
	
	//任务处理人配置类型
	public static final Integer TASK_ASSIGNMENT_TYPE_ASSGINEE = 1; //assignee
	public static final Integer TASK_ASSIGNMENT_TYPE_CANDIDATEUSERID = 2; //candidateUser
	public static final Integer TASK_ASSIGNMENT_TYPE_CANDIDATEGROUP = 3; //candidateGroup
	
	//人事范围
	public static final String[] HR_PERSONAL_RANGE = new String[]{"金立","电商事业部","海外事业部","金铭","售后","金尚","金卓","金众"};
	public static final String[] HR_PERSONAL_SUB_RANGE = new String[]{"深圳","东莞","北京"};
	
	//合同类型
	public static final String LABOR_CONTRACT_TYPE_FIXED = "01";//固定期限合同
	public static final String LABOR_CONTRACT_TYPE_INFINITE  = "02";//无固定期限合同
	
}
