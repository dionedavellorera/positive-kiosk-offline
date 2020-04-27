package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Takas")
public class Takas {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private int core_id;
    private String takas_type;


    public Takas(int core_id, String takas_type) {
        this.core_id = core_id;
        this.takas_type = takas_type;
    }

    public int getId() {
        return id;
    }

    public int getCore_id() {
        return core_id;
    }

    public String getTakas_type() {
        return takas_type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public void setTakas_type(String takas_type) {
        this.takas_type = takas_type;
    }
}
