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
    public ServiceCharge(Double value, boolean is_percentage) {
        this.value = value;
        this.is_percentage = is_percentage;
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
