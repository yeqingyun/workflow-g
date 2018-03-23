/**
 * @(#) ActivitiHelpService.java Created on 2014年7月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.biz.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.AccountHandel;
import com.gionee.gniflow.biz.model.BpmTaskExecution;
import com.gionee.gniflow.biz.model.EngineerCollect;
import com.gionee.gniflow.biz.model.EoaTaskInfoEntity;
import com.gionee.gniflow.biz.model.InterviewEntity;
import com.gionee.gniflow.biz.model.LaborContractRenewalEntity;
import com.gionee.gniflow.biz.model.LeaveProcessInstanceEntity;
import com.gionee.gniflow.biz.model.Probation;
import com.gionee.gniflow.biz.model.Requirements;
import com.gionee.gniflow.dto.ProcessDefinitionDto;
import com.gionee.gniflow.web.bmp.BpmHisActivityEntity;
import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;
import com.gionee.gniflow.web.bmp.BpmHisTaskEntity;
import com.gionee.gniflow.web.bmp.BpmIdentityLinkEntity;
import com.gionee.gniflow.web.bmp.BpmTaskEntity;
import com.gionee.gniflow.web.bmp.SendOrderApplyEntity;
import com.gionee.gniflow.web.easyui.AjaxJson;

/**
 * The class <code>ActivitiHelpService</code>
 *
 * @author lipw
 * @version 1.0
 */
public interface ActivitiHelpService {
	
	String findActHiActinstId(String taskId);
	
	String findActHiActinstTaskId(String id);
	
	void deleteActHiActinst4Id(String id);
	
	List<ProcessDefinitionDto> page4ProcessDefinition4Page(QueryMap queryMap);
	
	Integer count4ProcessDefinition(QueryMap queryMap);
	
	void updateProCategory(String processDefId, Integer categoryId);
	/**
	 *  查询历史步骤，主要显示account对应的人员
	 * @param queryMap
	 * 			key:procInstId
	 * @return
	 */
	List<BpmHisActivityEntity> loadBpmHisActivity(QueryMap queryMap);
	
	void updateStartAssignee(String actId, String procInstId, String assignee);
	
	/** 查询工程师正在处理的派单申请的任务  */
	List<BpmTaskEntity> page4EngineerTask(QueryMap queryMap);
	
	/** 查询正在处理的派单申请的任务总数  */
	Integer count4EngineerTask(QueryMap queryMap);
	
	/** 查询任务处理人的任务  */
	List<BpmTaskEntity> page4AssigneeTask(QueryMap queryMap);
	
	/** 查询任务处理人的任务总数  */
	Integer count4AssigneeTask(QueryMap queryMap);
	
	/** 查询已办任务  */
	List<BpmHisTaskEntity> page4CompletedTask(QueryMap queryMap);
	
	/** 查询已办任务总数  */
	Integer count4CompletedTask(QueryMap queryMap);
	
	/** 查询已办任务  */
	List<EoaTaskInfoEntity> page4AssigneeAllTask(QueryMap queryMap);
	
	/** 查询已办任务总数  */
	Integer count4AssigneeAllTask(QueryMap queryMap);
	
	
	/** 查询用户发起的流程  */
	List<BpmHisProcessInstanceEntity> page4StartProcess(QueryMap queryMap);
	
	/** 查询用户发起流程总数  */
	Integer count4StartProcess(QueryMap queryMap);
	
	/** 查询用户参与的流程  */
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcess(QueryMap queryMap);
	
	/** 查询用户参与流程总数  */
	Integer count4InvolvedUserProcess(QueryMap queryMap);
	/** 查询用户参与的流程硬件部派单  */
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessEngineer(QueryMap queryMap);
	
	/** 查询用户参与流程总数硬件部派单 */
	Integer count4InvolvedUserProcessEngineer(QueryMap queryMap);
	
	/** 查询用户参与的流程SAP需求  */
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessReq(QueryMap queryMap);
	
	/** 查询用户参与流程总数SAP需求 */
	Integer count4InvolvedUserProcessReq(QueryMap queryMap);
	
	/** 查询用户参与的流程数据申请  */
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessInfo(QueryMap queryMap);
	
	/** 查询用户参与流程总数数据申请 */
	Integer count4InvolvedUserProcessInfo(QueryMap queryMap);
	
	/** 查询任务的候选人 */
	List<BpmIdentityLinkEntity> queryTaskCandidates(QueryMap queryMap);
	
	/** 根据ACT_HI_ACTINST的ID_获取数据 */
	BpmHisActivityEntity queryHisActivity(String id);
	
	/** 根据流程实例的ID获取流程的历史Activity数据，主要是排除跳过的节点 */
	List<HistoricActivityInstance> queryTrackHistoricActivityInstance(String processInstanceId);
	
	/** 查询进入该包含网关的非包含网关节点*/
	HistoricActivityInstance queryPreviousNotInclusivegatewayHisActInst4CurNode(String procInstId, String executionId, String actType);
	
	/**查询包含网关的最近的节点id*/
	String queryInclusivegatewayHisActInstMaxId(String instanceId,String lastId);
	
	/**查询包含网关的开始id*/
	String queryInclusivegatewayHisActInstMinId(String instanceId,String maxId);
	
	/**查询包含网关之间的最近节点*/
	List<HistoricActivityInstance> queryInclusivegatewayBetNode(String instanceId,String minId,String maxId);
	
	/** 更新包含网关的节点endtime */
	void updateHisInclusivegatewayEndTime(String procInstId, String executionId, Date endTime);
	
	/** 开启新事务查询最新的历史节点数据 */
	List<HistoricActivityInstance> queryHisActInst4NewTransaction(String procInstId);

	/** 更新任务的执行人 */
	void updateTaskAssignee(BpmTaskExecution taskExecution);
	
	/** 平台流程查询按升序查询 */
	List<ProcessDefinition> queryLastVersionAndCategoryBySort(String category);
	
	/** 根据流程实例编号删除运行时变量 */
	void deleteActRuVariable4ProInstId(String procInstId);
	
	/**获取未完成任务*/
	List<Map<String,Object>> getNoFinishTask(String code,Integer beginDays,Integer endDays);
	
	/**派单申请查询*/
	List<SendOrderApplyEntity> page4SendOrderProcess(QueryMap map);
	
	/**派单申请总数查询*/
	Integer count4SendOrderProcess(QueryMap map);
	
	/**面试评审查询*/
	List<InterviewEntity> getInterviewInfo(QueryMap map);
	
	/**面试评审总数查询*/
	Integer getInterviewCount(QueryMap map);
	
	/**导出面试评审数据*/
	void toInterviewInfoExcel(List<InterviewEntity> interviews,HttpServletResponse response,HttpServletRequest request) throws IOException;
	/**获取审批人*/
	List<String> getApprover(String id);
	/**获取两个子部门的父级部门*/
	String getFatherDepartmentName(QueryMap queryMap);
	/**查询派单申请工程师处理的汇总信息*/
	List<EngineerCollect> getMaintainCollect(QueryMap queryMap);
	/**导出派单汇总数据*/
	void toMaintainCollectExcel(List<EngineerCollect> collects,HttpServletResponse response,HttpServletRequest request) throws IOException;
	/**判断固定资产编号是否在申请报废中*/
	List<String> assetsNoisOffApply();
	/**导出派单详细数据*/
	void toSendOrderDetailExcel(List<SendOrderApplyEntity> sendOrderProcess,HttpServletResponse response,HttpServletRequest request) throws IOException;
	/**获取试用期转正数据*/
	List<Probation> getIprobationEation(QueryMap queryMap);
	List<Probation> getIprobationEation4Hr(QueryMap queryMap);
	/**获取试用期转正的数量*/
	Integer getProbationCount(QueryMap queryMap);
	/**招聘专员监控相关的人员需求、录用流程*/
	List<BpmHisProcessInstanceEntity> page4RecruiterUserProcess(QueryMap queryMap) throws Exception;
	/**招聘专员监控相关的人员需求、录用流程的数量*/
	Integer page4RecruiterUserProcessCount(QueryMap queryMap) throws Exception;
	/**人员需求数据*/
	List<Requirements> getRequirementReport(QueryMap queryMap);

	Integer getRequirementReportCount(QueryMap queryMap);
	/**导出人员需求数据*/
	void toRequirementReportExcel(List<Requirements> interviews,HttpServletResponse response,HttpServletRequest request) throws IOException;
	/**导出试用期报表*/
	void toIprobationEationExcel(List<Probation> probations,HttpServletResponse response,HttpServletRequest request) throws IOException;
	void toIprobationEationExcel4Hr(List<Probation> probations,HttpServletResponse response,HttpServletRequest request) throws IOException;
	//户口报表数据
	List<AccountHandel> getAllAccountHandelProcess(QueryMap queryMap);

	Integer getAllAccountHandelProcessCount(QueryMap queryMap);
	/**导出户口报表
	* @throws IOException */
	void toAccountHandelExcel(List<AccountHandel> probations, HttpServletResponse response,HttpServletRequest request) throws IOException;
	
	/**导出离职流程报表信息*/
	void toLeaveProcessInfoExcel(List<LeaveProcessInstanceEntity> leaves,HttpServletResponse response,HttpServletRequest request) throws IOException;
	/**获取劳动合同续签申请记录 */
	List<LaborContractRenewalEntity> getLaborContractRenewals(QueryMap map);
	/**获取劳动合同续签申请记录的总数*/
	Integer getLaborContractRenewalsCount(QueryMap map);
	/**导出劳动合同续签申请记录*/
	public void toLaborContractRenewalExcel(List<LaborContractRenewalEntity> airlineTicketApplications,HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**返回劳动合同续签申请记录的excel工作表*/
	public HSSFWorkbook returnLaborContractHSSFWorkbook(List<LaborContractRenewalEntity> laborContractRenewals);
	/**发送劳动合同续签邮件发送成功确认邮件*/
	public AjaxJson sendLCRSureEmail(List<LaborContractRenewalEntity> laborContractRenewals,String renewalDate);
	/**发送劳动合同续签提醒邮件*/
	public AjaxJson sendLCRReminderEmail(LaborContractRenewalEntity laborContractRenewals,String email,String renewalDate) ;
	/**上传文件流并返回完整的下载地址*/
	public String returnDownloadUrLAfterUpload(InputStream inputStream,String fileName); 
	/**获取用户频繁申请的流程*/
	List<Map<String,Object>> getFrequentlyStartedProcess(QueryMap queryMap);
	/**获取用户最近申请的流程*/
	List<Map<String,Object>> getRecentlyStartedProcess(QueryMap queryMap);
}
