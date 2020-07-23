package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.PaymentTypes;

import java.util.List;

@Dao
public interface PaymentTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PaymentTypes> paymentTypesList);

    @Query("SELECT * FROM PaymentTypes")
    List<PaymentTypes> paymentTypeList();


    @Query("DELETE FROM PaymentTypes")
    void truncatePaymentTypes();
//
//    @Query("SELECT * FROM DataSync WHERE isSynced = '0'")
//    List<DataSync> unsyncList();
//
//    @Update()
//    void update(DataSync dataSync);

}
