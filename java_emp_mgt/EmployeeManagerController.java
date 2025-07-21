import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class EmployeeManagerController {
	private static final String url="jdbc:mysql://localhost:3306/java_db";
	private static final String username="root";
	private static final String password="password";
	
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url,username,password);
	}
	
	public List<String[]> readCsv(String filePath){
		List<String[]> records=new ArrayList<>();
		
		try(BufferedReader br=new BufferedReader(new FileReader(filePath))){
			br.readLine();
			String nextLine;
			while((nextLine=br.readLine())!=null) {
				records.add(nextLine.split(","));
			}
		}
		catch(IOException ioe) {
			System.out.println("Error reading csv file "+ioe);
		}
		return records;
	}
	public void writeToDb(List<String[]> data) {
		String sql="insert into employee values(?,?,?,?,?,?,?,?)";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		try(Connection connection=getConnection();
			PreparedStatement ps=connection.prepareStatement(sql))
		{
			connection.setAutoCommit(false);
			int count=0,lineNo=1;
			for(String[] str:data) {
				lineNo++;
				if(str.length!=8) {
					System.out.println("Line "+lineNo+" skipped due to missing values. Please check csv file!");
					continue;
				}
				try {
					int emp_id=Integer.parseInt(str[0].trim());
					if(employeeExists(connection,emp_id)) {
						System.out.println("Employee with id "+emp_id+" already exists!");
						System.out.println("Skipping the line "+lineNo);
						continue;
					}
					String first_name=str[1].trim();
					String last_name=str[2].trim();
					String email=str[3].trim();
					String phone=str[4].trim();
					String department=str[5].trim();
					int salary=Integer.parseInt(str[6].trim());
					String joinDateStr=str[7].trim();			
					LocalDate ld;
					try {
						ld=LocalDate.parse(joinDateStr,formatter);
					}
					catch(DateTimeParseException dte) {
						System.out.println("Line "+lineNo+" skipped due to invalid date. Enter the date in dd-mm-yyyy format in csv "+dte);
						continue;
					}
					Date join_date=Date.valueOf(ld);
					ps.setInt(1, emp_id);
					ps.setString(2, first_name);
					ps.setString(3, last_name);
					ps.setString(4, email);
					ps.setString(5, phone);
					ps.setString(6, department);
					ps.setInt(7, salary);
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
		List<String[]> data=readCsv(filePath);
		writeToDb(data);
	}
	public static boolean employeeExists(Connection con,int empId) throws SQLException {
		String query="select employee_id from employee where employee_id=?";
		try(PreparedStatement p=con.prepareStatement(query)){
			p.setInt(1, empId);
			try(ResultSet rs=p.executeQuery()){
				return rs.next();
			}
		}
		
	}
}
