package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.HardwareTypeService;
import com.gionee.gniflow.biz.model.HardwareType;
import com.gionee.gniflow.integration.dao.HardwareTypeDao;

@Service
public class HardwareTypeServiceImpl implements HardwareTypeService {

    @Autowired
    private HardwareTypeDao hardwareTypeDao;

	@Override
	public void save(HardwareType hardwareType) {
		if (hardwareType.getId() == null) {
			hardwareTypeDao.insert(hardwareType);
		}
		else {
			hardwareTypeDao.update(hardwareType);
		}
	}

	@Override
	public void delete(Integer id) {
		hardwareTypeDao.delete(id);
	}

	@Override
	public List<HardwareType> query(QueryMap critera) {
		return hardwareTypeDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<HardwareType> queryPage(QueryMap critera) {
		PageResult<HardwareType> result = new PageResult<HardwareType>();
		result.setTotal(hardwareTypeDao.getPageCount(critera.getMap()));
		result.setRows(hardwareTypeDao.getPage(critera.getMap()));
		return result;
	}

}
