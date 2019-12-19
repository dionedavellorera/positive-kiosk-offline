package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.OrDetails;

@Dao
public interface OrDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrDetails orDetailsList);

    @Query("SELECT * FROM OrDetails WHERE transaction_id = :transaction_id")
    OrDetails orDetails(String transaction_id);


}
