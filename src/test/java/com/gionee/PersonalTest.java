package com.gionee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gniflow.biz.service.PersonalRequirementExchangeService;
import com.gionee.gniflow.integration.dao.PersonalRequirementExchangeDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class PersonalTest {

	@Autowired
    private PersonalRequirementExchangeService personalRequirementExchangeService;
	
	@Test
//	@Ignore
	public void testGetUserName(){
		personalRequirementExchangeService.save(null);
	}
	
}
