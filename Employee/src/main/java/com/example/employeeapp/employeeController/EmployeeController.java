package com.example.employeeapp.employeeController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeeapp.EmployeeResponse.EmployeeRequest;
import com.example.employeeapp.EmployeeResponse.EmployeeResponse;
import com.example.employeeapp.EmployeeResponse.EmployeeWithAddressResponse;
import com.example.employeeapp.EmployeeService.EmployeeService;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Create - POST (with optional address)
    @PostMapping("/employee")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        try {
            // Validate request
            if (employeeRequest.getName() == null || employeeRequest.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Name is required\"}");
            }
            if (employeeRequest.getEmail() == null || employeeRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"Email is required\"}");
            }
            
            // If address is provided, create employee with address
            if (employeeRequest.getAddress() != null) {
                EmployeeWithAddressResponse response = employeeService.createEmployeeWithAddress(employeeRequest);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                // Create employee only
                EmployeeResponse employeeResponse = employeeService.createEmployee(employeeRequest);
                return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return JSON error response
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + errorMessage.replace("\"", "\\\"") + "\"}");
        }
    }

    // Read - Get by ID
    @GetMapping("/employee/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeDetails(@PathVariable int id) {
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        if (employeeResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
    }

    // Read - Get All
    @GetMapping("/employee")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        try {
            List<EmployeeResponse> employees = employeeService.getAllEmployees();
            return ResponseEntity.status(HttpStatus.OK).body(employees);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    // Update - PUT (with optional address update)
    @PutMapping("/employee/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable int id,
            @RequestBody EmployeeRequest employeeRequest) {
        try {
            // If address is provided, update employee with address
            if (employeeRequest.getAddress() != null) {
                EmployeeWithAddressResponse response = employeeService.updateEmployeeWithAddress(id, employeeRequest);
                if (response == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\": \"Employee not found\"}");
                }
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                // Update employee only
                EmployeeResponse employeeResponse = employeeService.updateEmployee(id, employeeRequest);
                if (employeeResponse == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\": \"Employee not found\"}");
                }
                return ResponseEntity.status(HttpStatus.OK).body(employeeResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + errorMessage.replace("\"", "\\\"") + "\"}");
        }
    }

    // Delete - DELETE (deletes employee and associated address)
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
        try {
            boolean deleted = employeeService.deleteEmployeeWithAddress(id);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"Employee not found\"}");
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + errorMessage.replace("\"", "\\\"") + "\"}");
        }
    }
}
