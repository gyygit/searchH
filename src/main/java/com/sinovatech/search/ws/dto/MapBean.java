package com.sinovatech.search.ws.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <b>function:</b> MapBean 封装Map集合元素
 * 
 * @author elvman
 * @file MapBean.java
 * @project CXFWebService
 * @email elmvan@sina.com.cn
 * @version 1.0
 */
@XmlRootElement
public class MapBean {
	private Map<String, User> map;

	// @XmlElement(type = User.class)
	public Map<String, User> getMap() {
		return map;
	}

	public void setMap(Map<String, User> map) {
		this.map = map;
	}
}
