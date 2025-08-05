package com.litmus7.employeeManager.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.litmus7.employeeManager.constants.SQLConstants;
import com.litmus7.employeeManager.exceptions.EmployeeManagerException;
import com.litmus7.employeeManager.model.Employee;


public class EmployeeDao {	
	public Connection getConnection() throws EmployeeManagerException {
	    Properties properties = new Properties();
	    try (FileInputStream fis = new FileInputStream("src\\com\\litmus7\\resources\\db.properties")) {
	        properties.load(fis);
	    }
	    catch (IOException e) {
	        throw new EmployeeManagerException("Failed to load database properties", e);
	    }

	    String URL = properties.getProperty("jdbc.url");
	    String USERNAME = properties.getProperty("jdbc.username");
	    String PASSWORD = properties.getProperty("jdbc.password");

	    try {
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} 
	    catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
	}

	    
	public boolean employeeExists(Connection con,int empId) throws EmployeeManagerException {
		String getEmployeeData=SQLConstants.checkEmployeeExists;
		try(PreparedStatement p=con.prepareStatement(getEmployeeData)){
			p.setInt(1, empId);
			try(ResultSet rs=p.executeQuery()){
				return rs.next();
			}
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
		
	}
	
	public void saveDataToDb(Connection connection, List<Employee> employeeList) throws EmployeeManagerException {
        String insertEmployee = SQLConstants.insertEmployee;
        try (PreparedStatement ps = connection.prepareStatement(insertEmployee)) {
        	int batchSize=0;
            for (Employee employee : employeeList) {
                ps.setInt(1, employee.getEmployeeId());
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
        catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
    }
	
	public List<String> fetchEmployeeNames(Connection connection) throws EmployeeManagerException{
		List<String> employeeNames=new ArrayList<>();
		String getEmployeeData=SQLConstants.getEmployeeName;
		try(PreparedStatement statement=connection.prepareStatement(getEmployeeData)){
			ResultSet rs=statement.executeQuery();
			while(rs.next()) {
				String fullName=rs.getString("first_name")+" "+rs.getString("last_name");
				employeeNames.add(fullName);
			}
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
		return employeeNames;
	}
	
}
