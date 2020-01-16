package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.Rooms;

@Dao
public interface RoomRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoomRates roomRates);


}

