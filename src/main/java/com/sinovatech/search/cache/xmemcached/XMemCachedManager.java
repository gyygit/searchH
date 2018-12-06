package com.sinovatech.search.cache.xmemcached;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.sinovatech.search.cache.manager.ICacheManager;
import com.sinovatech.search.spring.AbstractSmartLifeCycle;

/**
 * xmemcached ����
 * 
 * @author dzh
 * 
 */
public class XMemCachedManager extends AbstractSmartLifeCycle implements ICacheManager {

	private static Logger logger = LoggerFactory
			.getLogger(XMemCachedManager.class);
	private MemcachedClient memcacheClient;
	@Autowired
	private MemcachedClientBuilder memClientBuilder;
	
	//Xmemcached 读超时时间设置，单位为秒
	private long opTimeout = 1000L;

	private static final String FALSE = "false";// 默认不使用缓存的常量
	private String useMemcache = FALSE;// 系统是否使用Memcache，默认不使用

	public boolean useMemcache() {
		return FALSE.equals(getUseMemcache().toLowerCase()) ? false : true;
	}

	public void afterPropertiesSet() throws Exception {
		if (!useMemcache()) {
			logger
					.info("system does not use xmemcached,return directly...,does not build memcacheClient ever.");
			return;
		}
		memcacheClient = memClientBuilder.build();
		memcacheClient.setOpTimeout(getOpTimeout());
		Assert.notNull(memcacheClient, "memcacheClient is built failed...");
	}

	/**
	 * ���û������
	 */
	public void setObject(String key, Object obj) {
		try {
			if (memcacheClient != null)
				memcacheClient.set(key, 0, obj);
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} catch (MemcachedException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * @param key
	 * @param obj
	 *            �������
	 * @param expire
	 *            缓存时间，单位：秒 ����ʱ��
	 * @expiry ͨ������������ʱ��
	 * 
	 */
	public void setObject(String key, Object obj, int expire) {
		try {
			if (memcacheClient != null)
				memcacheClient.set(key, expire, obj);
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} catch (MemcachedException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * ��ȡ�������
	 */
	public Object getObject(String key) {
		try {
			if (memcacheClient != null)
				return memcacheClient.get(key);
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} catch (MemcachedException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public boolean delete(String key) {
		try {
			if (memcacheClient != null) {
				memcacheClient.delete(key);
				return true;
			}
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} catch (MemcachedException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	public boolean flushAll() {
		try {
			if (memcacheClient != null) {
				memcacheClient.flushAll();
				return true;
			}
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} catch (MemcachedException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * Increment the value at the specified key by 1, and then return it.
	 * 
	 * @param key
	 *            key where the data is stored
	 * @return -1, if the key is not found, the value after incrementing
	 *         otherwise
	 */
	public long incr(String key) {
		try {
			if (memcacheClient != null)
				return memcacheClient.incr(key, 1L);
		} catch (TimeoutException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} catch (MemcachedException e) {
			logger.error(e.getMessage());
		}
		return -1L;
	}

	public XMemCachedManager() throws IOException {
		super();
	}

	public String getUseMemcache() {
		return useMemcache;
	}

	public void setUseMemcache(String useMemcache) {
		this.useMemcache = useMemcache;
	}

	public long getOpTimeout() {
		return opTimeout;
	}

	public void setOpTimeout(long opTimeout) {
		this.opTimeout = opTimeout;
	}

}
