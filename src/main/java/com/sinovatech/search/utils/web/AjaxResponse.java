package com.sinovatech.search.utils.web;

import java.io.Serializable;

/**
 * ajax响应对象
 * 
 * @author elvman
 * 
 */
public class AjaxResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 成功
	 */
	private boolean success;

	/**
	 * 序列话的数据，可以存储任意值或错误消息
	 */
	private Object data;

	public AjaxResponse() {
		success = true;
	}

	public AjaxResponse(boolean success) {
		super();
		this.success = success;
	}

	public AjaxResponse(boolean success, Object data) {
		super();
		this.success = success;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
