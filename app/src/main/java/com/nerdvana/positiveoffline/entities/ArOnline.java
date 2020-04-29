package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ArOnline")
public class ArOnline {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private int core_id;
    private String ar_online;
    private String image_file;

    public ArOnline(int core_id, String ar_online,
                    String image_file) {
        this.core_id = core_id;
        this.ar_online = ar_online;
        this.image_file = image_file;
    }

    public int getCore_id() {
        return core_id;
    }

    public String getAr_online() {
        return ar_online;
    }


    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public void setAr_online(String ar_online) {
        this.ar_online = ar_online;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }
}
