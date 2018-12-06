package com.sinovatech.search.cache.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sinovatech.search.utils.GeneralCommonUtil;
 
public class CachedManagerImpl implements CachedManager {

	private static Logger logger = LoggerFactory
			.getLogger(CachedManagerImpl.class);
 
	@Autowired
	@Qualifier("xMemCachedManager")
	private ICacheManager memcachedMgr;

	public Object getObject(String key) {
		return memcachedMgr.getObject(key);
	}

	public void setObject(String key, Object obj, int expire) {
		memcachedMgr.setObject(key, obj, expire);
	}

	public void deleteObject(String key) {
		memcachedMgr.delete(key);
	}

	/**
	 * 根据keyGroup 清楚keyGroup中存放的缓存的key
	 * 
	 * @param keyGroup
	 */
	public void flushKeyByKeyGroup(String keyGroup) {
		// 根据给定的keyGroup 得到缓存List
		List<String> keyList = getKeyListByKeyGourp(keyGroup);
		if (GeneralCommonUtil.isNullOrSizeZero(keyList)) {
			logger.info("keyGroup :{} has no any value.", keyGroup);
			return;
		}
		for (String key : keyList) {
			logger.debug("@@@@@@@======>>delete result for key:{}", key);
			memcachedMgr.delete(key);// 清实际数据
		}
		// 最后清除keyGroup缓存数据
		logger.debug("@@@@@@@======>>delete result for keyGroup:" + keyGroup);
		memcachedMgr.delete(keyGroup);
	}

	/**
	 * 根据缓存组名 获取在该组下写入的多个KEY集合
	 * 
	 * @param keyGroup
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getKeyListByKeyGourp(String keyGroup) {
		// 根据给定的keyGroup 得到缓存的KeyList
		List<String> keyList = (List<String>) getObject(keyGroup);
		return keyList;
	}

	/**
	 * 缓存对象，并将其加入到缓存组中，然后将缓存组Key缓存
	 * 
	 * @param keyGroup
	 * @param key
	 * @param expire
	 * @param keyList
	 */
	public void setObject(String keyGroup, String key, Object obj, int expire) {
		// 缓存对象
		this.setObject(key, obj, expire);
		if (keyGroup == null) {
			logger.warn("keyGroup is null,can not add key :" + key
					+ " to a null keyGroup");
			return;
		}
		List<String> keyList = null;
		// 缓存中存在缓存组Key
		keyList = getKeyListByKeyGourp(keyGroup);
		if (keyList == null) {// key has expired
			keyList = new ArrayList<String>();
		}
		addKeyToKeyGroup(keyGroup, key, expire, keyList);
	}

	/**
	 * 将key添加到keyGroup中，并将keyGroup重新缓存
	 * 
	 * @param keyGroup
	 * @param key
	 * @param expire
	 * @param keyList
	 */
	private void addKeyToKeyGroup(String keyGroup, String key, int expire,
			List<String> keyList) {
		keyList.add(key);
		setObject(keyGroup, keyList, expire);
		if (logger.isDebugEnabled()) {
			logger.debug("add the key to keyGroup successfully,key :" + key
					+ ",keyGroup:" + keyGroup + ",mem time(s):" + expire);
		}
	}

	/**
	 * 清空缓存
	 */
	public void flushAll() {
		memcachedMgr.flushAll();
		logger.info("clear all the cache data.");
	}

	public boolean isMemcacheEnabled() {
		return memcachedMgr.useMemcache();
	}
}
