package com.sinovatech.search.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 对象转换json工厂
 * 
 * @author Administrator
 * 
 */
public class JsonFactory {

	private static final Log log = LogFactory.getLog(JsonFactory.class);
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * map or bean to json
	 * 
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String toJson2(Object o) {
		if (o == null) {
			return "";
		}
		JSONObject json = JSONObject.fromObject(o);
		return json.toString();
	}

	/**
	 * json to object
	 * 
	 * @param <T>
	 * @param json
	 * @param cla
	 * @return
	 */
	public static <T> T jsonToObject(String json, Class<T> cla) {
		try {
			return mapper.readValue(json, cla);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * json to map
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = null;
		if (jsonStr != null && !"".equals(jsonStr)) {
			map = new HashMap<String, Object>();
			// 最外层解析
			if (jsonStr.indexOf("{") == 0) {
				JSONObject json = JSONObject.fromObject(jsonStr);
				for (Object k : json.keySet()) {
					Object v = json.get(k);
					parseJSON(map, k, v);
				}
			}
		}
		return map;
	}

	private static void parseJSON(Map<String, Object> map, Object k, Object v) {
		// 如果内层还是数组的话，继续解析
		if (v instanceof JSONObject) {
			JSONObject v1 = (JSONObject) v;
			Map<String, Object> m = new HashMap<String, Object>();
			for (Object k2 : v1.keySet()) {
				Object v2 = v1.get(k2);
				if (v2.toString().indexOf("{") == 0) {
					m.put(k2.toString(), parseJSON2Map(v2.toString()));
				} else if (v2.toString().indexOf("[") == 0) {
					log.info(v2.toString());
					JSONArray jsonArray = JSONArray.fromObject(v2.toString());
					parseJSON(map, k2.toString(), jsonArray);
				} else {
					m.put(k2.toString(), v2.toString());
				}
			}
			map.put(k.toString(), m);
		} else if (v instanceof JSONArray) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Iterator<JSONObject> it = ((JSONArray) v).iterator();
			while (it.hasNext()) {
				JSONObject json2 = it.next();
				list.add(parseJSON2Map(json2.toString()));
			}
			map.put(k.toString(), list);
		} else {
			map.put(k.toString(), v);
		}
	}

}
