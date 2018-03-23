package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.constant.BPMConstant;
import com.gionee.gniflow.integration.dao.SignFileInformationDao;

@Service
public class SignFileInformationServiceImpl implements SignFileInformationService {

    @Autowired
    private SignFileInformationDao signFileInformationDao;

	@Override
	public void save(SignFileInformation signFileInformation) {
		if (signFileInformation.getId() == null) {
			signFileInformationDao.insert(signFileInformation);
		}
		else {
			signFileInformationDao.update(signFileInformation);
		}
	}

	@Override
	public SignFileInformation getSignFileInformation(Integer id) {
		return signFileInformationDao.get(id);
	}

	@Override
	public void logicDelete(Integer id) {
		SignFileInformation signFileInformation = new SignFileInformation();
		signFileInformation.setId(id);
		signFileInformation.setStatus(BPMConstant.STATUS_DELETED);
		signFileInformationDao.update(signFileInformation);
	}

	@Override
	public List<SignFileInformation> querySignFileInformation(QueryMap queryMap) {
		return signFileInformationDao.getAll(queryMap.getMap());
	}

	@Override
	public SignFileInformation getMaxSignFile(QueryMap queryMap) {
		return signFileInformationDao.getMaxFileCodeInfo(queryMap.getMap());
	}

}
