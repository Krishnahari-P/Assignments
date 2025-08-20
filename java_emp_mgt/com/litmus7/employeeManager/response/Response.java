package com.litmus7.employeeManager.response;

import com.litmus7.employeeManager.constants.ErrorCode;

public class Response<T> {
	int statusCode;
	String message;
	private T data;
	public Response(int statusCode,String message) {
		this.statusCode=statusCode;
		this.message=message;
	}
	
	public Response(int statusCode,String message, T data) {
		this.statusCode=statusCode;
		this.message=message;
		this.data=data;
	}
	
	public Response(ErrorCode errorCode) {
		this.statusCode=errorCode.getCode();
		this.message=errorCode.getMessage();
	}
	
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getMessage() {
		return message;
	}
	
	public T getData() {
		return data;
	}
}
