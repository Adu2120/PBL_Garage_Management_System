package com.pbl.garagemanagementsystem.classes;

import java.util.ArrayList;

public class JobCard {
    String carRegNo;
    String date;
    ArrayList<String> complaints;
    ArrayList<String> spares;
    int totalEstimate;

    public JobCard() {
        //public no-arg constructor needed
    }

    public JobCard(String carRegNo, String date, ArrayList<String> complaints, ArrayList<String> spares, int totalEstimate) {
        this.carRegNo = carRegNo;
        this.date = date;
        this.complaints = complaints;
        this.spares = spares;
        this.totalEstimate = totalEstimate;
    }

    public String getCarRegNo() {
        return carRegNo;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getComplaints() {
        return complaints;
    }

    public ArrayList<String> getSpares() {
        return spares;
    }

    public int getTotalEstimate() {
        return totalEstimate;
    }
}
