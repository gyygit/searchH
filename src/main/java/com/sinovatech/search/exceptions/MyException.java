package com.sinovatech.search.exceptions;

import com.sinovatech.search.constants.MyExceptionReason;

/**
 * 自定义异常(示例)<br>
 * 注意：异常原因为枚举类型，方便统一管理，具体见<code>MyExceptionReason</code>
 * @author 陈杰
 *
 */
public class MyException extends Exception {

	private static final long serialVersionUID = 1L;
	private MyExceptionReason reason;

	public MyException(MyExceptionReason reason) {
		super();
		this.reason = reason;
	}

	public MyExceptionReason getReason() {
		return reason;
	}

	public void setReason(MyExceptionReason reason) {
		this.reason = reason;
	}

}
