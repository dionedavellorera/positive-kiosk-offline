package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ArOnline")
public class ArOnline {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private int core_id;
    private String ar_online;

    public ArOnline(int core_id, String ar_online) {
        this.core_id = core_id;
        this.ar_online = ar_online;
    }

    public int getId() {
        return id;
    }

    public int getCore_id() {
        return core_id;
    }

    public String getAr_online() {
        return ar_online;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public void setAr_online(String ar_online) {
        this.ar_online = ar_online;
    }
}
