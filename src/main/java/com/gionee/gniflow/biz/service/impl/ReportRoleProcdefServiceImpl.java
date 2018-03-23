package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.service.ReportRoleProcdefService;
import com.gionee.gniflow.biz.model.ReportRoleProcdef;
import com.gionee.gniflow.integration.dao.ReportRoleProcdefDao;

@Service
public class ReportRoleProcdefServiceImpl implements ReportRoleProcdefService {

    @Autowired
    private ReportRoleProcdefDao reportRoleProcdefDao;

	@Override
	public ReportRoleProcdef getReportRoleProcdef(Integer id) {
		return reportRoleProcdefDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		reportRoleProcdefDao.delete(id);
	}

	@Override
	public List<ReportRoleProcdef> query(QueryMap critera) {
		return reportRoleProcdefDao.getAll(critera.getMap());
	}

}
