package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ServiceCharge")
public class ServiceCharge {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private Double value;
    private boolean is_percentage;

    private boolean is_selected = false;

    private int is_sent_to_server = 0;
    private int machine_id;
    private int branch_id;
    public ServiceCharge(Double value, boolean is_percentage,
                         int machine_id, int branch_id) {
        this.branch_id = branch_id;
        this.machine_id = machine_id;
        this.value = value;
        this.is_percentage = is_percentage;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public boolean isIs_percentage() {
        return is_percentage;
    }

    public void setIs_percentage(boolean is_percentage) {
        this.is_percentage = is_percentage;
    }

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
