package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

@Dao
public interface PayoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Payout payout);

    @Query("SELECT * FROM Payout WHERE series_number != '' ORDER BY id DESC LIMIT 1 ")
    Payout lastPayoutSeries();

    @Query("SELECT * FROM Payout WHERE cut_off_id = 0")
    List<Payout> getUnCutOffPayouts();


    @Update
    public void update(Payout payout);

}
