package com.sinovatech.search.luceneindex;

import java.util.List;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.util.JSONUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 1、DTO：Data Transfer Object，数据传送对象 2、对于日期格式的问题，也已经处理
 * 
 * @author Administrator
 * 
 */
public class LuceneJsonUtil {

	public static String getJSONString(Object obj) {
		try {
			return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
			// return JSON.toJSONString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// json转对象
	public static <T> T getDTOforme(String json, Class<T> cl) {
		try {
			return JSON.parseObject(json, cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// json转集合类型
	public static  <T>  List<?>   jsonToList(String jsonArray, Class<T> cl) {
		try {
			return JSON.parseArray(jsonArray, cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
	private static void setDataFormat2JAVA() {
		// 设定日期转换格式
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd",
			
				"yyyy-MM-dd HH:mm:ss" }));
	}
	public static <T> T getDTOforme(com.alibaba.fastjson.JSONObject jsonObject,
			Class<T> clazz) {
		setDataFormat2JAVA();
		return (T) com.alibaba.fastjson.JSONObject.toJavaObject(jsonObject,
				clazz);
	}
}
