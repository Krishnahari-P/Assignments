package com.litmus7.employeeManager.dao;
import com.litmus7.employeeManager.model.Employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class EmployeeDao {
	private static final String URL="jdbc:mysql://localhost:3306/java_db";
	private static final String USERNAME="root";
	private static final String PASSWORD="krish@123";
	
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL,USERNAME,PASSWORD);
	}
	
	public static boolean employeeExists(Connection con,int empId) throws SQLException {
		String getEmployeeData="select employee_id from employee where employee_id=?";
		try(PreparedStatement p=con.prepareStatement(getEmployeeData)){
			p.setInt(1, empId);
			try(ResultSet rs=p.executeQuery()){
				return rs.next();
			}
		}
		
	}
	
	public void insertEmployees(Connection con, List<Employee> employeeList) throws SQLException {
        String insertEmployee = "INSERT INTO employee VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertEmployee)) {
        	int batchSize=0;
            for (Employee employee : employeeList) {
                ps.setInt(1, employee.getId());
                ps.setString(2, employee.getFirstName());
                ps.setString(3, employee.getLastName());
                ps.setString(4, employee.getEmail());
                ps.setString(5, employee.getPhoneNumber());
                ps.setString(6, employee.getDepartment());
                ps.setInt(7, employee.getSalary());
                ps.setDate(8, Date.valueOf(employee.getJoinDate()));
                ps.addBatch();
                if(++batchSize%3==0) {
                	ps.executeBatch();
                }
            }
            ps.executeBatch();
        }
    }
	
}
