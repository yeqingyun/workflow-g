package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.model.SysCategory;
import com.gionee.gniflow.biz.service.SysCategoryService;
import com.gionee.gniflow.integration.dao.SysCategoryDao;

@Service
public class SysCategoryServiceImpl implements SysCategoryService{
	@Autowired
    SysCategoryDao categoryDao;
	@Override
	public void add(SysCategory sysCategory) {
		// TODO Auto-generated method stub
		categoryDao.add(sysCategory);
	}
	@Override
	public List<Integer> getAllByPid(Integer pid) {
		// TODO Auto-generated method stub
		return categoryDao.getAllByPid(pid);
	}
	@Override
	public Integer query(SysCategory sysCategory) {
		// TODO Auto-generated method stub
		return categoryDao.query(sysCategory);
	}

}
