package com.example.employeeapp.EmployeeResponse;

public class EmployeeWithAddressResponse {
    
    private EmployeeResponse employee;
    private AddressResponse address;
    
    // Getters and Setters
    public EmployeeResponse getEmployee() {
        return employee;
    }
    
    public void setEmployee(EmployeeResponse employee) {
        this.employee = employee;
    }
    
    public AddressResponse getAddress() {
        return address;
    }
    
    public void setAddress(AddressResponse address) {
        this.address = address;
    }
}

