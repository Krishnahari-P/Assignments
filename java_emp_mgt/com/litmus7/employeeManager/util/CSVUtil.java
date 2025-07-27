package com.litmus7.employeeManager.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    public static List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists() || !file.canRead() || !file.getName().toLowerCase().endsWith(".csv")){
        	return records;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); 
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                records.add(nextLine.split(","));
            }
        } 
        catch (IOException ioe) {
        	System.out.println("Error reading CSV file "+ioe);
            ioe.printStackTrace();
        }
        return records;
    }
}

 {
    
}
