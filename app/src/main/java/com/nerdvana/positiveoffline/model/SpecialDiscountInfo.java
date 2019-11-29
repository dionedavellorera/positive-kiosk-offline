package com.nerdvana.positiveoffline.model;

public class SpecialDiscountInfo {
    private String card_number;
    private String name;
    private String address;

    public SpecialDiscountInfo(String card_number, String name, String address) {
        this.card_number = card_number;
        this.name = name;
        this.address = address;
    }

    public String getCard_number() {
        return card_number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
