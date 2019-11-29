package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;

import java.util.List;

@Dao
public interface OrderDiscountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<OrderDiscounts> orderDiscountsList);

    @Query("SELECT * FROM Orders where transaction_id = :transaction_id")
    List<OrderWithDiscounts> orderDiscountList(String transaction_id);

    @Query("SELECT * FROM OrderDiscounts where posted_discount_id = :posted_discount_id")
    List<OrderDiscounts> discountList(int posted_discount_id);

    @Update
    void update(OrderDiscounts orderDiscounts);

}
