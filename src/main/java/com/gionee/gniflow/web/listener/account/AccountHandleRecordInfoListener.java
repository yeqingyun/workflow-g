/**
 * @(#) AccountHandleSendEmailListener.java Created on 2014年9月1日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.listener.account;

import java.util.Date;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.ProAccountHandleInfo;
import com.gionee.gniflow.biz.service.ProAccountHandleInfoService;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * The class <code>AccountHandleSendEmailListener</code>
 *
 * @author lipw
 * @version 1.0
 */
public class AccountHandleRecordInfoListener implements ExecutionListener {

	private static final long serialVersionUID = -6614282570994646794L;
	
	private Logger logger = LoggerFactory.getLogger(AccountHandleRecordInfoListener.class);

	private RuntimeService reuntimeService;
	
	private ProAccountHandleInfoService proAccountHandleInfoService;
	
	private HistoryService historyService;
	
	private String executionId;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("-----Running in AccountHandleSendEmailListener begin!-----");
		
		initService(execution);
		executionId = execution.getId();
		
		HistoricProcessInstance hisProInst = historyService.createHistoricProcessInstanceQuery()
			.processInstanceId(execution.getProcessInstanceId()).singleResult();
		
		//记录办理户口申请记录-begin
		ProAccountHandleInfo accountInfo = new ProAccountHandleInfo();
		accountInfo.setStartUserAccount(getVariable("applyUserId"));
		accountInfo.setStartUserName(getVariable("userName"));
		accountInfo.setProcessInstId(execution.getProcessInstanceId());
		accountInfo.setProcessDefKey(execution.getProcessDefinitionId());
		accountInfo.setProcessName("金立户口办理流程");
		accountInfo.setStatus(ProAccountHandleInfo.STATUS_HANDLED);
		if (hisProInst != null) {
			accountInfo.setStartTime(hisProInst.getStartTime());
		}
		accountInfo.setEndTime(new Date());
		proAccountHandleInfoService.save(accountInfo);
		//记录办理户口申请记录-end
		
		logger.debug("-----Running in AccountHandleSendEmailListener end!-----");
	}
	
	/**
	 * 初始化服务
	 * @param execution
	 */
	private void initService(DelegateExecution execution){
		proAccountHandleInfoService = (ProAccountHandleInfoService) SpringTools.getBean(ProAccountHandleInfoService.class);
		reuntimeService = execution.getEngineServices().getRuntimeService();
		historyService = execution.getEngineServices().getHistoryService();
	}
	
	/**
	 * 获取变量值
	 * @param key
	 * @return
	 */
	private String getVariable(String key){
		return (String) reuntimeService.getVariable(executionId, key);
	}
}
