package com.sinovatech.search.cache.manager;

import java.util.List;

/**
 * 缓存管理类
 * 
 * @author 陈杰
 * 
 */
public interface CachedManager {

	/**
	 * 系统是否使用MemCache缓存，其值配置在memconfig.conf文件中，1為使用，0為不用
	 * 
	 * @return
	 */
	boolean isMemcacheEnabled();

	/**
	 * 从内存读取对象
	 * 
	 * @param key
	 * @return
	 */
	Object getObject(String key);

	/**
	 * 缓存对象到内存
	 * 
	 * @param key
	 * @param obj
	 * @param expire
	 */
	void setObject(String key, Object obj, int expire);

	/**
	 * 缓存对象，并将其加入到缓存组中，然后将缓存组Key缓存
	 * 
	 * @param keyGroup
	 * @param key
	 * @param obj
	 * @param expire
	 */
	void setObject(String keyGroup, String key, Object obj, int expire);

	/**
	 * 根据keyGroup 清楚keyGroup中存放的缓存的key
	 * 
	 * @param keyGroup
	 */
	void flushKeyByKeyGroup(String keyGroup);

	/**
	 * 根据缓存组名 获取在该组下写入的多个KEY集合
	 * 
	 * @param keyGroup
	 * @return
	 */
	List<String> getKeyListByKeyGourp(String keyGroup);

	/**
	 * 删除key 对应的缓存
	 * 
	 * @param key
	 */
	void deleteObject(String key);

	/**
	 * 清除所有缓存
	 */
	void flushAll();
}
