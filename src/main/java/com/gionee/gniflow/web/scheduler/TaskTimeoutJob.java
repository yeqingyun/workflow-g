/**
 * @(#) TaskTimeOutJob.java Created on 2014年11月19日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.scheduler;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.gniflow.sap.quartz.FetchSapReqSheetJob;
import com.gionee.gniflow.web.cmd.SendTaskTimeoutNoticeCmd;

/**
 * The class <code>TaskTimeOutJob</code>
 *
 * @author lipw
 * @version 1.0
 */
public class TaskTimeoutJob implements Serializable{

	private static final long serialVersionUID = 4375075808791992395L;
	
	private static final Logger logger = LoggerFactory.getLogger(FetchSapReqSheetJob.class);
	
	@Autowired
	private ProcessEngine processEngine;
	
	
	public void processTaskTimeoutJob(){
		logger.debug("------ Task Timeout Job Begin------");
		
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().list();

        for (Task task : tasks) {
            if (task.getDueDate() != null && StringUtils.isNotEmpty(task.getAssignee())) {
            	SendTaskTimeoutNoticeCmd sendTaskTimeoutNoticeCmd = new SendTaskTimeoutNoticeCmd(task.getId());
                processEngine.getManagementService().executeCommand(
                		sendTaskTimeoutNoticeCmd);
            }
        }
        
        logger.debug("------ Task Timeout Job End------");
	}
}
