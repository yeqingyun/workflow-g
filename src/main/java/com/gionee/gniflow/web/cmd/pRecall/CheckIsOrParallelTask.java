package com.gionee.gniflow.web.cmd.pRecall;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

import com.gionee.gniflow.web.cmd.pRecall.impl.TrainingRecordRecall;

public class CheckIsOrParallelTask {
	
	private PRecallTask pRecallTask=null;
	
	private String historyDefinitionKey;
	
	private String taskDefinitionKey;
	
	public CheckIsOrParallelTask(String taskDefinitionKey){
		this.taskDefinitionKey=taskDefinitionKey;
	}
	
	public void getRecallTaskClass(HistoricTaskInstanceEntity historicTaskInstanceEntity){
		historyDefinitionKey=historicTaskInstanceEntity.getTaskDefinitionKey();
		String processDefinitionId=historicTaskInstanceEntity.getProcessDefinitionId();
		String processKey=processDefinitionId.substring(0,processDefinitionId.indexOf(":"));
		
		if("L-Training-Record".equals(processKey)){
			pRecallTask=new TrainingRecordRecall();
		}
	}
	
	public boolean isOrBrotherNode(){
		boolean flag=true;
		if(pRecallTask != null){
			flag=pRecallTask.isOrBrotherNode(historyDefinitionKey, taskDefinitionKey);
		}
		return flag;
	}
}
