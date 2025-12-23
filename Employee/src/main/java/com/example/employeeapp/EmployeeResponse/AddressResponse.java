package com.example.employeeapp.EmployeeResponse;

public class AddressResponse {
    
    private int addressId;
    private String lane_1;
    private String lane_2;
    private String state;
    private int zip;
    
    // Getters and Setters
    public int getAddressId() {
        return addressId;
    }
    
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    
    public String getLane_1() {
        return lane_1;
    }
    
    public void setLane_1(String lane_1) {
        this.lane_1 = lane_1;
    }
    
    public String getLane_2() {
        return lane_2;
    }
    
    public void setLane_2(String lane_2) {
        this.lane_2 = lane_2;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public int getZip() {
        return zip;
    }
    
    public void setZip(int zip) {
        this.zip = zip;
    }
}

