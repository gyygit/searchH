package com.sinovatech.search.cache.manager;

import org.springframework.beans.factory.InitializingBean;

/**
 * 本系统支持的MemCache方法,目前Xmemcache实现<br>
 * 如不使用Xmemcache可以自行实现本接口<br>
 * 
 * @author 陈杰
 * 
 */
public interface ICacheManager extends InitializingBean {
	/**
	 * cache switch
	 * 
	 * @return
	 */
	public boolean useMemcache();

	/**
	 * set value into the memcache,no expire date
	 */
	public void setObject(String key, Object obj);

	/**
	 * @param key
	 * @param obj
	 *            �������
	 * @param expire
	 *            cache unit is second
	 * 
	 */
	public void setObject(String key, Object obj, int expire);

	/**
	 * get the value from memcache
	 */
	public Object getObject(String key);

	/**
	 * delete the value in the memcache by the key
	 * 
	 * @param key
	 * @return
	 */
	public boolean delete(String key);

	/**
	 * flush all in the memcache
	 * 
	 * @return
	 */
	public boolean flushAll();

	/**
	 * Increment the value at the specified key by 1, and then return it.
	 * 
	 * @param key
	 *            key where the data is stored
	 * @return -1, if the key is not found, the value after incrementing
	 *         otherwise
	 */
	public long incr(String key);

}
