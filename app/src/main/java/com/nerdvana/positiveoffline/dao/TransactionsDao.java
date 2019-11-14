package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

@Dao
public interface TransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Transactions> dataSyncList);

    @Query("SELECT * FROM Transactions WHERE is_saved = 0 AND is_completed = 0")
    LiveData<List<Transactions>> ldTransactionsList();

    @Query("SELECT * FROM Transactions WHERE is_saved = 0 AND is_completed = 0")
    List<Transactions> transactionsList();

    @Update()
    void update(Transactions transaction);


}
