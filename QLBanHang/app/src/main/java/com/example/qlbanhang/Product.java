package com.example.qlbanhang;

public class Product {
    String proLink, proID, proName, proBrand, NSX, HSD, proDes;
    double imPrice, exPrice;

    public Product() {
    }

    public Product(String proLink, String proID, String proName, String proBrand, String NSX, String HSD, String proDes, double imPrice, double exPrice) {
        this.proLink = proLink;
        this.proID = proID;
        this.proName = proName;
        this.proBrand = proBrand;
        this.NSX = NSX;
        this.HSD = HSD;
        this.proDes = proDes;
        this.imPrice = imPrice;
        this.exPrice = exPrice;
    }

    public String getProLink() {
        return proLink;
    }

    public void setProLink(String proLink) {
        this.proLink = proLink;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProBrand() {
        return proBrand;
    }

    public void setProBrand(String proBrand) {
        this.proBrand = proBrand;
    }

    public String getNSX() {
        return NSX;
    }

    public void setNSX(String NSX) {
        this.NSX = NSX;
    }

    public String getHSD() {
        return HSD;
    }

    public void setHSD(String HSD) {
        this.HSD = HSD;
    }

    public String getProDes() {
        return proDes;
    }

    public void setProDes(String proDes) {
        this.proDes = proDes;
    }

    public double getImPrice() {
        return imPrice;
    }

    public void setImPrice(double imPrice) {
        this.imPrice = imPrice;
    }

    public double getExPrice() {
        return exPrice;
    }

    public void setExPrice(double exPrice) {
        this.exPrice = exPrice;
    }
}
