package com.sinovatech.search.filter;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.search.utils.JsonFactory;
import com.sinovatech.search.utils.ReadPropertyPlaceholderConfigurer;

public class MessageForException {
	private static final Log log = LogFactory.getLog(MessageForException.class);

	/**
	 * 通过配置文件返回错误信息
	 * @param exceptionCode为配置文件中根据各个业务配置的错误提示编码
	 * @param args 按照模板填充
	 * @return
	 */
	public static String exceptionMessage(String exceptionCode,String... args) {
		String json = "{\"status\":\"1\",\"code\":\"999999\",\"msg\":\"系统异常请稍后再试！\"}";
		try {
			String message = (String)ReadPropertyPlaceholderConfigurer.getContextProperty("AppException."+exceptionCode);
			if(args != null && args.length > 0){
				 message = MessageFormat.format(message,args);
			}
			Map<String,String> data = new HashMap<String, String>();
			data.put("status", "1");
			data.put("code", exceptionCode);
			data.put("msg", message);
			json = JsonFactory.toJson(data);
		} catch (Exception e) {
			log.error("封装错误返回信息发生异常："+e.getMessage(),e);
		}
		return json;
	}
	/**
	 * 通过配置文件返回错误信息
	 * @param exceptionCode为配置文件中根据各个业务配置的错误提示编码
	 * @return
	 */
	public static String exceptionMessage(String exceptionCode) {
		String json = "{\"status\":\"1\",\"code\":\"999999\",\"msg\":\"系统异常请稍后再试！\"}";
		try {
			String message = (String)ReadPropertyPlaceholderConfigurer.getContextProperty("AppException."+exceptionCode);
			Map<String,String> data = new HashMap<String, String>();
			data.put("status", "1");
			data.put("code", exceptionCode);
			data.put("msg", message);
			json = JsonFactory.toJson(data);
		} catch (Exception e) {
			log.error("封装错误返回信息发生异常："+e.getMessage(),e);
		}
		return json;
	}	
}
