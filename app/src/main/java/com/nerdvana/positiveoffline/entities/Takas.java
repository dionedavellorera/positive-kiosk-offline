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
    private String image_file;


    public Takas(int core_id, String takas_type,
                 String image_file) {
        this.core_id = core_id;
        this.takas_type = takas_type;
        this.image_file = image_file;
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

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }
}
