package com.sinovatech.search.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * 执行调用各个接口服务 <br>
 * 
 * @author guohuaining
 * @Date: Aug 23, 2013 4:25:13 PM<br>
 * @version 1.0
 */
public class HttpClientCall {
	private static final Logger logger = Logger.getLogger(HttpClientCall.class);
	private static HttpClientCall httpClientCall = null;

	public static HttpClientCall getInstance() {
		if (httpClientCall == null) {
			httpClientCall = new HttpClientCall();
		}
		return httpClientCall;
	}

	/**
	 * 发送请求 获取接口响应数据 Description:<br>
	 * 
	 * @param code
	 *            业务编码
	 * @param params
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public String callHttpPost(String reqUrl, Map<String, String> reqParams) {
		// System.out.println("请求后台接口服务地址：" + reqUrl);
		String respContent = sendHttpPostRequest(reqUrl, reqParams);
		return respContent;
	}

	/**
	 * 发送请求 获取接口响应数据 Description:<br>
	 * 
	 * @param url
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @return
	 */
	@SuppressWarnings("finally")
	public String sendHttpPostRequest(String url, Map<String, String> params) {
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);

		postMethod.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		// 设置请求参数
		NameValuePair[] data = null;
		if (params != null) {
			data = new NameValuePair[params.size()];
			int i = 0;
			for (Iterator<String> iterator = params.keySet().iterator(); iterator
					.hasNext();) {
				String name = (String) iterator.next();
				String value = params.get(name);
				data[i] = new NameValuePair(name, value);
				i++;
			}
		}
		// 设置连接超时时间10秒
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(10000);
		// 设置读取超时时间 20秒
		client.getHttpConnectionManager().getParams().setSoTimeout(20000);

		postMethod.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);
		// FIXME 需要定义异常响应数据格式
		StringBuffer respContent = new StringBuffer("");
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			// 执行postMethod
			int statusCode = client.executeMethod(postMethod);
			logger.info("-------------接口响应状态码：" + statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				logger.info("-------------接口响应状态码：" + statusCode);
				logger.error("Method failed: " + postMethod.getStatusLine());
			}
			// System.out.println("============: " +
			// postMethod.getStatusLine());
			in = postMethod.getResponseBodyAsStream();
			isr = new InputStreamReader(in, "UTF-8");
			reader = new BufferedReader(isr);
			String str = "";
			while ((str = reader.readLine()) != null) {
				respContent.append(str);
			}
		} catch (Exception e) {
			logger.error("---------------接口异常：url=" + url + ",reqParam="
					+ params, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			postMethod.releaseConnection();
			return respContent.toString();
		}
	}

}
