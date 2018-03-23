package com.gionee.gniflow.biz.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.model.Engineer;
import com.gionee.gniflow.biz.service.EngineerService;
import com.gionee.gniflow.integration.dao.EngineerDao;

@Service
public class EngineerServiceImpl implements EngineerService {

    @Autowired
    private EngineerDao engineerDao;

	@Override
	public void save(Engineer engineer) {
		
		engineer.setUpdateBy(AppContext.getCurrentUser().getAccount());
		engineer.setUpdateTime(new Date());
		if (engineer.getId() == null) {
			engineer.setCreateBy(AppContext.getCurrentUser().getAccount());
			engineer.setCreateTime(new Date());
			engineerDao.insert(engineer);
		}
		else {
			engineerDao.update(engineer);
		}
	}

	@Override
	public Engineer getEngineer(Integer id) {
		return engineerDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		Engineer engineer = new Engineer();
		engineer.setId(id);
		engineer.setStatus(Engineer.STATUS_DELETED);
		engineerDao.update(engineer);
	}

	@Override
	public List<Engineer> query(QueryMap critera){
		String[] codes=String.valueOf(critera.getMap().get("address")).split(",");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("code",codes);
		return engineerDao.getAll(map);
	}

	@Override
	public PageResult<Engineer> queryPage(QueryMap critera) {
		PageResult<Engineer> result = new PageResult<Engineer>();
		result.setRows(engineerDao.getPage(critera.getMap()));
		result.setTotal(engineerDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public List<Engineer> getAddress() {
		return engineerDao.getAddress();
	}

	@Override
	public Engineer getUserById(String userId) {
		return engineerDao.getUserById(userId);
	}

	@Override
	public Integer getCountByAddress(QueryMap queryMap) {
		return engineerDao.getCountByAddress(queryMap.getMap());
	}

}
