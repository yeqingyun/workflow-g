package com.gionee.gniflow.biz.model;

import java.io.Serializable;

public class KeepRunTask implements Serializable{
	private String taskId;
	private String runTime;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	
}
