package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.BpmTaskExecution;

public interface BpmTaskExecutionService {

	void save(BpmTaskExecution bpmTaskExecution);

	BpmTaskExecution getBpmTaskExecution(Integer id);

	void delete(Integer id);

	List<BpmTaskExecution> query(QueryMap critera);

	PageResult<BpmTaskExecution> queryPage(QueryMap critera);

}
