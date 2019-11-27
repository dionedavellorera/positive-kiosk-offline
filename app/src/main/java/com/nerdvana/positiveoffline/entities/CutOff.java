package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CutOff")
public class CutOff {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;


}
