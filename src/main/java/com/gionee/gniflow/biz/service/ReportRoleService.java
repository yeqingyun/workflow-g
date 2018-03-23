package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.ReportRole;

public interface ReportRoleService {

	void save(ReportRole reportRole, String roleProDefIds);

	ReportRole getReportRole(Integer id);

	void delete(Integer id);

	List<ReportRole> query(QueryMap critera);

	PageResult<ReportRole> queryPage(QueryMap critera);
	
	List<ReportRole> getReportRoles(Integer id);
}
