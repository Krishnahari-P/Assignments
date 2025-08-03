package com.litmus7.employeeManager.controller;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import com.litmus7.employeeManager.model.Employee;
import com.litmus7.employeeManager.response.Response;
import com.litmus7.employeeManager.service.EmployeeService;
import com.litmus7.employeeManager.util.CSVUtil;

public class EmployeeManagerController {
    private EmployeeService employeeService = new EmployeeService();

    public Response writeDataToDb(String filePath) {
    	File file = new File(filePath);
    	if (!file.exists() || !file.canRead() || !file.getName().toLowerCase().endsWith(".csv")){
        	return new Response(400,"Invalid CSV file!");
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
        catch (Exception e) {
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
        catch (SQLException e) {
            e.printStackTrace();
            return new Response(500, "Database error: " + e.getMessage());
        }
    }

}

