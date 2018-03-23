package com.gionee.gniflow.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/formkey")
@Controller
public class FormKeyController {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping("/index.html")
	public void flows(Model model) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		model.addAttribute("flows", list);
	}
	
	@RequestMapping("/start/{procdefId}.html")
	public ModelAndView start(@PathVariable("procdefId") String processDefinitionId) {
		ModelAndView mav = new ModelAndView("formkey/start");
		mav.addObject("startForm", formService.getRenderedStartForm(processDefinitionId, "freemarker"));
		return mav;
	}
	
	@RequestMapping("/create/{procdefId}.html")
	public String create(@PathVariable("procdefId") String processDefinitionId, HttpServletRequest request, RedirectAttributes ra) {
        Map<String, String> formProperties = new HashMap<String, String>();

        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
        for (Entry<String, String[]> entry : entrySet) {
            String key = entry.getKey();

            if (key.startsWith("fp_")) {
                formProperties.put(key.split("fp_")[1], entry.getValue()[0]);
            }
        }
        
        formService.submitStartFormData(processDefinitionId, formProperties);
        
        ra.addFlashAttribute("message", "流程启动成功！");
        return "redirect:../tasks.html";
	}
	
	@RequestMapping("/task/{taskId}.html")
	@ResponseBody
	public Object task(@PathVariable String taskId) {
		return formService.getRenderedTaskForm(taskId);
	}
	
	public void claim() {
		
	}
	
	@RequestMapping("/complete/{taskId}.html")
	public String complete(@PathVariable("taskId") String taskId, HttpServletRequest request, RedirectAttributes ra) {
        Map<String, String> formProperties = new HashMap<String, String>();

		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			if (key.startsWith("fp_")) {
				String[] paramSplit = key.split("fp_");
				formProperties.put(paramSplit[1], entry.getValue()[0]);
			}
		}

		formService.submitTaskFormData(taskId, formProperties);
		
		ra.addFlashAttribute("message", "任务完成！");
		return "redirect:../tasks.html";
	}
	
	@RequestMapping("/tasks.html")
	public void tasks(Model model) {
		List<Task> tasks = taskService.createTaskQuery().list();
		model.addAttribute("tasks", tasks);
	}
	
	@RequestMapping("/runs.html")
	public void runnings(Model model) {
		List<ProcessInstance> runs = runtimeService.createProcessInstanceQuery().list();
		model.addAttribute("runs", runs);
	}
	
	@RequestMapping("/hist.html")
	public void finishs(Model model) {
		List<HistoricProcessInstance> hist = historyService.createHistoricProcessInstanceQuery().finished().list();
		model.addAttribute("hist", hist);
	}
	
}
