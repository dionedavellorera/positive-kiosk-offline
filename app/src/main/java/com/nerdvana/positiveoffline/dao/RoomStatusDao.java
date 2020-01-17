package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.RoomStatus;

import java.util.List;

@Dao
public interface RoomStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RoomStatus> roomStatusList);

    @Query("SELECT * FROM RoomStatus")
    List<RoomStatus> getRoomStatus();

    @Query("SELECT * FROM RoomStatus WHERE core_id = :core_id")
    RoomStatus getRoomStatusViaId(int core_id);
}
