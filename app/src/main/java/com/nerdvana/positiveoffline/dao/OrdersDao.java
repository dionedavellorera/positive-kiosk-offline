package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

@Dao
public interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Orders> dataSyncList);

    @Query("SELECT * FROM Orders WHERE transaction_id = :transaction_id")
    LiveData<List<Orders>> ldOrderList(String transaction_id);

    @Query("SELECT * FROM Orders WHERE transaction_id = :transaction_id")
    List<Orders> orderList(String transaction_id);

    @Update()
    void update(DataSync dataSync);
}
