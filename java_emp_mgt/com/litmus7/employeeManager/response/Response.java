package com.litmus7.employeeManager.response;

public class Response {
	boolean success;
	String message;
	public Response(boolean success,String message) {
		this.success=success;
		this.message=message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getMessage() {
		return message;
	}
}
