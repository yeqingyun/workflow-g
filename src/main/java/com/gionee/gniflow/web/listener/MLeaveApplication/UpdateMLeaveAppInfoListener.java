package com.gionee.gniflow.web.listener.MLeaveApplication;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;
import com.gionee.gniflow.biz.service.MLeaveApplicationInfoService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateMLeaveAppInfoListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8944614024603751528L;
	private Logger logger = (Logger)LoggerFactory.getLogger(UpdateMLeaveAppInfoListener.class);
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateLeaveAppInfo begin!");
		 
		MLeaveApplicationInfoService leaveInfoService = (MLeaveApplicationInfoService)SpringTools.getBean(MLeaveApplicationInfoService.class);
		List<MLeaveApplicationInfo> list = null;
		String proInstId =  execution.getProcessInstanceId();
		QueryMap queryMap = new QueryMap();
		queryMap.put("proInstId", proInstId);
		list = leaveInfoService.query(queryMap);
		if (!CollectionUtils.isEmpty(list)) {
			MLeaveApplicationInfo leaveInfo = new MLeaveApplicationInfo();
			leaveInfo = list.get(0);
			leaveInfo.setProcessState(WebReqConstant.SIGN_FILE_PRO_AUDIED);
			leaveInfoService.save(leaveInfo);
		}
		
		logger.debug("Running in UpdateLeaveAppInfo end!");
		
	}

}
