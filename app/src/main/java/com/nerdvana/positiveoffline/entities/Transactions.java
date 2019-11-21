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
    private String user_id;
    private Boolean is_void = false;
    private String is_void_by = "";
    private Boolean is_completed = false;
    private String is_completed_by = "";
    private Boolean is_saved = false;
    private String is_saved_by = "";
    private Boolean is_cut_off = false;
    private String is_cut_off_by = "";
    private String trans_name;


    public Transactions(@NonNull String control_number,
                        String user_id) {
        this.control_number = control_number;
        this.user_id = user_id;
    }

    @Ignore
    public Transactions(int id,@NonNull String control_number,
                        String user_id, Boolean is_void,
                        Boolean is_completed, Boolean is_saved) {
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

    public void setUser_id(String user_id) {
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

    public String getUser_id() {
        return user_id;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public Boolean getIs_cut_off() {
        return is_cut_off;
    }

    public void setIs_cut_off(Boolean is_cut_off) {
        this.is_cut_off = is_cut_off;
    }

    public String getIs_void_by() {
        return is_void_by;
    }

    public void setIs_void_by(String is_void_by) {
        this.is_void_by = is_void_by;
    }

    public String getIs_completed_by() {
        return is_completed_by;
    }

    public void setIs_completed_by(String is_completed_by) {
        this.is_completed_by = is_completed_by;
    }

    public String getIs_saved_by() {
        return is_saved_by;
    }

    public void setIs_saved_by(String is_saved_by) {
        this.is_saved_by = is_saved_by;
    }

    public String getIs_cut_off_by() {
        return is_cut_off_by;
    }

    public void setIs_cut_off_by(String is_cut_off_by) {
        this.is_cut_off_by = is_cut_off_by;
    }
}
