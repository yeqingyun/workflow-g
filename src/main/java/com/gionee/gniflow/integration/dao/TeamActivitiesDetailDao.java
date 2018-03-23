package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.TeamActivitiesDetail;

import java.util.List;
import java.util.Map;

public interface TeamActivitiesDetailDao {

    int insert(TeamActivitiesDetail teamActivitiesDetail);

    int update(TeamActivitiesDetail teamActivitiesDetail);

    int delete(String proInstId);

	List<TeamActivitiesDetail> get(String account);

	List<TeamActivitiesDetail> getAll(Map<String, Object> map);

	List<TeamActivitiesDetail> getPage(Map<String, Object> map);

	Integer getPageCount(Map<String, Object> map);
	
	TeamActivitiesDetail getByAccount(String account);

}
