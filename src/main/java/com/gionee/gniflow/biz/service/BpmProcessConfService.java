package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmProcessConf;

public interface BpmProcessConfService {
	void save(BpmProcessConf bpmProcessConf);

	BpmProcessConf getBpmProcessConf(Integer id);

	void delete(Integer id);

	List<BpmProcessConf> query(QueryMap critera);

	PageResult<BpmProcessConf> queryPage(QueryMap critera);
	
	String getAuthority(String processName);

}
