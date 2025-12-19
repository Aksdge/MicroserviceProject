package com.example.employeeapp.EmployeeService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.employeeapp.EmployeeResponse.AddressResponse;
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