package com.example.qlbanhang;

import java.util.HashMap;
import java.util.Map;

public class Brand {
    String brID, brName, brLink;

    public Brand() {
    }

    public Brand(String brID, String brName, String brLink) {
        this.brID = brID;
        this.brName = brName;
        this.brLink = brLink;
    }

    public String getBrID() {
        return brID;
    }

    public void setBrID(String brID) {
        this.brID = brID;
    }

    public String getBrName() {
        return brName;
    }

    public void setBrName(String brName) {
        this.brName = brName;
    }

    public String getBrLink() {
        return brLink;
    }

    public void setBrLink(String brLink) {
        this.brLink = brLink;
    }
}
