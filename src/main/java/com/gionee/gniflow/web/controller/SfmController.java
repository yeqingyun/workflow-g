package com.gionee.gniflow.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSONObject;
import com.gionee.gnif.util.AppContext;
import com.gionee.gniflow.biz.service.impl.sfm.Supplier;
import com.gionee.gniflow.biz.sfm.DictionaryFile;
import com.gionee.gniflow.biz.sfm.InnerDictionaryFileTree;
import com.gionee.gniflow.biz.sfm.SfmService;
import com.gionee.gniflow.dto.sfm.SearchFile;
import com.gionee.gniflow.dto.sfm.SuppliersDto;
import com.gionee.gniflow.web.easyui.AjaxJson;
import com.gionee.gniflow.web.util.PropertyHolder;

@RequestMapping("/crossdomain")
@Controller
public class SfmController {
	
	@Autowired
	private SfmService sfmService;
	
	@RequestMapping("/hrUser")
	@ResponseBody
	public List hrUser(HttpServletRequest request, Integer page, Integer rows,String username) throws Exception{
		List<Object> innerUsers = new ArrayList();
		String result = "";
	    BufferedReader in = null;
	    String urlNameString = PropertyHolder.getContextProperty("sfm.url")+"/api/hrUser?page=1&rows=50&username="+username+"&account="+AppContext.getCurrentAppUser().getAccount();
		try {
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", request.getHeader("user-agent"));
			//System.out.println(request.getHeader("user-agent"));
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
			connection.getInputStream(),"utf8"));
			String line;
			while ((line = in.readLine()) != null) {
			    result += line;
			}
		} finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(result!=null&&!result.equals("")){
			JSONParser parser = new JSONParser(result);
			innerUsers = parser.parseArray();
		}
		return innerUsers;
		
	}
	
	@RequestMapping("/listFile")
	@ResponseBody
	public Collection<InnerDictionaryFileTree> listFile(HttpServletRequest request,String path) throws Exception{
		String result = "";
	    BufferedReader in = null;
	    String urlNameString;
	    if(path==null||path.trim().equals("")){
	    	urlNameString = PropertyHolder.getContextProperty("sfm.url")+"/api/listFile?account="+AppContext.getCurrentAppUser().getAccount();
	    }else{
	    	urlNameString = PropertyHolder.getContextProperty("sfm.url")+"/api/listFile?account="+AppContext.getCurrentAppUser().getAccount()+"&path="+path;
	    }
	    
		try {
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", request.getHeader("user-agent"));
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
			connection.getInputStream(),"utf8"));
			String line;
			while ((line = in.readLine()) != null) {
			    result += line;
			}
		} finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		Map<String, InnerDictionaryFileTree> map = new HashMap<String, InnerDictionaryFileTree>();
		if(result!=null&&!result.equals("")){
			List<DictionaryFile> dictionaryFiles = JSONObject.parseArray(result, DictionaryFile.class);
			for(DictionaryFile dictionaryFile : dictionaryFiles){
				if(map.containsKey(dictionaryFile.getPid())){
					InnerDictionaryFileTree innerDictionaryFileTreeP = map.get(dictionaryFile.getPid());
					InnerDictionaryFileTree innerDictionaryFileTree = new InnerDictionaryFileTree();
					innerDictionaryFileTree.setId(dictionaryFile.getId());
					innerDictionaryFileTree.addAttribute("path", dictionaryFile.getPath());
					if(dictionaryFile.getIsParent()){
						innerDictionaryFileTree.addAttribute("isDir", true);
						innerDictionaryFileTree.setChildren(new ArrayList<InnerDictionaryFileTree>());
						innerDictionaryFileTree.setText(dictionaryFile.getName());
					}else{
						innerDictionaryFileTree.addAttribute("isDir", false);
						innerDictionaryFileTree.setText(dictionaryFile.getName());
					}
					innerDictionaryFileTree.setState("open");
					innerDictionaryFileTreeP.addChildren(innerDictionaryFileTree);
				}else{
					InnerDictionaryFileTree innerDictionaryFileTree = new InnerDictionaryFileTree();
					innerDictionaryFileTree.setId(dictionaryFile.getId());
					innerDictionaryFileTree.addAttribute("path", dictionaryFile.getPath());
					if(dictionaryFile.getIsParent()){
						innerDictionaryFileTree.setChildren(new ArrayList<InnerDictionaryFileTree>());
						innerDictionaryFileTree.setState("closed");
						innerDictionaryFileTree.addAttribute("isDir", true);
						innerDictionaryFileTree.setText(dictionaryFile.getName());
					}else{
						innerDictionaryFileTree.addAttribute("isDir", false);
						innerDictionaryFileTree.setState("open");
						innerDictionaryFileTree.setText(dictionaryFile.getName());
					}
					map.put(innerDictionaryFileTree.getId(), innerDictionaryFileTree);
				}
			}
		}
		
		Collection<InnerDictionaryFileTree> collections = map.values();
		
		return collections;
	}
	
	@RequestMapping("/getEmailAddr")
	@ResponseBody
	public SuppliersDto getEmailAddr(Integer page, Integer rows){
		List<Supplier> suppliers = sfmService.getSuppliersInfo(page,rows);
		int total = sfmService.getCount();
		SuppliersDto suppliersDto = new SuppliersDto();
		suppliersDto.setRows(suppliers);
		suppliersDto.setTotal(total);
		return suppliersDto;
	}
	
	@RequestMapping("/isSafeEmail")
	public AjaxJson isSafeEmail(String email){
		AjaxJson ajaxJson = new AjaxJson(false);
		Supplier supplier = sfmService.getSupplierByEmail(email);
		if(supplier==null){
			ajaxJson.setSuccess(false);
		}else{
			ajaxJson.setSuccess(true);
		}
		return ajaxJson;
	}
	

	@RequestMapping("/addSupplier")
	public AjaxJson addSupplier(Supplier supplier){
		AjaxJson ajaxJson = sfmService.addSupplier(supplier);
		return ajaxJson;
	}
	
	@RequestMapping("/searchFiles")
	@ResponseBody
	public List<SearchFile> searchFiles(HttpServletRequest request, Integer page,Integer rows,String fileName) throws Exception{
		List<SearchFile> files = new ArrayList<SearchFile>();
		String result = "";
	    BufferedReader in = null;
		String urlNameString = PropertyHolder.getContextProperty("sfm.url")+"/api/searchFiles?account="+AppContext.getCurrentAppUser().getAccount()+"&fileName="+fileName;
		try {
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", request.getHeader("user-agent"));
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
			connection.getInputStream(),"utf8"));
			String line;
			while ((line = in.readLine()) != null) {
			    result += line;
			}
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(result!=null&&!result.equals("")){
			List<DictionaryFile> dictionaryFiles = JSONObject.parseArray(result, DictionaryFile.class);
			for(DictionaryFile dictionaryFile : dictionaryFiles){
				int spiltIndex = dictionaryFile.getDispPath().lastIndexOf("\\");
				files.add(new SearchFile(dictionaryFile.getPath(), dictionaryFile.getDispPath().substring(spiltIndex+1, dictionaryFile.getDispPath().length())+" : "+dictionaryFile.getDispPath().substring(0, spiltIndex)));
			}
		}
		
		return files;
	}
	
}
