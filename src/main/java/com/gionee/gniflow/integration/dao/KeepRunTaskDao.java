package com.gionee.gniflow.integration.dao;

import java.util.List;
import java.util.Map;

import com.gionee.gniflow.biz.model.ActRuTask;
import com.gionee.gniflow.biz.model.Candidate;
import com.gionee.gniflow.biz.model.CostCenter;
import com.gionee.gniflow.biz.model.InterviewEntity;
import com.gionee.gniflow.biz.model.JTeamMonth;
import com.gionee.gniflow.biz.model.KeepRunTask;
import com.gionee.gniflow.biz.model.LeaveProcessInstanceEntity;
import com.gionee.gniflow.biz.model.SaveEmpAndTeacherInfEntity;

public interface KeepRunTaskDao {
	void addRunTaskInf(KeepRunTask runTask);
	
	void delTaskInf(String id);
	
	List<String> getRunTaskId(String curTime);
	
	List<ActRuTask> getRunTask(String id);
	
	void delActRuTask(String id);
	
	void updateStatus(String id);
	
	void addEmpAndTeaInf(SaveEmpAndTeacherInfEntity empAndTeacherInfo);
	
	List<SaveEmpAndTeacherInfEntity> getEmailByCurTime(String curTime);
	
	void delEmpAndTeaInf(String empId);
	
	void updateSendEmailTime(Map<String,Object> map);
	
	String getApplyAccountByPro(String proceId);
	
	void saveProofNo(Map<String,Object> map);
	
	String getProofNoByProcessId(String processId);
	
	void delProofNoByProcessId(String proofNo);
	
	List<CostCenter> getCostCenterByFactory(String factory);
	
	List<CostCenter> getAllCostCenter(Map<String,Object> map);
	
	Integer getTotalCount();
	
	void saveCostCenter(Map<String,Object> map);
	
	List<InterviewEntity> getSararyAndGroupFromIter(Map<String,Object> map);
	
	String getEmpIdProbationApply(String empId);
	
	List<Integer> getJTeamMonth();
	
	List<JTeamMonth> getAllJTeamMonth();
	
	void updateJTeamMonth(Map<String,Object> map);
	
	List<LeaveProcessInstanceEntity> getAllLeaveProcess(Map<String,Object> map);
	
	Integer getLeaveCount(Map<String,Object> map);
	
	void delEmpAndTeaInfByProcId(String procId);
	
	String getProcessDefineId(String procId);
	
	void updateCostCenter(Map<String,Object> map);
	
	void deleteCostCenter(Map<String,Object> map);
	
	String getCandidateEmpIds(Map<String,Object> map);
	
	List<Candidate> getAllCandidateUser();
	
	Integer getAllCandidateCount();
	
	void editCandidateInfoById(Map<String,Object> map);
	
	void addCandidateInfo(Map<String,Object> map);
	
	void delCandidateInfoById(Map<String,Object> map);
}
