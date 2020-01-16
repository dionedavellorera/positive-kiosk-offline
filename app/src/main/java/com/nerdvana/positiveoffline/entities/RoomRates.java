package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RoomRates")
public class RoomRates {

    private int room_type_id;
    private int room_id;

    @PrimaryKey
    @NonNull
    private int room_rate_price_id;
    private Double amount;
    private String room_rate_description;

    public RoomRates(int room_type_id, int room_id,
                     int room_rate_price_id, Double amount,
                     String room_rate_description) {
        this.room_type_id = room_type_id;
        this.room_id = room_id;
        this.room_rate_price_id = room_rate_price_id;
        this.amount = amount;
        this.room_rate_description = room_rate_description;
    }

    public int getRoom_type_id() {
        return room_type_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public int getRoom_rate_price_id() {
        return room_rate_price_id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getRoom_rate_description() {
        return room_rate_description;
    }
}
