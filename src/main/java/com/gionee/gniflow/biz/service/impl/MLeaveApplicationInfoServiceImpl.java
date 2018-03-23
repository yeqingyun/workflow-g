package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.MLeaveApplicationInfo;
import com.gionee.gniflow.biz.service.MLeaveApplicationInfoService;
import com.gionee.gniflow.integration.dao.MLeaveApplicationInfoDao;

@Service
public class MLeaveApplicationInfoServiceImpl implements MLeaveApplicationInfoService {

    @Autowired
    private MLeaveApplicationInfoDao mLeaveApplicationInfoDao;

	@Override
	public void save(MLeaveApplicationInfo mLeaveApplicationInfo) {
		if (mLeaveApplicationInfo.getId() == null) {
			mLeaveApplicationInfoDao.insert(mLeaveApplicationInfo);
		}
		else {
			mLeaveApplicationInfoDao.update(mLeaveApplicationInfo);
		}
	}

	@Override
	public MLeaveApplicationInfo getMLeaveApplicationInfo(Integer id) {
		return mLeaveApplicationInfoDao.get(id);
	}

	@Override
	public List<MLeaveApplicationInfo> query(QueryMap critera) {
		return mLeaveApplicationInfoDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<MLeaveApplicationInfo> queryPage(QueryMap critera) {
		PageResult<MLeaveApplicationInfo> result = new PageResult<MLeaveApplicationInfo>();
		result.setRows(mLeaveApplicationInfoDao.getPage(critera.getMap()));
		result.setTotal(mLeaveApplicationInfoDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public void delete(String proInstId) {
		MLeaveApplicationInfo mLeaveApplicationInfo = new MLeaveApplicationInfo();
		mLeaveApplicationInfo.setProInstId(proInstId);
		mLeaveApplicationInfo.setStatus(MLeaveApplicationInfo.STATUS_DELETED);
		mLeaveApplicationInfoDao.update(mLeaveApplicationInfo);
	}

	@Override
	public void deleteObsoleteProcess(String proInstId) {
		MLeaveApplicationInfo mLeaveApplicationInfo = new MLeaveApplicationInfo();
		mLeaveApplicationInfo.setProInstId(proInstId);
		mLeaveApplicationInfoDao.deleteObsoleteProcess(mLeaveApplicationInfo.getProInstId());
	}


}
