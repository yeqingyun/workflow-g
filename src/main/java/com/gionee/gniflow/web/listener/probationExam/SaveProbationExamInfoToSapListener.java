package com.gionee.gniflow.web.listener.probationExam;

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

public class SaveProbationExamInfoToSapListener implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6213974793612989395L;

	private Logger logger = LoggerFactory.getLogger(SaveProbationExamInfoToSapListener.class);
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		logger.debug("Running in SapParameterPassLister begin!");
		RuntimeService runtimeService = execution.getEngineServices().getRuntimeService();
		//同意转正日期
		String agreeProbationTime = (String)runtimeService.getVariable(execution.getId(), "wageManagerAgreeTime");
		/*String finalMonth="";
		finalMonth = agreeProbationTime;
		String tempstr=finalMonth;
		if(finalMonth!=null&&!"".equals(finalMonth.trim())){
			finalMonth=tempstr.substring(0, 4)+tempstr.substring(5, 7)+"01";
			}*/
		//转正人员编号finalMonth
		String applyUserId = (String)runtimeService.getVariable(execution.getId(), "applyUserAccount");
		//操作原因
		String operationReason=(String)runtimeService.getVariable(execution.getId(), "operationReason");
		//将结果集存入map
		Map<String,String> map = new HashMap<String,String>();
			map.put("P_PERNR", applyUserId);//转正人员编号
			map.put("P_DATE", agreeProbationTime);//转正日期
			map.put("P_MASSG", operationReason);//操作原因
			map.put("P_PERSG","A");
			
			try {
				SapUtil sap=new SapUtil();
				String message=sap.writeToSapReturnStatusAndMessage("ZHR_OA09", map);//ZHR_OA09为写入转正信息接口名
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
		
		logger.debug("Running in SapParameterPassLister end!");
	}
	

}
