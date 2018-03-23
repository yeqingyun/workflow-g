/**
 * @(#) GnifStartProcessInstanceCmd.java Created on 2014年12月25日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.cmd;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * The class <code>GnifStartProcessInstanceCmd</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class GnifStartProcessInstanceCmd<T> implements Command<ProcessInstance>, Serializable {
	private static final long serialVersionUID = 7540021008004855480L;
	
	protected String processDefinitionKey;
	protected String processDefinitionId;
	protected Map<String, Object> variables;
	protected String businessKey;
	protected String tenantId;
	protected String processInstId;

	public GnifStartProcessInstanceCmd(String processInstId, Map<String, Object> variables) {
		this.processInstId = processInstId;
		this.variables = variables;
	}

	public ProcessInstance execute(CommandContext commandContext) {
		//
		DeploymentManager deploymentCache = Context
				.getProcessEngineConfiguration().getDeploymentManager();
		
		//查询已经启动过的流程
		ExecutionEntity curProcessInstance = (ExecutionEntity) Context
					.getProcessEngineConfiguration().getRuntimeService()
					.createProcessInstanceQuery().processInstanceId(processInstId)
					.singleResult();
		
		processDefinitionId = curProcessInstance.getProcessDefinitionId();
		
		// Find the process definition
		ProcessDefinitionEntity processDefinition = null;
		if (processDefinitionId != null) {
			processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
			if (processDefinition == null) {
				throw new ActivitiObjectNotFoundException("No process definition found for id = '" + processDefinitionId + "'", ProcessDefinition.class);
			}
		} else if (processDefinitionKey != null
				&& (tenantId == null || ProcessEngineConfiguration.NO_TENANT_ID.equals(tenantId))) {
			processDefinition = deploymentCache.findDeployedLatestProcessDefinitionByKey(processDefinitionKey);
			if (processDefinition == null) {
				throw new ActivitiObjectNotFoundException(
						"No process definition found for key '" + processDefinitionKey + "'", ProcessDefinition.class);
			}
		} else if (processDefinitionKey != null && tenantId != null
				&& !ProcessEngineConfiguration.NO_TENANT_ID.equals(tenantId)) {
			processDefinition = deploymentCache.findDeployedLatestProcessDefinitionByKeyAndTenantId(
							processDefinitionKey, tenantId);
			if (processDefinition == null) {
				throw new ActivitiObjectNotFoundException(
						"No process definition found for key '" + processDefinitionKey + "' for tenant identifier " + tenantId,
						ProcessDefinition.class);
			}
		} else {
			throw new ActivitiIllegalArgumentException(
					"processDefinitionKey and processDefinitionId are null");
		}

		// Do not start process a process instance if the process definition is
		// suspended
		if (processDefinition.isSuspended()) {
			throw new ActivitiException(
					"Cannot start process instance. Process definition "
							+ processDefinition.getName() + " (id = "
							+ processDefinition.getId() + ") is suspended");
		}
		
		//调用修改源码后的方法，让其初始化ProcessInstance--提交修改表单后再次从StartEvent开始向后流向
		ExecutionEntity processInstance = processDefinition.createProcessInstance4Modify(curProcessInstance);

		if (variables != null) {
			processInstance.setVariables(variables);
		}
		processInstance.start();

		return processInstance;
	}
}
