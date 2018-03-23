package com.gionee.gniflow.integration.dao;

import java.util.List;
import java.util.Map;

import com.gionee.auth.model.User;
import com.gionee.gniflow.dto.UserDto;

/**
 * 用于补充auth包提供的功能的不足
 * @author lihe
 * Oct 22, 2014
 * @version 1.0
 */
public interface GnifUserDao
{
	/**
	 * 通过指定参数查找相应的部门领导
	 * @param account
	 * @return
	 */
	public List<User> getLeadersByCurrentUser(Map<String,Object> map);
	
	/**
	 * 通过指定参数分页查找用户
	 * @param account
	 * @return
	 */
	public List<UserDto> queryPageUsers(Map<String,Object> map);
	/**
	 * 通过指定参数查找用户总数
	 * @param account
	 * @return
	 */
	public int countUsers(Map<String,Object> map);
	
	
	/**
	 * 根据account查找部门id
	 * @param account
	 * @return id
	 */
	String findById(String account);
	
	/**
	 * 根据职员id查找账号
	 * @param id
	 * @return
	 */
	String getAccountById(String id);
}
