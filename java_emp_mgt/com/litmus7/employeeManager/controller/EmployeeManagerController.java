package com.litmus7.employeeManager.controller;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.litmus7.employeeManager.exceptions.EmployeeManagerException;
import com.litmus7.employeeManager.model.Employee;
import com.litmus7.employeeManager.response.Response;
import com.litmus7.employeeManager.service.EmployeeService;
import com.litmus7.employeeManager.util.CSVUtil;

public class EmployeeManagerController {
    private EmployeeService employeeService = new EmployeeService();
    private static final Logger logger=LogManager.getLogger(EmployeeManagerController.class);
    
    public Response writeDataToDb(String filePath) {
    	logger.trace("Received request to writeDataToDb with filePath={}", filePath);
        File file = new File(filePath);
        if (!file.exists() || !file.canRead() || !file.getName().toLowerCase().endsWith(".csv")) {
        	logger.warn("Invalid CSV file");
            return new Response(400, "Invalid CSV file!");
        }

        try {
        	logger.info("Reading CSV file");
            List<String[]> employeeRecords = CSVUtil.readCSV(filePath);
            int totalRecords = employeeRecords.size();

            if (totalRecords == 0) {
            	logger.warn("CSV file is empty.");
                return new Response(204, "CSV file is empty.");
            }
            
            logger.info("Processing CSV data and saving {} employee records to the database.", employeeRecords.size());

            List<Employee> employeesInserted = employeeService.processCSVAndSaveData(employeeRecords);

            int insertedCount = employeesInserted.size();
            if (insertedCount == 0) {
            	logger.warn("No valid employees found in the input file {}",file.getName());
                return new Response(204, "No valid employees found in the input file (Total rows: " + totalRecords + ")");
            }

            String message = insertedCount + " employees inserted successfully out of " + totalRecords + " records.";
            logger.info("CSV processing completed successfully. {}", message);
            return new Response(200, message);

        } 
        catch (EmployeeManagerException e) {
        	logger.error("Error occurred while writing CSV data to DB: {}", e.getMessage(), e);
            return new Response(500, "An error occurred: " + e.getMessage());
        }
    }

    public Response getAllEmployeeNames() {
    	logger.trace("Received request to retrieve all employee names");
        try {
            List<String> employeeNames = employeeService.getEmployeeNames();
            if (employeeNames.isEmpty()) {
            	logger.warn("No employees found in the database");
                return new Response(204, "No employees found");
            }
            logger.info("Retrieved {} employee names", employeeNames.size());
            return new Response(200, String.join("\n", employeeNames));
        }  
        catch (EmployeeManagerException e) {
        	logger.error("Error occurred while retrieving employee names: {}", e.getMessage(), e);
        	return new Response(500, "Error: " + e.getMessage());
		}
    }
    
    public Response getEmployeesById(List<Integer> employeeId) {
    	logger.trace("Received request to retrieve employees by ID list: {}", employeeId);
    	if(employeeId.isEmpty()) {
    		logger.warn("Provided employee ID list is empty");
    		return new Response(204, "Provided list is empty");
    	}
    	try {
    		List<String> employeeDetails=employeeService.getEmployeesById(employeeId);
    		logger.info("Retrieved {} employee records for given IDs", employeeDetails.size());
    		return new Response(200,String.join("\n", employeeDetails));
    	}
    	catch(EmployeeManagerException e) {
    		logger.error("Error occurred while retrieving employees by ID: {}", e.getMessage(), e);
    		return new Response(500, "Error: " + e.getMessage());
    	}
    }

	public Response deleteEmployeeById(int employeeId) {
		logger.trace("Received request to delete employee with ID {}", employeeId);
		try {
			boolean employeeDeleted=employeeService.deleteEmployeeById(employeeId);
			if(!employeeDeleted) {
				logger.warn("Employee with ID {} not found or deletion failed", employeeId);
				return new Response(204,"Deletion failed");
			}
			logger.info("Successfully deleted employee with ID {}", employeeId);
			return new Response(200, "Employee deleted for the given ID: "+employeeId);
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while deleting employee ID {}: {}", employeeId, e.getMessage(), e);
			return new Response(500, "Error: " + e.getMessage());
		}
	}

	public Response addEmployee(Employee employee) {
		logger.trace("Received request to add a new employee: {}", employee);
		try {
			boolean employeeAdded=employeeService.addEmployee(employee);
			if(!employeeAdded) {
				logger.warn("Failed to insert employee: {}", employee);
				return new Response(204,"Insertion failed");
			}
			logger.info("Employee inserted successfully: {}", employee);
			return new Response(200,"Employee inserted successfully");
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while adding employee: {}", e.getMessage(), e);
			return new Response(500, "Error: " + e.getMessage());
		}
	}

	public Response updateEmployee(Employee employee) {
		logger.trace("Received request to update employee: {}", employee);
		try {
			boolean employeeUpdated=employeeService.updateEmployee(employee);
			if(!employeeUpdated) {
				logger.warn("Failed to update employee: {}", employee);
				return new Response(204, "Updation failed");
			}
			logger.info("Employee details updated successfully: {}", employee);
			return new Response(200,"Employee details updated");
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while updating employee: {}", e.getMessage(), e);
			return new Response(500, "Error: " + e.getMessage());
		}
	}

	public Response addEmployeesInBatch(List<Employee> employeeList) {
		logger.trace("Received request to add {} employees in batch", employeeList.size());
		try {
			int totalRecords=employeeList.size();
			int employeesAdded=employeeService.addEmployeesInBatch(employeeList);
			if(totalRecords==0) {
				logger.warn("Provided employee list is empty");
				return new Response(400,"Provided list is empty");
			}
			if(employeesAdded!=totalRecords) {
				logger.warn("Only {} out of {} employees inserted", employeesAdded, totalRecords);
				return new Response(202,"Inserted " + employeesAdded + " out of " + totalRecords + " employees.");
			}
			logger.info("All {} employees added successfully", totalRecords);
			return new Response(200,"All employees added successfully.");
		}
		catch(EmployeeManagerException e) {
			logger.error("Error occurred while adding employees in batch: {}", e.getMessage(), e);
			return new Response(500, "Error: " + e.getMessage());
		}
	}
	public Response transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) {
		logger.trace("Received request to transfer employees {} to department {}", employeeIds, newDepartment);
        try {
            boolean transferedEmployees=employeeService.transferEmployeesToDepartment(employeeIds, newDepartment);
            if(!transferedEmployees) {
            	logger.warn("Department transfer failed for employees {}", employeeIds);
            	return new Response(204,"Department update failed");
            }
            logger.info("Transferred {} employees to department {}", employeeIds.size(), newDepartment);
            return new Response(200, "All employees transferred successfully to " + newDepartment);
        } 
        catch (EmployeeManagerException e) {
        	logger.error("Error occurred while transferring employees to department {}: {}", newDepartment, e.getMessage(), e);
            return new Response(500, e.getMessage());
        }
    }
}
