package com.gionee.gniflow.biz.service;

import java.util.List;
import java.util.Map;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.ReportUserRole;

public interface ReportUserRoleService {

	void save(ReportUserRole reportUserRole);

	void delete(Integer id);

	List<ReportUserRole> query(QueryMap critera);
	
	List<String> getUserProcessDefKey(Integer userId);
	
	void handleReportUserRole(List<Map<String,String>> lists) throws Exception;
}
