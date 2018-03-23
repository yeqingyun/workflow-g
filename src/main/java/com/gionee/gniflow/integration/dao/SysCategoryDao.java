package com.gionee.gniflow.integration.dao;

import java.util.List;

import com.gionee.gniflow.biz.model.SysCategory;

public interface SysCategoryDao {
	void add(SysCategory sysCategory );
	List<Integer> getAllByPid(Integer pid);
	Integer query(SysCategory sysCategory);
}
