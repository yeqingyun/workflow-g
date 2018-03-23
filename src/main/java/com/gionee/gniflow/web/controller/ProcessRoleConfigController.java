/**
 * @(#) WorkspaceController.java Created on 2014年5月28日
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.auth.UserService;
import com.gionee.auth.model.User;
import com.gionee.gnif.biz.domain.OrganizationManager;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.dto.TreeDto;
import com.gionee.gniflow.biz.model.BpmConfProcessRole;
import com.gionee.gniflow.biz.service.BpmConfProcessRoleService;
import com.gionee.gniflow.dto.TaskDefinitionDto;
import com.gionee.gniflow.web.cmd.FindGraphCmd;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.easyui.GridPageData;
import com.gionee.gniflow.web.graph.Graph;
import com.gionee.gniflow.web.graph.Node;
import com.gionee.gniflow.web.response.BpmConfProcessRoleResponse;

/**
 * The class <code>WorkspaceController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/processRole")
@Controller
public class ProcessRoleConfigController {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProcessManagerController.class);
	
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private BpmConfProcessRoleService bpmConfProcessRoleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationManager organizationManager;

	
	@RequestMapping("/allUser.json")
	@ResponseBody
	public List<ComboBoxData> getAllUserJosn(QueryMap queryMap) {
		
		List<ComboBoxData> dataList = new ArrayList<ComboBoxData>();
		
		List<User> users = userService.queryUsers(queryMap.getMap());
		for (User user : users) {
			dataList.add(new ComboBoxData(user.getAccount(), "(" + user.getAccount() + ")"+ user.getName()));
		}
		return dataList;
	}
	
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
			 if ("userTask".equals(node.getType())) {
				 taskDefDto = new TaskDefinitionDto();
				 taskDefDto.setTaskDefKey(node.getId());
				 taskDefDto.setTaskName(node.getName());
				 if (node.getTaskDefinition() != null) {
					 if (node.getTaskDefinition().getAssigneeExpression() != null) {
						 taskDefDto.setAssigneeExpression(node.getTaskDefinition().getAssigneeExpression().getExpressionText());
					 }
				 }
				 taskDefDto.setProcessDefKey(processDefKey);
				 
				 String method = getMethod(taskDefDto.getAssigneeExpression());
				 if (StringUtils.isNotEmpty(method) && method.startsWith("getSpecial")) {
					 taskDefDtos.add(taskDefDto);
				 }
			 }
		 }
		
        return new GridPageData<TaskDefinitionDto>(taskDefDtos, (long)taskDefDtos.size());
    }
    
    /** 一次性加载OrgTree */
    @RequestMapping("/showAllOrgTree.json")
    @ResponseBody
    public List<TreeDto> showAllOrgTree() {
    	List<TreeDto> treeList = organizationManager.getSubOrganizationTree(0L, true);
        return treeList;
    }
    
    
    /** 保存getSpecialRole */
    @RequestMapping("/saveGetSpecialRoleMaster.json")
    @ResponseBody
    public AjaxJson saveGetSpecialRoleMaster(@RequestParam(value="id", required=false)Integer id, String processDefKey, String roleName, 
    		String assignee, String method) {
    	BpmConfProcessRole proRole = new BpmConfProcessRole();
    	proRole.setProcessDefKey(processDefKey);
    	proRole.setRoleName(roleName);
    	proRole.setAssignee(assignee);
    	proRole.setMethod(method);
    	try {
    		if (id == null) {
    			bpmConfProcessRoleService.save(proRole);
    		} else {
    			proRole.setId(id);
    			bpmConfProcessRoleService.update(proRole);
    		}
    		return new AjaxJson(true, "操作成功！");
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new AjaxJson(false, "操作失败！");
		}
    }
    
    /** 保存getSpecialAreaRole */
    @RequestMapping("/saveGetSpecialAreaRoleMaster.json")
    @ResponseBody
    public AjaxJson saveGetSpecialAreaRoleMaster(@RequestParam(value="id", required=false)Integer id,String processDefKey, String roleName, 
    		String assignee, String area, String method) {
    	BpmConfProcessRole proRole = new BpmConfProcessRole();
    	proRole.setProcessDefKey(processDefKey);
    	proRole.setRoleName(roleName);
    	proRole.setAssignee(assignee);
    	if (StringUtils.isNotEmpty(area)) {
    		proRole.setArea("," + area + ",");
    	}
    	proRole.setMethod(method);
    	try {
    		if (id == null) {
    			bpmConfProcessRoleService.save(proRole);
    		} else {
    			proRole.setId(id);
    			bpmConfProcessRoleService.update(proRole);
    		}	
    		
    		return new AjaxJson(true, "操作成功！");
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new AjaxJson(false, "操作失败！");
		}
    }
    
    /** 保存saveGetSpecialDeptMaster */
    @RequestMapping("/saveGetSpecialDeptMaster.json")
    @ResponseBody
    public AjaxJson saveGetSpecialDeptMaster(@RequestParam(value="id", required=false)Integer id, String processDefKey, String roleName, 
    		String assignee, String details, String method) {
    	BpmConfProcessRole proRole = new BpmConfProcessRole();
    	proRole.setProcessDefKey(processDefKey);
    	proRole.setRoleName(roleName);
    	proRole.setAssignee(assignee);
    	proRole.setDetails("," + details + ",");
    	proRole.setMethod(method);
    	try {
    		if (id == null) {
    			bpmConfProcessRoleService.save(proRole);
    		} else {
    			proRole.setId(id);
    			bpmConfProcessRoleService.update(proRole);
    		}
    		return new AjaxJson(true, "操作成功！");
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new AjaxJson(false, "操作失败！");
		}
    }
    
    /** 保存saveGetSpecialAreaDeptMaster */
    @RequestMapping("/saveGetSpecialAreaDeptMaster.json")
    @ResponseBody
    public AjaxJson saveGetSpecialAreaDeptMaster(@RequestParam(value="id", required=false)Integer id, String processDefKey, String roleName, 
    		String assignee, String details, String area, String method) {
    	BpmConfProcessRole proRole = new BpmConfProcessRole();
    	proRole.setProcessDefKey(processDefKey);
    	proRole.setRoleName(roleName);
    	proRole.setAssignee(assignee);
    	if (StringUtils.isNotEmpty(details)) {
    		proRole.setDetails("," + details + ",");
    	}
    	if (StringUtils.isNotEmpty(area)) {
    		proRole.setArea("," + area + ",");
    	}
    	proRole.setMethod(method);
    	try {
    		if (id == null) {
    			bpmConfProcessRoleService.save(proRole);
    		} else {
    			proRole.setId(id);
    			bpmConfProcessRoleService.update(proRole);
    		}
    		return new AjaxJson(true, "操作成功！");
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new AjaxJson(false, "操作失败！");
		}
    }
    
    /** 保存saveGetSpecialAreaFloorMaster */
    @RequestMapping("/saveGetSpecialAreaFloorMaster.json")
    @ResponseBody
    public AjaxJson saveGetSpecialAreaFloorMaster(@RequestParam(value="id", required=false)Integer id,String processDefKey, 
    		String roleName, String assignee, String details, String area, String method) {
    	BpmConfProcessRole proRole = new BpmConfProcessRole();
    	proRole.setProcessDefKey(processDefKey);
    	proRole.setRoleName(roleName);
    	proRole.setAssignee(assignee);
    	if (StringUtils.isNotEmpty(details)) {
    		proRole.setDetails("," + details + ",");
    	}
    	if (StringUtils.isNotEmpty(area)) {
    		proRole.setArea("," + area + ",");
    	}
    	proRole.setMethod(method);
    	try {
    		if (id == null) {
    			bpmConfProcessRoleService.save(proRole);
    		} else {
    			proRole.setId(id);
    			bpmConfProcessRoleService.update(proRole);
    		}
    		return new AjaxJson(true, "操作成功！");
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new AjaxJson(false, "操作失败！");
		}
    }
    
    
    /** 显示BpmConfProceessRole的配置 */
    @RequestMapping("/bpmConfProceessRole.json")
    @ResponseBody
    public GridPageData<BpmConfProcessRoleResponse> bpmConfProceessRole(String processDefKey, String roleName, String method) {
    	QueryMap param = new QueryMap();
    	List<BpmConfProcessRoleResponse> results = new ArrayList<BpmConfProcessRoleResponse>();
    	try {
    		if (StringUtils.isNotEmpty(processDefKey) && StringUtils.isNotEmpty(roleName)){
    			param.put("processDefKey", processDefKey);
        		param.put("roleName", roleName);
        		param.put("method", method);
        		List<BpmConfProcessRole> list = bpmConfProcessRoleService.query(param);
        		for (BpmConfProcessRole role : list) {
        			results.add(new BpmConfProcessRoleResponse(role));
        		}
    		}
    		
    		return new GridPageData<BpmConfProcessRoleResponse>(results, (long)results.size());
    		
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
    	return null;
    }
    
    /** 删除BpmConfProcessRole */
    @RequestMapping("/deleteProRole.json")
    @ResponseBody
    public AjaxJson deleteProRole(Integer id) {
    	try {
    		bpmConfProcessRoleService.delete(id);
    		
    		return new AjaxJson(true, "操作成功！");
    		
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return new AjaxJson(false, "操作失败！");
		}
    }
    
    /** 判断数据库是否存在 */
    @SuppressWarnings("unused")
	private boolean isExist(String processDefKey, String roleName){
    	QueryMap param = new QueryMap();
		param.put("processDefKey", processDefKey);
		param.put("roleName", roleName);
		BpmConfProcessRole bpmConfProRole = bpmConfProcessRoleService.querySingle(param);
		if (bpmConfProRole == null){
			return false;
		}
		return true;
    }
    /**
     * 获取表达式的方法名
     * 形如 ：${bpmConfProcessRoleService.getSpecialRoleMaster("考勤专员")}
     * @param getExpressionText
     * @return
     */
    private String getMethod(String assigneeExpression){
    	try {
    		int pointIndex = assigneeExpression.indexOf(".");
        	int leftBracketsIndex = assigneeExpression.indexOf("(");
        	return assigneeExpression.substring(pointIndex + 1, leftBracketsIndex);
		} catch (Exception e) {
			return null;
		}
    }
    
}
