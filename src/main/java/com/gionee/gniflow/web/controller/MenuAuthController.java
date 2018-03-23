package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.auth.model.Resource;
import com.gionee.gnif.constant.TreeConstant;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.biz.service.ActivitiHelpService;
import com.gionee.gniflow.biz.service.MenuService;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.FlowTreeDto;
import com.gionee.gniflow.dto.MenuDto;

@Controller
@RequestMapping("/menuAuth")
public class MenuAuthController {
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private ManagementService managementService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private ActivitiHelpService activitiHelpService;
	
	@Autowired
	private ProcessHelpService processHelpService;
	
	@RequestMapping("/getRootResources.json")
	@ResponseBody
	public List<MenuDto> getRootResources() {
		MenuDto dto = null;
		List<MenuDto> resultList = new ArrayList<MenuDto>();
		List<Resource> list = menuService.getUserResource4LevelOne();
		for (Resource res : list) {
			dto = new MenuDto();
			BeanUtils.copyProperties(res, dto);
			resultList.add(dto);
		}
		return resultList;
	}
	
	@RequestMapping("/getMenuTree.json")
	@ResponseBody
	public List<FlowTreeDto> getMenuTree(@RequestParam("pMenuId") Integer pMenuId){
		List<Resource> resList = menuService.getUserResource4ParnetMenu(pMenuId);
		//装换为FlowTreeDto
		List<FlowTreeDto> treeList = new ArrayList<FlowTreeDto>();
		for (Resource res : resList) {
			treeList.add(conver2FlowTreeDto(res));
		}
		return treeList;
	}
	
	
	private FlowTreeDto conver2FlowTreeDto(Resource resource){
		FlowTreeDto dto = new FlowTreeDto();
		dto.setId((long)resource.getId());
		dto.setPid((long)resource.getPid());
		dto.setChecked(false);
		dto.setState(TreeConstant.STATE_OPEN);
		if (resource.getName().equals("待办任务")) {
			QueryMap queryMap = new QueryMap();
			List<String> sapProIds = processHelpService.getSapProcessDef();
			
			queryMap.put("assignee", AppContext.getCurrentAccount());
			queryMap.put("suspensionState", 1);
			queryMap.put("sapProcessIds", sapProIds);
			queryMap.put("processStatus", BpmProcessRun.STATUS_RUN);
			
			Integer total = activitiHelpService.count4AssigneeTask(queryMap);
			
			dto.setText(resource.getName()+"<span style=\'color:red\'>(" + total + ")</span>");
		} else {
			dto.setText(resource.getName());
		}
		dto.setIconCls(resource.getIcon());
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("url", resource.getUrl());
		dto.setAttributes(attributes);
		return dto;
	}
	
}
