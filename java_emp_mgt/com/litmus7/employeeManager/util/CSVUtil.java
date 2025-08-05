package com.litmus7.employeeManager.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.litmus7.employeeManager.exceptions.EmployeeManagerException;

public class CSVUtil {
    public static List<String[]> readCSV(String filePath) throws EmployeeManagerException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); 
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                records.add(nextLine.split(","));
            }
        } 
        catch (IOException e) {
        	throw new EmployeeManagerException("File not found");
		} 
        return records;
    }
}

