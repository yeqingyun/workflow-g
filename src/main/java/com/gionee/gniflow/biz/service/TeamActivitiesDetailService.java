package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.TeamActivitiesDetail;

public interface TeamActivitiesDetailService {

	void save(TeamActivitiesDetail teamActivitiesDetail);

	List<TeamActivitiesDetail> getTeamActivitiesDetail(String account);

	void delete(String proInstId);

	List<TeamActivitiesDetail> query(QueryMap critera);

	PageResult<TeamActivitiesDetail> queryPage(QueryMap critera);
	
	public TeamActivitiesDetail getByAccount(String account);
}
