package com.gionee.gniflow.biz.service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.ProcessVariableChangeLog;

public interface ProcessVariableChangeLogService {

	void save(ProcessVariableChangeLog processVariableChangeLog);

	ProcessVariableChangeLog getProcessVariableChangeLog(Integer id);

	void delete(Integer id);

	PageResult<ProcessVariableChangeLog> queryPage(QueryMap critera);
	
	

}
