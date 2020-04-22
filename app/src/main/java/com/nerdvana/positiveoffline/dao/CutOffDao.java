package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;

import java.util.List;

@Dao
public interface CutOffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CutOff cutOffList);


    @Query("SELECT * FROM CutOff")
    List<CutOff> getAllCutOffData();

    @Query("SELECT * FROM CutOff WHERE id = :cut_off_id")
    CutOff cutOffData(long cut_off_id);

    @Query("SELECT * FROM CutOff WHERE z_read_id = 0")
    List<CutOff> unCutOffTransactions();

    @Query("SELECT * FROM CutOff WHERE DATE(treg) BETWEEN :startDate AND :endDate")
    List<CutOff> getCutOffViaDate(String startDate, String endDate);

    @Query("SELECT * FROM CutOff WHERE is_sent_to_server = 0")
    List<CutOff> unsyncedCutOff();

    @Update
    void update(CutOff cutOff);
}
