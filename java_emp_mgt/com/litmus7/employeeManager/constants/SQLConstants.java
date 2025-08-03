package com.litmus7.employeeManager.constants;

public class SQLConstants {
	public final static String checkEmployeeExists = "SELECT employee_id FROM employee WHERE employee_id=?";
	public final static String insertEmployee = "INSERT INTO employee VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public final static String getEmployeeName= "SELECT first_name,last_name FROM employee";
}
