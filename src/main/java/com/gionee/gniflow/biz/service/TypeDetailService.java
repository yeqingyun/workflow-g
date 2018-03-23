package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.TypeDetail;

public interface TypeDetailService {

	void save(TypeDetail typeDetail);

	TypeDetail getTypeDetail(Integer id);

	void delete(Integer id);

	List<TypeDetail> query(QueryMap critera);

	PageResult<TypeDetail> queryPage(QueryMap critera);

}
