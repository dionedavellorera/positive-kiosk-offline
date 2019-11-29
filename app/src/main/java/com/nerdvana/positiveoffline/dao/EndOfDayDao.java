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


    @Update
    void update(EndOfDay cutOff);
}
