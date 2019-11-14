package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DataSync")
public class DataSync {

    public DataSync(@NonNull String table, @NonNull Boolean isSynced) {
        this.table = table;
        this.isSynced = isSynced;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String table;
    @NonNull
    private Boolean isSynced;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getTable() {
        return table;
    }

    public void setTable(@NonNull String table) {
        this.table = table;
    }

    @NonNull
    public Boolean getSynced() {
        return isSynced;
    }

    public void setSynced(@NonNull Boolean synced) {
        isSynced = synced;
    }
}
