package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.ThemeSelection;

import java.util.List;

@Dao
public interface ThemeSelectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ThemeSelection> themeSelectionList);

    @Query("SELECT * FROM ThemeSelection")
    List<ThemeSelection> themeSelectionList();

    @Query("SELECT * FROM ThemeSelection")
    LiveData<List<ThemeSelection>> ldThemeSelection();

    @Update
    void update(List<ThemeSelection> themeSelection);

    @Query("DELETE FROM ThemeSelection")
    void truncateThemeSelection();

}
