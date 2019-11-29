package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.CutOff;

import java.util.List;

@Dao
public interface CutOffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CutOff cutOffList);

    @Query("SELECT * FROM CutOff WHERE id = :cut_off_id")
    CutOff cutOffData(long cut_off_id);

    @Query("SELECT * FROM CutOff WHERE z_read_id = 0")
    List<CutOff> unCutOffTransactions();

    @Update
    void update(CutOff cutOff);
}
