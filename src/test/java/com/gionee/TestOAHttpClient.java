/**
 * @(#) TestHttpClient.java Created on 2014年5月26日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.easyui.ComboBoxData;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.HttpClientUtil;

/**
 * The class <code>TestHttpClient</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class TestOAHttpClient {
	
	@Test
	@Ignore
	public void testGetDeptLeader(){
		String url = "http://hr.gionee.com/Usr!findOrgLeader.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("usr.login", "02000335");
		reqData.put("usr.wfRule", "6");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
	}
	
	@Test
	@Ignore
	public void testGetOtherDeptLeader(){
		String url = "http://hr.gionee.com/Usr!findOrgLeaderByOrgNo.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("orgId", "57277");
		reqData.put("usr.wfRule", "1");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
	}
	
	@Test
//	@Ignore
	public void testGetUserInfo(){
		String url = "http://16.6.10.24:7001/Hr/Emp!getEmpInfo.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("emp.empId", "00001021");//必须
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
		JSONObject jsonObj = JSONArray.parseObject(result);
		HrUserDto userDto = new HrUserDto();
		userDto.setAge(jsonObj.getInteger("age"));//年龄
		
		String sex = jsonObj.getString("gender");//性别
		if (sex.equals("1")) {
			userDto.setSex("男");
		} else if (sex.equals("2")) {
			userDto.setSex("女");
		}
		userDto.setPosition(jsonObj.getString("orgNm"));//
        userDto.setJob(jsonObj.getString("jobNm"));//岗位
        
        userDto.setEntryDate(DateUtil.parse(jsonObj.getString("inDate"), "yyyy-MM-dd"));//入职日期
        
        System.out.println(jsonObj.getString("school"));
        userDto.setEducation(jsonObj.getString("school"));//学历
        userDto.setNational(jsonObj.getString("natioNm"));//民族
        userDto.setMajor(jsonObj.getString("major"));//专业
        userDto.setMaritalStatus(jsonObj.getString("maritalNm"));//婚姻状况
        
        userDto.setBirthdayDate(DateUtil.parse(jsonObj.getString("birthday"), "yyyy-MM-dd"));
        userDto.setNativePlace(jsonObj.getString("brithAdd"));//籍贯
        
        Integer isAttend = jsonObj.getInteger("isAttend");//是否考勤人员
        if (isAttend != null) {
        	if (isAttend == 1) {
        		userDto.setIsAttend("是");
        	}
        	if (isAttend == 2) {
        		userDto.setIsAttend("否");
        	}
        }
        userDto.setEmail(jsonObj.getString("email"));//邮箱
        userDto.setCompanyName(jsonObj.getString("scoNm"));//所属公司
        userDto.setTel(jsonObj.getString("tel"));//联系电话
        
        Integer status = jsonObj.getInteger("status");//状态-0-离职，1-不活动，2-退休，3-在职
        if(status != null){
        	switch (status) {
			case 0:
				userDto.setStatus("离职");
		        userDto.setDepartureDate(DateUtil.parse(jsonObj.getString("leftDate"), "yyyy-MM-dd"));//离职日期
				break;
			case 1:
				userDto.setStatus("不活动");
				break;
			case 2:
				userDto.setStatus("退休");
				break;
			case 3:
				userDto.setStatus("在职");
				break;
			default:
				break;
			}
        }
        //身份证
        JSONArray jsonArray =  jsonObj.getJSONArray("empCreds");
        for (int i = 0; i < jsonArray.size(); i++) {
        	JSONObject curJsonObj = (JSONObject) jsonArray.get(i);
        	if (curJsonObj != null && curJsonObj.getString("credTypNm").equals("身份证")) {
        		userDto.setIdCard(curJsonObj.getString("credNo"));
        	}
		}
        
        //人员工作地点
        Integer value = jsonObj.getInteger("scoChrNo");
        if (value == 1000) {
        	userDto.setWorkPlace("深圳");
        } else if (value == 2000){
        	userDto.setWorkPlace("东莞");
        } else if (value == 3000) {
        	userDto.setWorkPlace("北京");
        }
        
        
        //不常用
        userDto.setHouseholdRegAddress(jsonObj.getString("regAddr"));//户籍地址
        userDto.setResidenceAddress(jsonObj.getString("regNatureNm"));//户口类型-如：外地非农户口regNatureNm
        userDto.setEnLevel(jsonObj.getString("englishLevel"));//英语等级
        userDto.setComputerLevel(jsonObj.getString("computer"));//计算机等级
        
        System.out.println(userDto);
	}
	
	/** 判断是否转正 */
	@Test
	@Ignore
	public void testisPositive(){
		String url = "http://hr.gionee.com/Usr!isRegularEmp.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("usr.empId", "00001021");
		reqData.put("usr.wfRule", "1");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
	}
	
	/** 判断是否中层管理人员 */
	@Test
	@Ignore
	public void testisMiddleManager(){
		String url = "http://hr.gionee.com/Usr!isMidManager.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("usr.empId", "00001021");
		reqData.put("usr.wfRule", "1");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
	}
	
	/** 获取公司下的系统名称 */
	@Test
	@Ignore
	public void testGetSystemInfo(){
		String url = "http://16.6.10.24:7001/PosDeptSys!getDeptSys.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("comId", "56877");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
	}
	
	/** 获取系统下的部门名称 */
	@Test
	@Ignore
	public void testGetDeptInfo(){
		String url = "http://16.6.10.24:7001/PosDeptSys!getDepts.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("comId", "56877");
		reqData.put("sysNo", "06");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
	}
	
	/** 获取系统下的部门名称 */
	@Test
	@Ignore
	public void testGetJobInfo(){
		String url = "http://16.6.10.24:7001/PosDeptSys!getNullPos.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("deptId", "57707");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		List<ComboBoxData> dataList = new ArrayList<ComboBoxData>();
		JSONArray jsonArray = JSONArray.parseArray(result);
		 for (int i = 0; i < jsonArray.size(); i++) {
	        JSONObject curJsonObj = (JSONObject) jsonArray.get(i);
	        dataList.add(new ComboBoxData(curJsonObj.getString("orgId"), curJsonObj.getString("orgNm")));
		}
		System.out.println(dataList);
	}
}
