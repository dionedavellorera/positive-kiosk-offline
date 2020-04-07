package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ThemeSelection")
public class ThemeSelection {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int theme_id;
    @NonNull
    private String theme_name;

    @NonNull
    private Boolean is_selected;

    public ThemeSelection(int theme_id, @NonNull String theme_name,
                          Boolean is_selected) {
        this.is_selected = is_selected;
        this.theme_id = theme_id;
        this.theme_name = theme_name;
    }


    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    @NonNull
    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(@NonNull String theme_name) {
        this.theme_name = theme_name;
    }

    @NonNull
    public Boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(@NonNull Boolean is_selected) {
        this.is_selected = is_selected;
    }
}
