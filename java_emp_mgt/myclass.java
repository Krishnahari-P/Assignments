package my_java_project;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.*;

class myclass {
	private static final String url="jdbc:mysql://localhost:3306/java_db";
	private static final String username="root";
	private static final String password="password";
	int batchSize=3;
	public static void  main(String[] args){	
		String filePath="C:\\Users\\Krish\\Downloads\\employees.csv";
		String sql="insert into employee values(?,?,?,?,?,?,?,?)";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try(Connection connection=DriverManager.getConnection(url,username,password);
				
			PreparedStatement ps=connection.prepareStatement(sql);
			
			BufferedReader br=new BufferedReader(new FileReader(filePath));)
		{
			connection.setAutoCommit(false);
			String nextLine;
			int count=0,lineNo=1;
			br.readLine();
			while((nextLine=br.readLine())!=null) {
				lineNo++;
				String[] data=nextLine.split(",");
				if(data.length!=8) {
					System.out.println("Line "+lineNo+" skipped due to missing values. Please check csv file!");
					continue;
				}
				try {
					int emp_id=Integer.parseInt(data[0].trim());
					if(employeeExists(connection,emp_id)) {
						System.out.println("Employee with id "+emp_id+" already exists!");
						System.out.println("Skipping the line "+lineNo);
						continue;
					}
					String first_name=data[1].trim();
					String last_name=data[2].trim();
					String email=data[3].trim();
					String phone=data[4].trim();
					String department=data[5].trim();
					int salary=Integer.parseInt(data[6].trim());
					String joinDateStr=data[7].trim();			
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
