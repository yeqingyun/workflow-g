package com.gionee.gniflow.web.listener.TeamActivitiesDetail;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.web.util.SpringTools;

public class DeleteTeamActivitiesDetailListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3315346223070525680L;
	
	private Logger logger = LoggerFactory.getLogger(DeleteTeamActivitiesDetailListener.class);

	@Override
	public void notify(DelegateTask delegateTask) {
		//删除数据库中的数据
		logger.debug("Running in DeleteTeamActivitiesDetailListener begin!");
		TeamActivitiesDetailService sfService = (TeamActivitiesDetailService)SpringTools.getBean(TeamActivitiesDetailService.class);
		String processInstanceId=delegateTask.getExecution().getProcessInstanceId();
		sfService.delete(processInstanceId);
		
		logger.debug("Running in DeleteTeamActivitiesDetailListener End!");
	}

}
