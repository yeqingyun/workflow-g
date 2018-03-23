/**
 * @(#) DynamicFormController.java Created on 2014年4月9日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gionee.gniflow.web.easyui.AjaxJson;


/**
 * The class <code>DynamicFormController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/dynamic")
public class DynamicFormController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private FormService formService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RuntimeService runtimeService;

	/**
	 * 流程列表
	 */
	@RequestMapping("/index.html")
	public void flows(Model model) {
		List<ProcessDefinition> list = repositoryService
				.createProcessDefinitionQuery().list();
		model.addAttribute("flows", list);
	}

	// 读取开始节点的表单
	@SuppressWarnings("unchecked")
	@RequestMapping("/start/{procdefId}.json")
	@ResponseBody
	public Object startJson(
			@PathVariable("procdefId") String processDefinitionId) {
		// StartFormData startFormData =
		// formService.getStartFormData(processDefinitionId);
		// return startFormData.getFormProperties();

		Map<String, Object> result = new HashMap<String, Object>();
		StartFormDataImpl startFormData = (StartFormDataImpl) formService.getStartFormData(processDefinitionId);
		startFormData.setProcessDefinition(null);

		// 读取enum类型数据，用于下拉框
		List<FormProperty> formProperties = startFormData.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
			if (values != null) {
				for (Entry<String, String> enumEntry : values.entrySet()) {
					logger.debug("enum, key: {}, value: {}",enumEntry.getKey(), enumEntry.getValue());
				}
				result.put("enum_" + formProperty.getId(), values);
			}
		}

		result.put("form", startFormData);

		return result;
	}

	/**
	 * 启动流程
	 * 
	 * @param processDefinitionId
	 * @param request
	 * @param ra
	 * @return
	 */
	@RequestMapping("/startprocess/{procdefId}.json")
	@ResponseBody
	public AjaxJson createProcess(@PathVariable("procdefId") String processDefinitionId,
			HttpServletRequest request, RedirectAttributes ra) {
		Map<String, String> formProperties = new HashMap<String, String>();
		AjaxJson aj = null;
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();

				if (key.startsWith("fp_")) {
					formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
				}
			}

			formService.submitStartFormData(processDefinitionId, formProperties);
			aj = new AjaxJson(true, "流程启动成功!");
		} catch (Exception e) {
			aj = new AjaxJson(false, "流程启动失败!");
		}
		return aj;
	}

	@RequestMapping("/tasks.html")
	public void tasks(Model model) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		taskQuery.orderByTaskId().desc();
		List<Task> tasks = taskQuery.list();
		model.addAttribute("tasks", tasks);
	}

	/**
	 * 读取Task的表单
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getform/task/{taskId}.json")
	@ResponseBody
	public Map<String, Object> findTaskForm(@PathVariable("taskId") String taskId) throws Exception {

		// taskService.claim(taskId, "1234");

		Map<String, Object> result = new HashMap<String, Object>();
		TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);

		// 设置task为null，否则输出json的时候会报错
		taskFormData.setTask(null);

		result.put("taskFormData", taskFormData);
		/*
		 * 读取enum类型数据，用于下拉框
		 */
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
			if (values != null) {
				for (Entry<String, String> enumEntry : values.entrySet()) {
					logger.debug("enum, key: {}, value: {}",enumEntry.getKey(), enumEntry.getValue());
				}
				result.put(formProperty.getId(), values);
			}
		}

		return result;
	}

	/**
	 * 提交task的并保存form
	 */
	@RequestMapping(value = "task/complete/{taskId}")
	@ResponseBody
	public AjaxJson completeTask(@PathVariable("taskId") String taskId,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Map<String, String> formProperties = new HashMap<String, String>();

		AjaxJson aj = null;
		try {
			// 从request中读取参数然后转换
			Map<String, String[]> parameterMap = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				String key = entry.getKey();

				// fp_的意思是form paremeter
				if (StringUtils.defaultString(key).startsWith("fp_")) {
					formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
				}
			}

			logger.debug("start form parameters: {}", formProperties);

			formService.submitTaskFormData(taskId, formProperties);

			aj = new AjaxJson(true, "任务完成!");
		} catch (Exception e) {
			aj = new AjaxJson(false, "流程启动失败!");
		}
		return aj;
	}
	
	  /**
	   * 已结束的流程实例
	   */
	  @RequestMapping(value = "/finished.html")
	  public void finished(Model model, HttpServletRequest request) {
	    List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("test-process").finished().list();
	    List<HistoricProcessInstance> list2 = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("dispatch").finished().list();
	    list.addAll(list2);
	    model.addAttribute("tasks", list);
	  }
}
