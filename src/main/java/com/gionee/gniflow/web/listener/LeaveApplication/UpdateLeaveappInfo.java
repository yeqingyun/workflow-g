package com.gionee.gniflow.web.listener.LeaveApplication;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.QuitApplicationInfo;
import com.gionee.gniflow.biz.service.QuitApplicationInfoService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

public class UpdateLeaveappInfo implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4212888782230415006L;
	private Logger logger = LoggerFactory.getLogger(UpdateLeaveappInfo.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in UpdateLeaveappInfo begin!");
		QuitApplicationInfoService sfService = (QuitApplicationInfoService) SpringTools
				.getBean(QuitApplicationInfoService.class);
		List<QuitApplicationInfo> list = null;
		String processInstanceId = execution.getProcessInstanceId();
		QueryMap queryMap = new QueryMap();
		queryMap.put("processid", processInstanceId);
		list = sfService.query(queryMap);
		if (!CollectionUtils.isEmpty(list)) {
			QuitApplicationInfo quitInfo = new QuitApplicationInfo();
			quitInfo = list.get(0);
			quitInfo.setState(WebReqConstant.SIGN_FILE_PRO_AUDIED);
			sfService.save(quitInfo);
		}

		logger.debug("Running in UpdateLeaveappInfo end!");
	}

}
