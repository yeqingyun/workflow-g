package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.BpmConfNodeEmailService;
import com.gionee.gniflow.biz.model.BpmConfNodeEmail;
import com.gionee.gniflow.integration.dao.BpmConfNodeEmailDao;

@Service
public class BpmConfNodeEmailServiceImpl implements BpmConfNodeEmailService {

    @Autowired
    private BpmConfNodeEmailDao bpmConfNodeEmailDao;

	@Override
	public void save(BpmConfNodeEmail bpmConfNodeEmail) {
		if (bpmConfNodeEmail.getId() == null) {
			bpmConfNodeEmailDao.insert(bpmConfNodeEmail);
		}
		else {
			bpmConfNodeEmailDao.update(bpmConfNodeEmail);
		}
	}

	@Override
	public BpmConfNodeEmail getBpmConfNodeEmail(Integer id) {
		return bpmConfNodeEmailDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		bpmConfNodeEmailDao.delete(id);
	}

	@Override
	public List<BpmConfNodeEmail> query(QueryMap critera) {
		return bpmConfNodeEmailDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmConfNodeEmail> queryPage(QueryMap critera) {
		PageResult<BpmConfNodeEmail> result = new PageResult<BpmConfNodeEmail>();
		result.setRows(bpmConfNodeEmailDao.getPage(critera.getMap()));
		result.setTotal(bpmConfNodeEmailDao.getPageCount(critera.getMap()));
		return result;
	}

}
