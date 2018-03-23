package com.gionee;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gniflow.biz.model.ProAccountHandleInfo;
import com.gionee.gniflow.biz.service.ProAccountHandleInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class ProAccountHandleInfoServiceTest {

	@Autowired
	private ProAccountHandleInfoService proAccountHandleInfoService;
	
	@Test
//	@Ignore
	public void testGetUserName(){
		QueryMap queryMap = new QueryMap();
    	queryMap.put("startUserAccount", "00001222");
    	queryMap.put("status", ProAccountHandleInfo.STATUS_NORMAL);
    	List<ProAccountHandleInfo> accountHandleInfos = proAccountHandleInfoService.query(queryMap);
		System.out.println("userName-->" + accountHandleInfos.size());
	}
	
}
