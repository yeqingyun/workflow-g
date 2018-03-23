package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.BpmCommCodeService;
import com.gionee.gniflow.biz.model.BpmCommCode;
import com.gionee.gniflow.integration.dao.BpmCommCodeDao;

@Service
public class BpmCommCodeServiceImpl implements BpmCommCodeService {

    @Autowired
    private BpmCommCodeDao bpmCommCodeDao;

	@Override
	public void save(BpmCommCode bpmCommCode) {
		if (bpmCommCode.getId() == null) {
			bpmCommCodeDao.insert(bpmCommCode);
		}
		else {
			bpmCommCodeDao.update(bpmCommCode);
		}
	}

	@Override
	public BpmCommCode getBpmCommCode(Integer id) {
		return bpmCommCodeDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		BpmCommCode bpmCommCode = new BpmCommCode();
		bpmCommCode.setId(id);
		bpmCommCode.setStatus(BpmCommCode.STATUS_DELETED);
		bpmCommCodeDao.update(bpmCommCode);
	}

	@Override
	public List<BpmCommCode> query(QueryMap critera) {
		return bpmCommCodeDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmCommCode> queryPage(QueryMap critera) {
		PageResult<BpmCommCode> result = new PageResult<BpmCommCode>();
		result.setRows(bpmCommCodeDao.getPage(critera.getMap()));
		result.setTotal(bpmCommCodeDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public void deleteDictionary(Integer id) {
		bpmCommCodeDao.delete(id);
	}

	@Override
	public List<BpmCommCode> loadSysLike(QueryMap critera) {
		// TODO Auto-generated method stub
		return bpmCommCodeDao.loadSysLike(critera.getMap());
	}

}
