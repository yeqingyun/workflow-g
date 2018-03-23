package com.gionee.gniflow.web.listener.MLeaveApplication;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.service.MLeaveApplicationInfoService;
import com.gionee.gniflow.web.util.SpringTools;

public class DeleteMLeaveAppInfoListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2683257684630435334L;

	private Logger logger = (Logger)LoggerFactory.getLogger(DeleteMLeaveAppInfoListener.class);
	@Override
	public void notify(DelegateTask delegateTask) {
		logger.debug("Running in DeleteLeaveAppInfo begin!");
		
		MLeaveApplicationInfoService leaveInfoService = (MLeaveApplicationInfoService)SpringTools.getBean(MLeaveApplicationInfoService.class);
		
		String proInstId = delegateTask.getExecution().getProcessInstanceId();
		
		leaveInfoService.delete(proInstId);
		
		logger.debug("Running in DeleteLeaveAppInfo end!");
		
	}

}
