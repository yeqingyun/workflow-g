/**
 * @(#) SapReqSheetAgreeEndListener.java Created on 2014年7月24日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.sap.listener;

import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SapReqSheetAttachment;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.SapReqSheetAttachmentService;
import com.gionee.gniflow.biz.service.SapRequisitionSheetService;
import com.gionee.gniflow.constant.WebReqConstant;
//import com.gionee.gniflow.web.notice.SendEmailThread;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailTool;
import com.gionee.gniflow.web.util.SpringTools;

/**
 * The class <code>SapReqSheetAgreeEndListener</code>
 *
 * @author lipw
 * @version 1.0
 */
public class SapReqSheetAgreeEndListener implements ExecutionListener {
	
	private Logger logger = LoggerFactory.getLogger(SapReqSheetAgreeEndListener.class);
	
	private static final long serialVersionUID = 4471360126718196106L;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SapReqSheetEndListener!");
		SapRequisitionSheetService sapRequisitionSheetService = (SapRequisitionSheetService) SpringTools.getBean(SapRequisitionSheetService.class);
		SapRequisitionSheet sapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(execution.getProcessInstanceId());
		//跟单据的状态为已审核
		sapReqSheet.setStatus(WebReqConstant.PROCURE_SHEET_AUDIED);
		//跟新单据的审批日期
		sapReqSheet.setRelDate(new Date());
		sapRequisitionSheetService.update(sapReqSheet);
		
		//查询流程是不是有附件，有附件则发送邮件
		if(sapReqSheet != null){
			SapReqSheetAttachmentService sapReqSheetAttachmentService = 
					(SapReqSheetAttachmentService) SpringTools.getBean(SapReqSheetAttachmentService.class);
			QueryMap queryMap = new QueryMap();
			queryMap.put("preqNo", sapReqSheet.getPreqNo());
			queryMap.put("preqItem", sapReqSheet.getPreqItem());
			List<SapReqSheetAttachment> attachments = sapReqSheetAttachmentService.getAllSapReqSheetAttachment(queryMap);
			if (!CollectionUtils.isEmpty(attachments)) {
				//获取单据的信息
				String to = "receiver_test@163.com";
				String subject = "采购申请单:" + sapReqSheet.getPreqNo() + "-" + sapReqSheet.getPreqItem() + "已审批完成";
				 String url = PropertyHolder.getContextProperty("email.url");
				String message = "<a href='"+ url +"?title=采购单查询&url=sapspace/procureSearchProcess.html&icon=icon-prosearch'>采购申请单:" + sapReqSheet.getPreqNo() + "-" + sapReqSheet.getPreqItem() + "已审批完成!" 
						+ "请点击链接查看相关附件信息。" + "</a>。"
						+ "此邮件由Gionee工作流平台自动生成，请勿回复此邮件!";
				//发送邮件
				//new SendEmailThread(to, subject, message).start();
				SendMailTool.sendMailByAddress(subject, message, to);
			}
		}
		
	}

}
