package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;

import java.util.List;

@Dao
public interface OrDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrDetails orDetailsList);

    @Update
    void update(OrDetails orDetails);


    @Query("SELECT * FROM OrDetails")
    List<OrDetails> getAllOrDetails();

    @Query("SELECT * FROM OrDetails WHERE transaction_id = :transaction_id")
    OrDetails orDetails(String transaction_id);

    @Query("SELECT * FROM OrDetails WHERE is_sent_to_server = 0")
    List<OrDetails> unsyncedOrDetails();

    @Query("SELECT * FROM OrDetails")
    List<OrDetails> getAllSavedOr();




}
