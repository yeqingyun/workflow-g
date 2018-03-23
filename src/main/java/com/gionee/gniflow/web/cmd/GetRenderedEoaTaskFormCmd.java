/**
 * @(#) GetRenderedTaskHistoryPrintFormCmd.java Created on 2017年06月20日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.cmd;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.activiti.spring.SpringProcessEngineConfiguration;

import com.gionee.gniflow.util.FreemarkerFormEngine;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>GetRenderedTaskHistoryPrintFormCmd</code>
 * 获取历史的任务打印表单
 * @author zhangp
 * @version 1.0
 */
public class GetRenderedEoaTaskFormCmd implements Command<Object>, Serializable {
	
	private static final long serialVersionUID = 5039164042361021480L;
	
	protected String taskId;
	protected String formEngineName;
	
	
	public GetRenderedEoaTaskFormCmd(String taskId, String formEngineName) {
		super();
		this.taskId = taskId;
		this.formEngineName = formEngineName;
	}



	@Override
	public Object execute(CommandContext commandContext) {
		FormEngine formEngine = Context
			      .getProcessEngineConfiguration()
			      .getFormEngines()
			      .get(formEngineName);
			    
		if (formEngine == null) {
			throw new ActivitiException("No formEngine '" + formEngineName +"' defined process engine configuration");
		}
		SpringProcessEngineConfiguration engineConfig = (SpringProcessEngineConfiguration) SpringTools.getBean("processEngineConfiguration");
		
		Map<String, FormEngine> engines = engineConfig.getFormEngines();
		 
		FreemarkerFormEngine freeMarkerEngine = (FreemarkerFormEngine) engines.get("freemarker");
	    TaskEntity task = Context.getCommandContext().getTaskEntityManager().findTaskById(taskId);
	    if (task == null) {
	      throw new ActivitiObjectNotFoundException("Task '" + taskId +"' not found", Task.class);
	    }
	    
	    if (task.getTaskDefinition() == null) {
	      throw new ActivitiException("Task form definition for '" + taskId +"' not found");
	    }
	    TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
	    if (taskFormHandler == null) {
	      return null;
	    }
	    TaskFormData taskForm = taskFormHandler.createTaskForm(task);
		return freeMarkerEngine.renderEoaTaskForm(taskForm);
	}

}
