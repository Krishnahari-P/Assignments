package com.litmus7.employeeManager.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.litmus7.employeeManager.validator.Validator;
import com.litmus7.employeeManager.dao.EmployeeDao;
import com.litmus7.employeeManager.model.Employee;
import com.litmus7.employeeManager.util.CSVUtil;

public class EmployeeService {

    private final EmployeeDao employeeDao = new EmployeeDao();

    public List<Employee> processCSVAndSaveData(String filePath) throws SQLException {
        List<String[]> records = CSVUtil.readCSV(filePath);
        List<Employee> validEmployees = new ArrayList<>();

        if (records.isEmpty()) {
        	return validEmployees;
        }

        try (Connection connection = employeeDao.getConnection()) {
            connection.setAutoCommit(false);

            for (String[] record : records) {
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
            LocalDate joinDate = Validator.validateDate(record[7].trim());

            if (joinDate == null) {
            	return null;
            }

            return new Employee(employeeId, firstName, lastName, email, phoneNo, department, salary, joinDate);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public List<String> getEmployeeNames() throws SQLException {
        try (Connection connection = employeeDao.getConnection()) {
            return employeeDao.fetchEmployeeNames(connection);
        }
    }

}
