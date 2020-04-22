package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    private int is_sent_to_server;
    private int machine_id;
    private int branch_id;

    private String treg;

    public String getTreg() {
        return treg;
    }

    public void setTreg(String treg) {
        this.treg = treg;
    }

    public int getIs_sent_to_server() {
        return is_sent_to_server;
    }

    public void setIs_sent_to_server(int is_sent_to_server) {
        this.is_sent_to_server = is_sent_to_server;
    }

    public int getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(int machine_id) {
        this.machine_id = machine_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

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

    public OrDetails() {}
}
