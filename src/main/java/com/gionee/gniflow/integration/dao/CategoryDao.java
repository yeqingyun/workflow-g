package com.gionee.gniflow.integration.dao;

import java.util.List;

import com.gionee.gniflow.biz.model.Category;
import com.gionee.gnif.integration.dao.AbstractDao;

public interface CategoryDao extends AbstractDao<Category> {
	public List<Category> query4Pid(Long pid);
	
	public List<Category> querySubCategory(Long id);
}
