package com.pbl.garagemanagementsystem.classes;

public class Spares {
    public String spare;
    public String Estimate;
    public int quantity;

    public Spares() {
        //public no-arg constructor needed
    }
    public Spares(String spare, String amount, int quantity) {
        this.spare = spare;
        this.Estimate = amount;
        this.quantity = quantity;
    }

}
