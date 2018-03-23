package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.QuitApplicationInfoService;
import com.gionee.gniflow.biz.model.QuitApplicationInfo;
import com.gionee.gniflow.integration.dao.QuitApplicationInfoDao;

@Service
public class QuitApplicationInfoServiceImpl implements QuitApplicationInfoService {

    @Autowired
    private QuitApplicationInfoDao quitApplicationInfoDao;


	
	@Override
	public void save(QuitApplicationInfo quitApplicationInfo) {
		if (quitApplicationInfo.getId() == null) {
			quitApplicationInfoDao.insert(quitApplicationInfo);
		}
		else {
			quitApplicationInfoDao.update(quitApplicationInfo);
		}
	}

	@Override
	public QuitApplicationInfo getQuitApplicationInfo(Integer id) {
		return quitApplicationInfoDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		quitApplicationInfoDao.delete(id);
	}


	@Override
	public List<QuitApplicationInfo> query(QueryMap critera) {
		return quitApplicationInfoDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<QuitApplicationInfo> queryPage(QueryMap critera) {
		PageResult<QuitApplicationInfo> result = new PageResult<QuitApplicationInfo>();
		result.setRows(quitApplicationInfoDao.getPage(critera.getMap()));
		result.setTotal(quitApplicationInfoDao.getPageCount(critera.getMap()));
		return result;
	}

}
