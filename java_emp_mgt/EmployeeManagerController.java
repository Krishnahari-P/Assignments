package com.litmus7.employeeManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManagerController {
	private static final String URL="jdbc:mysql://localhost:3306/java_db";
	private static final String USERNAME="root";
	private static final String PASSWORD="krish@123";
	
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL,USERNAME,PASSWORD);
	}
	
	public List<String[]> readCSV(String filePath){
		List<String[]> records=new ArrayList<>();
		File file = new File(filePath);
		if (!file.exists() || !file.canRead()) {
	        System.out.println("CSV file not found or is not readable.");
	        return records;
	    }
		try(BufferedReader br=new BufferedReader(new FileReader(filePath))){
			br.readLine();
			String nextLine;
			while((nextLine=br.readLine())!=null) {
				records.add(nextLine.split(","));
			}
		}
		catch(IOException ioe) {
			System.out.println("Error reading CSV file "+ioe);
		}
		return records;
	}
	public void writeToDb(List<String[]> data) {
		String sql="insert into employee values(?,?,?,?,?,?,?,?)";
		
		try(Connection connection=getConnection();
			PreparedStatement ps=connection.prepareStatement(sql))
		{
			connection.setAutoCommit(false);
			int count=0,lineNo=1;
			for(String[] str:data) {
				lineNo++;
				if(!Validator.isValidRow(str)) {
					System.out.println("Line "+lineNo+" skipped due to missing values. Please check csv file!");
					continue;
				}
				try {
					int empId=Integer.parseInt(str[0].trim());
					if(Validator.employeeExists(connection,empId)) {
						System.out.println("Employee with id "+empId+" already exists! Line "+lineNo+" skipped");
						continue;
					}
					String firstName=str[1].trim();
					if(firstName.isEmpty()) {
						System.out.println("First name field can't be empty. Line "+lineNo+" skipped");
						continue;
					}
					if(!Validator.validateName(firstName)) {
						System.out.println("First name must contain only letters. Line " + lineNo + " skipped");
					    continue;
					}
					String lastName=str[2].trim();
					if(lastName.isEmpty()) {
						System.out.println("Last name field can't be empty. Line "+lineNo+" skipped");
						continue;
					}
					if(!Validator.validateName(lastName)) {
						System.out.println("Last name must contain only letters. Line " + lineNo + " skipped");
					    continue;
					}
					String email=str[3].trim();
					if(!Validator.validateEmail(email)) {
						System.out.println("Invalid email: Line " + lineNo + " skipped");
					    continue;
					}
					String phoneNo=str[4].trim();
					if(!Validator.validatePhoneNumber(phoneNo)) {
						System.out.println("Invalid phone number: Line " + lineNo + " skipped");
					    continue;
					}
					String department=str[5].trim();
					if(!Validator.validateName(department)) {
						System.out.println("Department name must contain only letters. Line " + lineNo + " skipped");
					    continue;
					}
				    String salaryStr=str[6].trim();
				    String validSalary=Validator.validateSalary(salaryStr);
				    if(validSalary==null) {
						System.out.println("Salary must contain only digits.Line "+lineNo+" skipped");
						continue;
					}
					String joinDateStr=str[7].trim();			
					LocalDate ld=Validator.validateDate(joinDateStr);
					if(ld==null) {
						System.out.println("Invalid format of date. Enter the date in dd-MM-yyyy. Line "+lineNo+" skipped");
						continue;
					}
					Date join_date=Date.valueOf(ld);
					ps.setInt(1, empId);
					ps.setString(2, firstName);
					ps.setString(3, lastName);
					ps.setString(4, email);
					ps.setString(5, phoneNo);
					ps.setString(6, department);
					ps.setInt(7, Integer.parseInt(validSalary));
					ps.setDate(8,join_date);
					ps.addBatch();
					System.out.println("Line "+lineNo+" proccessed correctly");
					if(++count%3==0) {
						ps.executeBatch();
					}
					
				}
				catch(NumberFormatException nfe) {
					System.out.println("Incorrect number format: "+nfe);
				}
				catch(SQLException sqe) {
					System.out.println("SQL error: "+sqe);
				}
				
			}
			ps.executeBatch();
			connection.commit();
			System.out.println("All valid data inserted successfully");
			
		}
		catch(Exception e) {
			System.out.println("Something went wrong!"+e);
			e.printStackTrace();
		}
	}
	public void writeDataToDb(String filePath) {
		List<String[]> data=readCSV(filePath);
		writeToDb(data);
	}

}
