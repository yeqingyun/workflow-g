package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.constant.TreeConstant;
import com.gionee.gnif.dto.TreeDto;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.BpmUserRoleService;
import com.gionee.gniflow.biz.service.CategoryService;

@Controller
public class IndexController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private BpmUserRoleService bpmUserRoleService;
	
	@RequestMapping("/menus.json")
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
//		List<ProcessDefinition> list = repositoryService
//				.createProcessDefinitionQuery()
//				.processDefinitionCategory(tree.getId().toString()).latestVersion().list();
		
		List<ProcessDefinition> processList = activitiHelpService.queryLastVersionAndCategoryBySort(tree.getId().toString());
		//找出用户角色所具有的流程定义的KEY
		List<String> userProDefKeys = bpmUserRoleService.getUserProcessDefKey(AppContext.getCurrentAppUser().getUserId());
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
	
	@RequestMapping("/eoaMenus.json")
	@ResponseBody
	public List<TreeDto> eoaMenus(HttpServletRequest request) {
		int userId = -1;
		if(request.getParameter("userId") != null && !request.getParameter("userId").trim().equals("")) {
			try {
				userId = Integer.parseInt(request.getParameter("userId").trim());
			} catch(Exception e) {
				userId = -1;
			}
		}
		
		List<TreeDto> trees = categoryService.getTree(0L, true);
		if (trees != null && trees.size() > 0) {
			for (int i=trees.size()-1; i>=0; i--) {
				if (resolveMenuUser(trees.get(i),userId)) {
					trees.remove(i);
				}
			}
		}
		return trees;
	}
	
	private boolean resolveMenuUser(TreeDto tree,int userId) {
		List<TreeDto> trees = tree.getChildren();
		List<ProcessDefinition> processList = activitiHelpService.queryLastVersionAndCategoryBySort(tree.getId().toString());
		//找出用户角色所具有的流程定义的KEY
		List<String> userProDefKeys = bpmUserRoleService.getUserProcessDefKey(userId);
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
				if (resolveMenuUser(trees.get(i),userId)) {
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
	
}
