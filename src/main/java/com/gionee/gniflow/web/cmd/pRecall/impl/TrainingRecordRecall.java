package com.gionee.gniflow.web.cmd.pRecall.impl;

import java.util.ArrayList;
import java.util.List;

import com.gionee.gniflow.web.cmd.pRecall.PRecallTask;

public class TrainingRecordRecall implements PRecallTask {
	
	private List<String> taskKeys=new ArrayList<String>();
	
	public TrainingRecordRecall(){
		taskKeys.add("nmb_deptTeacherAudiTask");
		taskKeys.add("nmb_traineeAudiTask");
	}
	@Override
	public boolean isOrBrotherNode(String historyDefinitionKey, String taskDefinitionKey) {
		if(taskKeys.contains(historyDefinitionKey) && taskKeys.contains(taskDefinitionKey)){
			return false;
		}else{
			return true;
		}
	}

}
