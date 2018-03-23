package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.TeamActivitiesDetailService;
import com.gionee.gniflow.biz.model.TeamActivitiesDetail;
import com.gionee.gniflow.integration.dao.TeamActivitiesDetailDao;

@Service
public class TeamActivitiesDetailServiceImpl implements TeamActivitiesDetailService {

    @Autowired
    private TeamActivitiesDetailDao teamActivitiesDetailDao;

	@Override
	public void save(TeamActivitiesDetail teamActivitiesDetail) {
		QueryMap queryMap=new QueryMap();
		queryMap.put("procInstId", teamActivitiesDetail.getProcInstId());
		queryMap.put("account", teamActivitiesDetail.getAccount());
		List<TeamActivitiesDetail> teams=teamActivitiesDetailDao.getAll(queryMap.getMap());
		if (teams == null || teams.size() <= 0) {
			teamActivitiesDetailDao.insert(teamActivitiesDetail);
		}
		else {
			teamActivitiesDetailDao.update(teamActivitiesDetail);
		}
	}

	@Override
	public List<TeamActivitiesDetail> getTeamActivitiesDetail(String account) {
		return teamActivitiesDetailDao.get(account);
	}

	@Override
	public void delete(String proInstId) {
		teamActivitiesDetailDao.delete(proInstId);
	}

	@Override
	public List<TeamActivitiesDetail> query(QueryMap critera) {
		return teamActivitiesDetailDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<TeamActivitiesDetail> queryPage(QueryMap critera) {
		PageResult<TeamActivitiesDetail> result = new PageResult<TeamActivitiesDetail>();
		result.setRows(teamActivitiesDetailDao.getPage(critera.getMap()));
		result.setTotal(teamActivitiesDetailDao.getPageCount(critera.getMap()));
		return result;
	}
	
	@Override
	public TeamActivitiesDetail getByAccount(String account) {
		return teamActivitiesDetailDao.getByAccount(account);
	}

}
