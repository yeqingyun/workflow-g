package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.QuitApplicationInfo;

public interface QuitApplicationInfoService {


	void save(QuitApplicationInfo quitApplicationInfo);

	QuitApplicationInfo getQuitApplicationInfo(Integer id);

	void delete(Integer id);


	List<QuitApplicationInfo> query(QueryMap critera);

	PageResult<QuitApplicationInfo> queryPage(QueryMap critera);

}
