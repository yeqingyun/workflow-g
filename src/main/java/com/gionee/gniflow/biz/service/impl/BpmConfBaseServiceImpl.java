package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.BpmConfBaseService;
import com.gionee.gniflow.biz.model.BpmConfBase;
import com.gionee.gniflow.integration.dao.BpmConfBaseDao;

@Service
public class BpmConfBaseServiceImpl implements BpmConfBaseService {

    @Autowired
    private BpmConfBaseDao bpmConfBaseDao;

	@Override
	public void save(BpmConfBase bpmConfBase) {
		if (bpmConfBase.getId() == null) {
			bpmConfBaseDao.insert(bpmConfBase);
		}
		else {
			bpmConfBaseDao.update(bpmConfBase);
		}
	}

	@Override
	public BpmConfBase getBpmConfBase(Integer id) {
		return bpmConfBaseDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		BpmConfBase bpmConfBase = new BpmConfBase();
		bpmConfBase.setId(id);
		bpmConfBase.setStatus(BpmConfBase.STATUS_DELETED);
		bpmConfBaseDao.update(bpmConfBase);
	}

	@Override
	public List<BpmConfBase> query(QueryMap critera) {
		return bpmConfBaseDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmConfBase> queryPage(QueryMap critera) {
		PageResult<BpmConfBase> result = new PageResult<BpmConfBase>();
		result.setRows(bpmConfBaseDao.getPage(critera.getMap()));
		result.setTotal(bpmConfBaseDao.getPageCount(critera.getMap()));
		return result;
	}

}
