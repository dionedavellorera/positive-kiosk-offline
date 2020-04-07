package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Payout")
public class Payout {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String series_number;

    private String username;
    private Double amount;
    private String reason;
    private String manager_username;
    private String treg;
    private int is_sent_to_server;
    private int machine_id;
    private int branch_id;

    private Boolean is_cut_off = false;
    private String is_cut_off_by = "";
    private String is_cut_off_at = "";
    private int cut_off_id = 0;

    public Payout(@NonNull String series_number, String username,
                  Double amount, String reason, String manager_username,
                  String treg,int is_sent_to_server,
                  int machine_id, int branch_id) {
        this.series_number = series_number;
        this.username = username;
        this.amount = amount;
        this.reason = reason;
        this.manager_username = manager_username;
        this.treg = treg;
        this.is_sent_to_server = is_sent_to_server;
        this.machine_id = machine_id;
        this.branch_id = branch_id;
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

    @NonNull
    public String getSeries_number() {
        return series_number;
    }

    public String getUsername() {
        return username;
    }

    public Double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public String getManager_username() {
        return manager_username;
    }

    public String getTreg() {
        return treg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSeries_number(@NonNull String series_number) {
        this.series_number = series_number;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setManager_username(String manager_username) {
        this.manager_username = manager_username;
    }

    public void setTreg(String treg) {
        this.treg = treg;
    }

    public Boolean getIs_cut_off() {
        return is_cut_off;
    }

    public void setIs_cut_off(Boolean is_cut_off) {
        this.is_cut_off = is_cut_off;
    }

    public String getIs_cut_off_by() {
        return is_cut_off_by;
    }

    public void setIs_cut_off_by(String is_cut_off_by) {
        this.is_cut_off_by = is_cut_off_by;
    }

    public String getIs_cut_off_at() {
        return is_cut_off_at;
    }

    public void setIs_cut_off_at(String is_cut_off_at) {
        this.is_cut_off_at = is_cut_off_at;
    }

    public int getCut_off_id() {
        return cut_off_id;
    }

    public void setCut_off_id(int cut_off_id) {
        this.cut_off_id = cut_off_id;
    }
}
