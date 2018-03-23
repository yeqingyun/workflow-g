/**
 * @(#) GetRenderedBatchTaskFormCmd.java Created on 2014年11月10日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.cmd;

import java.io.Serializable;

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

import com.gionee.gniflow.util.FreemarkerFormEngine;

/**
 * The class <code>GetRenderedBatchTaskFormCmd</code> 获取批量操作时任务表单
 * 
 * @author lipw
 * @version 1.0
 */
public class GetRenderedBatchTaskFormCmd implements Command<Object>,
		Serializable {

	private static final long serialVersionUID = 5039164042361021480L;

	protected String taskId;
	protected String formEngineName;

	public GetRenderedBatchTaskFormCmd(String taskId, String formEngineName) {
		super();
		this.taskId = taskId;
		this.formEngineName = formEngineName;
	}

	@Override
	public Object execute(CommandContext commandContext) {
		TaskEntity task = Context.getCommandContext().getTaskEntityManager()
				.findTaskById(taskId);
		if (task == null) {
			throw new ActivitiObjectNotFoundException("Task '" + taskId
					+ "' not found", Task.class);
		}

		if (task.getTaskDefinition() == null) {
			throw new ActivitiException("Task form definition for '" + taskId
					+ "' not found");
		}

		TaskFormHandler taskFormHandler = task.getTaskDefinition()
				.getTaskFormHandler();
		if (taskFormHandler == null) {
			return null;
		}

		FormEngine formEngine = Context.getProcessEngineConfiguration()
				.getFormEngines().get(formEngineName);

		if (formEngine == null) {
			throw new ActivitiException("No formEngine '" + formEngineName
					+ "' defined process engine configuration");
		}
		
		TaskFormData taskForm = taskFormHandler.createTaskForm(task);
		
		FreemarkerFormEngine freeMarkerEngine = (FreemarkerFormEngine) formEngine;

		return freeMarkerEngine.renderBatchTaskForm(taskForm);
	}

}
