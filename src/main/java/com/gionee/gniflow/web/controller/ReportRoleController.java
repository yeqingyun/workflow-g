/**
 * @(#) BpmRoleController.java Created on 2014年5月28日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.auth.UserService;
import com.gionee.auth.model.User;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.web.util.ResponseFactory;
import com.gionee.gniflow.biz.model.BpmRole;
import com.gionee.gniflow.biz.model.BpmUserRole;
import com.gionee.gniflow.biz.model.ReportRole;
import com.gionee.gniflow.biz.model.ReportRoleProcdef;
import com.gionee.gniflow.biz.model.ReportUserRole;
import com.gionee.gniflow.biz.service.BpmRoleService;
import com.gionee.gniflow.biz.service.BpmUserRoleService;
import com.gionee.gniflow.biz.service.ReportRoleProcdefService;
import com.gionee.gniflow.biz.service.ReportRoleService;
import com.gionee.gniflow.biz.service.ReportUserRoleService;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.response.ReportRoleResponse;
import com.gionee.gniflow.web.response.UserResponse;

/**
 * The class <code>BpmRoleController</code>
 * 
 * @author lipw
 * @version 1.0
 */
@RequestMapping("/reportRole")
@Controller
public class ReportRoleController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportRoleController.class);
	
	@Autowired
	private ReportRoleService reportRoleService;
	
	@Autowired
	private BpmUserRoleService bpmUserRoleService;
	
	@Autowired
	private ReportUserRoleService reportUserRoleService;

	@Autowired
	private ReportRoleProcdefService reportRoleProcdefService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BpmRoleService bpmRoleService;
	
	/** 加载流程角色*/
    @RequestMapping("/loadAllReportRole.json")
    @ResponseBody
    public List<ReportRoleResponse> loadAllDictionary(QueryMap queryMap){
    	PageResult<ReportRole> pageResult = reportRoleService.queryPage(queryMap);
    	
    	QueryMap newQueryMap = new QueryMap();
    	if (!CollectionUtils.isEmpty(pageResult.getRows())) {
    		for (ReportRole reportRole : pageResult.getRows()) {
    			newQueryMap.getMap().clear();
    			newQueryMap.put("roleId", reportRole.getId());
    			List<ReportRoleProcdef> result = reportRoleProcdefService.query(newQueryMap);
    			reportRole.setRelationDetail(handleRelationDetail(result));
    		}
    	}
    	return ResponseFactory.convertList(pageResult.getRows(), ReportRoleResponse.class);
    }
    /**
     * 获取BPM_ROLE_PROCDEF
     * @param bpmRoleProcDefList
     * @return
     */
    private String handleRelationDetail(List<ReportRoleProcdef> reportRoleProcDefList){
    	StringBuffer sb = new StringBuffer();
    	for (ReportRoleProcdef def : reportRoleProcDefList) {
    		sb.append(def.getProcdefKey()).append(",");
    	}
    	if (sb.length() > 0) {
    		sb = sb.delete(sb.length() - 1, sb.length());
    	}
    	
    	return sb.toString();
    }
    
    
    /** 保存流程角色 及 角色-资源关系表 */
    @RequestMapping(value="/saveReportRole.html")
    @ResponseBody
    public AjaxJson saveDictionary(ReportRole reportRole, @RequestParam("roleResources") String roleResources){
    	AjaxJson ajaxJson = null;
    	try {
    		reportRoleService.save(reportRole, roleResources);
    		ajaxJson = new AjaxJson(true, "保存流程角色成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存流程角色失败!");
		}
    	return ajaxJson;
    }
    
    /** 删除流程角色 */
    @RequestMapping(value="/deleteReportRole.json")
    @ResponseBody
    public AjaxJson deleteCategory(@RequestParam("id") int id) {
    	AjaxJson ajaxJson = null;
    	try {
    		reportRoleService.delete(id);
    		ajaxJson = new AjaxJson(true, "删除流程角色成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "删除流程角色失败!");
		}
    	return  ajaxJson;
    }
    
    //保存人员分配
    @RequestMapping("/saveUserRole.json")
	@ResponseBody
	public AjaxJson saveUserRole(@RequestParam("userIds") String userIds, @RequestParam("roleIds") String roleIds) {
    	AjaxJson ajaxJson = null;
    	try {
    		if (StringUtils.isNotEmpty(userIds) && StringUtils.isNotEmpty(roleIds)) {
    			String[] userIdsArr = userIds.split(",");
    			String[] roleIdsArr = roleIds.split(",");
    			
    			ReportUserRole userRole = null;
    			//先删除之前的关系
    			for (String userId : userIdsArr) {
    				reportUserRoleService.delete(Integer.parseInt(userId));
    			}
    			
    			for (String userId : userIdsArr) {
    				for (String roleId : roleIdsArr) {
    					userRole = new ReportUserRole(Integer.parseInt(userId), Integer.parseInt(roleId));
    					reportUserRoleService.save(userRole);
    				}
    			}
    		}
    		ajaxJson = new AjaxJson(true, "保存数据成功!");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = new AjaxJson(false, "保存数据失败!");
		}
    	return  ajaxJson;
	}
    //查询用户组
    @RequestMapping("/users.json")
	@ResponseBody
	public List<UserResponse> users(Integer id, @RequestParam(value="userAccount",required=false) String userAccount,
			@RequestParam(value="userName",required=false) String userName) {
		List<com.gionee.gniflow.web.response.UserResponse> ret = new ArrayList<UserResponse>();
		List<User> users = null;
		if (id != null) {
			users = userService.getUsersByOrgId(id);
		} else {
			QueryMap queryMap = new QueryMap();
			if (StringUtils.isNotEmpty(userAccount)) {
				queryMap.put("account", userAccount);
			}
			if (StringUtils.isNotEmpty(userName)) {
				queryMap.put("nameLike", "%" + userName + "%");
//				queryMap.put("name", userName);
			}
			users = userService.queryUsers(queryMap.getMap());
		}
		
		
		for (User user:users) {
			UserResponse userResponse = new UserResponse(user);
			userResponse.setRoleEntities(bpmRoleService.getReportRoles(user.getId()));
			ret.add(userResponse);
		}
		return ret;
	}
}
