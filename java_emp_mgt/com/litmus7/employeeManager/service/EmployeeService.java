package com.litmus7.employeeManager.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.litmus7.employeeManager.validator.Validator;
import com.litmus7.employeeManager.dao.EmployeeDao;
import com.litmus7.employeeManager.exceptions.EmployeeManagerException;
import com.litmus7.employeeManager.model.Employee;

public class EmployeeService {

    private final EmployeeDao employeeDao = new EmployeeDao();

    public List<Employee> processCSVAndSaveData(List<String[]> employeeRecords) throws EmployeeManagerException {
        List<Employee> validEmployees = new ArrayList<>();

        if (employeeRecords.isEmpty()) {
        	return validEmployees;
        }

        try (Connection connection = employeeDao.getConnection()) {
            connection.setAutoCommit(false);

            for (String[] record : employeeRecords) {
                try {
                    Employee employee = mapToEmployee(record);
                    if (employee == null || !Validator.isValidEmployee(employee)) {
                    	continue;
                    }

                    if (employeeDao.employeeExists(employee.getEmployeeId())) {
                    	
                    	continue;
                    }

                    validEmployees.add(employee);
                }
                catch (Exception e) {
                    continue;
                }
            }

            employeeDao.saveDataToDb(validEmployees);
            connection.commit();
        } 
        catch (SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db: "+e.getMessage(),e);
		} 

        return validEmployees;
    }

    private Employee mapToEmployee(String[] record) {
        if (record.length != 8) {
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
        	e.printStackTrace();
            return null;
        }
    }
    
    public List<String> getEmployeeNames() throws EmployeeManagerException {
        try {
            return employeeDao.fetchEmployeeNames();
        }
        catch( Exception e) {
        	throw new EmployeeManagerException("Fetching employee names failed: "+e.getMessage(),e);
        }
    }

    public List<String> getEmployeesById(List<Integer> employeeIdList) throws EmployeeManagerException {
        try  {
            List<Integer> validEmployeeIds = new ArrayList<>();
            for (int employeeId : employeeIdList) {
                if (employeeDao.employeeExists(employeeId)) {
                    validEmployeeIds.add(employeeId);
                }
            }

            if (validEmployeeIds.isEmpty()) {
                throw new EmployeeManagerException("No valid employee ID's found.");
            }

            return employeeDao.fetchEmployeesById(validEmployeeIds);
        }
        catch (Exception e) {
            throw new EmployeeManagerException("Fetching employee details failed: "+e.getMessage(),e);
        }
    }


	public boolean deleteEmployeeById(int employeeId) throws EmployeeManagerException {
		try{
			if (!employeeDao.employeeExists(employeeId)) {
                throw new EmployeeManagerException("Employee doesn't exists for the given ID "+employeeId);
            }
			return employeeDao.deleteEmployeeById(employeeId);
		}
		catch(Exception e) {
			throw new EmployeeManagerException("Deletion falied: "+e.getMessage(),e);
		}
	}

	public boolean addEmployee(Employee employee) throws EmployeeManagerException {
		
		try{
			if (employeeDao.employeeExists(employee.getEmployeeId())) {
				throw new EmployeeManagerException("Employee already exists with given employee Id");
            }
			if (employee == null || !Validator.isValidEmployee(employee)) {
            	throw new EmployeeManagerException("Invalid employee details");
            }
			return employeeDao.addEmployee(employee);
		}
		catch(Exception e) {
			throw new EmployeeManagerException("Employee addition failed: "+e.getMessage(),e);
		}
		
	}

	public boolean updateEmployee(Employee employee) throws EmployeeManagerException {
		try{
			if (employee == null || !Validator.isValidEmployee(employee)) {
            	throw new EmployeeManagerException("Invalid employee details");
            }
			if(!employeeDao.employeeExists(employee.getEmployeeId())) {
				throw new EmployeeManagerException("Employee doesn't exists for the given ID "+employee.getEmployeeId());
			}
			
			return employeeDao.updateEmployee(employee);
		}
		catch(Exception e) {
			throw new EmployeeManagerException("Update employee failed: "+e.getMessage(),e);
		}
	}

	public int addEmployeesInBatch(List<Employee> employeeList) throws EmployeeManagerException {
		List<Employee> validEmployees=new ArrayList<>();
		try{
			for(Employee employee:employeeList) {
				if(!Validator.isValidEmployee(employee)) {
					continue;
				}
				if(employeeDao.employeeExists(employee.getEmployeeId())) {
					continue;
				}
				validEmployees.add(employee);
			}
			return employeeDao.addEmployeesInBatch(validEmployees);
		}
		catch(Exception e) {
			throw new EmployeeManagerException("Batch addition failed: "+e.getMessage(),e);
		}
	}
	
	public boolean transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) throws EmployeeManagerException {
        if (employeeIds.isEmpty() || newDepartment == null) {
            throw new EmployeeManagerException("Employee IDs or Department name is invalid.");
        }
        try{
			return employeeDao.transferEmployeesToDepartment(employeeIds, newDepartment);
		}
		catch(Exception e) {
			throw new EmployeeManagerException("Employee transfer failed: "+e.getMessage(),e);
		}
        
    }

}
