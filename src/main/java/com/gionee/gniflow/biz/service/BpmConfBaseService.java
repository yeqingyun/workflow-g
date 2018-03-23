package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.BpmConfBase;

public interface BpmConfBaseService {

	void save(BpmConfBase bpmConfBase);

	BpmConfBase getBpmConfBase(Integer id);

	void delete(Integer id);

	List<BpmConfBase> query(QueryMap critera);

	PageResult<BpmConfBase> queryPage(QueryMap critera);

}
