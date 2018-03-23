package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.HardwareType;

public interface HardwareTypeService {

	void save(HardwareType hardwareType);

	void delete(Integer id);

	List<HardwareType> query(QueryMap critera);

	PageResult<HardwareType> queryPage(QueryMap critera);

}
