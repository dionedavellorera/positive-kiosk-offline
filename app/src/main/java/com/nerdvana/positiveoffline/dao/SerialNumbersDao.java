package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.SerialNumbers;

import java.util.List;

@Dao
public interface SerialNumbersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SerialNumbers serialNumbersList);

    @Query("SELECT * FROM SerialNumbers ")
    List<SerialNumbers> getSerialNumbers();


    @Query("SELECT * FROM SerialNumbers WHERE transaction_id = :transaction_id AND is_void = 0")
    List<SerialNumbers> serialNumbersList(String transaction_id);

    @Query("SELECT * FROM SerialNumbers WHERE order_id = :order_id AND is_void = 0")
    List<SerialNumbers> serialNumbersListOrderId(int order_id);

    @Query("SELECT * FROM SerialNumbers WHERE is_sent_to_server = 0")
    List<SerialNumbers> getUnsyncedSerialNumbers();

    @Update()
    void update(SerialNumbers serialNumbers);


}
