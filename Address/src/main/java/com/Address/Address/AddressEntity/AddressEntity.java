package com.Address.Address.AddressEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="address")
public class AddressEntity {


    @Id
    @GeneratedValue(strategy=jakarta.persistence.GenerationType.IDENTITY)
    @Column(name="address_id")
    private int addressId;
    @Column(name="lane_1")
    private String lane_1;
    @Column(name="lane_2")
    private String lane_2;
    @Column(name="state")
    private String state;
    @Column(name="zip")
    private int zip;
    
    public int getAddressId() {
        return addressId;
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

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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
