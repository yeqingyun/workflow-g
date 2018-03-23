package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.BpmConfNodeEmail;

public interface BpmConfNodeEmailService {

	void save(BpmConfNodeEmail bpmConfNodeEmail);

	BpmConfNodeEmail getBpmConfNodeEmail(Integer id);

	void delete(Integer id);

	List<BpmConfNodeEmail> query(QueryMap critera);

	PageResult<BpmConfNodeEmail> queryPage(QueryMap critera);

}
