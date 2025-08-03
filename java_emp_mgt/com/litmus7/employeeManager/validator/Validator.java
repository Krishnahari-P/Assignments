package com.litmus7.employeeManager.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.litmus7.employeeManager.model.Employee;

public class Validator {
	
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
	
	public static boolean isValidEmployee(Employee emp) {
        return emp != null &&
               validEmployeeId(emp.getEmployeeId()) &&
               validateName(emp.getFirstName()) &&
               validateName(emp.getLastName()) &&
               validateEmail(emp.getEmail()) &&
               validatePhoneNumber(emp.getPhoneNumber()) &&
               validateName(emp.getDepartment()) &&
               emp.getSalary() > 0 &&
               emp.getJoinDate() != null;
    }
}
