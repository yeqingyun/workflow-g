package com.gionee.gniflow.integration.dao;

import java.util.List;
import java.util.Map;

import com.gionee.gniflow.biz.model.LTeamActivitiesDetail;

public interface LTeamActivitiesDetailDao {

    int insert(LTeamActivitiesDetail lTeamActivitiesDetail);

    int update(LTeamActivitiesDetail lTeamActivitiesDetail);

    List<LTeamActivitiesDetail> getAllByProInstId(String proInstId);
    
    List<LTeamActivitiesDetail> getAll(Map<String,Object> map);

    int deleteByProInstId(String proInstId);

}
