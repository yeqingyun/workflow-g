/**
 * @(#) SapReqSheetGeneralNoAgreeListener.java Created on 2014年7月24日
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
import com.gionee.gniflow.sap.rfc.RfcSapReqRestRelease;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>SapReqSheetGeneralNoAgreeListener</code>
 * 总经理审批不通过，通过监听器取消工作部负责人审批，以便再次审批
 * @author lipw
 * @version 1.0
 */
public class SapReqSheetGeneralNoAgreeListener implements ExecutionListener {
	
	private static final long serialVersionUID = 7790590661500208561L;
	
	private Logger logger = LoggerFactory.getLogger(SapReqSheetGeneralNoAgreeListener.class);
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("[Running in SapReqSheetGeneralNoAgreeListener--Begin]");
		SapRequisitionSheetService sapRequisitionSheetService = (SapRequisitionSheetService) SpringTools.getBean(SapRequisitionSheetService.class);
		SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(execution.getProcessInstanceId());
		//调用SAP的接口，取消审批
		String approvalCode = sapReqSheet.getFrgc1();
		
		RfcSapReqRestRelease restRelease = new RfcSapReqRestRelease();
		try {
			restRelease.sapReqSheetResetAudi(sapReqSheet.getPreqNo(),
							"000" + sapReqSheet.getPreqItem(), approvalCode);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.debug("[Running in SapReqSheetGeneralNoAgreeListener--End]");
	}

}
