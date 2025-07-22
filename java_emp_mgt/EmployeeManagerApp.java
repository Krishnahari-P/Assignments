package com.litmus7.employeeManager;
class EmployeeManagerApp {
	public static void  main(String[] args){	
		String filePath="C:\\Users\\Krish\\Downloads\\employees.csv";
		EmployeeManagerController obj=new EmployeeManagerController();
		obj.writeDataToDb(filePath);
	}
}
