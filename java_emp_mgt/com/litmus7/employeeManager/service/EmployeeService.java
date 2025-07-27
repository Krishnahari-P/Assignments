package com.litmus7.employeeManager.service;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.litmus7.employeeManager.Validator;
import com.litmus7.employeeManager.dao.EmployeeDao;
import com.litmus7.employeeManager.model.Employee;
import com.litmus7.employeeManager.response.Response;
import com.litmus7.employeeManager.util.CSVUtil;

public class EmployeeService {
    private EmployeeDao dao = new EmployeeDao();

    public Response processCSVAndSave(String filePath) {
        List<String[]> employeeRecords = CSVUtil.readCSV(filePath);
        List<Employee> validEmployees = new ArrayList<>();

        if (employeeRecords.isEmpty()) {
            return new Response(false, "CSV file is empty or unreadable.");
        }

        try (Connection connection = dao.getConnection()) {
            connection.setAutoCommit(false);
        
            for (String[] record : employeeRecords) {
                if (!Validator.isValidRow(record)) {
                	continue;
                }

                int empId = Integer.parseInt(record[0].trim());
                if (EmployeeDao.employeeExists(connection,empId)) {
                	continue;
                }

                String firstName = record[1].trim();
                String lastName = record[2].trim();
                String email = record[3].trim();
                String phoneNumber = record[4].trim();
                String department = record[5].trim();
                String salaryStr = record[6].trim();
                String joinDateStr = record[7].trim();

                if (!Validator.validateName(firstName) || !Validator.validateName(lastName)
                    || !Validator.validateEmail(email) || !Validator.validatePhoneNumber(phoneNumber)
                    || !Validator.validateName(department)) {
                	continue;
                }

                String validSalary = Validator.validateSalary(salaryStr);
                if (validSalary == null) {
                	continue;
                }

                LocalDate joinDate = Validator.validateDate(joinDateStr);
                if (joinDate == null) {
                	continue;
                }

                Employee emp = new Employee(empId, firstName, lastName, email, phoneNumber, department, Integer.parseInt(validSalary), joinDate);
                validEmployees.add(emp);
            }

            dao.insertEmployees(connection, validEmployees);
            connection.commit();
            return new Response(true, validEmployees.size() + " employees inserted successfully.");
        } 
        catch (Exception e) {
            return new Response(false, "Error occurred: " + e.getMessage());
        }
    }
}

