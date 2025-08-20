package com.litmus7.employeeManager.controller;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.litmus7.employeeManager.constants.ErrorCode;
import com.litmus7.employeeManager.exceptions.EmployeeManagerException;
import com.litmus7.employeeManager.model.Employee;
import com.litmus7.employeeManager.response.Response;
import com.litmus7.employeeManager.service.EmployeeService;
import com.litmus7.employeeManager.util.CSVUtil;

public class EmployeeManagerController {
    private EmployeeService employeeService = new EmployeeService();
    private static final Logger logger=LogManager.getLogger(EmployeeManagerController.class);
    
    public Response<Object> writeDataToDb(String filePath) {
    	logger.trace("Received request to writeDataToDb with filePath={}", filePath);
        File file = new File(filePath);
        if (!file.exists() || !file.canRead() || !file.getName().toLowerCase().endsWith(".csv")) {
        	logger.warn("Invalid CSV file");
        	return new Response<>(ErrorCode.INVALID_CSV);
        }

        try {
        	logger.info("Reading CSV file");
            List<String[]> employeeRecords = CSVUtil.readCSV(filePath);
            int totalRecords = employeeRecords.size();

            if (totalRecords == 0) {
            	logger.warn("CSV file is empty.");
            	return new Response<>(ErrorCode.EMPTY_CSV);
            }
            
            logger.info("Processing CSV data and saving {} employee records to the database.", employeeRecords.size());

            List<Employee> employeesInserted = employeeService.processCSVAndSaveData(employeeRecords);

            int insertedCount = employeesInserted.size();
            if (insertedCount == 0) {
            	logger.warn("No valid employees found in the input file {}",file.getName());
            	return new Response<>(ErrorCode.NO_DATA_FOUND);
            }

            String message = insertedCount + " employees inserted successfully out of " + totalRecords + " records.";
            logger.info("CSV processing completed successfully. {}", message);
            return new Response<>(206,message);

        } 
        catch (EmployeeManagerException e) {
        	logger.error("Error occurred while writing CSV data to DB: {}", e.getMessage(), e);
            return new Response<>(e.getErrorCode());
        }
    }

    public Response<List<String>> getAllEmployeeNames() {
    	logger.trace("Received request to retrieve all employee names");
        try {
            List<String> employeeNames = employeeService.getEmployeeNames();
            if (employeeNames.isEmpty()) {
            	logger.warn("No employees found in the database");
            	return new Response<>(ErrorCode.EMPTY_LIST);
            }
            logger.info("Retrieved {} employee names", employeeNames.size());
            return new Response<>(201,"Employee names fetching successfull",employeeNames);
        }  
        catch (EmployeeManagerException e) {
        	logger.error("Error occurred while retrieving employee names: {}", e.getMessage(), e);
        	return new Response<>(e.getErrorCode());
		}
    }
    
    public Response<List<String>> getEmployeesById(List<Integer> employeeId) {
    	logger.trace("Received request to retrieve employees by ID list: {}", employeeId);
    	if(employeeId.isEmpty()) {
    		logger.warn("Provided employee ID list is empty");
    		return new Response<>(ErrorCode.EMPTY_LIST);
    	}
    	try {
    		List<String> employeeDetails=employeeService.getEmployeesById(employeeId);
    		logger.info("Retrieved {} employee records for given IDs", employeeDetails.size());
    		return new Response<>(201,"Employee details fetching success",employeeDetails);
    	}
    	catch(EmployeeManagerException e) {
    		logger.error("Error occurred while retrieving employees by ID: {}", e.getMessage(), e);
    		return new Response<>(e.getErrorCode());
    	}
    }

	public Response<Object> deleteEmployeeById(int employeeId) {
		logger.trace("Received request to delete employee with ID {}", employeeId);
		try {
			employeeService.deleteEmployeeById(employeeId);
			logger.info("Successfully deleted employee with ID {}", employeeId);
			return new Response<>(202,"Successfully deleted employee with ID "+employeeId);
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while deleting employee ID {}: {}", employeeId, e.getMessage(), e);
			return new Response<>(e.getErrorCode());
		}
	}

	public Response<Object> addEmployee(Employee employee) {
		logger.trace("Received request to add a new employee: {}", employee);
		try {
			boolean employeeAdded=employeeService.addEmployee(employee);
			if(!employeeAdded) {
				logger.warn("Failed to insert employee: {}", employee);
				return new Response<>(ErrorCode.ADD_ERROR);
			}
			logger.info("Employee inserted successfully: {}", employee);
			return new Response<>(ErrorCode.EMPLOYEE_ADDED_SUCCESS);
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while adding employee: {}", e.getMessage(), e);
			return new Response<>(e.getErrorCode());
		}
	}

	public Response<Object> updateEmployee(Employee employee) {
		logger.trace("Received request to update employee: {}", employee);
		try {
			boolean employeeUpdated=employeeService.updateEmployee(employee);
			if(!employeeUpdated) {
				logger.warn("Failed to update employee: {}", employee);
				return new Response<>(ErrorCode.UPDATE_ERROR);
			}
			logger.info("Employee details updated successfully: {}", employee);
			return new Response<>(ErrorCode.EMPLOYEE_UPDATE_SUCCESS);
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while updating employee: {}", e.getMessage(), e);
			return new Response<>(e.getErrorCode());
		}
	}

	public Response<Object> addEmployeesInBatch(List<Employee> employeeList) {
		logger.trace("Received request to add {} employees in batch", employeeList.size());
		try {
			int totalRecords=employeeList.size();
			int employeesAdded=employeeService.addEmployeesInBatch(employeeList);
			if(totalRecords==0) {
				logger.warn("Provided employee list is empty");
				return new Response<>(ErrorCode.EMPTY_LIST);
			}
			if(employeesAdded==0) {
				logger.warn("No valid employees found");
				return new Response<>(ErrorCode.INVALID_EMPLOYEE);
			}
			if(employeesAdded!=totalRecords) {
				logger.warn("Only {} out of {} employees inserted", employeesAdded, totalRecords);
				return new Response<>(204,"Inserted " + employeesAdded + " out of " + totalRecords + " employees.");
			}
			logger.info("All {} employees added successfully", totalRecords);
			return new Response<>(200,"All employees added successfully.");
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while adding employees in batch: {}", e.getMessage(), e);
			return new Response<>(e.getErrorCode());
		}
	}
	public Response<Object> transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) {
		logger.trace("Received request to transfer employees {} to department {}", employeeIds, newDepartment);
        try {
            boolean transferedEmployees=employeeService.transferEmployeesToDepartment(employeeIds, newDepartment);
            if(!transferedEmployees) {
            	logger.warn("Department transfer failed for employees {}", employeeIds);
            	//return new Response(204,"Department update failed");
            	return new Response<>(ErrorCode.TRANSFER_ERROR);
            }
            logger.info("Transferred {} employees to department {}", employeeIds.size(), newDepartment);
            return new Response<>(205, "All employees transferred successfully to " + newDepartment);
        } 
        catch (EmployeeManagerException e) {
        	logger.error("Error occurred while transferring employees to department {}: {}", newDepartment, e.getMessage(), e);
            return new Response<>(e.getErrorCode());
        }
    }
}
