package com.gionee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gniflow.biz.model.SignOrgCode;
import com.gionee.gniflow.integration.dao.ExportLeaveInfoDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class TestExportLeaveInfoDao {
	@Autowired
	ExportLeaveInfoDao exportLeaveInfoDao;
	@Test
	public void testQuerySignOrgCode(){
		HashMap<String, Object> map=new HashMap<>();
//		List<Map> list = exportLeaveInfoDao.getLeaveInfo(map);
//		System.out.println(1);
	}
}
