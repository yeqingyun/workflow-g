package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.biz.service.AbstractService;
import com.gionee.gniflow.biz.model.Category;

public interface CategoryService extends AbstractService<Category> {
	/** 根据父节点获取Category */
	public List<Category> query4Pid(Long pid);
	/** 获取节点下的所有子节点 */
	public List<Category> querySubCategory(Long id);
	/** 删除Category */
	public void deleteCatetory(Long id);
}
