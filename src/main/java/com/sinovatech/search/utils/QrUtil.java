package com.sinovatech.search.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinovatech.common.config.GlobalConfig;
import com.sinovatech.common.exception.AppException;
import com.sinovatech.search.utils.encode.JsonBinder;

public class QrUtil {
	private static final Log log = LogFactory.getLog(QrUtil.class);
	
	public static Map<String, String> getShortUrl(String url){
		
		Map<String, String> map = null;
		try {
			//封装请求数据
			List<String> longUrl = new ArrayList<String>();
			longUrl.add(url);
			Map<String,String> params=new HashMap<String,String>();
			params.put("apptx", ""+System.currentTimeMillis());
			params.put("method",  GlobalConfig.getProperty("application","method"));
			params.put("secretkey",  GlobalConfig.getProperty("application","secretkey"));
			params.put("timestamp", new Date().toString());
			params.put("appkey",  GlobalConfig.getProperty("application","appkey"));
			Map<String,List<String>> r=new HashMap<String,List<String>>();
			r.put("longurl", longUrl);
			params.put("request", JsonBinder.buildNormalBinder().toJson(r)); 
			//发送请求获取请求数据
			String responseStr="";//new HttpClientUtil().sendHttpPostRequest(GlobalConfig.getProperty("application","reqUrl"),params);
			Map respMap=JsonBinder.buildNormalBinder().fromJson(responseStr, Map.class);
			String resp=(String)respMap.get("response");
			List<HashMap<String, String>> list=JsonBinder.buildNormalBinder().fromJson(resp, List.class);
			HashMap<String, String> su = list.get(0);
			map = new HashMap<String, String>(); 
			map.put("sUrl", su.get("domain")+"/"+su.get("shorturl"));
			map.put("imgUrl", su.get("qrcode"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return map;
		}
	}
}
