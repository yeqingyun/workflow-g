package com.gionee.gniflow.sap.quartz;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.biz.model.QuartzJobLog;
import com.gionee.gniflow.biz.model.SapRequisitionSheet;
import com.gionee.gniflow.biz.service.QuartzJobLogService;
import com.gionee.gniflow.biz.service.SapRequisitionSheetService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.sap.rfc.RfcSapReqSheet;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * The class <code>FetchMatlBomJob</code>
 * @author lipw
 * @version 1.0
 */
public class FetchSapReqSheetJob implements Serializable{

	private static final long serialVersionUID = -927313789059127496L;

	private static final Logger logger = LoggerFactory.getLogger(FetchSapReqSheetJob.class);
	
	private SapRequisitionSheetService sapRequisitionSheetService;
	
	private QuartzJobLogService quartzJobLogService;
	
	public void fetchSapReqSheetJob() {
		logger.info("-----------FetchSapReqSheetJob task starting-----------" 
				+ DateUtil.formatDate(new Date(), WebReqConstant.DATE_FORMAT_DEFAULT));
		
		Date startDate = new Date();
		long time = System.currentTimeMillis();
		
		try{
			if (sapRequisitionSheetService == null) {
				sapRequisitionSheetService = (SapRequisitionSheetService) SpringTools.getBean(SapRequisitionSheetService.class);
			}
			if (quartzJobLogService == null) {
				quartzJobLogService = (QuartzJobLogService) SpringTools.getBean(QuartzJobLogService.class);
			}
			RfcSapReqSheet rfcSapReqSheet = new RfcSapReqSheet();
			
			List<SapRequisitionSheet> sapReqSheets4Req = rfcSapReqSheet.getSapReqSheets4ReqDate();
			
			List<SapRequisitionSheet> sapReqSheets4Change = rfcSapReqSheet.getSapReqSheets4ReqDate();
			
			//合并两个List去掉preqNo，preqItem重复的数据
			List<SapRequisitionSheet> resultList = rfcSapReqSheet.removeDuplicateItem(sapReqSheets4Req, sapReqSheets4Change);
			
			//保存数据
			SapRequisitionSheet curSapReqSheet = null;
			for (SapRequisitionSheet saoReqSheet : resultList) {
				//判断当前库中的单据是否存在状态为待修改的数据，如果存在更新状态为待审核，并将流程实例置空
				curSapReqSheet = sapRequisitionSheetService.getSapRequisitionSheet(saoReqSheet.getPreqNo(), saoReqSheet.getPreqItem());
				if (curSapReqSheet != null && curSapReqSheet.getStatus() == WebReqConstant.PROCURE_SHEET_TOBEMODEFY) {
					saoReqSheet.setStatus(WebReqConstant.PROCURE_SHEET_TOBEAUDI);
					saoReqSheet.setProcInstId(null);
					
					sapRequisitionSheetService.update(saoReqSheet);
				} else {
					sapRequisitionSheetService.save(saoReqSheet);
				}
			}
			
			Date endDate = new Date();
			time = System.currentTimeMillis() - time;
			QuartzJobLog quartzLog = new QuartzJobLog();
			quartzLog.setStartetime(startDate);
			quartzLog.setEndtime(endDate);
			quartzLog.setTaskname("FetchSapReqSheetJob");
			quartzLog.setIntervaltime((int)time/1000/60);
			quartzJobLogService.save(quartzLog);
			
			logger.info("-----------FetchSapReqSheetJob task end-----------" + DateUtil.formatDate(new Date(), WebReqConstant.DATE_FORMAT_DEFAULT));
		} catch(Exception e) {
			e.printStackTrace();
			logger.info("FetchSapReqSheetJob task Exception:" + DateUtil.formatDate(new Date(), WebReqConstant.DATE_FORMAT_DEFAULT));
		}
	}
}
