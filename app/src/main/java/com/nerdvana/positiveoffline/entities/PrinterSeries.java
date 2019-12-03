package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PrinterSeries")
public class PrinterSeries {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String model_name;
    private int model_constant;
    private Boolean is_selected;

    public PrinterSeries(String model_name, int model_constant, Boolean is_selected) {
        this.model_name = model_name;
        this.model_constant = model_constant;
        this.is_selected = is_selected;
    }

    public Boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(Boolean is_selected) {
        this.is_selected = is_selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public int getModel_constant() {
        return model_constant;
    }

    public void setModel_constant(int model_constant) {
        this.model_constant = model_constant;
    }
}
