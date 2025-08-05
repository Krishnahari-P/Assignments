package com.litmus7.employeeManager.exceptions;

public class EmployeeManagerException extends Exception {
	
	public EmployeeManagerException(String errorMessage) {
		super(errorMessage);
	}
	
	public EmployeeManagerException(String errorMessage,Throwable cause) {
		super(errorMessage,cause);
	}
}
