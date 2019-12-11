package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;

import java.util.List;

@Dao
public interface PostedDiscountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PostedDiscounts postedDiscounts);

    @Query("SELECT * FROM PostedDiscounts WHERE id = :posted_discount_id")
    PostedDiscounts getPostedDiscount(int posted_discount_id);

    @Query(("SELECT * FROM PostedDiscounts WHERE cut_off_id = 0"))
    List<PostedDiscounts> getUnCutOffPostedDiscounts();

    @Query(("SELECT * FROM PostedDiscounts WHERE end_of_day_id = 0"))
    List<PostedDiscounts> getZeroEndOfDay();


    @Query("SELECT pd.transaction_id as id,pd.id as pd_id, pd.discount_name, ds.percentage " +
            " FROM PostedDiscounts pd " +
            " INNER JOIN DiscountSettings ds ON pd.discount_id = ds.core_id " +
            " WHERE pd.transaction_id = :transaction_id AND pd.is_void = 0")
    List<TransactionWithDiscounts> postedDiscountList(String transaction_id);

    @Update
    public void update(PostedDiscounts postedDiscounts);
}