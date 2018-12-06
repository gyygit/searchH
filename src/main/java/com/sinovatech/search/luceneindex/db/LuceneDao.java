package com.sinovatech.search.luceneindex.db;

import java.util.List;
import java.util.Map;



public interface LuceneDao<T> {
	
	boolean add(T obj);
	
	boolean  update(T obj);
	
	public Page list(Page page);
	
	boolean del(T obj);
	
	List<Map<String,String>> getAll();   
	
	T  getById(String id);   
	
	List<T> listByIds(String ids); 
}
