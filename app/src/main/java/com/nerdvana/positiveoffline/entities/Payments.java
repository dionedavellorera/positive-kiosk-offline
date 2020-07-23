package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    private int is_sent_to_server;
    private int machine_id;
    private int branch_id;

    private String treg;

    private int is_redeemed = 0;
    private String is_redeemed_by = "";
    private String is_redeemed_at = "";

    private String is_redeemed_for = "";

    private String link_payment_id = "";
    private int is_from_other_shift = 0;

    private double change = 0.00;

    @Ignore
    public Payments() {}



    public Payments(int transaction_id, int core_id,
                    @NonNull Double amount, @NonNull String name,
                    int is_sent_to_server, int machine_id,
                    int branch_id, String treg,
                    int is_redeemed, String is_redeemed_by,
                    String is_redeemed_at, double change) {
        this.change = change;
        this.is_redeemed = is_redeemed;
        this.is_redeemed_by = is_redeemed_by;
        this.is_redeemed_at = is_redeemed_at;
        this.is_sent_to_server = is_sent_to_server;
        this.machine_id = machine_id;
        this.branch_id = branch_id;
        this.transaction_id = transaction_id;
        this.core_id = core_id;
        this.amount = amount;
        this.name = name;
        this.treg = treg;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public int getIs_from_other_shift() {
        return is_from_other_shift;
    }

    public void setIs_from_other_shift(int is_from_other_shift) {
        this.is_from_other_shift = is_from_other_shift;
    }

    public String getLink_payment_id() {
        return link_payment_id;
    }

    public void setLink_payment_id(String link_payment_id) {
        this.link_payment_id = link_payment_id;
    }

    public int getIs_redeemed() {
        return is_redeemed;
    }

    public void setIs_redeemed(int is_redeemed) {
        this.is_redeemed = is_redeemed;
    }

    public String getIs_redeemed_by() {
        return is_redeemed_by;
    }

    public void setIs_redeemed_by(String is_redeemed_by) {
        this.is_redeemed_by = is_redeemed_by;
    }

    public String getIs_redeemed_at() {
        return is_redeemed_at;
    }

    public void setIs_redeemed_at(String is_redeemed_at) {
        this.is_redeemed_at = is_redeemed_at;
    }

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

    public String getIs_redeemed_for() {
        return is_redeemed_for;
    }

    public void setIs_redeemed_for(String is_redeemed_for) {
        this.is_redeemed_for = is_redeemed_for;
    }
}
