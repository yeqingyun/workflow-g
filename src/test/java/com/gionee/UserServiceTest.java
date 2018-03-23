package com.gionee;

import java.util.Arrays;
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

import com.gionee.auth.PrivilegeService;
import com.gionee.auth.UserService;
import com.gionee.auth.dao.ResourceDao;
import com.gionee.auth.model.Privilege;
import com.gionee.auth.model.Resource;
import com.gionee.auth.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:biz-context.xml")
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Test
	@Ignore
	public void testGetUserPri() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("account", "ali");
		List<User> users = userService.queryUsers(params);
		User user = null;
		Integer orgId = null;
		List<User> orgUsers = null;
		if (users != null && users.size() > 0) {
			user = users.get(0);
			orgId = user.getOrgId();
			//查找部分下的用户
			if (orgId != null) {
				orgUsers = userService.getUsersByOrgId(orgId);
			}
			if (orgUsers != null) {
				for (User tempUser : orgUsers) {
					if (tempUser.getPriGroupId() != null) {
						Privilege privilege = privilegeService.getPrivilegeByKey(tempUser.getPriGroupId(), "deptLeader");
						String value = privilege.getPriValue();
						Assert.assertEquals("1", value);
						if (value.equals("1")) {
							System.out.println("Account--->" + tempUser.getAccount());
							break;
						}
					}
				}
			}
		}
	}
	
	@Test
	@Ignore
	public void testGetResource(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", "27034");
		param.put("pid", 0);
		param.put("types", Arrays.asList(new Integer[]{Resource.TYPE_MENU}));
		List<Resource> list = resourceDao.getUserResources(param);
		System.out.println(list.size());
	}
	
	@Test
	public void testGetUserName(){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("account", "00001222");
		List<User> users = userService.queryUsers(param);
		for (User user : users) {
			System.out.println(user.getName());
		}
	}

}
