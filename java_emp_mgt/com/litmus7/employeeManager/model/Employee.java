package com.litmus7.employeeManager.model;

import java.time.LocalDate;
public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private int salary;
    private LocalDate joinDate;


    public Employee(int employeeId, String firstName, String lastName, String email, 
                    String phoneNumber, String department, int salary, LocalDate joinDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.salary = salary;
        this.joinDate = joinDate;
    }
    
    public int getId() {
    	return employeeId;
    }
    
    public String getFirstName() {
    	return firstName;
    }
    
    public String getLastName() {
    	return lastName;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public String getPhoneNumber() {
    	return phoneNumber;
    }
    
    public String getDepartment() {
    	return department;
    }
    
    public int getSalary() {
    	return salary;
    }
    
    public LocalDate getJoinDate() {
    	return joinDate;
    }
}
