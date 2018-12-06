/*
 *	Title：HttpClientUtil.java<br>
 *  Date: Mar 14, 2014 9:45:01 AM<br>
 *
 * 	警告:
 * 	---------------------------------------------------
 * 		本程序受著作权法的保护。未经授权，不能使用
 * 	和修改本软件全部或部分源代码。凡擅自复制、盗用或散
 * 	布此程序或部分程序或者有其它任何越权行为，将遭到民
 * 	事赔偿及刑事的处罚，并将依法以最高刑罚进行追诉。
 * 	---------------------------------------------------
 */
package com.sinovatech.search.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.search.utils.encode.JsonBinder;


/**
 * 
 * @author lzq
 * @Date: 11 18, 2014 11:45:01 AM<br>
 * @version 1.0
 */
public class HttpClientUtil {
	private static final Log log = LogFactory.getLog(HttpClientUtil.class);
	private static MultiThreadedHttpConnectionManager connectionManager;
	private static HttpClient httpclient;
	static{
		log.info("HttpClientSpiderUtil static begin");
		/**
		 * 在httpclient运行的过程中，每个http协议的方法，使用一个HttpConnection实例。
		 * 由于连接是一种有限的资源，每个连接在某一时刻只能供一个线程和方法使用，所以需要确保在需要时正确地分配连接。
		 * HttpClient采用了一种类似jdbc连接池的方法来管理连接，这个管理工作由
		 * MultiThreadedHttpConnectionManager完成。
		 */
		//System.getProperties().setProperty("httpclient.useragent", "Mozilla/4.0");
		int  timeout =10000;
		try{
			timeout =Integer.parseInt(GlobalConfig.getProperty("searchh", "connection-timeout"));
		}catch(Exception e)
		{
			log.error("no config connection-timeout......",e);
		}
        //System.getProperties().setProperty("httpclient.useragent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
		connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setConnectionTimeout(timeout);
		params.setSoTimeout(timeout);
		params.setDefaultMaxConnectionsPerHost(2);
		params.setMaxTotalConnections(20);
		connectionManager.setParams(params);
		/**
		 * client可以在多个线程中被用来执行多个方法。 每次调用HttpClient.executeMethod()
		 * 方法，都会去链接管理器申请一个连接实例， 申请成功这个链接实例被签出(checkout)，随之在链接使用完后必须归还管理器
		 */
		httpclient = new HttpClient(connectionManager);
		log.info("HttpClientSpiderUtil static end");
	}
	public static String sendHttpPostRequest(String url,Map params,String charset) {
		if(charset==null || "".equals(charset))
		{
			charset="UTF-8";
		}
		if (null == httpclient) {
			return null;
		} 
		// 创建post方法的实例
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Connection", "close");
		
		postMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		// 处理中文乱码
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		// 设置请求超时为 120 秒

		// 设置请求参数
		if(params!=null){
			NameValuePair[] data = new NameValuePair[params.size()];
			int index=0;
			for (Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				String value=(String)params.get(key);
				data[index++] = new NameValuePair(key, value);
			}
			// 将表单的值放入postMethod中
			if(data!=null&&data.length>0){
				postMethod.setRequestBody(data);
			}
			
		}
		postMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		// 处理中文乱码
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		String responseBody = null;
		try {
			// 执行postMethod
			int statusCode = httpclient.executeMethod(postMethod); 
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + postMethod.getStatusLine());
				
			}else{
				//获取httpClient响应返回的流
				responseBody = postMethod.getResponseBodyAsString();
			}
			return  responseBody;
		} catch (Exception e) {
			log.error("---------------调用后台接口异常：", e);
			return  responseBody;
		} finally {
			postMethod.releaseConnection();
		}
	}
	
	/*public static  void main(String[] args) throws IOException {
		String reqUrl="http://t.10010.com/bbdservice/common/getTwo";
		//获取图片流
		Map<String,String> params=new HashMap<String,String>();
		byte [] b=new HttpClientUtil().sendHttpPostRequest(reqUrl,params);
		File imgFile = new File("F://workspace/我是二维码2.jpg");
		InputStream in=new ByteArrayInputStream(b);
		BufferedImage bb =ImageIO.read(in);
		ImageIO.write(bb, "png", imgFile);
	}*/
	
	public static void main(String args[]){
		//封装请求数据
		String url = "http://124.192.56.213:180";
				List<String> longUrl = new ArrayList<String>();
				longUrl.add(url);
				Map<String,String> params=new HashMap<String,String>();
				params.put("apptx", "11111111111111111111111111");
				params.put("method",  "com.sinova.method.makeShortUrl");
				params.put("secretkey",  "com.sinova.app.b2b");
				params.put("timestamp", new Date().toString());
				params.put("appkey",  "com.sinova.app.b2b");
				Map<String,List<String>> r=new HashMap<String,List<String>>();
				r.put("longurl", longUrl);
				params.put("request", JsonBinder.buildNormalBinder().toJson(r));
				//发送请求获取请求数据
				String responseStr=new HttpClientUtil().sendHttpPostRequest("http://10.143.131.63:8008/bbdservice/open",params,null);
				Map respMap=JsonBinder.buildNormalBinder().fromJson(responseStr, Map.class);
				String resp=(String)respMap.get("response");
				List<HashMap<String, String>> list=JsonBinder.buildNormalBinder().fromJson(resp, List.class);
				Map<String, String> map = new HashMap<String, String>();
				HashMap<String, String> su = list.get(0);
				map.put("sUrl", su.get("domain")+"/"+su.get("shorturl"));
				map.put("imgUrl", su.get("qrcode"));
				System.out.println("-------------"+map.get("sUrl"));
				System.out.println("-------------"+map.get("imgUrl"));
	}
}
