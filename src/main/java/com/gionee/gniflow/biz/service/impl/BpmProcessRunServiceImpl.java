package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.BpmProcessRunService;
import com.gionee.gniflow.biz.model.BpmProcessRun;
import com.gionee.gniflow.integration.dao.BpmProcessRunDao;

@Service
public class BpmProcessRunServiceImpl implements BpmProcessRunService {

    @Autowired
    private BpmProcessRunDao bpmProcessRunDao;

	@Override
	public BpmProcessRun getBpmProcessRun(String procInstId) {
		return bpmProcessRunDao.get(procInstId);
	}

	@Override
	public void save(BpmProcessRun bpmProcessRun) {
		if (bpmProcessRun.getId() == null) {
			bpmProcessRunDao.insert(bpmProcessRun);
		}
		else {
			bpmProcessRunDao.update(bpmProcessRun);
		}
	}

	@Override
	public void delete(Integer id) {
		BpmProcessRun bpmProcessRun = new BpmProcessRun();
		bpmProcessRun.setId(id);
		bpmProcessRun.setStatus(BpmProcessRun.STATUS_DELETED);
		bpmProcessRunDao.update(bpmProcessRun);
	}

	@Override
	public List<BpmProcessRun> query(QueryMap critera) {
		return bpmProcessRunDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmProcessRun> queryPage(QueryMap critera) {
		PageResult<BpmProcessRun> result = new PageResult<BpmProcessRun>();
		result.setRows(bpmProcessRunDao.getPage(critera.getMap()));
		result.setTotal(bpmProcessRunDao.getPageCount(critera.getMap()));
		return result;
	}

}
