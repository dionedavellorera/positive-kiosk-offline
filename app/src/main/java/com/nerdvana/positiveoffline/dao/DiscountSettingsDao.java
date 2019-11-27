package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;

import java.util.List;

@Dao
public interface DiscountSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DiscountSettings> discountSettingsList);

    @Query("SELECT * FROM DiscountSettings")
    List<DiscountSettings> discountSettingsList();
}
