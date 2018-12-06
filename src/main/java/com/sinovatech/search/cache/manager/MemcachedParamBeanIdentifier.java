package com.sinovatech.search.cache.manager;

/**
 * 所有可缓存方法的参数Bean（非原始类型）必须要实现的接口，用于拼装缓存的Key
 * 
 * @author dzh
 * @Date 20101230
 * 
 */
public interface MemcachedParamBeanIdentifier {

	String getMemIdentifier();
}
