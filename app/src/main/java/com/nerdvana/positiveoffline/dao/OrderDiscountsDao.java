package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;

import java.util.List;

@Dao
public interface OrderDiscountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<OrderDiscounts> orderDiscountsList);

//    @Query("SELECT *,od.id as q FROM Orders o LEFT JOIN OrderDiscounts od on o.transaction_id = od.transaction_id WHERE o.transaction_id = :transaction_id AND is_void = 0")
    @Query("SELECT * FROM Orders WHERE transaction_id = :transaction_id AND is_void = 0")
    List<OrderWithDiscounts> orderDiscountList(String transaction_id);


}
