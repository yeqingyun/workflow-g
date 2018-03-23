package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.Project;

import java.util.List;
import java.util.Map;

public interface ProjectDao {

    int insert(Project project);

    int update(Project project);

    int delete(Integer id);

    List<Project> getAll(Map<String, Object> param);

    List<Project> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    Project get(Integer id);

	List<Project> queryItemName();
    
	Project loadAccount(String name);
}
