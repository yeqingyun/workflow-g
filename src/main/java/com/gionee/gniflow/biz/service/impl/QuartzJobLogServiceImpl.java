package com.gionee.gniflow.biz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.service.QuartzJobLogService;
import com.gionee.gniflow.biz.model.QuartzJobLog;
import com.gionee.gniflow.integration.dao.QuartzJobLogDao;

@Service
public class QuartzJobLogServiceImpl implements QuartzJobLogService {

    @Autowired
    private QuartzJobLogDao quartzJobLogDao;

	@Override
	public void save(QuartzJobLog quartzJobLog) {
		if (quartzJobLog.getId() == null) {
			quartzJobLogDao.insert(quartzJobLog);
		}
		else {
			quartzJobLogDao.update(quartzJobLog);
		}
	}
	
}
