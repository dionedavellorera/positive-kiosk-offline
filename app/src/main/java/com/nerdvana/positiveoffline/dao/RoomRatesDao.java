package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.Rooms;

import java.util.List;

@Dao
public interface RoomRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RoomRates roomRates);

    @Query("SELECT * FROM RoomRates WHERE room_id = :room_id")
    List<RoomRates> getRoomRates(int room_id);

}

