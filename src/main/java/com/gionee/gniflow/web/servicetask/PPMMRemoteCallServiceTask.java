package com.gionee.gniflow.web.servicetask;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricTaskInstance;
import org.restlet.engine.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
/**
 * 
 * The class <code>PPMMRemoteCallServiceTask</code>
 * 远程调用的服务类
 * @author lipw
 * @version 1.0
 */
public class PPMMRemoteCallServiceTask implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(PPMMRemoteCallServiceTask.class);
	
	private static final String[] assistArr = new String[]{"leaderAssignee","prototypeAssignee"};
	
	private Expression subSystem;
	
	private Expression flowSystem;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		//传递参数表单
		Map<String,String> reqData = new LinkedHashMap<String, String>();
		
		//读取历史表单数据
		Map<String,String> hisVariable = new LinkedHashMap<String, String>();
		List<HistoricDetail> formProperties = execution.getEngineServices().getHistoryService().createHistoricDetailQuery()
				.processInstanceId(execution.getProcessInstanceId()).formProperties().list();
		for(HistoricDetail historicDetail : formProperties) {
			HistoricFormProperty field = (HistoricFormProperty) historicDetail;
			hisVariable.put(field.getPropertyId(), field.getPropertyValue());
		}
		
		Map<String,Object> runVariables = execution.getEngineServices().getRuntimeService().getVariables(execution.getId());
		for (Map.Entry<String, Object> entry : runVariables.entrySet()) {
			if (entry.getValue() instanceof Date) {
				String value = DateUtils.format((Date)entry.getValue(), "yyyy-MM-dd");
				hisVariable.put(entry.getKey(), value);
			} else {
				hisVariable.put(entry.getKey(), (String)entry.getValue());
			}
		}
		
		logger.debug("hisVariable->" + hisVariable);
		
		//获取流程中的Java 服务任务中的定义的变量
		String[] subSystemArr = ((String)subSystem.getValue(execution)).split(",");
		String[] flowSystemArr = ((String)flowSystem.getValue(execution)).split(",");
		
		for (int i = 1; i < subSystemArr.length; i++) {
			if (hisVariable.get(flowSystemArr[i]) == null) {
				reqData.put(subSystemArr[i], flowSystemArr[i]);
			} else {
				reqData.put(subSystemArr[i], hisVariable.get(flowSystemArr[i]));
			}
		}
		
		//获取任务的处理人
		List<HistoricTaskInstance> historicTasks = execution.getEngineServices().getHistoryService()
	                .createHistoricTaskInstanceQuery()
	                .processInstanceId(execution.getProcessInstanceId()).orderByTaskId().asc().list();
		
		for (int i=0;i<assistArr.length;i++) {
			reqData.put(assistArr[i], historicTasks.get(i).getAssignee());
		}
		
		logger.debug("reqData->" + reqData);
		
		//测试
		logger.debug("notify url---->" +  flowSystemArr[0]);
		HttpClientUtil.sendPostRequest(PropertyHolder.getContextProperty(flowSystemArr[0]), reqData, "UTF-8");
		
		logger.debug("Load the method [PPMMRemoteCallServiceTask]-->notify sub system!!!");
		
	}

}
