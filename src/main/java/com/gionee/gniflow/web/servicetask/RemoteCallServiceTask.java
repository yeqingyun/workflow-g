package com.gionee.gniflow.web.servicetask;

import java.util.LinkedHashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gionee.gniflow.dto.FlowMessageDto;
import com.gionee.gniflow.web.support.AudiException;
import com.gionee.gniflow.web.util.HttpClientUtil;
import com.gionee.gniflow.web.util.PropertyHolder;
/**
 * 
 * The class <code>RemoteCallServiceTask</code>
 * 远程调用的服务类
 * @author lipw
 * @version 1.0
 */
public class RemoteCallServiceTask implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(RemoteCallServiceTask.class);
	// 参数信息
	private static final String PMS_CALL_BACK = "pms.callback.url";
	private static final String PROJECTID = "projectId";
	private static final String CHARGECONTENT = "changeContent";
	private static final String PROJECT_MANAGER_ACCOUNT = "projectManagerAccount";
	
	private RuntimeService runtimeService;
	private String executionId;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		//传递参数表单
		Map<String,String> reqData = new LinkedHashMap<String, String>();
		
		runtimeService = execution.getEngineServices().getRuntimeService();
		executionId = execution.getId();
		//获取流程中的Java 服务任务中的定义的变量
		//增加变量流程变量
		reqData.put("processInstanceId", execution.getId());
		reqData.put("project_id", getVariable(PROJECTID));//项目ID
		reqData.put("description", getVariable(CHARGECONTENT));//需求描述
		reqData.put("project_principal", getVariable(PROJECT_MANAGER_ACCOUNT));//项目负责人的账号
		reqData.put("status", "7");
		//测试,得到结果
		try {
			String result =  HttpClientUtil.sendPostRequest(PropertyHolder.getContextProperty(PMS_CALL_BACK), reqData, "UTF-8");
			logger.info("[RemoteCallServiceTask]------->" + result);
			if(!result.contains(":")){
				throw new AudiException("PMS服务器连接失败");
			} else {
				FlowMessageDto md = new ObjectMapper().readValue(result, FlowMessageDto.class);
				if(!md.isSuccess()) {
					// 设置错误信息给流程变量
					//execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), "errorMessage", "");
					throw new AudiException("PMS服务器创建任务失败");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			// 设置错误信息给流程变量
			//execution.getEngineServices().getRuntimeService().setVariable(execution.getId(), "errorMessage", "PMS服务器连接失败");
			throw e;
		}
		
	}
	
	/**
	 * 获取变量值
	 * @param key
	 * @return
	 */
	private String getVariable(String key){
		return (String) runtimeService.getVariable(executionId, key);
	}

}
