package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.LTeamActivitiesDetail;
import com.gionee.gniflow.biz.service.LTeamActivitiesDetailService;
import com.gionee.gniflow.integration.dao.LTeamActivitiesDetailDao;

@Service
public class LTeamActivitiesDetailServiceImpl implements LTeamActivitiesDetailService {

	@Autowired
	LTeamActivitiesDetailDao lTeamActivitiesDetailDao;
	@Override
	public void save(LTeamActivitiesDetail lTeamActivitiesDetail) {
		// TODO Auto-generated method stub
			lTeamActivitiesDetailDao.insert(lTeamActivitiesDetail);
	}

	@Override
	public void deleteByProInstId(String proInstId) {
		// TODO Auto-generated method stub
		lTeamActivitiesDetailDao.deleteByProInstId(proInstId);
	}

	@Override
	public List<LTeamActivitiesDetail> query(String proInstId) {
		// TODO Auto-generated method stub
		return lTeamActivitiesDetailDao.getAllByProInstId(proInstId);
	}

	@Override
	public List<LTeamActivitiesDetail> query(QueryMap queryMap) {
		// TODO Auto-generated method stub
		return lTeamActivitiesDetailDao.getAll(queryMap.getMap());
	}

	@Override
	public void save(List<LTeamActivitiesDetail> lTeamActivitiesDetails) {
		// TODO Auto-generated method stub
		if(lTeamActivitiesDetails!=null&&lTeamActivitiesDetails.size()>0){
			for(LTeamActivitiesDetail lTeamActivitiesDetail:lTeamActivitiesDetails){
				lTeamActivitiesDetailDao.insert(lTeamActivitiesDetail);
			}
		}
	}

	@Override
	public void update(LTeamActivitiesDetail lTeamActivitiesDetail) {
		// TODO Auto-generated method stub
		lTeamActivitiesDetailDao.update(lTeamActivitiesDetail);
	}

}
