package com.gionee.gniflow.integration.dao;

import com.gionee.gniflow.biz.model.PersonalRequirementExchange;

import java.util.List;
import java.util.Map;

public interface PersonalRequirementExchangeDao {

    int insert(PersonalRequirementExchange personalRequirementExchange);

    int update(PersonalRequirementExchange personalRequirementExchange);

    int delete(Integer id);

    PersonalRequirementExchange getById(Integer id);

	List<PersonalRequirementExchange> getAll(Map<String, Object> map);

	List<PersonalRequirementExchange> getPage(Map<String, Object> map);

	Integer getPageCount(Map<String, Object> map);
	
	List<PersonalRequirementExchange> getSelectedOnce(String account);

}
