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
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;

import java.util.List;

@Dao
public interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Orders> dataSyncList);

    @Query("SELECT * FROM Orders")
    LiveData<List<Orders>> ldOrderList();

    @Query("SELECT * FROM Orders WHERE transaction_id = :transaction_id AND is_void = 0")
    List<Orders> orderList(String transaction_id);

    @Query("SELECT * FROM Orders WHERE transaction_id = :transaction_id AND is_void = 0 AND is_editing = 1")
    List<Orders> editingOrderList(String transaction_id);

//    @Query("SELECT * FROM Orders o LEFT JOIN OrderDiscounts od on o.core_id = od.product_id WHERE transaction_id = :transaction_id AND is_void = 0")
//    List<OrderWithDiscounts> orderWithDiscountList(String transaction_id);


    @Update()
    void update(Orders order);
}
