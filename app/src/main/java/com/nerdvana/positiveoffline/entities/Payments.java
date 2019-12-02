package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Payments")
public class Payments {

    @NonNull
    private int transaction_id;
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int core_id;
    @NonNull
    private Double amount;
    @NonNull
    private String name;
    private Boolean is_void = false;

    private String other_data;

    private int cut_off_id = 0;

    public Payments(int transaction_id, int core_id,
                    @NonNull Double amount, @NonNull String name) {
        this.transaction_id = transaction_id;
        this.core_id = core_id;
        this.amount = amount;
        this.name = name;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    @NonNull
    public Double getAmount() {
        return amount;
    }

    public void setAmount(@NonNull Double amount) {
        this.amount = amount;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public void setIs_void(Boolean is_void) {
        this.is_void = is_void;
    }

    public String getOther_data() {
        return other_data;
    }

    public void setOther_data(String other_data) {
        this.other_data = other_data;
    }


    public int getCut_off_id() {
        return cut_off_id;
    }

    public void setCut_off_id(int cut_off_id) {
        this.cut_off_id = cut_off_id;
    }
}
