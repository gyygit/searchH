package com.sinovatech.search.ws.result;

import javax.xml.bind.annotation.XmlType;

/**
 * WebService返回结果基类,定义所有返回码.
 * 
 * @author calvin
 */
@XmlType(name = "WSResult")
public class WSResult {


	// -- 返回代码定义 --//
	// 按项目的规则进行定义.
	/**
	 * SUCCESS
	 */
	public static final String SUCCESS = "0";

	/**
	 * 4xx代表客户端参数错误
	 */
	public static final String PARAMETER_ERROR = "400";

	/**
	 * 5xx代表服务端业务错误等
	 */
	public static final String SYSTEM_ERROR = "500";

	/**
	 * SYSTEM_ERROR_MESSAGE
	 */
	public static final String SYSTEM_ERROR_MESSAGE = "Runtime unknown internal error.";

	// -- WSResult基本属性 --//
	/**
	 * SUCCESS
	 */
	private String code = SUCCESS;
	/**
	 * message
	 */
	private String message;

	
	/**
	 * 创建结果.
	 * @param <T> WSResult
	 * @param resultCode .
	 * @param resultMessage .
	 * @return T
	 */ 
	@SuppressWarnings("unchecked")
	public <T extends WSResult> T buildResult(String resultCode,
			String resultMessage) {
		code = resultCode;
		message = resultMessage;
		return (T) this;
	}

	/**
	 * 创建默认异常结果
	 * @param <T> WSResult
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public <T extends WSResult> T buildDefaultErrorResult() {
		return (T) buildResult(SYSTEM_ERROR, SYSTEM_ERROR_MESSAGE);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
