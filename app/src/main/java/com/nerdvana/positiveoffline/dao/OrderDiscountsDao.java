package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.model.OdWithPd;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;

import java.util.List;

@Dao
public interface OrderDiscountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<OrderDiscounts> orderDiscountsList);

    @Query("SELECT * FROM OrderDiscounts")
    List<OrderDiscounts> getAllOrderDiscounts();

    @Query("SELECT * FROM OrderDiscounts where is_sent_to_server = 0")
    List<OrderDiscounts> unsyncedOrderDiscounts();

    @Query("SELECT * FROM OrderDiscounts where is_sent_to_server = 0 AND to_id != 0")
    List<OrderDiscounts> unsyncedToOrderDiscounts();

    @Query("SELECT * FROM Orders where transaction_id = :transaction_id")
    List<OrderWithDiscounts> orderDiscountList(String transaction_id);

    @Query("SELECT * FROM OrderDiscounts where posted_discount_id = :posted_discount_id")
    List<OrderDiscounts> discountList(int posted_discount_id);

    @Query("SELECT * FROM OrderDiscounts where transaction_id = :transaction_id")
    List<OrderDiscounts> odiscountTransId(int transaction_id);

    @Query("SELECT * FROM OrderDiscounts where transaction_id = :transaction_id")
    List<OdWithPd> odWithPd(int transaction_id);

    @Update
    void update(OrderDiscounts orderDiscounts);

    @Query("UPDATE OrderDiscounts set is_sent_to_server = 1 where transaction_id = :transaction_id")
    int updateSentToServer(String transaction_id);

}
