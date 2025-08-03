package com.litmus7.employeeManager;
import com.litmus7.employeeManager.controller.EmployeeManagerController;
import com.litmus7.employeeManager.response.Response;

class EmployeeManagerApp {
	public static void  main(String[] args){	
		String filePath="Assignments\\resources\\employees.csv";
		EmployeeManagerController employeeManagerController=new EmployeeManagerController();
		saveDataToDb(employeeManagerController,filePath);
		getEmployeeData(employeeManagerController);
	}
	
	public static void saveDataToDb(EmployeeManagerController employeeManagerController,String filePath) {
		Response response=employeeManagerController.writeDataToDb(filePath);
		if (response.getStatusCode()==200) {
            System.out.println("Success: " + response.getMessage());
        } 
		else {
            System.out.println("Failure: "+response.getStatusCode() +" "+ response.getMessage());
        }
	}
	
	public static void getEmployeeData(EmployeeManagerController employeeManagerController) {
		Response response= employeeManagerController.getAllEmployeeNames();
		if(response.getStatusCode()==200) {
			System.out.println("Name of existing employees in the database :");
			System.out.println(response.getMessage());
		}
		else {
			System.out.println(response.getMessage());
		}
	}
}
