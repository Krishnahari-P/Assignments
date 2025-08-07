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

                    if (employeeDao.employeeExists(connection, employee.getEmployeeId())) {
                    	
                    	continue;
                    }

                    validEmployees.add(employee);
                }
                catch (Exception e) {
                    continue;
                }
            }

            employeeDao.saveDataToDb(connection, validEmployees);
            connection.commit();
        } 
        catch (SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
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
//            LocalDate joinDate = Validator.validateDate(record[7].trim());
            String joinDate=record[7].trim();

//            if (joinDate == null) {
//            	return null;
//            }

            return new Employee(employeeId, firstName, lastName, email, phoneNo, department, salary, joinDate);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public List<String> getEmployeeNames() throws EmployeeManagerException {
        try (Connection connection = employeeDao.getConnection()) {
            return employeeDao.fetchEmployeeNames(connection);
        }
        catch( SQLException e) {
        	throw new EmployeeManagerException("Couldn't connect to db");
        }
    }

    public List<String> getEmployeesById(List<Integer> employeeIdList) throws EmployeeManagerException {
        try (Connection connection = employeeDao.getConnection()) {
            List<Integer> validEmployeeIds = new ArrayList<>();
            for (int employeeId : employeeIdList) {
                if (employeeDao.employeeExists(connection, employeeId)) {
                    validEmployeeIds.add(employeeId);
                }
            }

            if (validEmployeeIds.isEmpty()) {
                throw new EmployeeManagerException("No valid employee ID's found.");
            }

            return employeeDao.fetchEmployeesById(connection, validEmployeeIds);
        }
        catch (SQLException e) {
            throw new EmployeeManagerException("Couldn't connect to db");
        }
    }


	public boolean deleteEmployeeById(int employeeId) throws EmployeeManagerException {
		try(Connection connection = employeeDao.getConnection()){
			if (!employeeDao.employeeExists(connection, employeeId)) {
                throw new EmployeeManagerException("Employee doesn't exists for the given ID "+employeeId);
            }
			return employeeDao.deleteEmployeeById(connection,employeeId);
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
	}

	public boolean addEmployee(Employee employee) throws EmployeeManagerException {
		
		try(Connection connection = employeeDao.getConnection()){
			if (employeeDao.employeeExists(connection, employee.getEmployeeId())) {
				throw new EmployeeManagerException("Employee already exists with given employee Id");
            }
			if (employee == null || !Validator.isValidEmployee(employee)) {
            	throw new EmployeeManagerException("Invalid employee details");
            }
			return employeeDao.addEmployee(connection,employee);
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
		
	}

	public boolean updateEmployee(Employee employee) throws EmployeeManagerException {
		try(Connection connection=employeeDao.getConnection()){
			if (employee == null || !Validator.isValidEmployee(employee)) {
            	throw new EmployeeManagerException("Invalid employee details");
            }
			if(!employeeDao.employeeExists(connection, employee.getEmployeeId())) {
				throw new EmployeeManagerException("Employee doesn't exists for the given ID "+employee.getEmployeeId());
			}
			
			return employeeDao.updateEmployee(connection,employee);
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
	}

	public int addEmployeesInBatch(List<Employee> employeeList) throws EmployeeManagerException {
		List<Employee> validEmployees=new ArrayList<>();
		try(Connection connection=employeeDao.getConnection()){
			for(Employee employee:employeeList) {
				if(!Validator.isValidEmployee(employee)) {
					continue;
				}
				if(employeeDao.employeeExists(connection, employee.getEmployeeId())) {
					continue;
				}
				validEmployees.add(employee);
			}
			return employeeDao.addEmployeesInBatch(connection,validEmployees);
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
	}
	
	public boolean transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) throws EmployeeManagerException {
        if (employeeIds.isEmpty() || newDepartment == null) {
            throw new EmployeeManagerException("Employee IDs or Department name is invalid.");
        }
        try(Connection connection=employeeDao.getConnection()){
			return employeeDao.transferEmployeesToDepartment(connection,employeeIds, newDepartment);
		}
		catch(SQLException e) {
			throw new EmployeeManagerException("Couldn't connect to db");
		}
        
    }

}
