package com.gionee.gniflow.integration.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.ibatis.annotations.Param;

import com.gionee.gniflow.biz.model.AccountHandel;
import com.gionee.gniflow.biz.model.EngineerCollect;
import com.gionee.gniflow.biz.model.EoaTaskInfoEntity;
import com.gionee.gniflow.biz.model.InterviewEntity;
import com.gionee.gniflow.biz.model.LaborContractRenewalEntity;
import com.gionee.gniflow.biz.model.Probation;
import com.gionee.gniflow.biz.model.Requirements;
import com.gionee.gniflow.dto.ProcessDefinitionDto;
import com.gionee.gniflow.web.bmp.BpmHisActivityEntity;
import com.gionee.gniflow.web.bmp.BpmHisProcessInstanceEntity;
import com.gionee.gniflow.web.bmp.BpmHisTaskEntity;
import com.gionee.gniflow.web.bmp.BpmIdentityLinkEntity;
import com.gionee.gniflow.web.bmp.BpmTaskEntity;
import com.gionee.gniflow.web.bmp.SendOrderApplyEntity;

public interface ActivitiHelpDao {
	
	String findActHiActinstId(String taskId);

	String findActHiActinstTaskId(String id);

	void deleteActHiActinst4Id(String id);
	
	List<ProcessDefinitionDto> page4ProcessDefinition4Page(Map<String, Object> param);

	Integer count4ProcessDefinition(Map<String, Object> param);

	void updateProCategory(@Param("processDefId") String processDefId, @Param("categoryId") Integer categoryId);

	List<BpmHisActivityEntity> loadBpmHisActivity(Map<String, Object> param);
	
	void updateStartAssignee(@Param("actId") String actId, 
			@Param("procInstId") String procInstId,
			@Param("assignee") String assignee);
	
	List<BpmTaskEntity> page4AssigneeTask(Map<String, Object> param);

	Integer count4AssigneeTask(Map<String, Object> param);
	
	List<BpmTaskEntity> page4EngineerTask(Map<String, Object> param);

	Integer count4EngineerTask(Map<String, Object> param);
	
	List<BpmHisTaskEntity> page4CompletedTask(Map<String, Object> param);

	Integer count4CompletedTask(Map<String, Object> param);
	
	List<EoaTaskInfoEntity> page4AssigneeAllTask(Map<String, Object> param);

	Integer count4AssigneeAllTask(Map<String, Object> param);
	
	List<BpmHisProcessInstanceEntity> page4StartProcess(Map<String, Object> param);

	Integer count4StartProcess(Map<String, Object> param);
	
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessEngineer(Map<String, Object> param);

	Integer count4InvolvedUserProcessEngineer(Map<String, Object> param);
	
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessReq(Map<String, Object> param);

	Integer count4InvolvedUserProcessReq(Map<String, Object> param);
	
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcessInfo(Map<String, Object> param);

	Integer count4InvolvedUserProcessInfo(Map<String, Object> param);
	
	List<BpmHisProcessInstanceEntity> page4InvolvedUserProcess(Map<String, Object> param);

	Integer count4InvolvedUserProcess(Map<String, Object> param);
	
	List<BpmIdentityLinkEntity> queryTaskCandidates(Map<String, Object> param);
	
	BpmHisActivityEntity queryHisActivity(String id);
	
	List<HistoricActivityInstance> queryTrackHistoricActivityInstance(String processInstanceId);
	
	List<HistoricActivityInstance> queryHistoricActivityInstance(String processInstanceId);
	
	HistoricActivityInstance queryPreviousNotInclusivegatewayHisActInst4CurNode(@Param("procInstId") String procInstId, 
			 @Param("executionId") String executionId,  @Param("actType") String actType);
	
	String queryInclusivegatewayHisActInstMaxId(@Param("instanceId") String instanceId, 
			 @Param("lastId") String lastId);
	
	String queryInclusivegatewayHisActInstMinId(@Param("instanceId") String instanceId, 
			 @Param("maxId") String maxId);
	
	List<HistoricActivityInstance> queryInclusivegatewayBetNode(@Param("instanceId") String instanceId,@Param("minId") String minId, 
			 @Param("maxId") String maxId);
	
	Integer getActHiDetailLoopTimesByProcInstId(@Param("procInstId") String procInstId);
	
	String getActHiDetailLastFlowByProcInstId(@Param("procInstId") String procInstId,
			@Param("actInstId") String actInstId);
	
	String getActHiDetailLastIdByProcInstId(@Param("procInstId") String procInstId,
			@Param("actInstId") String actInstId,@Param("nodeId") String nodeId);
	
	String getActHiDetailBeginIdByProcInstId(@Param("procInstId") String procInstId,
			@Param("lastFlowAuditId") String lastFlowAuditId);
	
	String getActHiDetailNextFlowByProcInstId(@Param("procInstId") String procInstId,
			@Param("actInstId") String actInstId);
	
	String getActHiDetailNextIdByProcInstId(@Param("procInstId") String procInstId,
			@Param("actInstId") String actInstId,@Param("nodeId") String nodeId);
	
	String getActHiDetailEndIdByProcInstId(@Param("procInstId") String procInstId,
			@Param("nextFlowAuditId") String nextFlowAuditId);
	
	void updateHisInclusivegatewayEndTime(@Param("procInstId") String procInstId,
			@Param("executionId") String executionId, @Param("endTime") Date endTime);
	
	List<HistoricActivityInstance> queryHisActInst4NewTransaction(@Param("procInstId") String procInstId);
	
	void updateTaskAssignee(@Param("taskId") String taskId, @Param("userAccount") String userAccount);
	
	void updateHisTaskAssignee(@Param("taskId") String taskId, @Param("userAccount") String userAccount);
	
	void updateHisTaskInstAssignee(@Param("id") String id, @Param("userAccount") String userAccount);
	
	List<ProcessDefinition> queryLastVersionAndCategoryBySort(@Param("category") String category);
	
	Integer validateRepeatStartProcess(@Param("processDefKey") String processDefKey, @Param("processStatus") Integer processStatus, 
			@Param("startUser") String startUser);
	
	void deleteHisActivityByTaskId(@Param("taskId") String taskId);
	
	void deleteActRuVariable4ProInstId(@Param("procInstId") String procInstId);
	
	List<Map<String,Object>> getNoFinishTask(@Param("code") String code,@Param("beginDays") Integer beginDays,@Param("endDays") Integer endDays);
	
	List<SendOrderApplyEntity> page4SendOrderProcess(Map<String,Object> param);
	
	Integer count4SendOrderProcess(Map<String,Object> param);
	
	List<InterviewEntity> getInterviewInfo(Map<String,Object> param);
	
	Integer getInterviewCount(Map<String,Object> param);
	
	List<String> getApprover(String id);
	
	String getFatherDepartmentName(Map<String,Object> param);
	
	List<EngineerCollect> getMaintainCollect(Map<String,Object> param);
	
	List<String> assetsNoisOffApply();
	
	Integer getOrgId(@Param("account") String account);
	
	List<Probation> getIprobationEation(Map<String,Object> param);
	
	Integer getProbationCount(Map<String,Object> param);
	
	List<Probation> getSalaryGroup(String procId);
	
	List<BpmHisProcessInstanceEntity> page4RecruiterUserProcess(Map<String,Object> param);
	
	Integer page4RecruiterUserProcessCount(Map<String,Object> param);

	List<Requirements> getRequirementReport(Map<String, Object> map);

	Integer getRequirementReportCount(Map<String, Object> map);
	
	List<AccountHandel> getAllAccountHandelProcess(Map<String, Object> map);

	Integer getAllAccountHandelProcessCount(Map<String, Object> map);
	
	List<LaborContractRenewalEntity> getLaborContractRenewals(Map<String,Object> param);
	
	Integer getLaborContractRenewalsCount(Map<String,Object> param);
	
	List<Map<String,Object>> getFrequentlyStartedProcess(Map<String,Object> param);
	
	List<Map<String,Object>> getRecentlyStartedProcess(Map<String,Object> param);
}
