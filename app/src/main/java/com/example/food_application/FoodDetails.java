package com.example.food_application;

import java.util.ArrayList;

public class FoodDetails {

    String name,contactNo,address,donorId,id;
    ArrayList<String >foodName,foodQuantity;

    FoodDetails(){}

    public FoodDetails(ArrayList<String> foodName, ArrayList<String > foodQuantity, String name, String contactNo,String address,String userId,String id) {
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
        this.name = name;
        this.contactNo = contactNo;
        this.address=address;
        donorId=userId;
        this.id=id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getFoodName() {
        return foodName;
    }

    public ArrayList<String> getFoodQuantity() {
        return foodQuantity;
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

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }
}
