package com.example.qlbanhang;

public class Bill {
    String billID, billName, billCus, billStaff, billTime;
    double billTotal;

    public Bill() {
    }

    public Bill(String billID, String billName, String billCus, String billStaff, String billTime, double billTotal) {
        this.billID = billID;
        this.billName = billName;
        this.billCus = billCus;
        this.billStaff = billStaff;
        this.billTime = billTime;
        this.billTotal = billTotal;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillCus() {
        return billCus;
    }

    public void setBillCus(String billCus) {
        this.billCus = billCus;
    }

    public String getBillStaff() {
        return billStaff;
    }

    public void setBillStaff(String billStaff) {
        this.billStaff = billStaff;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public double getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(double billTotal) {
        this.billTotal = billTotal;
    }
}
