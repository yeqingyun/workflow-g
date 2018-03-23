package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.Project;

public interface ProjectService {

	void save(Project project);

	Project getProject(Integer id);
	
	Project getUserById(String userId);

	void delete(Integer id);

	List<Project> query(QueryMap critera);

	PageResult<Project> queryPage(QueryMap critera);

	List<Project> queryItemName();
	
	Project loadAccount(String name);

}
