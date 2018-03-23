/**
 * @(#) WorkspaceController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.auth.OrganizationService;
import com.gionee.auth.UserService;
import com.gionee.auth.model.Organization;
import com.gionee.auth.model.User;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.dto.TreeDto;
import com.gionee.gniflow.biz.service.CategoryService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.response.OrganizationResponse;
import com.gionee.gniflow.web.util.DevResponseFactory;

/**
 * The class <code>WorkspaceController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/flowService")
@Controller
public class FlowServiceController {

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;

	/** 获取组织机构和人员 */
	@RequestMapping("/orgTree.json")
	@ResponseBody
	public List<OrganizationResponse> getOrgTree(Integer id) {
		if (id == null) {
			OrganizationResponse rootOrg = new OrganizationResponse();
			rootOrg.setChildren(DevResponseFactory.convertOrganization(organizationService.getRoots()));
			List<OrganizationResponse> ret = new ArrayList<OrganizationResponse>();
			ret.add(rootOrg);
			return ret;
		}
		else if (id == 0) {
			return DevResponseFactory.convertOrganization(organizationService.getRoots());
		}
		else {
			List<Organization> orgs = organizationService.getOrganization(id).getSubOrganizations();
			List<User> users = userService.getUsersByOrgId(id);
			return DevResponseFactory.convertOrganization(orgs, users);
		}
	}
	
	/** 获取用户名 */
	@RequestMapping("/showUserName.json")
	@ResponseBody
	public AjaxJson showUserName(@RequestParam("account")String account) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("account", account);
		List<User> users = userService.queryUsers(params);
		if (users != null && users.size() > 0) {
			return new AjaxJson(true, users.get(0).getName());
		}
		return new AjaxJson(false, "请求数据失败!");
	}
	
	/** 从PMS获取任务完成结果 */
	@RequestMapping("/receiveProjectReuslt.json")
	@ResponseBody
	public AjaxJson receiveReuslt4PMS(@RequestParam("processInstanceId") String processInstanceId, 
			@RequestParam("feedBack") String feedBack){
		AjaxJson ajaxJson = new AjaxJson();
		Map<String, String> formProperties = new HashMap<String, String>();
		formProperties.put("processInstanceId", processInstanceId);
		formProperties.put("feedBack", feedBack);
		
		Execution execution = runtimeService.createExecutionQuery()
				.processInstanceId(processInstanceId).singleResult();
		
		// 如果该流程已经被中止，则返回相应的信息
		if(execution == null) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("流程已经中止，不需要再进行办理");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("flag", "suspend");
			ajaxJson.setAttributes(map);
		} else {
			runtimeService.setVariable(execution.getId(), "feedBack", feedBack);
			try {
				runtimeService.signal(execution.getId());
				ajaxJson.setSuccess(true);
				ajaxJson.setMsg("任务处理成功 !");
			} catch (Exception e) {
				e.printStackTrace();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("任务处理失败 !");
			}
		}
		return ajaxJson;
	}
	
	/** 获取流程分类树 */
    @RequestMapping("/processCategoryTree.json")
    @ResponseBody
    public List<TreeDto> viewHistory(QueryMap queryMap) {
    	List<TreeDto> categoryTree = categoryService.getTree(null, true);
        return categoryTree;
    }
	
}
