package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.LTeamActivitiesDetail;

public interface LTeamActivitiesDetailService {

	void save(LTeamActivitiesDetail lTeamActivitiesDetail);
	
	public void update(LTeamActivitiesDetail lTeamActivitiesDetail);
	
	void save(List<LTeamActivitiesDetail> lTeamActivitiesDetails);

	void deleteByProInstId(String proInstId);

	List<LTeamActivitiesDetail> query(String proInstId);
	
	List<LTeamActivitiesDetail> query(QueryMap queryMap);
}
