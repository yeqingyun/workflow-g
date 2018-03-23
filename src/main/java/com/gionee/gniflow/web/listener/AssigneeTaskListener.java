/**
 * @(#) AssigneeTaskListener.java Created on 2014年11月19日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.listener;

import java.util.Set;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.service.BpmConfNodeService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.dto.BpmConfNodeDto;
import com.gionee.gniflow.web.support.DefaultTaskListener;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>AssigneeTaskListener</code>
 *
 * @author lipw
 * @version 1.0
 */
public class AssigneeTaskListener  extends DefaultTaskListener {

	private static final long serialVersionUID = -7864813765011636635L;
	
    private static Logger logger = LoggerFactory.getLogger(AssigneeTaskListener.class);
	
    private BpmConfNodeService bpmConfNodeService;
    
	@Override
	public void onCreate(DelegateTask delegateTask) throws Exception {
		bpmConfNodeService = (BpmConfNodeService) SpringTools.getBean(BpmConfNodeService.class);
		
		QueryMap param = new QueryMap();
		param.put("code", delegateTask.getTaskDefinitionKey());
		param.put("processDefId", delegateTask.getProcessDefinitionId());
		BpmConfNodeDto confNodeDto = bpmConfNodeService.joinQueryBpmConfNode(param);
		
        ActivityImpl activityImpl = Context.getExecutionContext().getExecution().getActivity();
        TaskDefinition taskDefinition = (TaskDefinition) activityImpl.getProperty("taskDefinition");
        String activityType = (String) activityImpl.getProperty("type");
        logger.debug("activityType : {}", activityType);
        
		if (confNodeDto != null 
				&& !delegateTask.getTaskDefinitionKey().endsWith(BPMConstant.BPMN_MULTIINSTTASK_SUFFIX)) {
			 if (activityType.equals(BpmnXMLConstants.ELEMENT_TASK_USER)) {
		        Integer assgineeType = this.analysisTaskAssigneeExpression(taskDefinition);
		        if (StringUtils.isNotEmpty(confNodeDto.getAssignee())) {
		        	if (assgineeType == BPMConstant.TASK_ASSIGNMENT_TYPE_ASSGINEE) {
		        		delegateTask.setAssignee(confNodeDto.getAssignee());
		        	} else if (assgineeType == BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEUSERID) {
		        		//多个用户用逗号隔开
			        	String[] accounts = confNodeDto.getAssignee().split(",");
			        	for (String str : accounts) {
			        		delegateTask.addCandidateUser(str.trim());
			        	}
		        	} else if (assgineeType == BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEGROUP) {
		        		//多个用户用逗号隔开
			        	String[] accounts = confNodeDto.getAssignee().split(",");
			        	for (String str : accounts) {
			        		delegateTask.addCandidateGroup(str.trim());
			        	}
		        	}
		        }
		     }
		}
		
	}
	
	
	/**
     * 获取UserTask的责任人配置类型
     * @param taskDefinition
     * @return
     */
    private Integer analysisTaskAssigneeExpression(TaskDefinition taskDefinition){
    	
    	Expression assgineeExp = taskDefinition.getAssigneeExpression();
    	Set<Expression> canUserExp = taskDefinition.getCandidateUserIdExpressions();
    	Set<Expression> canGroupExp = taskDefinition.getCandidateGroupIdExpressions();
    	
    	if (assgineeExp != null && CollectionUtils.isEmpty(canUserExp)
    			&& CollectionUtils.isEmpty(canGroupExp)) {
    		return BPMConstant.TASK_ASSIGNMENT_TYPE_ASSGINEE;
    	}
    	if (assgineeExp == null && !CollectionUtils.isEmpty(canUserExp)
    			&& CollectionUtils.isEmpty(canGroupExp)) {
    		return BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEUSERID;
    	}
    	if (assgineeExp == null && CollectionUtils.isEmpty(canUserExp)
    			&& !CollectionUtils.isEmpty(canGroupExp)) {
    		return BPMConstant.TASK_ASSIGNMENT_TYPE_CANDIDATEGROUP;
    	}
		return BPMConstant.TASK_ASSIGNMENT_TYPE_ASSGINEE;
    }
}
