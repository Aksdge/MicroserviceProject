package com.example.employeeapp.EmployeeResponse;



public class EmployeeResponse {


    private int id;
    private String name;
    private String email;
    private String bloodgroup;

    
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

}
