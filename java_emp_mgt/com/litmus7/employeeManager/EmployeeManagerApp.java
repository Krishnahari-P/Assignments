package com.litmus7.employeeManager;
class EmployeeManagerApp {
	public static void  main(String[] args){	
		String filePath="C:\\Users\\Krish\\Downloads\\employees.csv";
		EmployeeManagerController controller=new EmployeeManagerController();
		Response response=controller.writeDataToDb(filePath);
		if (response.isSuccess()) {
            System.out.println("Success: " + response.getMessage());
        } 
		else {
            System.out.println("Failure: " + response.getMessage());
        }
	}
}
