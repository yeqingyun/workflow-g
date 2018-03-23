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

public class InsertLeaveReasonToSapListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4193215679386820970L;
	private Logger logger = LoggerFactory.getLogger(InsertLeaveReasonToSapListener.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("Running in SapParameterPassLister begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		//同意离职日期
		String agreeLeaveTime = (String)runtimeService.getVariable(execution.getId(), "leaveDate");
		//申请人
		String applyUserId = (String)runtimeService.getVariable(execution.getId(), "applyUserAccount");
		//操作类型
		String optionType=(String)runtimeService.getVariable(execution.getId(), "oprationTypevalue");//操作类型
		//离职原因
		String leaveReason = (String)runtimeService.getVariable(execution.getId(), "leaveReson");
		Map<String,String> map = new HashMap<String,String>();
		if("A6".equals(optionType)){
			map=null;
			String funName="ZGNHR07_003";
			String tableName="IT_PERNR";
			Map<String,String> tableMap = new HashMap<String,String>();
			tableMap.put("PERNR", applyUserId);//人员编号
			tableMap.put("BEGDA", agreeLeaveTime);//离职日期
			tableMap.put("ZTEXT", leaveReason);//离职原因
			//tableMap.put("MESSAGE", "");
			try {
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapWithInputTable(funName, map,tableName,tableMap);//ZGNHR07_003为写入离职原因接口名
				//System.out.println("SAP保存数据反馈信息："+message);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new GnifException("Failure of SaveInfoTo with SAP!");
			}
		logger.debug("Running in SapParameterPassLister end!");
		}
	}
}
