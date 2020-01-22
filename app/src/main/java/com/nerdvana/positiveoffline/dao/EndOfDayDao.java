package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;

import java.util.List;

@Dao
public interface EndOfDayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(EndOfDay endOfDay);

    @Query("SELECT * FROM EndOfDay WHERE id = :end_of_day_id")
    EndOfDay endOfDayData(long end_of_day_id);

    @Query("SELECT * FROM EndOfDay WHERE DATE(treg) BETWEEN :startDate AND :endDate")
    List<EndOfDay> getEndOfDayViaDate(String startDate, String endDate);

    @Query("SELECT * FROM EndOfDay ORDER BY id DESC")
    List<EndOfDay> endOfDayList();

    @Query("SELECT * FROM EndOfDay WHERE is_sent_to_server = 0")
    List<EndOfDay> unsyncedEndOfDay();

    @Update
    void update(EndOfDay cutOff);

    @Update
    void update(List<EndOfDay> cutOff);
}
