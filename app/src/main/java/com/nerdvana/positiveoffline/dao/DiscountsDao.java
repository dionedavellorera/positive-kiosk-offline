package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;

import java.util.List;

@Dao
public interface DiscountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Discounts> discountsList);

    @Query("SELECT * FROM Discounts")
    List<Discounts> discountsList();

    @Query("SELECT * FROM Discounts")
    List<DiscountWithSettings> discountWithSettings();

    @Query("SELECT * FROM Discounts WHERE is_custom_discount = 1")
    List<DiscountWithSettings> customDiscounts();

    @Query("SELECT * FROM Discounts WHERE is_special = 1")
    List<DiscountWithSettings> specialDiscounts();

    @Query("SELECT * FROM Discounts WHERE core_id = 1000")
    List<DiscountWithSettings> manualDiscountList();

    @Query("SELECT * FROM Discounts WHERE core_id = 1001")
    List<DiscountWithSettings> customDiscountList();

//    @Query("SELECT * FROM Discounts WHERE is_special = 1 OR core_id = 1000 OR core_id = 1001")
    @Query("SELECT * FROM Discounts WHERE is_special = 1 OR core_id = 1000 OR core_id = 1001")
    List<DiscountWithSettings> menuDiscountList();
}
