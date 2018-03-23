package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmRole;

public interface BpmRoleService {

	void save(BpmRole bpmRole, String roleProDefIds);

	BpmRole getBpmRole(Integer id);

	void delete(Integer id);

	List<BpmRole> query(QueryMap critera);

	PageResult<BpmRole> queryPage(QueryMap critera);
	
	List<BpmRole> getBpmRoles(Integer id);
	
	//查询报表
	List<BpmRole> getReportRoles(Integer id);
}
