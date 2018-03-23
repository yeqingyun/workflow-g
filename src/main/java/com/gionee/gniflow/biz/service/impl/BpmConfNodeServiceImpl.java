package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.BpmConfNodeService;
import com.gionee.gniflow.biz.model.BpmConfNode;
import com.gionee.gniflow.dto.BpmConfNodeDto;
import com.gionee.gniflow.integration.dao.BpmConfNodeDao;

@Service
public class BpmConfNodeServiceImpl implements BpmConfNodeService {

    @Autowired
    private BpmConfNodeDao bpmConfNodeDao;

	@Override
	public Integer save(BpmConfNode bpmConfNode) {
		if (bpmConfNode.getId() == null) {
			return bpmConfNodeDao.insert(bpmConfNode);
		}
		else {
			bpmConfNodeDao.update(bpmConfNode);
		}
		return null;
	}

	@Override
	public BpmConfNode getBpmConfNode(Integer id) {
		return bpmConfNodeDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		BpmConfNode bpmConfNode = new BpmConfNode();
		bpmConfNode.setId(id);
		bpmConfNode.setStatus(BpmConfNode.STATUS_DELETED);
		bpmConfNodeDao.update(bpmConfNode);
	}

	@Override
	public List<BpmConfNode> query(QueryMap critera) {
		return bpmConfNodeDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<BpmConfNode> queryPage(QueryMap critera) {
		PageResult<BpmConfNode> result = new PageResult<BpmConfNode>();
		result.setRows(bpmConfNodeDao.getPage(critera.getMap()));
		result.setTotal(bpmConfNodeDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public BpmConfNodeDto joinQueryBpmConfNode(QueryMap critera) {
		return bpmConfNodeDao.joinQueryBpmConfNode(critera.getMap());
	}

}
