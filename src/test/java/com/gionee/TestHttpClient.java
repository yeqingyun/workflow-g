/**
 * @(#) TestHttpClient.java Created on 2014年5月26日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gionee.gniflow.biz.model.SignFileInformation;
import com.gionee.gniflow.dto.HrUserDto;
import com.gionee.gniflow.web.util.DateUtil;
import com.gionee.gniflow.web.util.HttpClientUtil;

/**
 * The class <code>TestHttpClient</code>
 * 
 * @author lipw
 * @version 1.0
 */
public class TestHttpClient {
	
	@Test
	@Ignore
	public void testHttpClient() {
		Map<String, String> reqData = new HashMap<String,String>();
		reqData.put("project_name", "sdsd");
		reqData.put("description", "This is content!");
		reqData.put("status", "7");
		HttpClientUtil.sendPostRequest("http://192.168.119.92:8080/pms/plan/addProjectPlan.json", reqData, "UTF-8");
//		HttpClientUtil.sendPostRequest("http://gservice.gionee.com", reqData, "UTF-8");
	}
	
	@Test
	@Ignore
	public void testPmsGetProLeader() {
//		String reqUrl = PropertyHolder.getContextProperty(WebReqConstant.PMS_URL_PROJECT_LEADER);
		String reqUrl = "http://192.168.119.92:8080/pms/project/getPrincipal.json";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("id", "10");
		String result = HttpClientUtil.sendPostRequest(reqUrl, reqData, "UTF-8");
		try {
			JSONObject json = JSON.parseObject(result);
			System.out.println(json.get("account"));
			System.out.println(json.get("name"));
		} catch (Exception e){
			e.printStackTrace();
			result = null;
		}
		System.out.println("结果是： -->" + result);
	}
	
	@Test
	@Ignore
	public void testJson(){
//		String result = "[{'id':1,'text':'Folder1','iconCls':'icon-save','children':[{'text':'File1','checked':true}]}]";
		String result = HttpClientUtil.sendGetRequest("http://192.168.119.92:8080/pms/project/getTree.json");
		List<Map<String, Object>> listMap = JSON.parseObject(result, new TypeReference<List<Map<String,Object>>>(){});
		System.out.println(listMap);
	}
	
	@Test
	@Ignore
	public void testreceivePMSResult(){
		String url = "http://localhost:8080/gniflow/flowService/receiveProjectReuslt.json";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("processInstanceId", "4301");
		reqData.put("feedBack", "This is feedBack content!");
		
		HttpClientUtil.sendPostRequest(url, reqData, "UTF-8");
		
	}
	
	@Test
	@Ignore
	public void testGetPPMMUsers(){
		String url = "http://192.168.119.3:8080/ppmm/bpm/queryUser.json";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("type", "PROTOTYPE_ADMINISTRATOR");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData, "UTF-8");
//		String result = "{\"stringList\":[\"ali\",\"pengbin\"]}";
		JSONObject json = JSON.parseObject(result);
		List<String> list = JSONArray.parseArray(json.get("stringList").toString(),  String.class);
		System.out.println("result-->" + result);
		System.out.println("list-->" + list);
		
	}
	
	@Test
	@Ignore
	public void testNotifyPPMM(){
		String url = "http://192.168.119.3:8080/ppmm/bpm/notify.json";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("bor_no", "vvvvvvvvvvv");
		reqData.put("leaderAudi", "1");
		reqData.put("prototypeAudi", "1");
		reqData.put("leaderAssignee", "00000065");
		reqData.put("prototypeAssignee", "ali");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData, "UTF-8");
		System.out.println("Post result===" + result);
		
	}
	
	@Test
	@Ignore
	public void testStartProcess(){
		String url = "http://192.168.119.4:8080/gniflow/myflow/subSystem/startProcess.json";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("processDefId", "borrow-material:8:3936");//必须
		reqData.put("fp_borrowNo", "XCXCXC");
		reqData.put("fp_currentUserId", "ali");
		reqData.put("fp_currentUserName", "阿里");
		reqData.put("fp_borrowDate", "2014-06-27");
		reqData.put("fp_returnDate", "2014-06-27");
		reqData.put("fp_remark", "remark");
		reqData.put("fp_item", "<table><tr><td>ddddddddddddddd</td></tr></table>");
		
//		String result = HttpClientUtil.sendMultipartFormPost(url, reqData, null,"UTF-8");
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		
		System.out.println("Post result===" + result);
		
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	@Ignore
	public void testStrResolveJson(){
		String josn = "{\"total\":6,\"rows\":[{\"productId\":\"1\",\"productName\":\"铅笔\",\"price\":\"10\",\"code\":\"0001\",\"color\":\"red\",\"fileName\":\"aaa.txt\",\"fileNo\":\"H0014000129\"},{\"productId\":\"2\",\"productName\":\"钢笔\",\"price\":\"21\",\"code\":\"0002\",\"color\":\"blue\"},{\"productId\":\"3\",\"productName\":\"圆珠笔\",\"price\":\"5.5\",\"code\":\"0003\",\"color\":\"red\"},{\"productId\":\"4\",\"productName\":\"水彩笔\",\"price\":\"2\",\"code\":\"0004\",\"color\":\"blue\"},{\"productId\":\"5\",\"productName\":\"签字笔\",\"price\":\"12.5\",\"code\":\"0005\",\"color\":\"red\"},{\"productId\":\"6\",\"productName\":\"毛笔\",\"price\":\"8.5\",\"code\":\"0006\",\"color\":\"blue\"}]}";
		JSONObject jsonObject = JSON.parseObject(josn);
		List<Map> lists = JSONArray.parseArray(jsonObject.get("rows").toString(),  Map.class);
		for (Map temp : lists) {
			Iterator entries = temp.entrySet().iterator();
			while (entries.hasNext()) {
			    Map.Entry entry = (Map.Entry) entries.next();
			    String key = (String)entry.getKey();
			    String value = (String)entry.getValue();
			    System.out.println("Key = " + key + ", Value = " + value);
			}
			System.out.println("***********************");
		}
		
		System.out.println(JSONObject.toJSONString(lists));
	}
	
	@Test
//	@Ignore
	public void testEmp(){
		String url = "http://192.168.119.182:7001/Hr/Emp!getEmpInfo.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("emp.empId", "00001300");//必须
		
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		
		JSONObject jsonObj =JSONArray.parseObject(result);
		HrUserDto userDto = new HrUserDto();
		userDto.setAge(jsonObj.getInteger("age"));
		
		String sex = jsonObj.getString("gender");
		if (sex.equals("1")) {
			userDto.setSex("男");
		} else if (sex.equals("2")) {
			userDto.setSex("女");
		}
		userDto.setPosition(jsonObj.getString(""));
        userDto.setJob(jsonObj.getString("jobNm"));
        
        userDto.setEntryDate(DateUtil.parse(jsonObj.getString("inDate"), "yyyy-MM-dd"));
        
        userDto.setEducation(jsonObj.getString("strong"));
        userDto.setNational(jsonObj.getString("natioNm"));
        userDto.setMajor(jsonObj.getString("major"));
        userDto.setMaritalStatus(jsonObj.getString("maritalNm"));
	}
	
	@Test
	@Ignore
	public void testDocument(){
		String url = "http://192.168.119.182:7777/Hr/Document!getDocumentInfo.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("document.docName", "t");//必须
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
		List<SignFileInformation> fileList = new ArrayList<SignFileInformation>();
//		String result = "[{\"categoryCode\":\"A\",\"comId\":56877,\"comNm\":\"深圳市金立通信设备有限公司\",\"createBy\":1,"
//				+ "\"createDate\":\"2014-08-30 09:36\",\"deptId\":56905,\"deptNm\":\"市场传播中心\",\"docDesc\":\"A\","
//				+ "\"docId\":3,\"docKey\":\"Test001\",\"docName\":\"Test001\",\"docNo\":\"Test001\","
//				+ "\"issueDate\":\"2014-08-29 00:00\",\"lastUpd\":1,\"lastUpdDate\":\"2014-08-30 09:36\","
//				+ "\"onLineUri\":\"文档需求.docx\",\"parentId\":0,\"remark\":\"\",\"reviewBy\":0,\"status\":1,"
//				+ "\"uri\":\"/hr/upload/document/201408/root/文档需求.docx\",\"version\":\"A01\"}]";
		
		JSONArray myArray =JSONArray.parseArray(result);
		JSONObject jsonObj = null;
		SignFileInformation signFileInfo = null;
        for(int i=0;i<myArray.size();i++) {
        	jsonObj = myArray.getJSONObject(i);
        	signFileInfo = new SignFileInformation();
        	signFileInfo.setCompanyId(jsonObj.getInteger("comId"));
        	signFileInfo.setCompanyName(jsonObj.getString("comNm"));
        	signFileInfo.setOrgId(jsonObj.getInteger("deptId"));
        	signFileInfo.setOrgName(jsonObj.getString("deptNm"));
        	signFileInfo.setCreateBy(jsonObj.getString("createByNo"));
        	signFileInfo.setCreateTime(DateUtil.parse(jsonObj.getString("createDate"), "yyyy-MM-dd HH:mm"));
        	signFileInfo.setCreaterName(jsonObj.getString("createByNm"));
        	
        	signFileInfo.setFileName(jsonObj.getString("docName"));
        	signFileInfo.setFileNo(jsonObj.getString("docNo"));
        	signFileInfo.setFileType(jsonObj.getString("categoryCode"));
        	
        	String oaVersion = jsonObj.getString("version");
        	signFileInfo.setFileVersion(oaVersion.substring(0,1));
        	signFileInfo.setFileVersionState(oaVersion.substring(1, oaVersion.length()));
        	
        	fileList.add(signFileInfo);
        }
		System.out.println(fileList.size());
	}
	
	@Test
	@Ignore
	public void testSyncDocument(){
		String url = "http://192.168.119.182:7777/Hr/Document!workFlow2OA.shtml";
		Map<String,String> reqData = new HashMap<String,String>();
		reqData.put("document.comId", "t");
		reqData.put("document.deptId", "t");
		reqData.put("document.docNo", "t");
		reqData.put("document.docName", "t");
		reqData.put("document.version", "t");
		reqData.put("document.fileNo", "t");
		reqData.put("document.status", "t");
		String result = HttpClientUtil.sendPostRequest(url, reqData,"UTF-8");
		System.out.println("Post result===" + result);
		
//		String url = "http://localhost:7001/Hr/Document!workFlow2OA.shtml?"
//		+ "document.comId=56877&document.deptId=56905&document.docNo=TestV1.0"
//		+ "&document.docName=ceshi&document.version=00&document.fileNo=F0014000358"
//		+ "&document.status=1";
		
	}
}
