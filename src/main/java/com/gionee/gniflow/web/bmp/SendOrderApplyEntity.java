package com.gionee.gniflow.web.bmp;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

public class SendOrderApplyEntity extends HistoricProcessInstanceEntity {
	
	private static final long serialVersionUID = 1245588297353162643L;
	//实例流程编号
	private String processInstanceId;
	//实例名称
	private String processInstanceName;
	//流程名称
	private String processDefName;
	//任务名称
	private String taskName;
	//任务处理工程师名称
	private String engineerName;
	//流程状态
	private Integer processStatus;
	//状态描述
	private String statusConent;
	//满意度
	private String satisfaction;
	//处理耗时
	private String elapsedTime;
	//满意率
	private String satisfactionRate;
	
	
	public String getStatusConent() {
		return statusConent;
	}
	public void setStatusConent(String statusConent) {
		this.statusConent = statusConent;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getSatisfactionRate() {
		return satisfactionRate;
	}
	public void setSatisfactionRate(String satisfactionRate) {
		this.satisfactionRate = satisfactionRate;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessInstanceName() {
		return processInstanceName;
	}
	public void setProcessInstanceName(String processInstanceName) {
		this.processInstanceName = processInstanceName;
	}
	public String getProcessDefName() {
		return processDefName;
	}
	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getEngineerName() {
		return engineerName;
	}
	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}
	public Integer getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}
	public String getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}
	
	
}
