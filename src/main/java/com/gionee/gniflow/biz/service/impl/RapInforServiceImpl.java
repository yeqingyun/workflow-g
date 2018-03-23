package com.gionee.gniflow.biz.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.service.RapInforService;
import com.gionee.gniflow.integration.dao.RapInfoDao;


@Service("RapInforService")  
public class RapInforServiceImpl implements RapInforService{
   @Autowired
   private  RapInfoDao  rapInfoDao;


	public void insert(Map<String, String> map) {
		// TODO Auto-generated method stub
		rapInfoDao.insert(map);
	}


	@Override
	public int query(Map<String, String> map) {
		// TODO Auto-generated method stub
		return rapInfoDao.query(map);
	}


	@Override
	public void delete(String instanceID) {
		// TODO Auto-generated method stub
		rapInfoDao.delete(instanceID);
	}

}
