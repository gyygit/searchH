package com.sinovatech.search.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;

import com.alibaba.fastjson.JSON;

/**
 * 1、DTO：Data Transfer Object，数据传送对象 2、对于日期格式的问题，也已经处理
 * 
 * @author Administrator
 * 
 */
public class JsonUtil {

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象，形如：{"id" : idValue, "name" : nameValue, "aBean"
	 * : {"aBeanId" : aBeanIdValue, ...}}
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T getDTO(String jsonString, Class<T> clazz) {
		JSONObject jsonObject = null;
		try {
			// JSON.parseObject(jsonString);
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDTO(jsonObject, clazz);
	}

	public static <T> T getDTOforme(String jsonString, Class<T> clazz) {
		com.alibaba.fastjson.JSONObject jsonObject = null;
		try {
			jsonObject = JSON.parseObject(jsonString);
			// jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDTOforme(jsonObject, clazz);
	}

	public static <T> T getDTOforme(com.alibaba.fastjson.JSONObject jsonObject,
			Class<T> clazz) {
		setDataFormat2JAVA();
		return (T) com.alibaba.fastjson.JSONObject.toJavaObject(jsonObject,
				clazz);
	}

	public static <T> T getDTO(JSONObject jsonObject, Class<T> clazz) {
//		setDataFormat2JAVA();
		return (T) JSONObject.toBean(jsonObject, clazz);
	}

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象，其中beansList是一类的集合，形如： {"id" : idValue, "name" :
	 * nameValue, "aBean" : {"aBeanId" : aBeanIdValue, ...}, beansList:[{}, {},
	 * ...]}
	 * 
	 * @param jsonString
	 * @param clazz
	 * @param map
	 *            集合属性的类型 (key : 集合属性名, value : 集合属性类型class) eg: ("beansList" :
	 *            Bean.class)
	 * @return
	 */
	public static <T> T getDTO(String jsonString, Class<T> clazz,
			Map<String, Class<T>> map) {
		JSONObject jsonObject = null;
		try {
			setDataFormat2JAVA();
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) JSONObject.toBean(jsonObject, clazz, map);
	}

	/**
	 * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" :
	 * idValue, "name" : nameValue}, ...]
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> T[] getDTOArray(String jsonString, Class<T> clazz) {
		setDataFormat2JAVA();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Object[] obj = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			obj[i] = JSONObject.toBean(jsonObject, clazz);
		}
		return (T[]) obj;
	}

	/**
	 * 从一个JSON数组得到一个java对象数组，形如： [{"id" : idValue, "name" : nameValue}, {"id" :
	 * idValue, "name" : nameValue}, ...]
	 * 
	 * @param object
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static <T> T[] getDTOArray(String jsonString, Class<T> clazz,
			Map<String, Class<T>> map) {
		setDataFormat2JAVA();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Object[] obj = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			obj[i] = JSONObject.toBean(jsonObject, clazz, map);
		}
		return (T[]) obj;
	}

	/**
	 * 从一个JSON数组得到一个java对象集合
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> getDTOList(String jsonString, Class<T> clazz) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return getDTOList(jsonArray, clazz);
	}

	/**
	 * 从一个JSON数组得到一个字符串集合
	 * 
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static List<String> getStringList(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		List<String> list = new ArrayList<String>();
		for (Iterator<String> iter = jsonArray.iterator(); iter.hasNext();) {
			list.add(iter.next());
		}
		return list;

	}

	public static <T> List<T> getDTOList(JSONArray jsonArray, Class<T> clazz) {
		setDataFormat2JAVA();
		List<T> list = new ArrayList<T>();
		for (Iterator iter = jsonArray.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add(getDTO(jsonObject, clazz));
		}
		return list;
	}

	/**
	 * 从一个JSON数组得到一个java对象集合，其中对象中包含有集合属性
	 * 
	 * @param object
	 * @param clazz
	 * @param map
	 *            集合属性的类型
	 * @return
	 */
	public static <T> List<T> getDTOList(String jsonString, Class<T> clazz,
			Map<String, Class<T>> map) {
		setDataFormat2JAVA();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		List<T> list = new ArrayList<T>();
		for (Iterator iter = jsonArray.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add((T) JSONObject.toBean(jsonObject, clazz, map));
		}
		return list;
	}

	/**
	 * 从json HASH表达式中获取一个map，该map支持嵌套功能 形如：{"id" : "johncon", "name" : "小强"}
	 * 注意commons
	 * -collections版本，必须包含org.apache.commons.collections.map.MultiKeyMap
	 * 
	 * @param object
	 * @return
	 */
	public static Map<String, Object> getMapFromJson(String jsonString) {
		setDataFormat2JAVA();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Map<String, Object> map = new HashMap<String, Object>();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}

	/**
	 * 从json数组中得到相应java数组 json形如：["123", "456"]
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArrayFromJson(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

	/**
	 * 把数据对象转换成json字符串 DTO对象形如：{"id" : idValue, "name" : nameValue, ...}
	 * 数组对象形如：[{}, {}, {}, ...] map对象形如：{key1 : {"id" : idValue, "name" :
	 * nameValue, ...}, key2 : {}, ...}
	 * 
	 * @param object
	 * @return
	 */
	public static String getJSONString(Object object) throws Exception {
		String jsonString = null;
		// 日期值处理器
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class,
				new JsonDateValueProcessor());
		if (object != null) {
			if (object instanceof Collection || object instanceof Object[]) {
				jsonString = JSONArray.fromObject(object, jsonConfig)
						.toString();
			} else {
				jsonString = JSONObject.fromObject(object, jsonConfig)
						.toString();
			}
		}
		return jsonString == null ? "{}" : jsonString;
	}

	private static void setDataFormat2JAVA() {
		// 设定日期转换格式
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd",
						"yyyy-MM-dd HH:mm:ss" }));
	}

	private static void setTimestap2JAVA() {
		// 设定日期转换格式
		String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
		JSONUtils.getMorpherRegistry().registerMorpher(
				new TimestampMorpher(formats));
		// JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new
		// String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" }));
	}

	public static void main(String[] arg) throws Exception {
		String s = "{status : 'success'}";
		System.out.println("object : " + JsonUtil.getJSONString(s));
	}
}

/*
 * 对于java对象转换成json数据格式时，要对日期格式化常用格式，类：JsonDateValueProcessor json日期值处理器
 */
class JsonDateValueProcessor implements JsonValueProcessor {
	private String format = "yyyy-MM-dd HH:mm:ss";

	public JsonDateValueProcessor() {
	}

	public JsonDateValueProcessor(String format) {
		this.format = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value, jsonConfig);
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value, jsonConfig);
	}

	private Object process(Object value, JsonConfig jsonConfig) {
		if (value instanceof Date) {
			String str = new SimpleDateFormat(format).format((Date) value);
			return str;
		}
		return value == null ? null : value.toString();
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
