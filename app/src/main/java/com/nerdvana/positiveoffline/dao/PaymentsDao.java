package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Payments;

import java.util.List;

@Dao
public interface PaymentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Payments> paymentList);

    @Query("SELECT * FROM Payments WHERE transaction_id = :transaction_id AND is_void = 0")
    List<Payments> paymentList(String transaction_id);

    @Query("SELECT * FROM Payments WHERE cut_off_id = 0")
    List<Payments> paymentsForCutOff();

    @Query("SELECT * FROM Payments WHERE transaction_id = :transaction_id AND is_void = 0")
    LiveData<List<Payments>> ldPaymentList(String transaction_id);


    @Update()
    void update(Payments payment);



}
