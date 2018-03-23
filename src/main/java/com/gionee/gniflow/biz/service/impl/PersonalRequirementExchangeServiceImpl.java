package com.gionee.gniflow.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.PersonalRequirementExchangeService;
import com.gionee.gniflow.biz.model.PersonalRequirementExchange;
import com.gionee.gniflow.integration.dao.PersonalRequirementExchangeDao;


@Service
public class PersonalRequirementExchangeServiceImpl implements PersonalRequirementExchangeService {

	@Autowired
    private PersonalRequirementExchangeDao personalDao;

	@Override
	public void save(PersonalRequirementExchange personalRequirementExchange) {
		if (personalRequirementExchange.getId() == null) {
			personalDao.insert(personalRequirementExchange);  
		}
		else {
			personalDao.update(personalRequirementExchange);
		}
	}

	@Override
	public PersonalRequirementExchange getPersonalRequirementExchange(Integer id) {
		return personalDao.getById(id);
	}

	@Override
	public void delete(Integer id) {
		personalDao.delete(id);
	}

	@Override
	public List<PersonalRequirementExchange> query(QueryMap critera) {
		return personalDao.getAll(critera.getMap());
	}

	@Override
	public PageResult<PersonalRequirementExchange> queryPage(QueryMap critera) {
		PageResult<PersonalRequirementExchange> result = new PageResult<PersonalRequirementExchange>();
		result.setRows(personalDao.getPage(critera.getMap()));
		result.setTotal(personalDao.getPageCount(critera.getMap()));
		return result;
	}

	@Override
	public List<PersonalRequirementExchange> getSelectedOnce(String account) {
		return personalDao.getSelectedOnce(account);
	}

}
