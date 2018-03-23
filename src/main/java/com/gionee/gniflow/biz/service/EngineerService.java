package com.gionee.gniflow.biz.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.gionee.gnif.biz.service.AbstractService;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.Category;
import com.gionee.gniflow.biz.model.Engineer;

public interface EngineerService{

	void save(Engineer engineer);

	Engineer getEngineer(Integer id);

	void delete(Integer id);

	List<Engineer> query(QueryMap critera);

	PageResult<Engineer> queryPage(QueryMap critera);

	List<Engineer> getAddress();

	Engineer getUserById(String userId);
	
	Integer getCountByAddress(QueryMap queryMap);

}
