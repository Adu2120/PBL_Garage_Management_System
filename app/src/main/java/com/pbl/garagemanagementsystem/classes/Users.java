package com.pbl.garagemanagementsystem.classes;

public class Users {
    private String customerName, mobileNo, email, carRegNo;

    public Users() {
        //public no-arg constructor needed
    }

    public void setCarRegNo(String carRegNo) {
        this.carRegNo = carRegNo;
    }

    public Users(String customerName, String mobileNo, String email, String carRegNo) {
        this.customerName = customerName;
        this.carRegNo = carRegNo;
        this.mobileNo = mobileNo;
        this.email = email;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getEmail() {
        return email;
    }
}
