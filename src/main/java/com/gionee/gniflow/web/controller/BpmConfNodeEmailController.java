/**
 * @(#) BpmConfNodeSendController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.BpmConfNodeEmail;
import com.gionee.gniflow.biz.service.BpmConfNodeEmailService;
import com.gionee.gniflow.biz.service.BpmConfProcessRoleService;
import com.gionee.gniflow.dto.TaskDefinitionDto;
import com.gionee.gniflow.web.cmd.FindGraphCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.graph.Graph;
import com.gionee.gniflow.web.graph.Node;
import com.gionee.gniflow.web.response.BpmConfNodeEmailResponse;

/**
 * The class <code>BpmConfNodeSendController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/bpmConfNodeEmail")
@Controller
public class BpmConfNodeEmailController {
	
	private static final Logger logger = LoggerFactory.getLogger(BpmConfNodeEmailController.class);
	
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private BpmConfProcessRoleService bpmConfProcessRoleService;
	
	@Autowired
	private BpmConfNodeEmailService bpmConfNodeEmailService;
	
	/** 查看流程的所有节点 */
    @RequestMapping("/showTaskNodes.json")
    @ResponseBody
    public GridPageData<TaskDefinitionDto> showTaskNodes(@RequestParam(value="processDefinitionId",required=false) String processDefinitionId) {
    	if (StringUtils.isEmpty(processDefinitionId)) {
    		return new GridPageData<TaskDefinitionDto>(new ArrayList<TaskDefinitionDto>(), 0L);
    	}
    	
    	String processDefKey = null;
    	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
    			.processDefinitionId(processDefinitionId).singleResult();
    	if (processDefinition != null) {
    		processDefKey = processDefinition.getKey();
    	}
    	
    	FindGraphCmd findGraphCmd = new FindGraphCmd(processDefinitionId);
		Graph graph = managementService.executeCommand(findGraphCmd);
        
		List<TaskDefinitionDto> taskDefDtos = new ArrayList<TaskDefinitionDto>();
		TaskDefinitionDto taskDefDto = null;
		 for (Node node : graph.getNodes()) {
			taskDefDto = new TaskDefinitionDto();
			taskDefDto.setTaskDefKey(node.getId());
			taskDefDto.setTaskName(node.getName());
			taskDefDto.setProcessDefKey(processDefKey);
			taskDefDto.setTaskType(node.getType());
			taskDefDtos.add(taskDefDto);
		 }
		
        return new GridPageData<TaskDefinitionDto>(taskDefDtos, (long)taskDefDtos.size());
    }
    
    /** 显示BpmConfNodeEmail的配置 */
    @RequestMapping("/loadBpmConfNodeEmail.json")
    @ResponseBody
    public GridPageData<BpmConfNodeEmailResponse> bpmConfProceessRole(QueryMap queryMap) {
    	List<BpmConfNodeEmailResponse> results = new ArrayList<BpmConfNodeEmailResponse>();
    	try {
    		List<BpmConfNodeEmail> list = bpmConfNodeEmailService.query(queryMap);
    		for (BpmConfNodeEmail confNodeEmail : list) {
    			results.add(new BpmConfNodeEmailResponse(confNodeEmail));
    		}
    		return new GridPageData<BpmConfNodeEmailResponse>(results, (long)results.size());
    		
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
    	return null;
    }
    
    @RequestMapping(value="/saveBpmConfNodeEmail.json")
    @ResponseBody
    public AjaxJson saveBpmConfNodeEmail(BpmConfNodeEmail bpmConfNodeEmail) {
    	AjaxJson ajaxJson = null;
    	try {
    		bpmConfNodeEmailService.save(bpmConfNodeEmail);
    		ajaxJson = new AjaxJson(true, "操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "操作失败!");
		}
    	return  ajaxJson;
    }
    
    @RequestMapping(value="/deleteBpmConfNodeEmail.json")
    @ResponseBody
    public AjaxJson deleteBpmConfNodeEmail(@RequestParam("id") Integer id) {
    	AjaxJson ajaxJson = null;
    	try {
    		bpmConfNodeEmailService.delete(id);
    		ajaxJson = new AjaxJson(true, "删除记录成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除记录失败!");
		}
    	return  ajaxJson;
    }
    
}
