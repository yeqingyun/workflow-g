package com.gionee.gniflow.biz.service;

import java.util.List;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;

import com.gionee.gniflow.biz.model.PersonalRequirementExchange;

public interface PersonalRequirementExchangeService {

	void save(PersonalRequirementExchange personalRequirementExchange);

	PersonalRequirementExchange getPersonalRequirementExchange(Integer id);

	void delete(Integer id);

	List<PersonalRequirementExchange> query(QueryMap critera);

	PageResult<PersonalRequirementExchange> queryPage(QueryMap critera);
	
	List<PersonalRequirementExchange> getSelectedOnce(String account);

}
