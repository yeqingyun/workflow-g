package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.BpmEmailTemplate;

public interface BpmEmailTemplateService {

	void save(BpmEmailTemplate bpmEmailTemplate);

	BpmEmailTemplate getBpmEmailTemplate(Integer id);

	void delete(Integer id);

	List<BpmEmailTemplate> query(QueryMap critera);

	PageResult<BpmEmailTemplate> queryPage(QueryMap critera);

}
