package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PrinterLanguage")
public class PrinterLanguage {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String language_name;
    private int language_constant;
    private Boolean is_selected;

    public PrinterLanguage(String language_name, int language_constant,
                           Boolean is_selected) {
        this.language_name = language_name;
        this.language_constant = language_constant;
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

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

    public int getLanguage_constant() {
        return language_constant;
    }

    public void setLanguage_constant(int language_constant) {
        this.language_constant = language_constant;
    }
}
