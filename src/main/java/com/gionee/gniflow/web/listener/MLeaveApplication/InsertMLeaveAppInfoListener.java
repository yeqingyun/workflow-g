package com.gionee.gniflow.web.listener.MLeaveApplication;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;
import com.gionee.gniflow.biz.service.MLeaveApplicationInfoService;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;

public class InsertMLeaveAppInfoListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7085393107773715988L;
	
	private Logger logger = LoggerFactory.getLogger(InsertMLeaveAppInfoListener.class);
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		logger.debug("Running in InsertMLeaveAppInfoListener  begin!");
		MLeaveApplicationInfoService leavaAppInfoService = (MLeaveApplicationInfoService)SpringTools.getBean(MLeaveApplicationInfoService.class);
		
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		//同意离职日期
		String agreeLeaveTime = (String)runtimeService.getVariable(execution.getId(), "leaveDate");
		//操作原因
		String operationReason  = (String)runtimeService.getVariable(execution.getId(), "operationReasonvalue");
		//离职人员编号
		String leaveAccount = (String)runtimeService.getVariable(execution.getId(), "applyUserAccount");
		//离职人员姓名
		String leaveName =(String)runtimeService.getVariable(execution.getId(), "userName");
		//申请日期
		String applyDate = (String)runtimeService.getVariable(execution.getId(), "applicationDate");
		
		//将监听数据填充到数据库中
		MLeaveApplicationInfo leaveInfo = new MLeaveApplicationInfo();
		
		leaveInfo.setProInstId(execution.getProcessInstanceId());
		leaveInfo.setLeaveAccount(leaveAccount);
		leaveInfo.setLeaveName(leaveName);
		leaveInfo.setLeaveDate(DateUtil.parse(agreeLeaveTime,"yyyy-MM-dd"));
		leaveInfo.setOperationReason(operationReason);
		leaveInfo.setApplyDate(DateUtil.parse(applyDate, "yyyy-MM-dd"));
		leaveInfo.setProcessState(WebReqConstant.SIGN_FILE_PRO_AUDIING);//审批中
		leaveInfo.setCreateBy(AppContext.getCurrentAccount());//获取当前登录用户
		leaveInfo.setCreateTime(new Date());
		
		leavaAppInfoService.save(leaveInfo);//将监听数据填充到数据库中
		
		logger.debug("Running in InsertMLeaveAppInfoListener end!");
	}
	
}
