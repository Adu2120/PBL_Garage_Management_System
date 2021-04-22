package com.pbl.garagemanagementsystem.classes;

public class Spare {
    private String name;
    private int price;
    private int quantity;
    private String carRegNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public Spare(String name, int price, int quantity, String carRegNo) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.carRegNo = carRegNo;
    }

}
