package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.BpmCommCode;

public interface BpmCommCodeService {

	void save(BpmCommCode bpmCommCode);

	BpmCommCode getBpmCommCode(Integer id);

	void delete(Integer id);

	List<BpmCommCode> query(QueryMap critera);

	PageResult<BpmCommCode> queryPage(QueryMap critera);

	void deleteDictionary(Integer id);
	
	List<BpmCommCode> loadSysLike(QueryMap critera);

}
