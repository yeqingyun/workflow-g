package com.gionee.gniflow.web.servicetask.filesign;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * 
 * The class <code>SendTimeOutEmailServiceTask</code>
 * 远程调用的服务类
 * @author lipw
 * @version 1.0
 */
public class SendTimeOutEmailServiceTask implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(SendTimeOutEmailServiceTask.class);
	
	private TaskService taskService;
	
	private ProcessHelpService processHelpService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		logger.debug("[SendTimeOutEmailServiceTask]--> Send Time out Email Begin! ");
		
		processHelpService = (ProcessHelpService) SpringTools.getBean(ProcessHelpService.class);
		taskService = execution.getEngineServices().getTaskService();
		
		String processInstanceId = execution.getProcessInstanceId();
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.taskDefinitionKey(BPMConstant.FILE_SIGN_TASK_KEY).list();
		
		List<String> userAccounts = new ArrayList<String>();
		
		if (!CollectionUtils.isEmpty(tasks)) {
			for (Task task : tasks) {
				userAccounts.add(task.getAssignee());
			}
		}
		
//		processHelpService.sendEmail4UserAccount(userAccounts, "会签任务即将超时提醒！", "文档会签任务即将过时，请及时处理。");
		
		logger.debug("[SendTimeOutEmailServiceTask]--> Send Time out Email End!");
		
	}

}
