package com.gionee.gniflow.biz.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.service.WelfareAppService;
import com.gionee.gniflow.integration.dao.WelfareAppDao;

@Service
public class WelfareAppServiceImpl implements WelfareAppService{
	@Autowired
	WelfareAppDao  welfareAppDao;
	@Override
	public int weddingIsRepeat(Map<String, String>map) {
		// TODO Auto-generated method stub
		return welfareAppDao.weddingIsRepeat(map);
	}
	@Override
	public int birthIsRepeat(Map<String, String> map) {
		// TODO Auto-generated method stub
		return welfareAppDao.birthIsRepeat(map);
	}
	@Override
	public Date birthIsRepeatToAgain(Map<String, String> map) {
		// TODO Auto-generated method stub
		return welfareAppDao.birthIsRepeatToAgain(map);
	}
	@Override
	public Date weddingIsRepeatToAgain(Map<String, String> map) {
		// TODO Auto-generated method stub
		return welfareAppDao.weddingIsRepeatToAgain(map);
	}

}
