package com.pbl.garagemanagementsystem.classes;

public class Spares {
    public String spare;
    public String Estimate;

    public Spares() {
        //public no-arg constructor needed
    }
    public Spares(String spare, String amount) {
        this.spare = spare;
        this.Estimate = amount;
    }

}
