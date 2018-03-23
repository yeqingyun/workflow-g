package com.gionee.gniflow.biz.service;

import java.util.List;
import java.util.Map;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmUserRole;
import com.gionee.gniflow.web.response.BpmUserRoleResponse;

public interface BpmUserRoleService {

	void save(BpmUserRole bpmUserRole);

	void delete(QueryMap queryMap);

	List<BpmUserRole> query(QueryMap critera);
	
	PageResult<BpmUserRoleResponse> queryPage(QueryMap queryMap);
	
	List<String> getUserProcessDefKey(Integer userId);
	
	void handleBpmUserRole(List<Map<String,String>> lists) throws Exception;
}
