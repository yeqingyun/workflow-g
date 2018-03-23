package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.Engineer;

import java.util.List;
import java.util.Map;

public interface EngineerDao {

    int insert(Engineer engineer);

    int update(Engineer engineer);

    int delete(Integer id);

    List<Engineer> getAll(Map<String, Object> param);

    List<Engineer> getPage(Map<String, Object> param);
    int getPageCount(Map<String, Object> param);

    Engineer get(Integer id);

	List<Engineer> getAddress();

	Engineer getUserById(String userId);
	
	Integer getCountByAddress(Map<String,Object> map);
}
