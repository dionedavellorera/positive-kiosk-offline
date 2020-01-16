package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RoomStatus")
public class RoomStatus {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int core_id;
    private String room_status;
    private String hex_color;
    private int is_blink;
    private int is_timer;
    private int is_name;
    private int is_buddy;
    private int is_cancel;

    public RoomStatus(int core_id, String room_status,
                      String hex_color, int is_blink,
                      int is_timer, int is_name,
                      int is_buddy, int is_cancel) {
        this.core_id = core_id;
        this.room_status = room_status;
        this.hex_color = hex_color;
        this.is_blink = is_blink;
        this.is_timer = is_timer;
        this.is_name = is_name;
        this.is_buddy = is_buddy;
        this.is_cancel = is_cancel;
    }

    public int getCore_id() {
        return core_id;
    }

    public String getRoom_status() {
        return room_status;
    }

    public String getHex_color() {
        return hex_color;
    }

    public int getIs_blink() {
        return is_blink;
    }

    public int getIs_timer() {
        return is_timer;
    }

    public int getIs_name() {
        return is_name;
    }

    public int getIs_buddy() {
        return is_buddy;
    }

    public int getIs_cancel() {
        return is_cancel;
    }
}
