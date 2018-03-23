/**
 * @(#) HttpClientUtil.java Created on 2014年5月26日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.gniflow.web.util;

import java.io.File;
import java.io.IOException;  
import java.net.SocketTimeoutException;  
import java.nio.charset.Charset;  
  
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;  
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ConnectTimeoutException;  
import org.apache.http.entity.ContentType;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The class <code>HttpClientUtil</code>
 * 
 * @version 1.0
 */
public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private HttpClientUtil() {

	}

	/**
	 * 发送HTTP_GET请求
	 * 
	 * @see 1)该方法会自动关闭连接,释放资源
	 * @see 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
	 * @see 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
	 * @see 4)该方法会自动获取到响应消息头中[Content-Type:text/html;charset=GBK]的charset值作为响应报文的解码字符集
	 * @see 若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
	 * @param requestURL
	 *            请求地址(含参数)
	 * @return 远程主机响应正文
	 */
	public static String sendGetRequest(String reqURL) {
		String respContent = "通信失败"; // 响应内容
		
		HttpClientBuilder httpBuilder = HttpClientBuilder.create(); 
		CloseableHttpClient httpClient = httpBuilder.build();// 创建默认的httpClient实例
		
		//连接超时10s      读取超时20s
		RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(20000);

		HttpGet httpGet = new HttpGet(reqURL); // 创建org.apache.http.client.methods.HttpGet
		try {
			HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (null != entity) {
				Charset respCharset = ContentType.getOrDefault(entity)
						.getCharset();
				respContent = EntityUtils.toString(entity, respCharset);
				// Consume response content
				EntityUtils.consume(entity);
			}
			
			logger.debug("-------------------------------------------------------------------------------------------");
			
			StringBuilder respHeaderDatas = new StringBuilder();
			for (Header header : response.getAllHeaders()) {
				respHeaderDatas.append(header.toString()).append("\r\n");
			}
			String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
			String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
			String respBodyMsg = respContent; // HTTP应答报文体信息
			logger.debug("HTTP应答完整报文=[" + respStatusLine + "\r\n"
					+ respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");
			logger.debug("-------------------------------------------------------------------------------------------");
		} catch (ConnectTimeoutException cte) {
			// Should catch ConnectTimeoutException, and don`t catch
			// org.apache.http.conn.HttpHostConnectException
			logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
		} catch (SocketTimeoutException ste) {
			logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
		} catch (ClientProtocolException cpe) {
			// 该异常通常是协议错误导致:比如构造HttpGet对象时传入协议不对(将'http'写成'htp')or响应内容不符合HTTP协议要求等
			logger.error("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下", cpe);
		} catch (ParseException pe) {
			logger.error("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下", pe);
		} catch (IOException ioe) {
			// 该异常通常是网络原因引起的,如HTTP服务器未启动等
			logger.error("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下", ioe);
		} catch (Exception e) {
			logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
		} finally {
			// 关闭连接,释放资源
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return respContent;
	}

	/**
	 * 发送HTTP_POST请求
	 * 
	 * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
	 * @see 2)该方法会自动关闭连接,释放资源
	 * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
	 * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
	 * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html;
	 *      charset=GBK]的charset值
	 * @see 若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
	 * @param reqURL
	 *            请求地址
	 * @param reqData
	 *            请求参数
	 * @param encodeCharset
	 *            编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
	 * @return 远程主机响应正文
	 */
	public static String sendPostRequest(String reqURL, Map<String, String> reqData,
			String encodeCharset) {
		String reseContent = "通信失败";
		HttpClientBuilder httpBuilder = HttpClientBuilder.create(); 
		CloseableHttpClient httpClient = httpBuilder.build();// 创建默认的httpClient实例
		
		RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(20000);
		
		HttpPost httpPost = new HttpPost(reqURL);
		
		httpPost.setHeader(HTTP.CONTENT_TYPE,
				"application/x-www-form-urlencoded; charset=" + encodeCharset);
		try {
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			
			for (Map.Entry<String, String> entry : reqData.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			//设置编码
			httpPost.setEntity(new UrlEncodedFormEntity(params, encodeCharset));
			
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
				EntityUtils.consume(entity);
			}
			
			StringBuilder respHeaderDatas = new StringBuilder();
			for (Header header : response.getAllHeaders()) {
				respHeaderDatas.append(header.toString()).append("\r\n");
			}
			String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
			String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
			logger.debug("HTTP应答完整报文=[" + respStatusLine + "\r\n"
					+ respHeaderMsg + "\r\n\r\n" + reseContent + "]");
			logger.debug("-------------------------------------------------------------------------------------------");
		} catch (ConnectTimeoutException cte) {
			logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
		} catch (SocketTimeoutException ste) {
			logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
		} catch (Exception e) {
			logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reseContent;
	}
	
	/**
	 * 发送HTTP_POST请求
	 * 
	 * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
	 * @see 2)该方法会自动关闭连接,释放资源
	 * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
	 * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
	 * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html;
	 *      charset=GBK]的charset值
	 * @see 若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
	 * @param reqURL
	 *            请求地址
	 * @param reqData
	 *            请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
	 * @param encodeCharset
	 *            编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
	 * @return 远程主机响应正文
	 */
	public static String sendPostRequest(String reqURL, String reqData,
			String encodeCharset) {
		String reseContent = "通信失败";
		HttpClientBuilder httpBuilder = HttpClientBuilder.create(); 
		CloseableHttpClient httpClient = httpBuilder.build();// 创建默认的httpClient实例
		
		RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(20000);
		
		HttpPost httpPost = new HttpPost(reqURL);
		
		httpPost.setHeader(HTTP.CONTENT_TYPE,
				"application/x-www-form-urlencoded; charset=" + encodeCharset);
		try {
			httpPost.setEntity(new StringEntity(reqData == null ? "" : reqData,
					encodeCharset));
			
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
				EntityUtils.consume(entity);
			}
			
			StringBuilder respHeaderDatas = new StringBuilder();
			for (Header header : response.getAllHeaders()) {
				respHeaderDatas.append(header.toString()).append("\r\n");
			}
			String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
			String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
			logger.debug("HTTP应答完整报文=[" + respStatusLine + "\r\n"
					+ respHeaderMsg + "\r\n\r\n" + reseContent + "]");
			logger.debug("-------------------------------------------------------------------------------------------");
		} catch (ConnectTimeoutException cte) {
			logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
		} catch (SocketTimeoutException ste) {
			logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
		} catch (Exception e) {
			logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reseContent;
	}
	
	/**
	 * 发送MultipartForm请求
	 * 
	 * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
	 * @see 2)该方法会自动关闭连接,释放资源
	 * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
	 * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
	 * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html;
	 *      charset=GBK]的charset值
	 * @see 若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
	 * @param reqURL
	 *            请求地址
	 * @param reqData
	 *            请求参数
	 * @param encodeCharset
	 *            编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
	 * @return 远程主机响应正文
	 */
	public static String sendMultipartFormPost(String reqURL, Map<String, String> reqData,
			Map<String, String> files,String encodeCharset) {
		String reseContent = "通信失败";
		HttpClientBuilder httpBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpBuilder.build();// 创建默认的httpClient实例

		RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(20000);

		HttpPost httpPost = new HttpPost(reqURL);

		httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);
		try {
			Iterator<Map.Entry<String, String>> iterator = reqData.entrySet().iterator();
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntityBuilder.setCharset(Charset.forName(encodeCharset));
			// 发送的数据
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				multipartEntityBuilder.addTextBody(
						entry.getKey(),
						entry.getValue(),
						ContentType.create("text/plain",Charset.forName(encodeCharset)));
			}
			// 发送的文件
			if (files != null) {
				iterator = files.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, String> entry = iterator.next();
					String path = entry.getValue();
					if ("".equals(path) || path == null)
						continue;
					File file = new File(entry.getValue());
					multipartEntityBuilder.addBinaryBody(entry.getKey(), file);
				}
			}
			// 生成 HTTP 实体
			HttpEntity httpEntity = multipartEntityBuilder.build();
			// 设置 POST 请求的实体部分
			httpPost.setEntity(httpEntity);

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				reseContent = EntityUtils.toString(entity, 
						ContentType.getOrDefault(entity).getCharset());
				EntityUtils.consume(entity);
			}

			StringBuilder respHeaderDatas = new StringBuilder();
			for (Header header : response.getAllHeaders()) {
				respHeaderDatas.append(header.toString()).append("\r\n");
			}
			String respStatusLine = response.getStatusLine().toString(); // HTTP应答状态行信息
			String respHeaderMsg = respHeaderDatas.toString().trim(); // HTTP应答报文头信息
			logger.debug("HTTP应答完整报文=[" + respStatusLine + "\r\n"
					+ respHeaderMsg + "\r\n\r\n" + reseContent + "]");
			logger.debug("-------------------------------------------------------------------------------------------");
		} catch (ConnectTimeoutException cte) {
			logger.error("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下", cte);
		} catch (SocketTimeoutException ste) {
			logger.error("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下", ste);
		} catch (Exception e) {
			logger.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return reseContent;
	}
}
