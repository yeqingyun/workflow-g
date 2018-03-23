package com.gionee.gniflow.web.listener.LeaveApplication;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.QuitApplicationInfo;
import com.gionee.gniflow.biz.service.QuitApplicationInfoService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class InsertLeaveAppInfoLister implements ExecutionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8871027786340018518L;
	private Logger logger = LoggerFactory.getLogger(InsertLeaveAppInfoLister.class);
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in InsertLeaveAppInfoLister begin!");
		QuitApplicationInfoService sfService= (QuitApplicationInfoService) SpringTools.getBean(QuitApplicationInfoService.class);
		QuitApplicationInfo quitInfo=new QuitApplicationInfo();
		RuntimeService runtimeService= execution.getEngineServices().getRuntimeService();	
		quitInfo.setAccount((String)runtimeService.getVariable(execution.getId(), "userAccount"));
		quitInfo.setApplicationDate(DateUtil.parse((String)runtimeService.getVariable(execution.getId(), "applicationDate"), "yyyy-MM-dd"));
		quitInfo.setPlanLeaveDate(DateUtil.parse((String)runtimeService.getVariable(execution.getId(), "planLeaveDate"),"yyyy-MM-dd"));
		quitInfo.setCreateBy((String)runtimeService.getVariable(execution.getId(), "userName"));
		quitInfo.setCreateTime(new Date());
		quitInfo.setProcessid((String)execution.getProcessInstanceId());
		quitInfo.setSignPerson((String)runtimeService.getVariable(execution.getId(), "userName"));
		quitInfo.setState(WebReqConstant.SIGN_FILE_PRO_AUDIING);
		quitInfo.setStatus(BPMConstant.STATUS_NORMAL);
		quitInfo.setName((String)runtimeService.getVariable(execution.getId(), "userName"));
		quitInfo.setIsCompanyShares((String)runtimeService.getVariable(execution.getId(), "companyShares"));
		sfService.save(quitInfo);
		logger.debug("Running in InsertLeaveAppInfoLister end!");
	}
}
