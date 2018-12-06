package com.sinovatech.search.cache.manager;

public class CachedAppManager implements ICacheManager{

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub das
		
	}

	@Override
	public boolean useMemcache() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setObject(String key, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObject(String key, Object obj, int expire) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean flushAll() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long incr(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

}
