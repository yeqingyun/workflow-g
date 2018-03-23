package com.gionee.gniflow.biz.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.ProcessVariableChangeLog;
import com.gionee.gniflow.biz.service.ProcessVariableChangeLogService;
import com.gionee.gniflow.integration.dao.ProcessVariableChangeLogDao;

@Service
public class ProcessVariableChangeLogServiceImpl implements ProcessVariableChangeLogService {

    @Autowired
    private ProcessVariableChangeLogDao processVariableChangeLogDao;

	@Override
	public void save(ProcessVariableChangeLog processVariableChangeLog) {
		//插入一条至流程变量变更记录表中
		processVariableChangeLog.setCreateBy(AppContext.getCurrentAccount());
		processVariableChangeLog.setCreateTime(new Date());
		processVariableChangeLogDao.insert(processVariableChangeLog);
		//变更流程变量
		processVariableChangeLogDao.updateDetailProcessVar(processVariableChangeLog);
		processVariableChangeLogDao.updateRunProcessVar(processVariableChangeLog);
		processVariableChangeLogDao.updateHisProcessVar(processVariableChangeLog);
		
	}

	@Override
	public ProcessVariableChangeLog getProcessVariableChangeLog(Integer id) {
		return processVariableChangeLogDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		processVariableChangeLogDao.delete(id);
	}

	@Override
	public PageResult<ProcessVariableChangeLog> queryPage(QueryMap critera) {
		PageResult<ProcessVariableChangeLog> result = new PageResult<ProcessVariableChangeLog>();
		result.setRows(processVariableChangeLogDao.getPage(critera.getMap()));
		result.setTotal(processVariableChangeLogDao.getPageCount(critera.getMap()));
		return result;
	}

}
