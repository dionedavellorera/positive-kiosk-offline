package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Rooms")
public class Rooms {
    @PrimaryKey
    @NonNull
    private int room_id;
    private String area_name;
    private int area_id;
    private String room_name;
    private String room_type;
    private int room_type_id;
    private int is_available;
    private int status_id;
    private String status_description;
    private String hex_color;
    private String transaction_id;

    public Rooms(int room_id, String area_name,
                 int area_id, String room_name,
                 String room_type, int room_type_id,
                 int is_available, int status_id,
                 String status_description, String hex_color,
                 String transaction_id) {
        this.transaction_id = transaction_id;
        this.hex_color = hex_color;
        this.status_id = status_id;
        this.status_description = status_description;
        this.room_id = room_id;
        this.area_name = area_name;
        this.area_id = area_id;
        this.room_name = room_name;
        this.room_type = room_type;
        this.room_type_id = room_type_id;
        this.is_available = is_available;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getHex_color() {
        return hex_color;
    }

    public int getStatus_id() {
        return status_id;
    }

    public String getStatus_description() {
        return status_description;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public int getArea_id() {
        return area_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getRoom_type() {
        return room_type;
    }

    public int getRoom_type_id() {
        return room_type_id;
    }

    public int getIs_available() {
        return is_available;
    }


    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public void setHex_color(String hex_color) {
        this.hex_color = hex_color;
    }

    public void setStatus_description(String status_description) {
        this.status_description = status_description;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
