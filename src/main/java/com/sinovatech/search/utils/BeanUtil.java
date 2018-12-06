package com.sinovatech.search.utils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class BeanUtil {
	
	private static final Log log = LogFactory.getLog(BeanUtil.class);
	/*
	 * 取得业务参数
	 * 
	 * 
	 */
	public static Map<String, Object> BeanToMap(Object obj)throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Map<String, Object> bizMap = new HashMap<String, Object>();
		Class c = obj.getClass();
		Method[] methods = c.getDeclaredMethods();
		Field[] fields = c.getDeclaredFields();
		for (int a = 0; a < fields.length; a++) {
			Field field = fields[a];
			String filedName = field.getName();
			for (int b = 0; b < methods.length; b++) {
				Method method = methods[b];
				String methodsName = method.getName();
				if (methodsName.equalsIgnoreCase("get" + filedName)) {
					Object result =  method.invoke(obj, null);
					if (result != null)
						bizMap.put(filedName, result);
				}
			}
		}

		return bizMap;
	}

	public void requestParamBind(ServletRequest request, Object obj) throws InstantiationException, IllegalAccessException {
		Class c = obj.getClass();
		Method[] methods = c.getDeclaredMethods();
		Field[] fields = c.getDeclaredFields();
		for (int a = 0; a < fields.length; a++) {
			Field field = fields[a];
			String filedName = field.getName();
			for (int b = 0; b < methods.length; b++) {
				Method method = methods[b];
				String methodsName = method.getName();
				if (methodsName.equalsIgnoreCase("set" + filedName)) {
					try {
						method.invoke(obj, request.getParameter("filedName"));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}

	}

	/*
	 * 完成map到JAVA BEAN 数据绑定
	 * 
	 */
	public static void MapToJavaBean(Map<String, Object> map, Object obj)
			throws InstantiationException, IllegalAccessException {
		Class c = obj.getClass();
		Method[] methods = c.getDeclaredMethods();
		Field[] fields = c.getDeclaredFields();
		c.getGenericSuperclass();
		for (int a = 0; a < fields.length; a++) {
			Field field = fields[a];

			String filedName = field.getName();
			for (int b = 0; b < methods.length; b++) {
				Method method = methods[b];
				String methodsName = method.getName();
				if (methodsName.equalsIgnoreCase("set" + filedName)) {
					try {
						method.invoke(obj, map.get(filedName));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		// 遍历父类方法集合
//		log.info(c.getGenericSuperclass());
		if (c.getGenericSuperclass() != null) {
			Class superClass = c.getSuperclass();// 父类
			Method[] superMethods = superClass.getDeclaredMethods();// 父类方法
			Field[] superfields = superClass.getDeclaredFields();
			for (int a = 0; a < superfields.length; a++) {
				Field field = superfields[a];
				String filedName = field.getName();
				for (int b = 0; b < superMethods.length; b++) {
					Method method = superMethods[b];
					String methodsName = method.getName();
					if (methodsName.equalsIgnoreCase("set" + filedName)) {
						try {
							method.invoke(obj, map.get(filedName));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			}

		}

	}
	/*
	 * 
	 * 完成返回结果为map<String,Stirng>类型结果封装
	 * 
	 */
	public static Object getRespose(Map map, Class c) {
		Object obj = null;
		try {
			if (map != null) {
				obj = c.newInstance();
				BeanUtil.MapToJavaBean(map, obj);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}
	
}
