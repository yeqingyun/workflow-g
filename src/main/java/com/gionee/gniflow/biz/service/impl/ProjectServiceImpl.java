package com.gionee.gniflow.biz.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.Project;
import com.gionee.gniflow.biz.service.ProjectService;
import com.gionee.gniflow.integration.dao.ProjectDao;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDao projectDao;

	@Override
	public void save(Project project) {
		  if(project.getId()==null){
			  project.setCreateBy(AppContext.getCurrentUser().getAccount());
				project.setCreateTime(new Date());
				projectDao.insert(project);
		  }else{
			  project.setUpdateBy(AppContext.getCurrentUser().getAccount());
			  project.setUpdateTime(new Date());
			  projectDao.update(project);
		  }
			
	}

	@Override
	public Project getProject(Integer id) {
		// TODO Auto-generated method stub
		return projectDao.get(id);
	}

	@Override
	public Project getUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		projectDao.delete(id);
	}

	@Override
	public List<Project> query(QueryMap critera) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<Project> queryPage(QueryMap critera) {
		
		PageResult<Project> result = new PageResult<Project>();
		result.setRows(projectDao.getPage(critera.getMap()));
		result.setTotal(projectDao.getPageCount(critera.getMap()));
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public List<Project> queryItemName() {
		// TODO Auto-generated method stub
		return projectDao.queryItemName();
	}

	@Override
	public Project loadAccount(String name) {
		// TODO Auto-generated method stub
		return projectDao.loadAccount(name);
	}

}
