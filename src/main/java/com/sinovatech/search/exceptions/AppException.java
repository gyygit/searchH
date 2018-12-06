package com.sinovatech.search.exceptions;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	private String[] args;

	public AppException(String errorCode,String message, Throwable cause,String... args) {
		super(message, cause);
		this.errorCode = errorCode;
		this.args = args;
	}

	public AppException(String errorCode,String message,String... args) {
		super(message);
		this.errorCode = errorCode;
		this.args = args;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

}
