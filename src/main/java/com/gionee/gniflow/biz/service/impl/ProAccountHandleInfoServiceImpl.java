package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.ProAccountHandleInfoService;
import com.gionee.gniflow.biz.model.ProAccountHandleInfo;
import com.gionee.gniflow.integration.dao.ProAccountHandleInfoDao;

@Service
public class ProAccountHandleInfoServiceImpl implements ProAccountHandleInfoService {

    @Autowired
    private ProAccountHandleInfoDao proAccountHandleInfoDao;

	@Override
	public void save(ProAccountHandleInfo proAccountHandleInfo) {
		if (proAccountHandleInfo.getId() == null) {
			proAccountHandleInfoDao.insert(proAccountHandleInfo);
		}
		else {
			proAccountHandleInfoDao.update(proAccountHandleInfo);
		}
	}

	@Override
	public ProAccountHandleInfo getProAccountHandleInfo(Integer id) {
		return proAccountHandleInfoDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		ProAccountHandleInfo proAccountHandleInfo = new ProAccountHandleInfo();
		proAccountHandleInfo.setId(id);
		proAccountHandleInfo.setStatus(ProAccountHandleInfo.STATUS_DELETED);
		proAccountHandleInfoDao.update(proAccountHandleInfo);
	}

	@Override
	public List<ProAccountHandleInfo> query(QueryMap critera) {
		return proAccountHandleInfoDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<ProAccountHandleInfo> queryPage(QueryMap critera) {
		PageResult<ProAccountHandleInfo> result = new PageResult<ProAccountHandleInfo>();
		result.setRows(proAccountHandleInfoDao.getPage(critera.getMap()));
		result.setTotal(proAccountHandleInfoDao.getPageCount(critera.getMap()));
		return result;
	}

}
