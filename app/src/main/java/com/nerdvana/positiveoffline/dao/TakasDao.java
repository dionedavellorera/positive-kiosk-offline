package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.ArOnline;
import com.nerdvana.positiveoffline.entities.Takas;

import java.util.List;

@Dao
public interface TakasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Takas> arOnlineList);

    @Query("SELECT * FROM Takas")
    List<Takas> arList();

    @Query("DELETE FROM Takas")
    void truncateTakas();

    @Update
    void update(Takas arOnline);

}
