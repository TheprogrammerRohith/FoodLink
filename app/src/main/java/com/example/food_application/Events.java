package com.example.food_application;

public class Events {

    String name,location,date,time,contact_no,user_id;

    public Events(){}

    public Events(String name, String location, String date, String time, String contact_no,String user_id) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.contact_no = contact_no;
        this.user_id=user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
