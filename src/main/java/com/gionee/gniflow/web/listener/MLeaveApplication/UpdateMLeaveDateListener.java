package com.gionee.gniflow.web.listener.MLeaveApplication;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;
import com.gionee.gniflow.biz.service.MLeaveApplicationInfoService;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateMLeaveDateListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6044547518956164192L;
	
	Logger logger = (Logger)LoggerFactory.getLogger(UpdateMLeaveDateListener.class);
	@Override
	public void notify(DelegateTask delegateTask) {
		logger.debug("Running in UpdateLeaveAppInfo begin!");
		
		MLeaveApplicationInfoService leaveInfoService = (MLeaveApplicationInfoService)SpringTools.getBean(MLeaveApplicationInfoService.class);
		RuntimeService runtimeService =delegateTask.getExecution().getEngineServices().getRuntimeService();
		List<MLeaveApplicationInfo> list = null;
		String proInstId =  delegateTask.getProcessInstanceId();
		QueryMap queryMap = new QueryMap();
		queryMap.put("proInstId", proInstId);
		list = leaveInfoService.query(queryMap);
		if (!CollectionUtils.isEmpty(list)) {
			MLeaveApplicationInfo leaveInfo = new MLeaveApplicationInfo();
			leaveInfo = list.get(0);
			String leaveDate = (String)runtimeService.getVariable(delegateTask.getExecutionId(), "leaveDate");
			leaveInfo.setLeaveDate(DateUtil.parse(leaveDate, "yyyy-MM-dd"));
			leaveInfoService.save(leaveInfo);
		}
		
		logger.debug("Running in UpdateLeaveAppInfo end!");
		
	}
	
}
