package com.gionee.gniflow.biz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gniflow.biz.service.SignOrgCodeService;
import com.gionee.gniflow.biz.model.SignOrgCode;
import com.gionee.gniflow.integration.dao.SignOrgCodeDao;

@Service
public class SignOrgCodeServiceImpl implements SignOrgCodeService {

    @Autowired
    private SignOrgCodeDao signOrgCodeDao;

	@Override
	public void save(SignOrgCode signOrgCode) {
		if (signOrgCode.getId() == null) {
			signOrgCodeDao.insert(signOrgCode);
		}
		else {
			signOrgCodeDao.update(signOrgCode);
		}
	}

	@Override
	public SignOrgCode getSignOrgCode(Integer id) {
		return signOrgCodeDao.get(id);
	}

	@Override
	public void delete(Integer id) {
		signOrgCodeDao.delete(id);
	}

	@Override
	public SignOrgCode getSignOrgCode4OrgId(Integer orgId) {
		return signOrgCodeDao.getOrgCode4OrgId(orgId);
	}

}
