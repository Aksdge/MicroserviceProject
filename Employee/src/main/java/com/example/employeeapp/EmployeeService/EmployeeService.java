package com.example.employeeapp.EmployeeService;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.employeeapp.EmployeeResponse.AddressRequest;
import com.example.employeeapp.EmployeeResponse.AddressResponse;
import com.example.employeeapp.EmployeeResponse.EmployeeRequest;
import com.example.employeeapp.EmployeeResponse.EmployeeResponse;
import com.example.employeeapp.EmployeeResponse.EmployeeWithAddressResponse;
import com.example.employeeapp.employeeEntity.Employee;
import com.example.employeeapp.employeeRepo.EmployeeRepo;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate;
    

    private static final String ADDRESS_SERVICE_URL = "http://localhost:8089/address/";
    private static final String ADDRESS_SERVICE_BASE_URL = "http://localhost:8089/address";

    // Create Employee with Address
    public EmployeeWithAddressResponse createEmployeeWithAddress(EmployeeRequest employeeRequest) {
        try {
            // Step 1: Create Employee
            Employee employee = modelMapper.map(employeeRequest, Employee.class);
            Employee savedEmployee = employeeRepo.save(employee);
            EmployeeResponse employeeResponse = modelMapper.map(savedEmployee, EmployeeResponse.class);
            
            // Step 2: Create Address if provided
            AddressResponse addressResponse = null;
            if (employeeRequest.getAddress() != null) {
                try {
                    // Call Address service to create address
                    AddressRequest addressRequest = employeeRequest.getAddress();
                    ResponseEntity<AddressResponse> addressResponseEntity = restTemplate.postForEntity(
                        ADDRESS_SERVICE_BASE_URL,
                        addressRequest,
                        AddressResponse.class
                    );
                    
                    if (addressResponseEntity.getStatusCode() == HttpStatus.CREATED && 
                        addressResponseEntity.getBody() != null) {
                        addressResponse = addressResponseEntity.getBody();
                    }
                } catch (Exception e) {
                    System.err.println("Error creating address: " + e.getMessage());
                    // Continue even if address creation fails - employee is already created
                }
            }
            
            // Step 3: Return combined response
            EmployeeWithAddressResponse response = new EmployeeWithAddressResponse();
            response.setEmployee(employeeResponse);
            response.setAddress(addressResponse);
            
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create employee: " + e.getMessage(), e);
        }
    }

    // Create Employee only (without address)
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        try {
            Employee employee = modelMapper.map(employeeRequest, Employee.class);
            Employee savedEmployee = employeeRepo.save(employee);
            return modelMapper.map(savedEmployee, EmployeeResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create employee: " + e.getMessage(), e);
        }
    }

    /**
     * Get employee only (without address)
     * @param id Employee ID
     * @return EmployeeResponse
     */
    public EmployeeResponse getEmployeeById(int id) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        return modelMapper.map(employee, EmployeeResponse.class);
    }

    // Read - Get All
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeResponse.class))
                .collect(Collectors.toList());
    }

    // Update Employee (with optional address update)
    public EmployeeWithAddressResponse updateEmployeeWithAddress(int id, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        
        // Update employee fields
        employee.setName(employeeRequest.getName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setBloodgroup(employeeRequest.getBloodgroup());
        Employee updatedEmployee = employeeRepo.save(employee);
        EmployeeResponse employeeResponse = modelMapper.map(updatedEmployee, EmployeeResponse.class);
        
        // Update address if provided
        AddressResponse addressResponse = null;
        if (employeeRequest.getAddress() != null) {
            try {
                // Try to get existing address first
                try {
                    ResponseEntity<AddressResponse> existingAddress = restTemplate.getForEntity(
                        ADDRESS_SERVICE_URL + id,
                        AddressResponse.class
                    );
                    
                    if (existingAddress.getStatusCode() == HttpStatus.OK && 
                        existingAddress.getBody() != null) {
                        // Address exists - update it
                        restTemplate.put(
                            ADDRESS_SERVICE_URL + id,
                            employeeRequest.getAddress()
                        );
                        // Get updated address
                        ResponseEntity<AddressResponse> updatedAddress = restTemplate.getForEntity(
                            ADDRESS_SERVICE_URL + id,
                            AddressResponse.class
                        );
                        if (updatedAddress.getBody() != null) {
                            addressResponse = updatedAddress.getBody();
                        }
                    }
                } catch (Exception e) {
                    // Address doesn't exist - create new one
                    ResponseEntity<AddressResponse> newAddress = restTemplate.postForEntity(
                        ADDRESS_SERVICE_BASE_URL,
                        employeeRequest.getAddress(),
                        AddressResponse.class
                    );
                    if (newAddress.getStatusCode() == HttpStatus.CREATED && 
                        newAddress.getBody() != null) {
                        addressResponse = newAddress.getBody();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error updating address: " + e.getMessage());
                // Continue even if address update fails
            }
        }
        
        EmployeeWithAddressResponse response = new EmployeeWithAddressResponse();
        response.setEmployee(employeeResponse);
        response.setAddress(addressResponse);
        
        return response;
    }

    // Update Employee only (without address)
    public EmployeeResponse updateEmployee(int id, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        
        employee.setName(employeeRequest.getName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setBloodgroup(employeeRequest.getBloodgroup());
        
        Employee updatedEmployee = employeeRepo.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeResponse.class);
    }

    // Delete Employee (with address deletion)
    public boolean deleteEmployeeWithAddress(int id) {
        if (employeeRepo.existsById(id)) {
            // Delete address first (if exists)
            try {
                restTemplate.delete(ADDRESS_SERVICE_URL + id);
            } catch (Exception e) {
                System.err.println("Error deleting address: " + e.getMessage());
                // Continue to delete employee even if address deletion fails
            }
            
            // Delete employee
            employeeRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // Delete Employee only (without address)
    public boolean deleteEmployee(int id) {
        if (employeeRepo.existsById(id)) {
            employeeRepo.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Get employee with address by calling Address microservice
     * @param id Employee ID
     * @return Combined employee and address data
     */
    public EmployeeWithAddressResponse getEmployeeWithAddress(int id) {
        // Step 1: Get employee from own database
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            return null;
        }
        
        EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
        
        // Step 2: Call Address microservice
        try {
            String addressUrl = ADDRESS_SERVICE_URL + id;
            ResponseEntity<AddressResponse> addressResponse = restTemplate.getForEntity(
                addressUrl, 
                AddressResponse.class
            );
            
            // Step 3: Combine data
            EmployeeWithAddressResponse response = new EmployeeWithAddressResponse();
            response.setEmployee(employeeResponse);
            
            if (addressResponse.getStatusCode() == HttpStatus.OK && addressResponse.getBody() != null) {
                response.setAddress(addressResponse.getBody());
            }
            
            return response;
        } catch (Exception e) {
            // Handle error - Address service might be down
            System.err.println("Error calling Address service: " + e.getMessage());
            EmployeeWithAddressResponse response = new EmployeeWithAddressResponse();
            response.setEmployee(employeeResponse);
            response.setAddress(null); // Address not available
            return response;
        }
    }

}