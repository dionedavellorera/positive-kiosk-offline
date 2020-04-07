package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.ServiceCharge;

import java.util.List;

@Dao
public interface ServiceChargeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ServiceCharge cashDenominationList);

    @Query("SELECT * FROM ServiceCharge")
    List<ServiceCharge> serviceChargeList();

    @Update
    public void update(ServiceCharge serviceCharge);

    @Query("SELECT * FROM ServiceCharge WHERE is_selected = '1'")
    ServiceCharge getActiveServiceCharge();
}
