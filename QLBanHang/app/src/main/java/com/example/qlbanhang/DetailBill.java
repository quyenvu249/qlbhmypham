package com.example.qlbanhang;

public class DetailBill {
    String id, billName, billPr;
    int account;
    double unitPrice, sale;
    double totalAmount;
    String note;

    public DetailBill() {
    }

    public DetailBill(String id,String billName, String billPr, int account, double unitPrice, double sale, double totalAmount, String note) {
        this.billName = billName;
        this.billPr = billPr;
        this.account = account;
        this.unitPrice = unitPrice;
        this.sale = sale;
        this.totalAmount = totalAmount;
        this.note = note;
        this.id = id;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillPr() {
        return billPr;
    }

    public void setBillPr(String billPr) {
        this.billPr = billPr;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
