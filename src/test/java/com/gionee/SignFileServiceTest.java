package com.gionee;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.biz.model.SignOrgCode;
import com.gionee.gniflow.biz.service.SignFileInformationService;
import com.gionee.gniflow.biz.service.SignOrgCodeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class SignFileServiceTest {

	@Autowired
	private SignFileInformationService signFileInformationService;
	
	@Autowired
	private SignOrgCodeService signOrgCodeService;
	
	@Test
//	@Ignore
	public void testQuerySignFile(){
		List<SignFileInformation> list = signFileInformationService.querySignFileInformation(new QueryMap());
		System.out.println(list.size());
	}
	
	@Test
	@Ignore
	public void testQuerySignOrgCode(){
		SignOrgCode signOrgCode = signOrgCodeService.getSignOrgCode(1);
		Assert.assertEquals("IM", signOrgCode.getOrgCode());
		System.out.println(signOrgCode.getCreateTime());
	}
	
}
