package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Transactions")
public class Transactions {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String control_number;
    @NonNull
    private int user_id;
    private Boolean is_void = false;
    private Boolean is_completed = false;
    private Boolean is_saved = false;


    public Transactions(@NonNull String control_number,
                        int user_id) {
        this.control_number = control_number;
        this.user_id = user_id;
    }

    @Ignore
    public Transactions(int id,@NonNull String control_number, int user_id, Boolean is_void, Boolean is_completed, Boolean is_saved) {
        this.id = id;
        this.control_number = control_number;
        this.user_id = user_id;
        this.is_void = is_void;
        this.is_completed = is_completed;
        this.is_saved = is_saved;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setControl_number(@NonNull String control_number) {
        this.control_number = control_number;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setIs_void(Boolean is_void) {
        this.is_void = is_void;
    }

    public void setIs_completed(Boolean is_completed) {
        this.is_completed = is_completed;
    }

    public void setIs_saved(Boolean is_saved) {
        this.is_saved = is_saved;
    }

    public Boolean getIs_saved() {
        return is_saved;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getControl_number() {
        return control_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }


}
