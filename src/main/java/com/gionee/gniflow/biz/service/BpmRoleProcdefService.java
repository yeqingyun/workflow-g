package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;

import com.gionee.gniflow.biz.model.BpmRoleProcdef;

public interface BpmRoleProcdefService {

	BpmRoleProcdef getBpmRoleProcdef(Integer id);

	void delete(Integer id);

	List<BpmRoleProcdef> query(QueryMap critera);

}
