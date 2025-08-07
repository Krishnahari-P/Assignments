package com.litmus7.employeeManager;

import java.util.ArrayList;
import java.util.List;

import com.litmus7.employeeManager.controller.EmployeeManagerController;
import com.litmus7.employeeManager.model.Employee;
import com.litmus7.employeeManager.response.Response;

class EmployeeManagerApp {
	public static void  main(String[] args){	
		String filePath="src\\com\\litmus7\\resources\\employees.csv";
		EmployeeManagerController employeeManagerController=new EmployeeManagerController();
		List<Integer> employeeId=new ArrayList<>();
		saveDataToDb(employeeManagerController,filePath);
		getEmployeeData(employeeManagerController);
		employeeId.add(101);
		getEmployeesById(employeeManagerController,employeeId);
		deleteEmployeeById(employeeManagerController,110);
		Employee addEmployee=new Employee(105,"MS","Dhoni","ms.dhoni.@example.com","9876566698","Sports",200000,"29-04-2022");
		addEmployee(employeeManagerController,addEmployee);
		Employee updateEmployee=new Employee(105,"Sachin","Tendulkar","sachin.tendulkar.@example.com","9876566988","Sports",200000,"29-04-2022");
		updateEmployee(employeeManagerController,updateEmployee);
		List<Employee> employeeList=new ArrayList<>();
		employeeList.add(new Employee(105,"MS","Dhoni","ms.dhoni.@example.com","9876566698","Sports",200000,"29-04-2022"));
		employeeList.add(new Employee(110, "Jasprit", "Bumrah", "jasprit.bumrah@example.com", "9876534567", "Sports", 160000, "22-05-2024"));
		
		addEmployeesInBatch(employeeManagerController,employeeList);
		
		List<Integer> employeeIds=new ArrayList<>();
		employeeIds.add(105);
		employeeIds.add(108);
		employeeIds.add(109);
		employeeIds.add(200);
		
		transferEmployeesToDepartment(employeeManagerController,employeeIds,"Football");
		
	}
	
	public static void saveDataToDb(EmployeeManagerController employeeManagerController,String filePath) {
		Response response = employeeManagerController.writeDataToDb(filePath);
		if (response.getStatusCode()==200) {
            System.out.println("Success: " + response.getMessage());
        } 
		else {
            System.out.println("Failure: "+response.getStatusCode() +" "+ response.getMessage());
        }
	}
	
	public static void getEmployeeData(EmployeeManagerController employeeManagerController) {
		Response response = employeeManagerController.getAllEmployeeNames();
		if(response.getStatusCode()==200) {
			System.out.println("Name of existing employees in the database :");
			System.out.println(response.getMessage());
		}
		else {
			System.out.println(response.getMessage());
		}
	}
	
	public static void getEmployeesById(EmployeeManagerController employeeManagerController,List<Integer> employeeId) {
		Response response = employeeManagerController.getEmployeesById(employeeId);
		if(response.getStatusCode()==200) {
			System.out.println("Details of valid employees: ");
			System.out.println(response.getMessage());
		}
		else {
			System.out.println(response.getMessage());
		}
	}
	
	public static void deleteEmployeeById(EmployeeManagerController employeeManagerController,int employeeId) {
		Response response = employeeManagerController.deleteEmployeeById(employeeId);
		if(response.getStatusCode()==200) {
			System.out.println("Success: "+response.getMessage());
		}
		else {
			System.out.println("Failure: "+response.getMessage());
		}
	}
	
	public static void addEmployee(EmployeeManagerController employeeManagerController,Employee employee) {
		Response response = employeeManagerController.addEmployee(employee);
		if(response.getStatusCode()==200) {
			System.out.println("Success: "+response.getMessage());
		}
		else {
			System.out.println("Failure: "+response.getMessage());
		}
	}
	
	public static void updateEmployee(EmployeeManagerController employeeManagerController,Employee employee) {
		Response response = employeeManagerController.updateEmployee(employee);
		if(response.getStatusCode()==200) {
			System.out.println("Success: "+response.getMessage());
		}
		else {
			System.out.println("Failure: "+response.getMessage());
		}
	}
	
	public static void  addEmployeesInBatch(EmployeeManagerController employeeManagerController,List<Employee> employeeList) {
		Response response = employeeManagerController.addEmployeesInBatch(employeeList);
		if(response.getStatusCode()==200) {
			System.out.println("Success: " + response.getMessage());
		}
		else if(response.getStatusCode()==202) {
			System.out.println("Partial Success: " + response.getMessage());
		}
		else {
			System.out.println("Failure: "+response.getMessage());
		}
	}
	public static void transferEmployeesToDepartment(EmployeeManagerController employeeManagerController,List<Integer> employeeIds, String newDepartment) {
		Response response = employeeManagerController.transferEmployeesToDepartment(employeeIds,newDepartment);
		if(response.getStatusCode()==200) {
			System.out.println("Success: " + response.getMessage());
		}
		else {
			System.out.println("Failure: "+response.getMessage());
		}
	}
}
