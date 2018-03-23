/**
 * @(#) SapReqSheetNoAgreeEndListener.java Created on 2014年7月24日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.sap.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.SapRequisitionSheetService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>SapReqSheetNoAgreeEndListener</code>
 *
 * @author lipw
 * @version 1.0
 */
public class SapReqSheetProjectNoAgreeListener implements ExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(SapReqSheetProjectNoAgreeListener.class);
	
	private static final long serialVersionUID = 4471360126718196106L;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SapReqSheetNoAgreeEndListener!");
		SapRequisitionSheetService sapRequisitionSheetService = (SapRequisitionSheetService) SpringTools.getBean(SapRequisitionSheetService.class);
		SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(execution.getProcessInstanceId());
		//跟新单据的状态为待修改
		sapReqSheet.setStatus(WebReqConstant.PROCURE_SHEET_TOBEMODEFY);
		sapRequisitionSheetService.update(sapReqSheet);
	}

}
