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
		try(PreparedStatement statement=con.prepareStatement(getEmployeeData)){
			statement.setInt(1, empId);
			try(ResultSet rs=statement.executeQuery()){
				return rs.next();
			}
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
		
	}
	
	public void saveDataToDb(Connection connection, List<Employee> employeeList) throws EmployeeManagerException {
        String insertEmployee = SQLConstants.insertEmployee;
        try (PreparedStatement statement = connection.prepareStatement(insertEmployee)) {
        	int batchSize=0;
            for (Employee employee : employeeList) {
                statement.setInt(1, employee.getEmployeeId());
                statement.setString(2, employee.getFirstName());
                statement.setString(3, employee.getLastName());
                statement.setString(4, employee.getEmail());
                statement.setString(5, employee.getPhoneNumber());
                statement.setString(6, employee.getDepartment());
                statement.setInt(7, employee.getSalary());
                statement.setDate(8, Date.valueOf(employee.getJoinDate()));
                statement.addBatch();
                if(++batchSize%3==0) {
                	statement.executeBatch();
                }
            }
            statement.executeBatch();
        }
        catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
    }
	
	public List<String> fetchEmployeeNames(Connection connection) throws EmployeeManagerException{
		List<String> employeeNames=new ArrayList<>();
		String getEmployeeData=SQLConstants.getEmployeeName;
		try(PreparedStatement statement=connection.prepareStatement(getEmployeeData)){
			ResultSet resultSet=statement.executeQuery();
			while(resultSet.next()) {
				String fullName=resultSet.getString("first_name")+" "+resultSet.getString("last_name");
				employeeNames.add(fullName);
			}
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
		return employeeNames;
	}


	public List<String> fetchEmployeesById(Connection connection,List<Integer> employeeIdList) throws EmployeeManagerException {
		List<String> employeeDetails=new ArrayList<>();
		String getEmployeeById=SQLConstants.getEmployeeById;
		try(PreparedStatement statement=connection.prepareStatement(getEmployeeById)){
			for(int employeeId:employeeIdList) {
				statement.setInt(1, employeeId);
				ResultSet resultSet=statement.executeQuery();
				while(resultSet.next()) {
					String firstName=resultSet.getString("first_name");
					String lastName=resultSet.getString("last_name");
					String email=resultSet.getString("email");
					String phoneNo=resultSet.getString("phone_no");
					String department=resultSet.getString("department");
					int salary=resultSet.getInt("salary");
					Date joinDate=resultSet.getDate("join_date");
					String formattedJoinDate = (joinDate != null) ? joinDate.toString() : "N/A";
					String employeeInformation = String.format("ID: %d, Name: %s %s, Email: %s, Phone: %s, Department: %s, Salary: %d, Join Date: %s",employeeId,firstName, lastName, email, phoneNo, department, salary, formattedJoinDate);
					employeeDetails.add(employeeInformation);
				}
			}
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
		return employeeDetails;
	}


	public boolean deleteEmployeeById(Connection connection, int employeeId) throws EmployeeManagerException {
		String deleteEmployeeById=SQLConstants.deleteEmployeeById;
		try(PreparedStatement statement=connection.prepareStatement(deleteEmployeeById)){
			statement.setInt(1, employeeId);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected > 0;
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
	}


	public boolean addEmployee(Connection connection,Employee employee) throws EmployeeManagerException {
		String insertEmployee=SQLConstants.insertEmployee;
		try(PreparedStatement statement=connection.prepareStatement(insertEmployee)){
			statement.setInt(1, employee.getEmployeeId());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getLastName());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getPhoneNumber());
            statement.setString(6, employee.getDepartment());
            statement.setInt(7, employee.getSalary());
            statement.setDate(8, Date.valueOf(employee.getJoinDate()));
            int rowsAffected = statement.executeUpdate();
            return rowsAffected>0;
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
	}


	public boolean updateEmployee(Connection connection,Employee employee) throws EmployeeManagerException {
		String updateEmployee=SQLConstants.updateEmployee;
		try(PreparedStatement statement=connection.prepareStatement(updateEmployee)){
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getEmail());
            statement.setString(4, employee.getPhoneNumber());
            statement.setString(5, employee.getDepartment());
            statement.setInt(6, employee.getSalary());
            statement.setDate(7, Date.valueOf(employee.getJoinDate()));
            statement.setInt(8, employee.getEmployeeId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected>0;
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
	}
	
}
