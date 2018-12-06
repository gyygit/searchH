package com.sinovatech.search.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 读取redis缓存内容信息内容
 * 
 * @author think
 * 
 */
public class JedisHelper {

	private static final String DEFAULT_INCR_VALUE = "1";
	private static final int DEFAULT_EXPIRE_TIME = 3600;
	private static final int hotResultsNum = 10;
	private int hashCodeNum = 120;

	private ShardedJedisPool jedisPool;

	public boolean setIfNotExists(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			Long result = jedis.setnx(key, DEFAULT_INCR_VALUE);
			jedis.expire(key, DEFAULT_EXPIRE_TIME);
			if (result == 0) {
				return false;
			}
		} finally {
			jedisPool.returnResource(jedis);
		}
		return true;
	}

	/**
	 * 获得redis连接 如果获得则返回true，反之false
	 * 
	 * @return
	 */
	public boolean getConnect() {
		ShardedJedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	public int incr(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.incr(key).intValue();
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @功能:设置失效时间
	 * @param key
	 * @param expireTimes
	 * @return
	 */
	public long expire(String key, int expireTimes) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.expire(key, expireTimes);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	public String get(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.get(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	public String hget(String groupKey, String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.hget(groupKey, key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	public Map<String,String> hgetAll(String groupKey) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.hgetAll(groupKey);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	public long hset(String groupKey, String key, String value) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.hset(groupKey, key, value);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	public long delete(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.del(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param monitorItemId
	 * @return
	 */
	public List<String> getResultsFromMemory(String monitorItemId) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.lrange(monitorItemId, 0, -1);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 
	 * @param monitorItemId
	 * @param resultJson
	 */
	public void setResultToMemory(String monitorItemId, String resultJson) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			jedis.lpush(monitorItemId, resultJson);
			jedis.ltrim(monitorItemId, 0, hotResultsNum);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param key
	 * @param detailKey
	 * @param snapshotJson
	 */
	public void setSnapshotToHash(String key, String detailKey,
			String snapshotJson) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			jedis.hset(key, detailKey, snapshotJson);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public long getLength(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.hlen(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public Long getLengthByKey(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.llen(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public Map<String, String> getSnapShots(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.hgetAll(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public Long lpush(String key, String value) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.lpush(key, value);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 把内容信息存放在list内容信息
	 * 
	 * @param key
	 *            key?
	 * @param list
	 *            存放redirs一组内容信息
	 * 
	 */
	public void setIpush(String key, List<String> list) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			for (String str : list) {
				jedis.rpush(key, str);
			}
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 从map中得到List值内容信息
	 * 
	 * @param key
	 * @return
	 */
	public List<String> getDataByMap(String key) {

		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.hvals(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> getlrange(String key, long start, long end) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.lrange(key, start, end);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 
	 * 更新缓存中的内容信息
	 * 
	 * @param monitorItemId
	 * @param resultJson
	 */
	public void updateIpush(String key, long index, String resultJson) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			jedis.lset(key, index, resultJson);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 通过tokenidhash值求hashCode模值信息
	 * 
	 * @param tokenId
	 */
	public int getHashCodeKey(String tokenId) {

		int key = Math.abs(tokenId.hashCode()) % hashCodeNum;
		return key;
	}

	/**
	 * liqi
	 * 
	 * @return
	 */
	public void setByMap(String key, Map map) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			jedis.hmset(key, map);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	public void setByMapWithTime(String key, Map map, int second) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			jedis.hmset(key, map);
			jedis.expire(key, second);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * liqi
	 * 
	 * @return
	 */
	public List getByMap(String key, String fields) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			return jedis.hmget(key, fields);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * liqi
	 * 
	 * @return
	 */
	public Map getByMapGroup(String key) {
		ShardedJedis jedis = jedisPool.getResource();
		try {

			return jedis.hgetAll(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * liqi
	 * 
	 * @return
	 */
	public void set(String key, String value) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * liqi 带有有效期的redis缓存
	 * 
	 * @return
	 */
	public void setWithOutTime(String key, String value, int secound) {
		ShardedJedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
			jedis.expire(key, secound);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * hefei
	 * 
	 * @param group
	 *            redis中一组
	 * @param key
	 *            reids中Map的key
	 * @param value
	 *            reids中Map中的value
	 * @return
	 */
	public boolean setValueByKeyToCacheMap(Object group, Object key,
			Object value) {
		ShardedJedis jedis = (ShardedJedis) this.jedisPool.getResource();
		boolean bool = false;
		try {
			boolean bool1;
			if (jedis.hset((String) group, (String) key, (String) value)
					.longValue() > 0L) {
				bool1 = true;
				return bool1;
			}
			return bool;
		} finally {
			this.jedisPool.returnResource(jedis);
		}
	}

	public List<Response<String>> pipeLine(String... keys) throws Exception {
		ShardedJedis jedis = jedisPool.getResource();
		ShardedJedisPipeline pipeLine = jedis.pipelined();

		List<Response<String>> responseList = new ArrayList<Response<String>>();
		try {
			for (String key : keys) {
				Response<String> r = pipeLine.get(key);
				responseList.add(r);
			}
			pipeLine.sync();
		} catch (Exception e) {
			throw e;
		} finally {
			jedisPool.returnResource(jedis);
			return responseList;
		}
	}

	public ShardedJedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(ShardedJedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public int getHotresultsnum() {
		return hotResultsNum;
	}

	public int getHashCodeNum() {
		return hashCodeNum;
	}

	/**
	 * 设置hashCode内容信息，以此设置数字内容
	 * 
	 * @param hashCodeNum
	 */
	public void setHashCodeNum(int hashCodeNum) {
		this.hashCodeNum = hashCodeNum;
	}

}
