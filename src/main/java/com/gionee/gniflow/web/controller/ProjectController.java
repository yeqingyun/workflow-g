package com.gionee.gniflow.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.MessageDto;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.web.util.EntryFactory;
import com.gionee.gniflow.biz.model.Project;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.biz.service.ProjectService;
import com.gionee.gniflow.web.easyui.ComboBoxData;


@Controller
@RequestMapping("/project")
public class ProjectController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	@Autowired
	ProjectService projectService;
	@Autowired
	ProcessHelpService processHelpService;
	
	@RequestMapping("/get.json")
	@ResponseBody
	public Project get(Integer id) {
		
		return projectService.getProject(id);
	}
	
	@RequestMapping("/sav.json")
	@ResponseBody
	public ResponseEntity<MessageDto> sav(Project project) {
		try {
			projectService.save(project);
		} catch (Exception e) {
			return EntryFactory.create(new MessageDto("保存失败", false));
		}
		return EntryFactory.create(new MessageDto("保存成功"));
	}
	
	@RequestMapping("/del.json")
	@ResponseBody
	public ResponseEntity<MessageDto> del(Integer id) {
		try {
			projectService.delete(id);
		} catch (Exception e) {
			return EntryFactory.create(new MessageDto("删除失败", false));
		}
		return EntryFactory.create(new MessageDto("删除成功"));
	}

	@RequestMapping("/queryPage.json")
	@ResponseBody
	public PageResult<Project> search(QueryMap critera) {
		 PageResult<Project> pageResult=projectService.queryPage(critera);
		 return pageResult;
	}
	
	@RequestMapping("/getLoginNo.json")
	@ResponseBody
	public String getLoginNo(String userId){
		try {
			return processHelpService.getEmpAccount(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping("/getUserInfo.json")
	@ResponseBody
	public Project getUserById(String userId){
		return projectService.getUserById(userId);
	}
	
	
	
	/** 根据分类加载数据 */
    @RequestMapping("/queryItemName.json")
    @ResponseBody
    public List<ComboBoxData> queryItemName() {
    	
    	List<ComboBoxData> comboData = new ArrayList<ComboBoxData>();
    	try {
    		List<Project> bpmCommCodes = projectService.queryItemName();
        	
        	for (Project code : bpmCommCodes) {
        		comboData.add(new ComboBoxData(String.valueOf(code.getId()), code.getProjectName()));
        	}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
        return comboData;
    }
    
    /** 根据name获取account */
    @RequestMapping("/ loadAccount.json")
    @ResponseBody
    public Project  loadAccount(Integer projectId) {
    		Project project = projectService.getProject(projectId);
    		try {
				String account =processHelpService.getEmpAccount(project.getUserId());
				project.setUserId(account);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return project;
    }
}
