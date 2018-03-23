package com.gionee.gniflow.biz.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.biz.service.impl.TreeService;
import com.gionee.gniflow.biz.model.Category;
import com.gionee.gniflow.biz.service.CategoryService;
import com.gionee.gniflow.integration.dao.CategoryDao;

@Service
public class CategoryServiceImpl extends TreeService<Category> implements CategoryService {
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	public void setDao(CategoryDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List<Category> query4Pid(Long pid) {
		return categoryDao.query4Pid(pid);
	}

	@Override
	public List<Category> querySubCategory(Long id) {
		return categoryDao.querySubCategory(id);
	}

	@Override
	public void deleteCatetory(Long id) {
		List<Long> ids = new ArrayList<Long>();
		List<Category> categorys = categoryDao.querySubCategory(id);
		if (categorys != null) {
			for (Category cate : categorys) {
				ids.add(cate.getId());
			}
		} else {
			ids.add(id);
		}
		categoryDao.remove(ids);
	}

}
