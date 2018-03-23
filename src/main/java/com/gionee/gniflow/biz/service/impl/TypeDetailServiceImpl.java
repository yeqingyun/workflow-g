package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.service.TypeDetailService;
import com.gionee.gniflow.biz.model.TypeDetail;
import com.gionee.gniflow.integration.dao.TypeDetailDao;

@Service
public class TypeDetailServiceImpl implements TypeDetailService {

    @Autowired
    private TypeDetailDao typeDetailDao;

	@Override
	public void save(TypeDetail typeDetail) {
		if (typeDetail.getId() == null) {
			typeDetailDao.insert(typeDetail);
		}
		else {
			typeDetailDao.update(typeDetail);
		}
	}

	@Override
	public TypeDetail getTypeDetail(Integer id) {
		return typeDetailDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		typeDetailDao.delete(id);
	}

/*	@Override
	public void delete(Integer id) {
		TypeDetail typeDetail = new TypeDetail();
		typeDetail.setId(id);
		typeDetail.setStatus(TypeDetail.STATUS_DELETED);
		typeDetailDao.update(typeDetail);
	}
*/
	@Override
	public List<TypeDetail> query(QueryMap critera) {
		return typeDetailDao.getAllById(critera.getMap());
	}

	@Override
	public PageResult<TypeDetail> queryPage(QueryMap critera) {
		PageResult<TypeDetail> result = new PageResult<TypeDetail>();
		result.setRows(typeDetailDao.getPage(critera.getMap()));
		result.setTotal(typeDetailDao.getPageCount(critera.getMap()));
		return result;
	}

}
