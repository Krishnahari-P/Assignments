package com.litmus7.employeeManager;
import com.litmus7.employeeManager.response.Response;
import com.litmus7.employeeManager.service.EmployeeService;

public class EmployeeManagerController {
    private EmployeeService service = new EmployeeService();

    public Response writeDataToDb(String filePath) {
        return service.processCSVAndSave(filePath);
    }
}
