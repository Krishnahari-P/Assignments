package com.litmus7.employeeManager.exceptions;

import com.litmus7.employeeManager.constants.ErrorCode;

public class EmployeeManagerException extends Exception {
	private final ErrorCode errorCode;
	
//	public EmployeeManagerException(String errorMessage) {
//		super(errorMessage);
//	}
//	
//	public EmployeeManagerException(String errorMessage,Throwable cause) {
//		super(errorMessage,cause);
//	}
	
	public EmployeeManagerException(ErrorCode errorCode) {
        super(errorCode.getMessage()); 
        this.errorCode = errorCode;
    }
	
	public EmployeeManagerException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
