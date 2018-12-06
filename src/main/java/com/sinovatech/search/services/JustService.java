package com.sinovatech.search.services;

import java.util.List;

import com.sinovatech.search.entity.JustBean;

/**
 * Just Service
 * 
 * @author 陈杰
 */
public interface JustService {
	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public List<JustBean> doSomething(String code,String url);

}
