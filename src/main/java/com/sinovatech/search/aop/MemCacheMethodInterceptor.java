package com.sinovatech.search.aop;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.sinovatech.search.annotation.SummerCached;
import com.sinovatech.search.annotation.SummerCached.OperatorType;
import com.sinovatech.search.annotation.SummerCached.RelatedItem;
import com.sinovatech.search.cache.manager.CachedManager;
import com.sinovatech.search.cache.manager.MemcachedParamBeanIdentifier;

/**
 * 
 * 缓存拦截器<br>
 * 针对加了缓存注解的方法进行拦截，以更新或删除缓存
 * 
 * @author <a href="mailto:chenjie1@ceopen.cn">陈杰</a>
 * @since 2010-09-22
 * 
 */
public class MemCacheMethodInterceptor implements MethodInterceptor {
	private static Logger logger = LoggerFactory
			.getLogger(MemCacheMethodInterceptor.class);
	private static final String KEY_CONNECT_SYMBOL = "-";

	@Autowired
	private CachedManager cachedManager;

	public Object invoke(MethodInvocation invocation) throws Throwable {

		// 先判断系统配置是否支持缓存,如果不支持则直接调用方法返回
		if (!cachedManager.isMemcacheEnabled()) {
			logger
					.debug("system does not use memcache,access database directly...");
			return invocation.proceed();
		}

		Method invokeMethod = invocation.getMethod();

		// 检查是否使用了SummerCached注解
		boolean flag = invokeMethod.isAnnotationPresent(SummerCached.class);
		if (!flag) {
			return invocation.proceed();
		}

		// 获取注解
		SummerCached annotation = invokeMethod
				.getAnnotation(SummerCached.class);

		RelatedItem[] relatedItems = annotation.related();
		OperatorType operationType = annotation.action();
		String keyPrefix = annotation.keyPrefix();
		int livingTime = annotation.livingTime();
		String methodName = invokeMethod.getName();

		if (OperatorType.SELECT.equals(operationType)) {
			return selectOperation(invocation, keyPrefix, livingTime,
					methodName, relatedItems);
		} else if (OperatorType.DELETE.equals(operationType)) {
			Object result = invocation.proceed();
			deleteOperation(relatedItems);
			return result;
		} else {
			logger.error("[invalid operation that memcache does not support.]");
			return null;
		}
	}

	/**
	 * 清除缓存组相关缓存数据
	 * 
	 * @param relatedItems
	 * @return
	 */
	private void deleteOperation(RelatedItem[] relatedItems) {
		for (RelatedItem keyGroup : relatedItems) {
			if (keyGroup == RelatedItem.NONE)
				continue;
			cachedManager.flushKeyByKeyGroup(keyGroup.toString());
		}
	}

	/**
	 * 从缓存中获取结果集，如果缓存中没有则从数据库中获取，然后将结果集缓存<br>
	 * 将缓存组对应关系也刷新<br>
	 * <b>理论上select操作只会对应一个缓存组，不然此方法实现就有问题</b>
	 * 
	 * @param invocation
	 *            反射体
	 * @param keyPrefix
	 *            Key前缀
	 * @param livingTime
	 *            缓存时间
	 * @param methodName
	 *            方法名
	 * @param relatedItems
	 *            查询涉及到的缓存组
	 * @return 方法调用结果
	 * @throws Throwable
	 */
	private Object selectOperation(MethodInvocation invocation,
			String keyPrefix, int livingTime, String methodName,
			RelatedItem[] relatedItems) throws Throwable {
		// 缓存组只有NONE
		boolean isRelatedItemsNotExist = relatedItems.length == 1
				&& relatedItems[0] == RelatedItem.NONE;

		Object result;
		// 通过规则取得key
		String cacheKey = getKeyByDefaultWay(keyPrefix, invocation, methodName);
		// 从缓存中取结果集
		Date startDate = new Date();
		result = cachedManager.getObject(cacheKey);
		if (result != null) {
			if (logger.isDebugEnabled()) {
				logger
						.debug("=====get the result from memcache=====target name:"
								+ invocation.getThis().getClass()
										.getSimpleName()
								+ ",method:"
								+ methodName
								+ ",used time:"
								+ (System.currentTimeMillis() - startDate
										.getTime()) + " ms");
			}
			return result;
		}
		// 必须要访问数据库
		if (logger.isDebugEnabled()) {
			logger.debug("======must get result from db======target name:"
					+ invocation.getThis().getClass().getSimpleName()
					+ ",method:" + methodName);
		}
		// 从数据库中获取真正的结果，并缓存起来
		result = getRealResult(cacheKey, methodName, invocation);
		if (logger.isDebugEnabled())
			logger.debug("caching the result...");
		if (isRelatedItemsNotExist) {
			cachedManager.setObject(cacheKey, result, livingTime);
		} else {
			for (int i = 0, n = relatedItems.length; i < n; i++) {// 将对象缓存，并加入到缓存组中,理论上此循环只会循环一次，不然cacheKey会在缓存中存入多次
				RelatedItem item = relatedItems[i];
				cachedManager.setObject(item.toString(), cacheKey, result,
						livingTime);
			}
		}
		return result;
	}

	private Object getRealResult(String cacheKey, String methodName,
			MethodInvocation invocation) throws Throwable {
		Date startDate = new Date();
		Object result = invocation.proceed();
		long timelong = new Date().getTime() - startDate.getTime();
		if (logger.isDebugEnabled())
			logger
					.debug("@@@@@@@@@@@=================@@@@@@@@@>>>>>>>target name:"
							+ invocation.getThis().getClass().getSimpleName()
							+ ",method name:"
							+ methodName
							+ " is called,the cache key: "
							+ cacheKey
							+ ",used time:" + timelong + " ms");
		if (timelong >= 1000L)
			logger.warn(invocation.getThis().getClass().getSimpleName()
					+ ",method name:" + methodName + ",used time:" + timelong
					+ " ms");
		return result;
	}

	/**
	 * 将单个参数值追加到緩存Key上
	 * 
	 * @param paramValues
	 * @param param
	 */
	private void appendValue(StringBuilder paramValues, Object param) {
		if (param == null)
			return;
		if (param instanceof MemcachedParamBeanIdentifier) {
			MemcachedParamBeanIdentifier identifier = (MemcachedParamBeanIdentifier) param;
			paramValues.append(identifier.getMemIdentifier()
					+ KEY_CONNECT_SYMBOL);
		} else if (param instanceof Collection || param instanceof Map) {
			Gson gson = new Gson();
			paramValues.append(gson.toJson(param) + KEY_CONNECT_SYMBOL);
		} else {
			paramValues.append((param.toString().trim()) + KEY_CONNECT_SYMBOL);
		}
	}

	/**
	 * 得到缓存KEY，由缓存Key前缀及各参数值决定<br>
	 * 
	 * @param keyPrefix
	 * @param invocation
	 * @return
	 */
	private String getKeyByDefaultWay(String keyPrefix, Invocation invocation,
			String methodName) {
		String targetName = invocation.getThis().getClass().getSimpleName();
		StringBuilder paramValues = new StringBuilder();
		Object[] objects = invocation.getArguments();
		if (objects == null || objects.length == 0)
			return keyPrefix + KEY_CONNECT_SYMBOL + methodName;
		for (Object param : objects) {
			if (param instanceof Object[]) {
				Object[] objs = (Object[]) param;
				if (objs == null || objs.length == 0)
					continue;
				else
					paramValues.append("[");
				for (Object obj : objs) {
					appendValue(paramValues, obj);
				}
				paramValues.append("]");
			} else {
				appendValue(paramValues, param);
			}
		}
		return keyPrefix + KEY_CONNECT_SYMBOL + targetName + KEY_CONNECT_SYMBOL
				+ methodName + KEY_CONNECT_SYMBOL + paramValues;
	}
}
