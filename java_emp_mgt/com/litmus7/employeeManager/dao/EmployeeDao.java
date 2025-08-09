package com.litmus7.employeeManager.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.litmus7.employeeManager.constants.SQLConstants;
import com.litmus7.employeeManager.exceptions.EmployeeManagerException;
import com.litmus7.employeeManager.model.Employee;


public class EmployeeDao {	
	private static final Logger logger=LogManager.getLogger(EmployeeDao.class);
	public Connection getConnection() throws EmployeeManagerException {
		logger.trace("Entering getConnection()");
		
	    Properties properties = new Properties();
	    try (FileInputStream fis = new FileInputStream("src\\\\com\\\\litmus7\\\\resources\\\\db.properties")) {
	        properties.load(fis);
	    }
	    catch (IOException e) {
	    	logger.error("Failed to load database properties", e);
	        throw new EmployeeManagerException("Failed to load database properties: "+e.getMessage(), e);
	    }

	    String URL = properties.getProperty("jdbc.url");
	    String USERNAME = properties.getProperty("jdbc.username");
	    String PASSWORD = properties.getProperty("jdbc.password");

	    try {
	    	logger.debug("Connecting to DB with URL: {}",URL);
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} 
	    catch(SQLException e) {
	    	logger.error("Database connection failed",e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
	}

	    
	public boolean employeeExists(int employeeId) throws EmployeeManagerException {
		logger.trace("Entering employeeExists(empId={})", employeeId);
		String getEmployeeData=SQLConstants.checkEmployeeExists;
		try(Connection connection=getConnection();PreparedStatement statement=connection.prepareStatement(getEmployeeData)){
			statement.setInt(1, employeeId);
			logger.debug("Executing SQL query: {} with employeeId: {}",getEmployeeData,employeeId);
			try(ResultSet rs=statement.executeQuery()){
				boolean exists = rs.next();
                logger.trace("Exiting employeeExists with result: {}", exists);
				return exists;
			}
		}
		catch(SQLException e) {
			logger.error("Error in checking if employee exists",e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
		
	}
	
	public void saveDataToDb(List<Employee> employeeList) throws EmployeeManagerException {
		logger.trace("Entering saveDataToDb() with employeeList size = {}", employeeList.size());
        String insertEmployee = SQLConstants.insertEmployee;
        try (Connection connection=getConnection();PreparedStatement statement = connection.prepareStatement(insertEmployee)) {
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
                logger.debug("Added employee to batch: {}", employee.getEmployeeId());
                if(++batchSize%3==0) {
                	statement.executeBatch();
                	logger.debug("Executed batch of 3 inserts");
                }
            }
            statement.executeBatch();
            logger.info("Employee insert completed.");
        }
        catch(SQLException e) {
        	logger.error("Error saving data to DB", e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
    }
	
	public List<String> fetchEmployeeNames() throws EmployeeManagerException{
		logger.trace("Entering fetchEmployeeNames()");
		List<String> employeeNames=new ArrayList<>();
		String getEmployeeData=SQLConstants.getEmployeeName;
		try(Connection connection=getConnection();PreparedStatement statement=connection.prepareStatement(getEmployeeData)){
			logger.debug("Executing SQL query: {}",getEmployeeData);
			ResultSet resultSet=statement.executeQuery();
			while(resultSet.next()) {
				String fullName=resultSet.getString("first_name")+" "+resultSet.getString("last_name");
				employeeNames.add(fullName);
			}
			logger.info("Exiting fetchEmployeeNames()");
		}
		catch(SQLException e) {
			logger.error("Error fetching employee names", e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
		return employeeNames;
	}


	public List<String> fetchEmployeesById(List<Integer> employeeIdList) throws EmployeeManagerException {
		logger.trace("Entering fetchEmployeeById() with employeeIdList of size {}",employeeIdList.size());
		List<String> employeeDetails=new ArrayList<>();
		String getEmployeeById=SQLConstants.getEmployeeById;
		try(Connection connection=getConnection();PreparedStatement statement=connection.prepareStatement(getEmployeeById)){
			for(int employeeId:employeeIdList) {
				statement.setInt(1, employeeId);
				logger.debug("Executing SQL query: {}",getEmployeeById);
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
			logger.error("Error fetching employee details", e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
		logger.trace("Exiting fetchEmployeeById()");
		return employeeDetails;
	}


	public boolean deleteEmployeeById(int employeeId) throws EmployeeManagerException {
		logger.trace("Entering deleteEmployeeById with employeeId={}", employeeId);
		String deleteEmployeeById=SQLConstants.deleteEmployeeById;
		try(Connection connection=getConnection();PreparedStatement statement=connection.prepareStatement(deleteEmployeeById)){
			statement.setInt(1, employeeId);
			logger.debug("Executing SQL query {}",deleteEmployeeById);
			int rowsAffected = statement.executeUpdate();
			logger.debug("Rows affected: {}", rowsAffected);
			logger.trace("Exiting deleteEmployeeById");
			return rowsAffected>0;
		}
		catch(SQLException e) {
			logger.error("Error in deleting employee with ID {}", employeeId, e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
	}


	public boolean addEmployee(Employee employee) throws EmployeeManagerException {
		logger.trace("Entering addEmployee() with employeeId = {}",employee.getEmployeeId());
		String insertEmployee=SQLConstants.insertEmployee;
		try(Connection connection=getConnection();PreparedStatement statement=connection.prepareStatement(insertEmployee)){
			statement.setInt(1, employee.getEmployeeId());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getLastName());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getPhoneNumber());
            statement.setString(6, employee.getDepartment());
            statement.setInt(7, employee.getSalary());
            statement.setDate(8, Date.valueOf(employee.getJoinDate()));
            logger.debug("Executing SQL query {}",insertEmployee);
            int rowsAffected = statement.executeUpdate();
            logger.info("Rows affected: {}",rowsAffected);
            logger.trace("Exiting addEmployee()");
            return rowsAffected>0;
		}
		catch(SQLException e) {
			logger.error("Error adding employee with employeeId {}",employee.getEmployeeId(),e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
	}


	public boolean updateEmployee(Employee employee) throws EmployeeManagerException {
		logger.trace("Entering updateEmployee() with employeeId {}",employee.getEmployeeId());
		String updateEmployee=SQLConstants.updateEmployee;
		try(Connection connection=getConnection();PreparedStatement statement=connection.prepareStatement(updateEmployee)){
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getEmail());
            statement.setString(4, employee.getPhoneNumber());
            statement.setString(5, employee.getDepartment());
            statement.setInt(6, employee.getSalary());
            statement.setDate(7, Date.valueOf(employee.getJoinDate()));
            statement.setInt(8, employee.getEmployeeId());
            logger.debug("Executing SQL query {}",updateEmployee);
            int rowsAffected = statement.executeUpdate();
            logger.info("Rows affected: {}",rowsAffected);
            logger.trace("Exiting updateEmployee()");
            return rowsAffected>0;
		}
		catch(SQLException e) {
			logger.error("Error updating employee with employeeId {}",employee.getEmployeeId(),e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
	}


	public int addEmployeesInBatch(List<Employee> employeeList) throws EmployeeManagerException {
		logger.trace("Entering addEmployeesInBatch() with employeeList of size {}",employeeList.size());
		String insertEmployee = SQLConstants.insertEmployee;
		int employeesInserted=0;
		try (Connection connection=getConnection();PreparedStatement statement = connection.prepareStatement(insertEmployee)) {
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
                logger.debug("Added employee to batch: {}", employee.getEmployeeId());
                
            }
            int[] results=statement.executeBatch();
            logger.info("Batch executed");
            for(int i=0;i<results.length;i++) {
            	if(results[i]==Statement.EXECUTE_FAILED) {
            		logger.warn("Insert failed for employeeId {}",employeeList.get(i).getEmployeeId());
            		//System.err.println("Insert failed for Employee ID: "+employeeList.get(i).getEmployeeId());
            	}
            	else {
            		employeesInserted++;
            	}
            }
            logger.trace("Exiting addEmployeesInBatch()");
            return employeesInserted;
        }
        catch(SQLException e) {
        	logger.error("Error in batch processing",e);
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		}
	}
	
	public boolean transferEmployeesToDepartment(List<Integer> employeeIds,String department) throws EmployeeManagerException {
		logger.trace("Entering transferEmployeesToDepartment() with employeeIds list of size {} and department {}",employeeIds.size(),department);
		String updateEmployeeDepartment=SQLConstants.updateEmployeeDepartment;
		Connection connection=null;
		PreparedStatement statement=null;
		try{
			connection=getConnection();
			connection.setAutoCommit(false);
			statement=connection.prepareStatement(updateEmployeeDepartment);
			for(int employeeId:employeeIds) {
				statement.setString(1,department);
				statement.setInt(2, employeeId);
				logger.debug("Executing SQL update for employeeId={} to department='{}'", employeeId, department);
				int updates=statement.executeUpdate();
				if(updates==0) {
					connection.rollback();
					logger.warn("Rollback due to invalid employeeId {}", employeeId);
					throw new EmployeeManagerException("Couldn't update due to invalid employee data for employeeId "+employeeId);
				}
			}
			logger.trace("Exiting transferEmployeesToDepartment()");
			connection.commit();
			return true;
		}
		catch(SQLException e) {
			try {
				connection.rollback();
			}
			catch(SQLException se) {
				logger.debug("Roll back failed ",se);
				throw new EmployeeManagerException("Roll back failed: "+se.getMessage(),se);
			}
			logger.error("SQL error while transferring employees to department '{}'", department, e);
			throw new EmployeeManagerException("Error in sql operation: "+e.getMessage(),e);
		}
		finally {
			try {
				statement.close();
			}
			catch (SQLException e) {
				logger.debug("Failed to close prepared statement ",e);
	            throw new EmployeeManagerException("Failed to close prepared statement: "+e.getMessage(), e);
	        }
			try {
				connection.setAutoCommit(true);
			}
			catch (SQLException e) {
				logger.debug("Failed to reset auto-commit ",e);
	            throw new EmployeeManagerException("Failed to reset auto-commit: "+e.getMessage(), e);
	        }
			try {
				connection.close();
			}
			catch (SQLException e) {
				logger.debug("Failed to close connection",e);
	            throw new EmployeeManagerException("Failed to close connection: "+e.getMessage(), e);
	        }
		}
	}
	
}
