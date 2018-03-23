package com.gionee.gniflow.web.listener.LTeamActivitiesDetail;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.LTeamActivitiesDetailService;
import com.gionee.gniflow.web.util.SpringTools;

public class DeleteLTeamActivitiesDetailListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3315346223070525680L;
	
	private Logger logger = LoggerFactory.getLogger(DeleteLTeamActivitiesDetailListener.class);

	@Override
	public void notify(DelegateTask delegateTask) {
		//删除数据库中的数据
		logger.debug("Running in DeleteTeamActivitiesDetailListener begin!");
		LTeamActivitiesDetailService LTeamActivitiesDetailService = (LTeamActivitiesDetailService)SpringTools.getBean(LTeamActivitiesDetailService.class);
		String processInstanceId=delegateTask.getExecution().getProcessInstanceId();
		LTeamActivitiesDetailService.deleteByProInstId(processInstanceId);
		logger.debug("Running in DeleteTeamActivitiesDetailListener End!");
	}

}
