package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.auth.UserService;
import com.gionee.gnif.constant.TreeConstant;
import com.gionee.gnif.dto.TreeDto;
import com.gionee.gnif.model.AppUser;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.BpmTaskExecution;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.BpmUserRoleService;
import com.gionee.gniflow.biz.service.CategoryService;
import com.gionee.gniflow.biz.service.ReportRoleProcdefService;
import com.gionee.gniflow.biz.service.ReportUserRoleService;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.web.easyui.AjaxJson;

@Controller
public class ReportController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ReportUserRoleService reportUserRoleService;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReportRoleProcdefService reportRoleProcdefService;
	
	@Autowired
	private TaskService taskService;
	
	@RequestMapping("/menusReport.json")
	@ResponseBody
	public List<TreeDto> getMenu() {
		List<TreeDto> trees = categoryService.getTree(0L, true);
		if (trees != null && trees.size() > 0) {
			for (int i=trees.size()-1; i>=0; i--) {
				if (resolveMenu(trees.get(i))) {
					trees.remove(i);
				}
			}
		}
		return trees;
	}
	
	private boolean resolveMenu(TreeDto tree) {
		List<TreeDto> trees = tree.getChildren();
		
		List<ProcessDefinition> processList = activitiHelpService.queryLastVersionAndCategoryBySort(tree.getId().toString());
		//找出用户角色所具有的流程定义的KEY
		List<String> userProDefKeys = reportUserRoleService.getUserProcessDefKey(AppContext.getCurrentAppUser().getUserId());
		//找出用户权限中所包含的流程
		List<ProcessDefinition> list = new ArrayList<ProcessDefinition>();
		for (ProcessDefinition processDefinition : processList) {
			if (userProDefKeys.contains(processDefinition.getKey())) {
				list.add(processDefinition);
			}
		}
		
		if ((trees == null || trees.size() == 0) && list.size() == 0) {
			return true; // tell parent to remove it.
		}
		
		boolean needRemove = true;
		if (trees != null && trees.size() > 0) {
			for (int i=trees.size()-1; i>=0; i--) {
				if (resolveMenu(trees.get(i))) {
					trees.remove(i);
				}
				else {
					needRemove = false;
				}
			}
		}
		if (list.size() > 0) {
			for (ProcessDefinition process:list) {
				TreeDto processNode = new TreeDto();
				processNode.setText(process.getName());
				processNode.setState(TreeConstant.STATE_OPEN);
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("processDefId", process.getId());
				attributes.put("processDefKey", process.getKey());
				processNode.setAttributes(attributes);
				tree.getChildren().add(processNode);
			}
			needRemove = false;
		}
		return needRemove;
	}
	
	/** 转办任务确认 */
	@RequestMapping("/turntodoTask/{taskId}.html")
	@ResponseBody
	public AjaxJson turntodoTask(@PathVariable("taskId") String taskId, @RequestParam("accepter") String accepter,
			@RequestParam("turntodoReason") String turntodoReason) {
		AjaxJson ajaxJson = null;
		try {
			AppUser curUser = AppContext.getCurrentAppUser();
			TaskEntity taskEntity = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
			if (taskEntity == null) {
				return new AjaxJson(false, "任务已经被处理，转办任务失败!");
			}
			String assignee = taskEntity.getAssignee();
			if ((!StringUtils.isEmpty(assignee)) && (assignee.equals(accepter))) {
				return new AjaxJson(false, "不能转办给任务执行人，转办任务失败!");
			}
			
			BpmTaskExecution taskExe = new BpmTaskExecution();
			taskExe.setTaskId(taskEntity.getId());
			taskExe.setAssignee(accepter);
			String accepterName = userService.getUserByAccount(accepter).getName();
			taskExe.setAssigneeName(accepterName);
			taskExe.setOwner(taskEntity.getAssignee());
			taskExe.setOwnerName(curUser.getName());
			taskExe.setTaskName(taskEntity.getName());
			taskExe.setTaskDefKey(taskEntity.getTaskDefinitionKey());
			taskExe.setProcInstId(taskEntity.getProcessInstanceId());
			taskExe.setSuggestion(turntodoReason);
			taskExe.setAssignType(BPMConstant.TASK_ASSIGN_TYPE_TODO);
			
			activitiHelpService.updateTaskAssignee(taskExe);
			
			ajaxJson = new AjaxJson(true, "转办任务成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "转办任务失败!");
		}
		return ajaxJson;
	}
	
}
