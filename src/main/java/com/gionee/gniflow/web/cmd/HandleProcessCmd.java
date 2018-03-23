/**
 * @(#) HandleProcessCmd.java Created on 2014年11月18日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.cmd;

import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDefinitionCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.BpmConfBase;
import com.gionee.gniflow.biz.model.BpmConfNode;
import com.gionee.gniflow.biz.model.BpmConfNodeSend;
import com.gionee.gniflow.biz.model.BpmProcessConf;
import com.gionee.gniflow.biz.service.BpmConfBaseService;
import com.gionee.gniflow.biz.service.BpmConfNodeSendService;
import com.gionee.gniflow.biz.service.BpmConfNodeService;
import com.gionee.gniflow.biz.service.BpmProcessConfService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.web.graph.Graph;
import com.gionee.gniflow.web.graph.Node;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>HandleProcessCmd</code>
 *
 * @author lipw
 * @version 1.0
 */
public class HandleProcessCmd implements Command<Void>{
	
	private static final Integer SORT_DEFAULT_VALUE = 100;
	
	private String processDefinitionId;
	
	private BpmConfBaseService bpmConfBaseService;
	
	private BpmConfNodeService bpmConfNodeService;
	
	private BpmConfNodeSendService bpmConfNodeSendService;
	
	private BpmProcessConfService bpmProcessConfService;
	
	public HandleProcessCmd(String processDefinitionId){
		this.processDefinitionId = processDefinitionId;
	}

	@Override
	public Void execute(CommandContext commandContext) {
		initService();
		//部署时，处理相关配置信息
		ProcessDefinitionEntity processDefinitionEntity = new GetDeploymentProcessDefinitionCmd(
                processDefinitionId).execute(commandContext);
        String processDefinitionKey = processDefinitionEntity.getKey();
        int processDefinitionVersion = processDefinitionEntity.getVersion();
        String processName = processDefinitionEntity.getName();
        
        //handle BpmConfBase begin
        BpmConfBase bpmConfBase = null;
        QueryMap param = new QueryMap();
        param.put("processDefKey", processDefinitionKey);
        param.put("processDefVersion", processDefinitionVersion);
        List<BpmConfBase> confBases = bpmConfBaseService.query(param);
        if (CollectionUtils.isEmpty(confBases)) {
        	bpmConfBase = null;
        } else {
        	bpmConfBase = confBases.get(0);
        }
        
        if (bpmConfBase == null) {
        	 bpmConfBase = new BpmConfBase();
             bpmConfBase.setProcessDefId(processDefinitionId);
             bpmConfBase.setProcessDefKey(processDefinitionKey);
             bpmConfBase.setProcessDefVersion(processDefinitionVersion);
             bpmConfBaseService.save(bpmConfBase);
        } else if (bpmConfBase.getProcessDefId() == null) {
            bpmConfBase.setProcessDefId(processDefinitionId);
            bpmConfBaseService.save(bpmConfBase);
        }     
        
        //handle BpmProcessConf begin
        param.getMap().clear();
        param.put("processDefKey", processDefinitionKey);
        List<BpmProcessConf> processSortList = bpmProcessConfService.query(param);
        if (CollectionUtils.isEmpty(processSortList)) {
        	BpmProcessConf proConf = new BpmProcessConf();
        	proConf.setProcessDefKey(processDefinitionKey);
        	proConf.setProcessName(processName);
        	proConf.setSort(SORT_DEFAULT_VALUE);
        	proConf.setCategoryId(processDefinitionEntity.getCategory());
        	proConf.setCanRepeat(BPMConstant.PROCESS_CAN_REPEAT_NO);
        	bpmProcessConfService.save(proConf);
        }
        //handle BpmProcessConf end
        
        //handle BpmConfNode
        BpmnModel bpmnModel = new GetBpmnModelCmd(processDefinitionId).execute(commandContext);
		Graph graph = new FindGraphCmd(processDefinitionId).execute(commandContext);
        
		int priority = 50;

        for (Node node : graph.getNodes()) {
            if ("exclusiveGateway".equals(node.getType())) {
                continue;
            } else if ("userTask".equals(node.getType())) {
                this.processProcessNode(node, bpmnModel, priority, bpmConfBase, true);
            } else if ("startEvent".equals(node.getType())) {
                this.processProcessNode(node, bpmnModel, priority, bpmConfBase, false);
            } else if ("endEvent".equals(node.getType())) {
                this.processProcessNode(node, bpmnModel, priority, bpmConfBase, false);
            }
        }
		return null;
	}
	
	/**
	 * 初始化服务
	 */
	private void initService(){
		bpmConfBaseService = (BpmConfBaseService) SpringTools.getBean(BpmConfBaseService.class);
		bpmConfNodeService = (BpmConfNodeService) SpringTools.getBean(BpmConfNodeService.class);
		bpmConfNodeSendService = (BpmConfNodeSendService) SpringTools.getBean(BpmConfNodeSendService.class);
		bpmProcessConfService = (BpmProcessConfService) SpringTools.getBean(BpmProcessConfService.class);
	}
	
	/**
     * 配置流程节点.
     */
    public void processProcessNode(Node node, BpmnModel bpmnModel, int priority,
            BpmConfBase bpmConfBase, boolean isUserTask) {
    	BpmConfNode bpmConfNode = null;
    	QueryMap param = new QueryMap();
    	param.put("code", node.getId());
    	param.put("confBaseId", bpmConfBase.getId());
    	List<BpmConfNode> confNodes = bpmConfNodeService.query(param);
    	
    	if (CollectionUtils.isEmpty(confNodes)) {
    		bpmConfNode = null;
        } else {
        	bpmConfNode = confNodes.get(0);
        }
    	
        if (bpmConfNode == null) {
            bpmConfNode = new BpmConfNode();
            bpmConfNode.setCode(node.getId());
            bpmConfNode.setName(node.getName());
            bpmConfNode.setType(node.getType());
            bpmConfNode.setPriority(priority);
            if (isUserTask) {
            	UserTask userTask = (UserTask) bpmnModel.getFlowElement(node.getId());
            	bpmConfNode.setDueDate(userTask.getDueDate());
            }
            bpmConfNode.setConfBaseId(bpmConfBase.getId());
            bpmConfNodeService.save(bpmConfNode);
            
            //handle bpm_conf_node_send
            for (int i = 0; i < 4; i++) {
            	 BpmConfNodeSend confNodeSend = new BpmConfNodeSend();
            	 confNodeSend.setSendType(i+1);
            	 confNodeSend.setSendMailFlag(1);
            	 confNodeSend.setIntervalDay(3);
            	 confNodeSend.setConfTemplateId(i+1);
            	 confNodeSend.setConfNodeId(bpmConfNode.getId());
            	 bpmConfNodeSendService.save(confNodeSend);
            }
        }
    }
	
}
