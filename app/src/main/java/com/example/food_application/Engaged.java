package com.example.food_application;

import java.util.ArrayList;

public class Engaged {
    String name,contactNo,address,id;
    ArrayList<String> foodname,foodquantity;

    public Engaged(){}

    public Engaged(String name, String contactNo,String address,String id,ArrayList<String> foodname,ArrayList<String> foodquantity) {
        this.name = name;
        this.contactNo = contactNo;
        this.address=address;
        this.id=id;
        this.foodname=foodname;
        this.foodquantity=foodquantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public ArrayList<String> getFoodname() {
        return foodname;
    }

    public ArrayList<String> getFoodquantity() {
        return foodquantity;
    }

}
