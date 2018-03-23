package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.BpmTaskExecutionService;
import com.gionee.gniflow.biz.model.BpmTaskExecution;
import com.gionee.gniflow.integration.dao.BpmTaskExecutionDao;

@Service
public class BpmTaskExecutionServiceImpl implements BpmTaskExecutionService {

    @Autowired
    private BpmTaskExecutionDao bpmTaskExecutionDao;

	@Override
	public void save(BpmTaskExecution bpmTaskExecution) {
		if (bpmTaskExecution.getId() == null) {
			bpmTaskExecutionDao.insert(bpmTaskExecution);
		}
		else {
			bpmTaskExecutionDao.update(bpmTaskExecution);
		}
	}

	@Override
	public BpmTaskExecution getBpmTaskExecution(Integer id) {
		return bpmTaskExecutionDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		BpmTaskExecution bpmTaskExecution = new BpmTaskExecution();
		bpmTaskExecution.setId(id);
		bpmTaskExecution.setStatus(BpmTaskExecution.STATUS_DELETED);
		bpmTaskExecutionDao.update(bpmTaskExecution);
	}

	@Override
	public List<BpmTaskExecution> query(QueryMap critera) {
		return bpmTaskExecutionDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmTaskExecution> queryPage(QueryMap critera) {
		PageResult<BpmTaskExecution> result = new PageResult<BpmTaskExecution>();
		result.setRows(bpmTaskExecutionDao.getPage(critera.getMap()));
		result.setTotal(bpmTaskExecutionDao.getPageCount(critera.getMap()));
		return result;
	}

}
