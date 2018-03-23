/**
 * @(#) SendTasktimeoutNoticeCmd.java Created on 2014年11月19日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.cmd;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.gionee.gniflow.web.notice.TaskTimeoutNotice;

/**
 * The class <code>SendTasktimeoutNoticeCmd</code>
 *
 * @author lipw
 * @version 1.0
 */
public class SendTaskTimeoutNoticeCmd implements Command<Void> {
	
	private String taskId;
	
	public SendTaskTimeoutNoticeCmd(String taskId){
		this.taskId = taskId;
	}
	
	@Override
	public Void execute(CommandContext commandContext) {
		 TaskEntity delegateTask = commandContext.getTaskEntityManager()
	                .findTaskById(taskId);
	        new TaskTimeoutNotice().process(delegateTask);
		return null;
	}

}
