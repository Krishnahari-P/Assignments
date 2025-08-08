package com.litmus7.employeeManager.controller;

import java.io.File;
import java.util.List;

import com.litmus7.employeeManager.exceptions.EmployeeManagerException;
import com.litmus7.employeeManager.model.Employee;
import com.litmus7.employeeManager.response.Response;
import com.litmus7.employeeManager.service.EmployeeService;
import com.litmus7.employeeManager.util.CSVUtil;

public class EmployeeManagerController {
    private EmployeeService employeeService = new EmployeeService();

    public Response writeDataToDb(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.canRead() || !file.getName().toLowerCase().endsWith(".csv")) {
            return new Response(400, "Invalid CSV file!");
        }

        try {
    
            List<String[]> employeeRecords = CSVUtil.readCSV(filePath);
            int totalRecords = employeeRecords.size();

            if (totalRecords == 0) {
                return new Response(204, "CSV file is empty.");
            }

            List<Employee> employeesInserted = employeeService.processCSVAndSaveData(employeeRecords);

            int insertedCount = employeesInserted.size();
            if (insertedCount == 0) {
                return new Response(204, "No valid employees found in the input file (Total rows: " + totalRecords + ")");
            }

            String message = insertedCount + " employees inserted successfully out of " + totalRecords + " records.";
            return new Response(200, message);

        } 
        catch (EmployeeManagerException e) {
            e.printStackTrace();
            return new Response(500, "An error occurred: " + e.getMessage());
        }
    }

    public Response getAllEmployeeNames() {
        try {
            List<String> employeeNames = employeeService.getEmployeeNames();
            if (employeeNames.isEmpty()) {
                return new Response(204, "No employees found");
            }
            return new Response(200, String.join("\n", employeeNames));
        }  
        catch (EmployeeManagerException e) {
        	return new Response(500, "Error: " + e.getMessage());
		}
    }
    
    public Response getEmployeesById(List<Integer> employeeId) {
    	if(employeeId.isEmpty()) {
    		return new Response(204, "Provided list is empty");
    	}
    	try {
    		List<String> employeeDetails=employeeService.getEmployeesById(employeeId);
//    		if (employeeDetails.isEmpty()) {
//                return new Response(204, "List is empty");
//            }
    		return new Response(200,String.join("\n", employeeDetails));
    	}
    	catch(EmployeeManagerException e) {
    		return new Response(500, "Error: " + e.getMessage());
    	}
    }

	public Response deleteEmployeeById(int employeeId) {
		try {
			boolean employeeDeleted=employeeService.deleteEmployeeById(employeeId);
			if(!employeeDeleted) {
				return new Response(204,"Deletion failed");
			}
			return new Response(200, "Employee deleted for the given ID: "+employeeId);
		}
		catch(EmployeeManagerException e) {
			return new Response(500, "Error: " + e.getMessage());
		}
	}

	public Response addEmployee(Employee employee) {
		try {
			boolean employeeAdded=employeeService.addEmployee(employee);
			if(!employeeAdded) {
				return new Response(204,"Insertion failed");
			}
			return new Response(200,"Employee inserted successfully");
		}
		catch(EmployeeManagerException e) {
			return new Response(500, "Error: " + e.getMessage());
		}
	}

	public Response updateEmployee(Employee employee) {
		try {
			boolean employeeUpdated=employeeService.updateEmployee(employee);
			if(!employeeUpdated) {
				return new Response(204, "Updation failed");
			}
			return new Response(200,"Employee details updated");
		}
		catch(EmployeeManagerException e) {
			return new Response(500, "Error: " + e.getMessage());
		}
	}

	public Response addEmployeesInBatch(List<Employee> employeeList) {
		try {
			int totalRecords=employeeList.size();
			int employeesAdded=employeeService.addEmployeesInBatch(employeeList);
			if(totalRecords==0) {
				return new Response(400,"Provided list is empty");
			}
			if(employeesAdded!=totalRecords) {
				return new Response(202,"Inserted " + employeesAdded + " out of " + totalRecords + " employees.");
			}
			
			return new Response(200,"All employees added successfully.");
		}
		catch(EmployeeManagerException e) {
			return new Response(500, "Error: " + e.getMessage());
		}
	}
	public Response transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) {
        try {
            boolean transferedEmployees=employeeService.transferEmployeesToDepartment(employeeIds, newDepartment);
            if(!transferedEmployees) {
            	return new Response(204,"Department update failed");
            }
            return new Response(200, "All employees transferred successfully to " + newDepartment);
        } 
        catch (EmployeeManagerException e) {
            return new Response(500, e.getMessage());
        }
    }
}
