package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.CreditCards;

import java.util.List;

@Dao
public interface CashDenominationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CashDenomination> cashDenominationList);

    @Query("SELECT * FROM CashDenomination ORDER BY amount DESC")
    List<CashDenomination> cashDenoList();

}
