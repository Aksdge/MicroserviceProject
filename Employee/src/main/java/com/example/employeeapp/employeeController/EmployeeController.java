package com.example.employeeapp.employeeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeeapp.EmployeeResponse.EmployeeResponse;
import com.example.employeeapp.EmployeeResponse.EmployeeWithAddressResponse;
import com.example.employeeapp.EmployeeService.EmployeeService;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeDetails(@PathVariable int id) {
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        if (employeeResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
    }
    
    /**
     * Get employee with address by calling Address microservice
     * @param id Employee ID
     * @return Combined employee and address data
     */
    @GetMapping("/employee/{id}/with-address")
    public ResponseEntity<EmployeeWithAddressResponse> getEmployeeWithAddress(@PathVariable int id) {
        EmployeeWithAddressResponse response = employeeService.getEmployeeWithAddress(id);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // @GetMapping("/employees")
    // public List<Employee> getAllEmployees() {
    //     return employeeService.getAllEmployees();
    // }
}
