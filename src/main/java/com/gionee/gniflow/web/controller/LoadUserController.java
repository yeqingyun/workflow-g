package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.auth.OrganizationService;
import com.gionee.auth.UserService;
import com.gionee.auth.model.User;
import com.gionee.devtool.web.response.auth.OrganizationResponse;
import com.gionee.devtool.web.util.DevResponseFactory;
import com.gionee.gniflow.biz.service.BpmRoleService;
import com.gionee.gniflow.web.response.UserResponse;
@Controller
@RequestMapping("/loaduser")
public class LoadUserController {
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BpmRoleService bpmRoleService;
	
	    @RequestMapping("/users.json")
		@ResponseBody
		public List<UserResponse> users() {
			List<com.gionee.gniflow.web.response.UserResponse> ret = new ArrayList<UserResponse>();
			List<User> users = null;
				users = userService.getUsersByOrgId(57707);
				List<User> users2 = null;
				users2 = userService.getUsersByOrgId(74583);
			for (User user:users) {
				UserResponse userResponse = new UserResponse(user);
				userResponse.setRoleEntities(bpmRoleService.getBpmRoles(user.getId()));
				ret.add(userResponse);
			}
			for (User user:users2) {
				UserResponse userResponse = new UserResponse(user);
				userResponse.setRoleEntities(bpmRoleService.getBpmRoles(user.getId()));
				ret.add(userResponse);
			}
			return ret;
		}
	    
	    
	    
	    @RequestMapping("/loadDepa.json")
		@ResponseBody
		public List<OrganizationResponse> loadDepa(Integer id) {
		     if(id==null){
		    	 return DevResponseFactory.convertOrganization(organizationService.getOrganization(57681).getSubOrganizations());
		     }
			return DevResponseFactory.convertOrganization(organizationService.getOrganization(id).getSubOrganizations());
		}
}
