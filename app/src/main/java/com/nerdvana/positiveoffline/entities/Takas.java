package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Takas")
public class Takas {


    @PrimaryKey(autoGenerate = false)
    @NonNull
    private int core_id;
    private String takas_type;


    public Takas(int core_id, String takas_type) {
        this.core_id = core_id;
        this.takas_type = takas_type;
    }

    public int getCore_id() {
        return core_id;
    }

    public String getTakas_type() {
        return takas_type;
    }


    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public void setTakas_type(String takas_type) {
        this.takas_type = takas_type;
    }
}
