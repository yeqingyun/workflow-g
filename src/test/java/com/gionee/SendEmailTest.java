/**
 * @(#) SendEmailTest.java Created on 2014年8月12日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gnif.biz.domain.AccountManager;
import com.gionee.gnif.biz.model.AccountDetail;
import com.gionee.gniflow.web.util.PropertyHolder;
import com.gionee.gniflow.web.util.SendMailUtil;

/**
 * The class <code>SendEmailTest</code>
 *
 * @author lipw
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:biz-context.xml"})
public class SendEmailTest {
	
	@Autowired
	private AccountManager accountManager;
	
	@Test
	@Ignore
	public void sendEmailTest(){
		System.out.println(PropertyHolder.getContextProperty("email.from"));
		 SendMailUtil.sendCommonMail("343505471@qq.com", "测试任务", "<a href='http://www.sina.com.cn'>aaaaaaaaaaaaaaaaaaaaa</a>");
	}
	
	@Test
	public void getEmailAddrTest(){
		List<AccountDetail> list= accountManager.getDetail(Arrays.asList(new String[]{"00001222"}));
		for (AccountDetail acc : list) {
			System.out.println(acc.getEmail());
		}
	}
}	
