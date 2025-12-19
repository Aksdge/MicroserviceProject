package com.Address;

public class AddressResponse {


    private int add_Id;
    private String lane_1;
    private String lane_2;
    private String state;
    private int zip;

    public int getAdd_Id() {
        return add_Id;
    }

    public String getLane_1() {
        return lane_1;
    }

    public String getLane_2() {
        return lane_2;
    }

    public String getState() {
        return state;
    }

    public int getZip() {
        return zip;
    }

    public void setAdd_Id(int add_Id) {
        this.add_Id = add_Id;
    }

    public void setLane_1(String lane_1) {
        this.lane_1 = lane_1;
    }

    public void setLane_2(String lane_2) {
        this.lane_2 = lane_2;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }



    
}
