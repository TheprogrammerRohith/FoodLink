package com.example.food_application;

public class DonorPL {
    String name,contactNo,address,date;

    public DonorPL(){}

    public DonorPL(String name, String contactNo, String address, String date) {
        this.name = name;
        this.contactNo = contactNo;
        this.address = address;
        this.date = date;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
