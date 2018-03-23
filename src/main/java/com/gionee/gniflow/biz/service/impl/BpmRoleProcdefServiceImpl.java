package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;

import com.gionee.gniflow.biz.service.BpmRoleProcdefService;
import com.gionee.gniflow.biz.model.BpmRoleProcdef;
import com.gionee.gniflow.integration.dao.BpmRoleProcdefDao;

@Service
public class BpmRoleProcdefServiceImpl implements BpmRoleProcdefService {

    @Autowired
    private BpmRoleProcdefDao bpmRoleProcdefDao;

	@Override
	public BpmRoleProcdef getBpmRoleProcdef(Integer id) {
		return bpmRoleProcdefDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		bpmRoleProcdefDao.delete(id);
	}

	@Override
	public List<BpmRoleProcdef> query(QueryMap critera) {
		return bpmRoleProcdefDao.getAll(critera.getMap());
	}

}
