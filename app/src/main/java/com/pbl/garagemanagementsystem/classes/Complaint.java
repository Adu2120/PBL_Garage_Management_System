package com.pbl.garagemanagementsystem.classes;
public class Complaint {
    private final String cComplaint;
    private final String carRegNo;

    public Complaint(String cComplaint, String carRegNo) {
        this.cComplaint = cComplaint;
        this.carRegNo = carRegNo;
    }

    public String getComplaint() {
        return cComplaint;
    }
    public String getCarRegNo() {
        return carRegNo;
    }
}