/**
 * @(#) DeleteHisStartVariableCmd.java Created on 2014年12月25日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.cmd;

import java.util.List;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * 
 * The class <code>DeleteHisStartVariableCmd</code>
 *
 * @author lipw
 * @version 1.0
 */
public class DeleteStartVariableCmd implements Command<Void> {
	
    private static Logger logger = LoggerFactory.getLogger(DeleteStartVariableCmd.class);
   
    private String processInstId;
    
    private ActivitiHelpService activitiHelpService;

    /**
     * 这个historyTaskId是已经完成的一个任务的id.
     */
    public DeleteStartVariableCmd(String processInstId) {
        this.processInstId = processInstId;
    }

    /**
     * 删除流程开始时而导致的历史变量
     */
    public Void execute(CommandContext commandContext) {
    	
    	logger.debug("[DeleteHisStartVariableCmd]--delete start variables--begin ");
    	
    	//删除ACT_HI_VARINST
    	Context.getCommandContext().getHistoricVariableInstanceEntityManager()
    		.deleteHistoricVariableInstanceByProcessInstanceId(processInstId);
    	
    	//删除ACT_HI_TASKINST
    	Context.getCommandContext().getHistoricTaskInstanceEntityManager()
    		.deleteHistoricTaskInstancesByProcessInstanceId(processInstId);
    	
    	//删除ACT_HI_DETAIL
    	Context.getCommandContext().getHistoricDetailEntityManager()
			.deleteHistoricDetailsByProcessInstanceId(processInstId);
    	
    	//删除ACT_HI_ACTINST--除了Start节点的数据，其他都删除
    	HistoryService historyService = Context.getProcessEngineConfiguration().getHistoryService();
    	activitiHelpService = (ActivitiHelpService) SpringTools.getBean(ActivitiHelpService.class);
    	List<HistoricActivityInstance> hisActivityList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstId).list();
    	if (!CollectionUtils.isEmpty(hisActivityList)) {
    		for (HistoricActivityInstance hisActInst : hisActivityList) {
    			if (!hisActInst.getActivityType().equals(BpmnXMLConstants.ELEMENT_EVENT_START)) {
    				activitiHelpService.deleteActHiActinst4Id(hisActInst.getId());
    			}
    		}
    	}
    	
    	//删除ACT_HI_ATTACHMENT-只删除开始节点上传的附件
    	List<Attachment> startAttachments = Context.getCommandContext().getAttachmentEntityManager().findAttachmentsByProcessInstanceId(processInstId);
    	if (!CollectionUtils.isEmpty(startAttachments)) {
    		for (Attachment entity : startAttachments) {
        		Context.getCommandContext().getAttachmentEntityManager().delete((AttachmentEntity)entity);
        	}
    	}
    	
    	//删除ACT_HI_COMMENT
    	List<Comment> commentList = Context.getCommandContext().getCommentEntityManager().findCommentsByProcessInstanceId(processInstId);
    	if (!CollectionUtils.isEmpty(commentList)) {
    		for (Comment comment : commentList) {
    			Context.getCommandContext().getCommentEntityManager().delete((CommentEntity)comment);
        	}
    	}
    	
    	//删除ACT_RU_TASK
    	Context.getCommandContext().getTaskEntityManager()
        	.deleteTasksByProcessInstanceId(processInstId, null, true);
    	
    	//删除ACT_RU_VARIABLE
    	//activitiHelpService.deleteActRuVariable4ProInstId(processInstId);
    	
    	logger.debug("[DeleteHisStartVariableCmd]--delete start variables--end ");
    	
    	return null;
    }

}
