package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmProcessRun;

public interface BpmProcessRunService {

	BpmProcessRun getBpmProcessRun(String procInstId);

	void save(BpmProcessRun bpmProcessRun);

	void delete(Integer id);

	List<BpmProcessRun> query(QueryMap critera);

	PageResult<BpmProcessRun> queryPage(QueryMap critera);

}
