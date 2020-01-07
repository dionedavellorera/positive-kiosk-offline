package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "OrDetails")
public class OrDetails {


    @NonNull
    private int transaction_id;
    @NonNull
    private String name;
    @NonNull
    private String address;
    @PrimaryKey
    @NonNull
    private String tin_number;
    @NonNull
    private String business_style;

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getTin_number() {
        return tin_number;
    }

    public void setTin_number(@NonNull String tin_number) {
        this.tin_number = tin_number;
    }

    @NonNull
    public String getBusiness_style() {
        return business_style;
    }

    public void setBusiness_style(@NonNull String business_style) {
        this.business_style = business_style;
    }
}
