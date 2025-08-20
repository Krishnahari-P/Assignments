package com.litmus7.employeeManager.constants;

public enum ErrorCode {
    EMPLOYEE_ADDED_SUCCESS(200, "Employee added successfully"),
    EMPLOYEE_FETCHING_SUCCESS(201, "Employee fetching successfull"),
    EMPLOYEE_DELETE_SUCCESS(202, "Employee deleted successfully"),
    EMPLOYEE_UPDATE_SUCCESS(203, "Employee details updated"),
    EMPLOYEE_BATCH_INSERTED(204, "Batch insertion successfull"),
    EMPLOYEE_TRANSFER_SUCCESS(205, "Employees transfered successfully"),
    CSV_WRITING_SUCCESS(206, "Employee data in csv inserted successfully"),
    CSV_READING_ERROR(207, "Error in reading csv file"),
    INVALID_CSV(300, "Invalid CSV format"),
    EMPTY_CSV(301, "Empty CSV"),
    NO_DATA_FOUND(302, "No valid data found"),
    EMPTY_LIST(303, "Provided list is empty"),
    EMPLOYEE_ALREADY_EXISTS(401, "Employee already exists"),
    EMPLOYEE_NOT_FOUND(402, "Employee not found"),
    DATABASE_ERROR(500, "Database error occurred"),
	INVALID_EMPLOYEE(403, "Invalid employee data"),
	FETCH_ERROR(501, "Fetching employees failed"),
	ADD_ERROR(502, "Error in adding employee"),
	DELETE_ERROR(503, "Error in deleting employee"),
	UPDATE_ERROR(504, "Error in updating employee"),
	BATCH_INSERT_ERROR(505, "Error in batch addition"),
	TRANSFER_ERROR(506, "Error in transfering employee to deparment"),
	WRITE_ERROR(507, "Error in writing CSV data to db");
	
    private final int code;       
    private final String message; 
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}