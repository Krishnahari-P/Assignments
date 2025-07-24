package com.litmus7.employeeManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {
	public static boolean isValidRow(String[] row) {
		return row.length==8;
	}
	
	public static boolean employeeExists(Connection con,int empId) throws SQLException {
		String query="select employee_id from employee where employee_id=?";
		try(PreparedStatement p=con.prepareStatement(query)){
			p.setInt(1, empId);
			try(ResultSet rs=p.executeQuery()){
				return rs.next();
			}
		}
		
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
		if (salaryStr == null || salaryStr.trim().isEmpty()) {
            return null;
        } 
		else if (salaryStr.matches("[1-9][0-9]*")) {
            return salaryStr;
        } 
		else {
            return null; // return null if it doesn't match
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
