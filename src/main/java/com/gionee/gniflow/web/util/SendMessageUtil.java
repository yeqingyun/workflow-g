package com.gionee.gniflow.web.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.gnif.util.PropertiesConfig;

//通过调用短信平台短信接口发送短信
public class SendMessageUtil {
	
	private static final String ISSENDMSG = PropertiesConfig.getString("message.enable");
	private static final String MSGURL = "http://msg.gionee.com/send_message/send1.json";
	private static final String MSGACCOUNT = "otherSys";
	private static final Logger logger = LoggerFactory.getLogger(SendMessageUtil.class);
	
	//参数说明:content:短信内容,mobile:手机号码
	public static void sendMessage(String content,String mobile) {
		//短信内容为空不予发送
		if(content == null || content.equals("")) {
			return;
		}
		
		//手机号码为空不予发送
		if(mobile == null || mobile.equals("")) {
			return;
		}
		
		//只在正式环境调接口发短信,本地、测试环境不发短信
		if(ISSENDMSG == null || !ISSENDMSG.equals("true")) {
			return;
		}

		HttpClientBuilder httpBuilder = HttpClientBuilder.create(); 
		CloseableHttpClient httpClient = httpBuilder.build();// 创建默认的httpClient实例	
		RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(20000);
		 
		HttpPost httpPost = new HttpPost(MSGURL); //发送短信接口,短信平台短信接口
		httpPost.setHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8");
		
		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("account", MSGACCOUNT); //具备发送短信权限的账号
		reqData.put("message_content", content);
		reqData.put("destination_number", mobile);
	
		try {
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			
			for (Map.Entry<String, String> entry : reqData.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			//设置编码
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpClient.execute(httpPost);

			logger.info("------message content:------" + content);
		} catch (ConnectTimeoutException cte) {
			cte.printStackTrace();
			logger.info("message send fail");
		} catch (SocketTimeoutException ste) {
			ste.printStackTrace();
			logger.info("message send fail");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("message send fail");
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
