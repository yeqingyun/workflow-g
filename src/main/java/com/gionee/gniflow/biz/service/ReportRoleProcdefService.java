package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.ReportRoleProcdef;

public interface ReportRoleProcdefService {

	ReportRoleProcdef getReportRoleProcdef(Integer id);

	void delete(Integer id);

	List<ReportRoleProcdef> query(QueryMap critera);

}
