package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.BpmConfNodeSendService;
import com.gionee.gniflow.biz.model.BpmConfNodeSend;
import com.gionee.gniflow.dto.BpmConfNodeSendDto;
import com.gionee.gniflow.integration.dao.BpmConfNodeSendDao;

@Service
public class BpmConfNodeSendServiceImpl implements BpmConfNodeSendService {

    @Autowired
    private BpmConfNodeSendDao bpmConfNodeSendDao;

	@Override
	public void save(BpmConfNodeSend bpmConfNodeSend) {
		if (bpmConfNodeSend.getId() == null) {
			bpmConfNodeSendDao.insert(bpmConfNodeSend);
		}
		else {
			bpmConfNodeSendDao.update(bpmConfNodeSend);
		}
	}

	@Override
	public BpmConfNodeSend getBpmConfNodeSend(Integer id) {
		return bpmConfNodeSendDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		BpmConfNodeSend bpmConfNodeSend = new BpmConfNodeSend();
		bpmConfNodeSend.setId(id);
		bpmConfNodeSend.setStatus(BpmConfNodeSend.STATUS_DELETED);
		bpmConfNodeSendDao.update(bpmConfNodeSend);
	}

	@Override
	public List<BpmConfNodeSend> query(QueryMap critera) {
		return bpmConfNodeSendDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmConfNodeSend> queryPage(QueryMap critera) {
		PageResult<BpmConfNodeSend> result = new PageResult<BpmConfNodeSend>();
		result.setRows(bpmConfNodeSendDao.getPage(critera.getMap()));
		result.setTotal(bpmConfNodeSendDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public List<BpmConfNodeSendDto> joinQueryBpmConfNodeSend(QueryMap critera) {
		return bpmConfNodeSendDao.joinQueryBpmConfNodeSend(critera.getMap());
	}

}
