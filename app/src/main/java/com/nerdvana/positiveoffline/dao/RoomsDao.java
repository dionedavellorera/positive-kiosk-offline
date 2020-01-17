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

    @Query("SELECT * FROM Rooms ")
    List<RoomWithRates> roomWithRatesList();

    @Query("SELECT * FROM Rooms WHERE is_available = 1")
    List<Rooms> rooms();

    @Query("SELECT * FROM Rooms")
    LiveData<Rooms> roomsLiveData();

    @Query("SELECT * FROM Rooms WHERE room_id = :room_id")
    Rooms getRoomViaId(int room_id);

    @Query("SELECT * FROM Rooms WHERE transaction_id = :transaction_id")
    Rooms getRoomViaTransactionId(int transaction_id);
}

