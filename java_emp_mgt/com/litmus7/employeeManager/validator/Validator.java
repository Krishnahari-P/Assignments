package com.litmus7.employeeManager.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {
	public static boolean isValidRow(String[] row) {
		return row.length==8;
	}
	
	public static boolean validEmployeeId(int employeeId) {
		return employeeId>0;
	}
	
	public static boolean validateName(String name) {
		return name.matches("[A-Za-z]+");	
	}
	
	public static boolean validateEmail(String email) {
		return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	}
	
	public static boolean validatePhoneNumber(String phoneNo) {
		return phoneNo.matches("^(\\+91|91)?[6-9]\\d{9}$");
	}
	
	public static String validateSalary(String salaryStr) {
		if (salaryStr != null && salaryStr.matches("[1-9][0-9]*")) {
		    return salaryStr;
		}
		else {
		    return null;
		}

	}
	
	public static LocalDate validateDate(String joinDateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate ld;
		try {
			ld=LocalDate.parse(joinDateStr,formatter);
		}
		catch(DateTimeParseException dte) {
			ld=null;
		}
		return ld;
	}
}
