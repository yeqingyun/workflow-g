package com.gionee.gniflow.web.servicetask.filesign;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.gionee.auth.OrganizationService;
import com.gionee.auth.UserService;
import com.gionee.auth.model.Organization;
import com.gionee.auth.model.User;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.constant.WebReqConstant;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.SpringTools;
/**
 * 
 * The class <code>SendTimeOutEmailServiceTask</code>
 * 任务超时，发送邮件通知
 * @author lipw
 * @version 1.0
 */
public class AutoCompleteServiceTask implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoCompleteServiceTask.class);
	
	private TaskService taskService;
	
	private UserService userService;
	
	private OrganizationService organizationService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		logger.debug("[AutoCompleteServiceTask]-->Auto complete Task Begin！");
		
		String processDefinitionId = execution.getProcessInstanceId();
		
		taskService = execution.getEngineServices().getTaskService();
		
		userService = (UserService) SpringTools.getBean(UserService.class);
		
		organizationService = (OrganizationService) SpringTools.getBean(OrganizationService.class);
		
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processDefinitionId)
				.taskDefinitionKey(BPMConstant.FILE_SIGN_TASK_KEY).list();
		if (!CollectionUtils.isEmpty(tasks)) {
			Map<String, Object> variables = new HashMap<String, Object>();
			Organization org = null;
			for (Task task : tasks) {
				variables.clear();
				String userAccount = task.getAssignee();
				User user = userService.getUserByAccount(userAccount);
				if (user != null) {
					org = organizationService.getOrganization(user.getOrgId());
				}
				if (org != null) {
					variables.put(userAccount+"SignAudiOrgName", org.getName());
				} else {
					variables.put(userAccount+"SignAudiOrgName", "");
				}
				variables.put(userAccount+"relevantdepartmentsSignAudi", "0");
				variables.put(userAccount+"redepSignAudiMan", user.getName());
				variables.put(userAccount+"relevantdepartmentsSignAudiTime", DateUtil.format(new Date(), WebReqConstant.DATE_FORMAT_DEFAULT));
				variables.put(userAccount+"relevantdepartmentsSignAudiAdvice", "任务超时，系统自动同意！");
				taskService.complete(task.getId(), variables);
			}
		}
		
		logger.debug("[AutoCompleteServiceTask]-->自动完成任务结束!");
		
	}

}
