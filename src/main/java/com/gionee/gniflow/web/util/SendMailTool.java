package com.gionee.gniflow.web.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.gionee.gnif.mail.biz.base.MailSender;
import com.alibaba.fastjson.JSONObject;

import com.gionee.gnif.util.PropertiesConfig;

//通过调用短信平台邮件接口发送邮件
public class SendMailTool {
	
	private static final String ISSENDMAIL = PropertiesConfig.getString("message.enable");
	private static final Logger logger = LoggerFactory.getLogger(SendMailTool.class);
	
	//参数说明:title:邮件主题,content:邮件内容,addressList:收件人邮箱地址,可传多个中间以逗号分隔,该方法仅发送在hr通讯录中的邮箱地址(邮箱地址是从hr通讯录中读取的)
	public static void sendMailByAddress(String title,String content,String addressList) {
		//邮件主题、邮件内容同时为空时不予发送
		if((title == null || title.equals("")) && (content == null || content.equals(""))) {
			return;
		}
		
		//收件人邮箱地址为空时不予发送
		if(addressList == null || addressList.equals("")) {
			return;
		}
		
		//只在正式环境调接口发邮件,本地、测试环境不发邮件
		if(ISSENDMAIL == null || !ISSENDMAIL.equals("true")) {
			return;
		}
		
		HttpClientBuilder httpBuilder = HttpClientBuilder.create(); 
		CloseableHttpClient httpClient = httpBuilder.build();// 创建默认的httpClient实例	
		RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(20000);
		
		String reqURL = "http://msg.gionee.com/send_mail/sendByAddress.json";  //短信平台邮件接口
		HttpPost httpPost = new HttpPost(reqURL);
		httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8");
		
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("title", title);
		reqData.put("content", content);
		reqData.put("addressList", addressList);
		
		String reseContent = "";
				
		try {
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			
			for (Map.Entry<String, String> entry : reqData.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			
			//设置编码
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
				EntityUtils.consume(entity);
			}
			
			logger.info("------mail content:------" + content);
		} catch (ConnectTimeoutException cte) {
			cte.printStackTrace();
		} catch (SocketTimeoutException ste) {
			ste.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		try {
			JSONObject jsonObj = JSONObject.parseObject(reseContent);
			if(!(jsonObj != null && jsonObj.getBoolean("success") != null && jsonObj.getBoolean("success") == true)) {
				MailSender.sendByAddress(addressList, title, content, 0);	
			}
		} catch(Exception e) {
			e.printStackTrace();
			MailSender.sendByAddress(addressList, title, content, 0);
		}		
	}
	
	//参数说明:title:邮件主题,content:邮件内容,addressList:收件人邮箱地址,可传多个中间以逗号分隔,该方法可发送不在hr通讯录中的邮箱地址(邮箱地址是在程序中固定的,如外部邮箱地址)
	public static void sendMailByAddressOnly(String title,String content,String addressList) {
		//邮件主题、邮件内容同时为空时不予发送
		if((title == null || title.equals("")) && (content == null || content.equals(""))) {
			return;
		}
		
		//收件人邮箱地址为空时不予发送
		if(addressList == null || addressList.equals("")) {
			return;
		}

		//只在正式环境调接口发邮件,本地、测试环境不发邮件
		if(ISSENDMAIL == null || !ISSENDMAIL.equals("true")) {
			return;
		}
		
		HttpClientBuilder httpBuilder = HttpClientBuilder.create(); 
		CloseableHttpClient httpClient = httpBuilder.build();// 创建默认的httpClient实例	
		RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(20000);
		
		String reqURL = "http://msg.gionee.com/send_mail/sendByAddressOnly.json";  //短信平台邮件接口
		HttpPost httpPost = new HttpPost(reqURL);
		httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8");
		
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("title", title);
		reqData.put("content", content);
		reqData.put("addressList", addressList);
		
		String reseContent = "";
				
		try {
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			
			for (Map.Entry<String, String> entry : reqData.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			
			//设置编码
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
				EntityUtils.consume(entity);
			}
			
			logger.info("------mail content:------" + content);
		} catch (ConnectTimeoutException cte) {
			cte.printStackTrace();
		} catch (SocketTimeoutException ste) {
			ste.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		try {
			JSONObject jsonObj = JSONObject.parseObject(reseContent);
			if(!(jsonObj != null && jsonObj.getBoolean("success") != null && jsonObj.getBoolean("success") == true)) {
				MailSender.saveAddressOnly(addressList, title, content, 0);
			}
		} catch(Exception e) {
			e.printStackTrace();
			MailSender.saveAddressOnly(addressList, title, content, 0);
		}		
	}

}
