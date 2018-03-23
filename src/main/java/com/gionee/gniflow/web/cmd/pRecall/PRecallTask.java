package com.gionee.gniflow.web.cmd.pRecall;


public interface PRecallTask {
	
	//并行或多实例节点取回任务另处理
	public boolean isOrBrotherNode(String historyDefinitionKey,String taskDefinitionKey);
}
