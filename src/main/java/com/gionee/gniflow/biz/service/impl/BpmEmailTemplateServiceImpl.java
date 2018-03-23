package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.BpmEmailTemplateService;
import com.gionee.gniflow.biz.model.BpmEmailTemplate;
import com.gionee.gniflow.integration.dao.BpmEmailTemplateDao;

@Service
public class BpmEmailTemplateServiceImpl implements BpmEmailTemplateService {

    @Autowired
    private BpmEmailTemplateDao bpmEmailTemplateDao;

	@Override
	public void save(BpmEmailTemplate bpmEmailTemplate) {
		if (bpmEmailTemplate.getId() == null) {
			bpmEmailTemplateDao.insert(bpmEmailTemplate);
		}
		else {
			bpmEmailTemplateDao.update(bpmEmailTemplate);
		}
	}

	@Override
	public BpmEmailTemplate getBpmEmailTemplate(Integer id) {
		return bpmEmailTemplateDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		bpmEmailTemplateDao.delete(id);
	}

	@Override
	public List<BpmEmailTemplate> query(QueryMap critera) {
		return bpmEmailTemplateDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmEmailTemplate> queryPage(QueryMap critera) {
		PageResult<BpmEmailTemplate> result = new PageResult<BpmEmailTemplate>();
		result.setRows(bpmEmailTemplateDao.getPage(critera.getMap()));
		result.setTotal(bpmEmailTemplateDao.getPageCount(critera.getMap()));
		return result;
	}

}
