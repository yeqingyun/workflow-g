package com.gionee.gniflow.web.listener.MLeaveApplication;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.GnifException;
import com.gionee.gniflow.sap.SapUtil;
import com.gionee.gniflow.web.support.AudiException;


public class InsertLeaveAppInfoToSapLister implements ExecutionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4554160919252582345L;
	private Logger logger = LoggerFactory.getLogger(InsertLeaveAppInfoToSapLister.class);
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SapParameterPassLister begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		//同意离职日期
		String agreeLeaveTime = (String)runtimeService.getVariable(execution.getId(), "leaveDate");
		//申请人
		String applyUserId = (String)runtimeService.getVariable(execution.getId(), "applyUserAccount");
		//操作类型
		String optionType=(String)runtimeService.getVariable(execution.getId(), "oprationTypevalue");//操作类型
		//操作原因
		String operationReason  = (String)runtimeService.getVariable(execution.getId(), "operationReasonvalue");
		
		//将结果集存入map
		Map<String,String> map = new HashMap<String,String>();
		if("A6".equals(optionType)){
			map.put("P_PERNR", applyUserId);
			map.put("P_DATE", agreeLeaveTime);
			map.put("P_MASSG", operationReason);
			
			try {
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA13", map);//ZHR_OA13为写入离职接口名
				String message1 = message.substring(0, 1);
				String message2 = message.substring(1,message.length());
				if(!("0".equals(message1))){
					//System.out.println("SAP保存数据反馈信息："+message2);
					throw new AudiException("SAP保存数据反馈信息："+message2);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				if(e instanceof AudiException)
					throw e;
				throw new GnifException("Failure of SaveInfoTo with SAP!");
			}
		}
		logger.debug("Running in SapParameterPassLister end!");
	}
	

}
