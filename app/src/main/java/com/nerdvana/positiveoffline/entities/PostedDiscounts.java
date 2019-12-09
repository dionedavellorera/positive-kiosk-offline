package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PostedDiscounts")
public class PostedDiscounts {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int transaction_id;
    @NonNull
    private int discount_id;
    @NonNull
    private String discount_name;
    private Boolean is_void = false;

    private String card_number;
    private String name;
    private String address;

    private int cut_off_id = 0;
    private int end_of_day_id = 0;

    private Double amount=  0.00;
    public PostedDiscounts(int transaction_id, int discount_id,
                           String discount_name, Boolean is_void,
                           String card_number, String name,
                           String address) {
        this.transaction_id = transaction_id;
        this.discount_id = discount_id;
        this.discount_name = discount_name;
        this.is_void = is_void;
        this.card_number = card_number;
        this.name = name;
        this.address = address;
    }

    public int getEnd_of_day_id() {
        return end_of_day_id;
    }

    public void setEnd_of_day_id(int end_of_day_id) {
        this.end_of_day_id = end_of_day_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getCut_off_id() {
        return cut_off_id;
    }

    public void setCut_off_id(int cut_off_id) {
        this.cut_off_id = cut_off_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public void setIs_void(Boolean is_void) {
        this.is_void = is_void;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public String getDiscount_name() {
        return discount_name;
    }

    public void setDiscount_name(String discount_name) {
        this.discount_name = discount_name;
    }
}
