package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.ProAccountHandleInfo;

public interface ProAccountHandleInfoService {

	void save(ProAccountHandleInfo proAccountHandleInfo);

	ProAccountHandleInfo getProAccountHandleInfo(Integer id);

	void delete(Integer id);

	List<ProAccountHandleInfo> query(QueryMap critera);

	PageResult<ProAccountHandleInfo> queryPage(QueryMap critera);

}
