package com.litmus7.employeeManager.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.litmus7.employeeManager.validator.Validator;
import com.litmus7.employeeManager.dao.EmployeeDao;
import com.litmus7.employeeManager.exceptions.EmployeeManagerException;
import com.litmus7.employeeManager.model.Employee;

public class EmployeeService {
	
	private static final Logger logger=LogManager.getLogger(EmployeeService.class);
    private final EmployeeDao employeeDao = new EmployeeDao();

    public List<Employee> processCSVAndSaveData(List<String[]> employeeRecords) throws EmployeeManagerException {
    	logger.trace("Entering processCSVAndSaveData() with {} records", employeeRecords.size());
        List<Employee> validEmployees = new ArrayList<>();

        if (employeeRecords.isEmpty()) {
        	logger.warn("No employee records provided for processing");
        	return validEmployees;
        }

        try (Connection connection = employeeDao.getConnection()) {
            connection.setAutoCommit(false);

            for (String[] record : employeeRecords) {
                try {
                    Employee employee = mapToEmployee(record);
                    if (employee == null || !Validator.isValidEmployee(employee)) {
                    	logger.debug("Skipping invalid employee record: {}", (Object) record);
                    	continue;
                    }

                    if (employeeDao.employeeExists(employee.getEmployeeId())) {
                    	logger.debug("Duplicate employee ID {} found, skipping", employee.getEmployeeId());
                    	continue;
                    }

                    validEmployees.add(employee);
                }
                catch (Exception e) {
                	logger.warn("Error mapping or validating record, skipping: {}", (Object) record, e);
                    continue;
                }
            }

            employeeDao.saveDataToDb(validEmployees);
            connection.commit();
            logger.info("Successfully saved {} valid employees", validEmployees.size());
        } 
        catch (SQLException e) {
        	logger.error("Database connection or commit failed", e);
			throw new EmployeeManagerException("Database connection or commit failed: "+e.getMessage(),e);
		} 
        logger.trace("Exiting processCSVAndSaveData()");

        return validEmployees;
    }

    private Employee mapToEmployee(String[] record) {
        if (record.length != 8) {
        	logger.debug("Invalid record length: expected 8, got {}", record.length);
        	return null;
        }

        try {
            int employeeId = Integer.parseInt(record[0].trim());
            String firstName = record[1].trim();
            String lastName = record[2].trim();
            String email = record[3].trim();
            String phoneNo = record[4].trim();
            String department = record[5].trim();
            int salary = Integer.parseInt(record[6].trim());
            String joinDate=record[7].trim();

            return new Employee(employeeId, firstName, lastName, email, phoneNo, department, salary, joinDate);
        }
        catch (Exception e) {
        	logger.warn("Error parsing record: {}", (Object) record, e);
        	e.printStackTrace();
            return null;
        }
    }
    
    public List<String> getEmployeeNames() throws EmployeeManagerException {
    	logger.trace("Attempting to fetch all employee names");
        try {
        	List<String> employeeNames = employeeDao.fetchEmployeeNames();
        	logger.info("Fetching completed");
            return employeeNames;
        }
        catch( Exception e) {
        	logger.error("Fetching employee names failed", e);
        	throw new EmployeeManagerException("Fetching employee names failed: "+e.getMessage(),e);
        }
    }

    public List<String> getEmployeesById(List<Integer> employeeIdList) throws EmployeeManagerException {
    	logger.trace("Attempting to Fetch details using {}",employeeIdList);
        try  {
            List<Integer> validEmployeeIds = new ArrayList<>();
            for (int employeeId : employeeIdList) {
                if (employeeDao.employeeExists(employeeId)) {
                    validEmployeeIds.add(employeeId);
                }
                else {
                    logger.info("Employee ID {} does not exist, skipping", employeeId);
                }
            }

            if (validEmployeeIds.isEmpty()) {
            	logger.warn("No valid employee IDs found in input list");
                throw new EmployeeManagerException("No valid employee ID's found.");
            }

            List<String> employees = employeeDao.fetchEmployeesById(validEmployeeIds);
            logger.info("Retrieved {} employee records", employees.size());
            return employees;
        }
        catch (Exception e) {
        	logger.error("Fetching employee details failed", e);
            throw new EmployeeManagerException("Fetching employee details failed:",e);
        }
    }


	public boolean deleteEmployeeById(int employeeId) throws EmployeeManagerException {
		logger.trace("Attempting to delete employee with ID {}", employeeId);
		try{
			if (!employeeDao.employeeExists(employeeId)) {
				logger.warn("Employee ID {} does not exist", employeeId);
                throw new EmployeeManagerException("Employee doesn't exists for the given ID "+employeeId);
            }
			 boolean employeeDeleted = employeeDao.deleteEmployeeById(employeeId);
			 if (employeeDeleted) {
		        logger.info("Successfully deleted employee with ID {}", employeeId);
		     }
			 else {
		        logger.warn("No employee found for deletion with ID {}", employeeId);
		     }
			 return employeeDeleted;
		}
		catch(Exception e) {
			logger.error("Deletion failed for employee ID {}", employeeId, e);
			throw new EmployeeManagerException("Deletion falied: "+e.getMessage(),e);
		}
	}

	public boolean addEmployee(Employee employee) throws EmployeeManagerException {
		logger.trace("Attempting to add an employee with ID {}",employee.getEmployeeId());
		try{
			if (employeeDao.employeeExists(employee.getEmployeeId())) {
				logger.warn("Employee already exists with ID {}",employee.getEmployeeId());
				throw new EmployeeManagerException("Employee already exists with given employee Id");
            }
			if (employee == null || !Validator.isValidEmployee(employee)) {
				logger.warn("Invalid employee with ID {}",employee.getEmployeeId());
            	throw new EmployeeManagerException("Invalid employee details");
            }
			boolean employeeAdded = employeeDao.addEmployee(employee);
			if(employeeAdded) {
				logger.info("Employee with ID {} added successfully",employee.getEmployeeId());
			}
			else {
				logger.warn("Employee with ID {} addition failed",employee.getEmployeeId());
			}
			return employeeAdded;
		}
		catch(Exception e) {
			logger.error("Addition failed for employee ID {}", employee.getEmployeeId(), e);
			throw new EmployeeManagerException("Employee addition failed: "+e.getMessage(),e);
		}
		
	}

	public boolean updateEmployee(Employee employee) throws EmployeeManagerException {
		logger.trace("Attempting to update an employee with ID {}",employee.getEmployeeId());
		try{
			if (employee == null || !Validator.isValidEmployee(employee)) {
				logger.warn("Invalid employee details with ID {}",employee.getEmployeeId());
            	throw new EmployeeManagerException("Invalid employee details");
            }
			if(!employeeDao.employeeExists(employee.getEmployeeId())) {
				logger.warn("Employee doesn't exists for the ID {}",employee.getEmployeeId());
				throw new EmployeeManagerException("Employee doesn't exists for the given ID "+employee.getEmployeeId());
			}
			boolean employeeUpdated = employeeDao.updateEmployee(employee);
			if(employeeUpdated) {
				logger.info("Employee with id {} updated successfully",employee.getEmployeeId());
			}
			else {
				logger.warn("Employee with id {} updation failed",employee.getEmployeeId());
			}
			
			return employeeUpdated;
		}
		catch(Exception e) {
			logger.error("Updation failed for employee ID {}", employee.getEmployeeId(), e);
			throw new EmployeeManagerException("Update employee failed: "+e.getMessage(),e);
		}
	}

	public int addEmployeesInBatch(List<Employee> employeeList) throws EmployeeManagerException {
		logger.trace("Attempting batch processing of {}",employeeList);
		List<Employee> validEmployees=new ArrayList<>();
		try{
			for(Employee employee:employeeList) {
				if(!Validator.isValidEmployee(employee)) {
					logger.warn("Skipping invalid employee details with ID {}",employee.getEmployeeId());
					continue;
				}
				if(employeeDao.employeeExists(employee.getEmployeeId())) {
					logger.warn("Skipping existing employee with ID {}",employee.getEmployeeId());
					continue;
				}
				validEmployees.add(employee);
			}
			int employeesInserted=employeeDao.addEmployeesInBatch(validEmployees);
			logger.info("Batch insert complete: {} succeeded, {} failed", employeesInserted, employeeList.size()-employeesInserted);
			return employeesInserted;
		}
		catch(Exception e) {
			logger.error("Batch addition failed:",e);
			throw new EmployeeManagerException("Batch addition failed: "+e.getMessage(),e);
		}
	}
	
	public boolean transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) throws EmployeeManagerException {
		logger.trace("Attempting transferEmployeesToDepartment()");
        if (employeeIds.isEmpty() || newDepartment == null) {
        	logger.warn("Employee IDs or Department name is invalid.");
            throw new EmployeeManagerException("Employee IDs or Department name is invalid.");
        }
        try{
			boolean employeesTransfered = employeeDao.transferEmployeesToDepartment(employeeIds, newDepartment);
			logger.info("Successfully transferred {} employees to department {}", employeeIds.size(), newDepartment);
			return employeesTransfered;
		}
		catch(Exception e) {
			logger.error("Employee transfer failed for {} employees to department {}" ,employeeIds.size(), newDepartment,e);
			throw new EmployeeManagerException("Employee transfer failed: "+e.getMessage(),e);
		}
        
    }

}
