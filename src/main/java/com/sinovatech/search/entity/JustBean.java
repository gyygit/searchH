package com.sinovatech.search.entity;

import com.sinovatech.search.entity.common.DtoSupport;

public class JustBean extends DtoSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String getUrl;
	private String name;
	private String code;
	private String responseContent;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGetUrl() {
		return getUrl;
	}

	public void setGetUrl(String getUrl) {
		this.getUrl = getUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
}
