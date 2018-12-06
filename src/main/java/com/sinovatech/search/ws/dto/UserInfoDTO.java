package com.sinovatech.search.ws.dto;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * WebService 对象传递类
 * 
 * 
 */
//@XmlType(name = "UserInfo")
public class UserInfoDTO {

	String id;
	String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * override default toString method..
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}