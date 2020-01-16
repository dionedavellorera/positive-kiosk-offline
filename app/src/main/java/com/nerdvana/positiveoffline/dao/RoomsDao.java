package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.model.RoomWithRates;

import java.util.List;

@Dao
public interface RoomsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Rooms room);

    @Update()
    void update(Rooms room);

    @Query("SELECT * FROM Rooms")
    List<RoomWithRates> roomWithRatesList();

    @Query("SELECT * FROM Rooms")
    List<Rooms> rooms();

    @Query("SELECT * FROM Rooms")
    LiveData<Rooms> roomsLiveData();
}
