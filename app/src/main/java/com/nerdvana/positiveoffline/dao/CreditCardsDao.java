package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.DataSync;

import java.util.List;

@Dao
public interface CreditCardsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CreditCards> creditCardsList);

    @Query("SELECT * FROM CreditCards")
    List<CreditCards> creditCardList();

    @Update()
    void update(CreditCards creditCards);
}
