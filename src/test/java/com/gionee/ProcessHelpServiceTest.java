package com.gionee;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.model.PageResult;
import com.gionee.gniflow.biz.service.ProcessHelpService;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.dto.UserDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class ProcessHelpServiceTest {

	@Autowired
	private ProcessHelpService processHelpService;
	
	@Test
	@Ignore
	public void testGetUserName(){
		String userName = processHelpService.getUserName("00001222");
		System.out.println("userName-->" + userName);
	}
	
	@Test
	@Ignore
	public void testGetUserEmail(){
		String email = processHelpService.getUserEmailAddress("00001222");
		System.out.println("email-->" + email);
	}
	
	@Test
	@Ignore
	public void testIsRegularEmployee(){
		Boolean result = processHelpService.isRegularEmployee("00001222");
		System.out.println("result-->" + result);
	}
	
	@Test
	@Ignore
	public void testIsMiddleManager(){
		Boolean result = processHelpService.isMiddleManager("00001222");
		System.out.println("result-->" + result);
	}
	
	@Test
	@Ignore
	public void testGetChairmanOrCEO(){
		String result = processHelpService.getChairmanOrCEO("00001222");
		System.out.println("result-->" + result);
	}
	
	@Test
	@Ignore
	public void testGetOrgLeader(){
		String result = processHelpService.getOrgLeader("00001021", "2");
		System.out.println("result-->" + result);
	}
	
	@Test
	@Ignore
	public void testGetOtherOrgLeader(){
		String result = processHelpService.getOtherOrgLeader("57707", "6");
		System.out.println("result-->" + result);
	}
	
	@Test
	@Ignore
	public void testQueryUser(){
		QueryMap map = new QueryMap();
		map.put("firstRow", 1);
		map.put("lastRow", 25);
		PageResult<UserDto> result = processHelpService.queryPageUsers(map);
		System.out.println("result-->" + result.getTotal());
	}
	
	public HrUserDto testGetEmpInfo(){
		HrUserDto userDto = new HrUserDto();
		
		return userDto;
	}
}
