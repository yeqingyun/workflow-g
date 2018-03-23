package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.model.BpmProcessConf;
import com.gionee.gniflow.biz.service.BpmProcessConfService;
import com.gionee.gniflow.integration.dao.BpmProcessConfDao;

@Service
public class BpmProcessConfServiceImpl implements BpmProcessConfService{

    @Autowired
    private BpmProcessConfDao bpmProcessConfDao;

	@Override
	public void save(BpmProcessConf bpmProcessConf) {
		if (bpmProcessConf.getId() == null) {
			bpmProcessConfDao.insert(bpmProcessConf);
		}
		else {
			bpmProcessConfDao.update(bpmProcessConf);
		}
	}

	@Override
	public BpmProcessConf getBpmProcessConf(Integer id) {
		return bpmProcessConfDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		bpmProcessConfDao.delete(id);
	}

	@Override
	public List<BpmProcessConf> query(QueryMap critera) {
		return bpmProcessConfDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmProcessConf> queryPage(QueryMap critera) {
		PageResult<BpmProcessConf> result = new PageResult<BpmProcessConf>();
		result.setRows(bpmProcessConfDao.getPage(critera.getMap()));
		result.setTotal(bpmProcessConfDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public String getAuthority(String processName) {
		return bpmProcessConfDao.getAuthority(processName);
	}

}
