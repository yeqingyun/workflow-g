/**
 * @(#) WorkspaceController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.gionee.gnif.constant.TreeConstant;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.Category;
import com.gionee.gniflow.biz.model.ProcessVariableChangeLog;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.CategoryService;
import com.gionee.gniflow.biz.service.ProcessVariableChangeLogService;
import com.gionee.gniflow.dto.ProcessDefinitionDto;
import com.gionee.gniflow.web.cmd.HandleProcessCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;

/**
 * The class <code>WorkspaceController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/promanager")
@Controller
public class ProcessManagerController {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProcessManagerController.class);

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	CommonsMultipartResolver multipartResolver;

	@Autowired
	ProcessEngineFactoryBean processEngineFactoryBean;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private ProcessVariableChangeLogService processVariableChangeLogService;

	
	/** 查看流程分类 */
    @RequestMapping("/processCategory.json")
    @ResponseBody
    public List<Category> viewProcessCategory(QueryMap queryMap) {
    	List<Category> categorys = categoryService.findAll(queryMap);
    	if (categorys != null) {
    		for (Category category : categorys) {
    			List<Category> results = categoryService.query4Pid(category.getId());
    			if (results == null || results.size() <= 0) {
    				category.setState(TreeConstant.STATE_OPEN);
    			}
    		}
    	}
        return categorys;
    }
    
    /** 保存流程分类 */
    @RequestMapping(value="/saveCategory", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveCategory(Category category) {
    	if (category.getPid() == null) {
    		category.setPid(0L);
    	}
    	AjaxJson ajaxJson = null;
    	try {
    		if (category.getId() != null) {
    			categoryService.modify(category);
    		} else {
    			categoryService.add(category);
    		}
    		ajaxJson = new AjaxJson(true, "保存流程分类成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存流程分类失败!");
		}
    	return  ajaxJson;
    }
    
    /** 删除流程分类 */
    @RequestMapping(value="/deleteCategory.json", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson deleteCategory(@RequestParam("id") Long id) {
    	AjaxJson ajaxJson = null;
    	try {
    		categoryService.deleteCatetory(id);
    		ajaxJson = new AjaxJson(true, "删除流程分类成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除流程分类失败!");
		}
    	return  ajaxJson;
    }
    
    /** 显示流程定义列表 */
    @RequestMapping("/processDefinitions-list.json")
    @ResponseBody
    public GridPageData<ProcessDefinitionDto> listProcessDefinitions(QueryMap queryMap) {
        List<ProcessDefinitionDto> processDefinitions = activitiHelpService.page4ProcessDefinition4Page(queryMap);
        
		Integer total = activitiHelpService.count4ProcessDefinition(queryMap);
		
		return new GridPageData<ProcessDefinitionDto>(processDefinitions, (long)total);
    }
    
    /** 显示流程定义的xml*/
    @RequestMapping("/viewProcessXml")
    public void viewProcessXml( @RequestParam("processDefinitionId") String processDefinitionId,
            HttpServletResponse response) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        String resourceName = processDefinition.getResourceName();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(), resourceName);
        response.setContentType("text/xml;charset=UTF-8");
        IOUtils.copy(resourceAsStream, response.getOutputStream());
    }
    
    /** 更新流程定义的分类*/
    @RequestMapping("/updateProCategory.json")
    @ResponseBody
    public AjaxJson viewProcessDefInfo(@RequestParam("id") String processDefId,
    		@RequestParam("category") Integer categoryId) {
    	AjaxJson ajaxJson = new AjaxJson();
    	try {
    		activitiHelpService.updateProCategory(processDefId, categoryId);
    		ajaxJson.setSuccess(true);
    		ajaxJson.setMsg("更新分类成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.setSuccess(true);
    		ajaxJson.setMsg("更新分类失败!");
		}
    	return ajaxJson;
    }
    
    @RequestMapping(value = "/deployPorcess.json")
    @ResponseBody
    public AjaxJson deploy(@RequestParam(value = "file", required = false) MultipartFile file) {
    	AjaxJson ajaxJson = new AjaxJson();
		String fileName = file.getOriginalFilename();

		try {
			InputStream fileInputStream = file.getInputStream();

			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				Deployment deployment = repositoryService.createDeployment().name(fileName).addZipInputStream(zip)
					.enableDuplicateFiltering().deploy();
				
				
				for (ProcessDefinition processDefinition : repositoryService
                        .createProcessDefinitionQuery()
                        .deploymentId(deployment.getId()).list()) {
					
					 managementService.executeCommand(
							 new HandleProcessCmd(processDefinition.getId()));
                }
			} else if (extension.equals("png")) {
				repositoryService.createDeployment().addInputStream(fileName, fileInputStream)
				.enableDuplicateFiltering().deploy();
			} else if (fileName.indexOf("bpmn20.xml") != -1) {
				repositoryService.createDeployment().addInputStream(fileName, fileInputStream)
					.enableDuplicateFiltering().deploy();
			} else if (extension.equals("bpmn")) {
				String baseName = FilenameUtils.getBaseName(fileName);
				repositoryService.createDeployment().addInputStream(baseName + ".bpmn20.xml",fileInputStream)
					.enableDuplicateFiltering().deploy();
			} else {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("部署流程失败!");
				throw new ActivitiException("no support file type of " + extension);
			}
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("部署流程成功!");
		} catch (Exception e) {
			logger.error("error on deploy process, because of file input stream", e);
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("部署流程失败!");
		}

      return ajaxJson;
    }
    /** 保存流程分类 */
    @RequestMapping(value="/updateProcessVar", method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson updateProcessVar(ProcessVariableChangeLog processVariableChangeLog) {
    	AjaxJson ajaxJson = null;
    	try {
    		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processVariableChangeLog.getProcInstId()).singleResult();
    		if(historicProcessInstance != null){
    			List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processVariableChangeLog.getProcInstId()).list();
    			boolean ifExistVar = false;
    			for(int i=0;i<historicVariableInstances.size();i++){
    				HistoricVariableInstance historicVariableInstance = historicVariableInstances.get(i);
    				if(historicVariableInstance.getVariableName().equals(processVariableChangeLog.getName())){
    						ifExistVar = true;
    						String value = historicVariableInstance.getValue() == null ?"":historicVariableInstance.getValue().toString();
    						processVariableChangeLog.setText2(value);
    						break;
    				}
    			}
    			if(ifExistVar){
    				String processDefinitionId = historyService.createHistoricProcessInstanceQuery().processInstanceId(processVariableChangeLog.getProcInstId()).singleResult().getProcessDefinitionId();
    				ProcessDefinition  processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    				if( processDefinition == null){
    					
    				}else{
    					processVariableChangeLog.setProcInstKey(processDefinition.getKey());
    					processVariableChangeLog.setProcInstName(processDefinition.getName());
    				}
    				processVariableChangeLogService.save(processVariableChangeLog);
    			}else{
    				ajaxJson = new AjaxJson(false, "请输入正确的流程变量名!");
    			}
    			ajaxJson = new AjaxJson(true, "流程变量变更成功!");
    		}else{
    			ajaxJson = new AjaxJson(false, "请输入正确的流程编号!");
    		}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "流程变量变更失败!");
		}
    	return  ajaxJson;
    }
    
    /** 显示流程变量修改日志列表 */
    @RequestMapping("/processVarLogs.json")
    @ResponseBody
    public PageResult<ProcessVariableChangeLog> processVarLogs(QueryMap queryMap) {
        PageResult<ProcessVariableChangeLog> processVariableChangeLog = processVariableChangeLogService.queryPage(queryMap);
		return processVariableChangeLog;
    }
    /** 显示流程变量日志 */
    @RequestMapping("/viewProcessVariableChangeLog.json")
    @ResponseBody
    public ProcessVariableChangeLog viewProcessVariableChangeLog(Integer id){
    	ProcessVariableChangeLog processVariableChangeLog = processVariableChangeLogService.getProcessVariableChangeLog(id);
    	return processVariableChangeLog;
    }
}
