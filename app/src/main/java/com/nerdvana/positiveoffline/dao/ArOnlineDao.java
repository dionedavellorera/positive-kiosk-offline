package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.ArOnline;
import com.nerdvana.positiveoffline.entities.PrinterLanguage;

import java.util.List;

@Dao
public interface ArOnlineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ArOnline> arOnlineList);

    @Query("SELECT * FROM ArOnline")
    List<ArOnline> arList();

    @Query("DELETE FROM ArOnline")
    void truncateArOnline();

    @Update
    void update(ArOnline arOnline);

}
