package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.PrinterLanguage;
import com.nerdvana.positiveoffline.entities.PrinterSeries;

import java.util.List;

@Dao
public interface PrinterLanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PrinterLanguage> printerLanguageList);

    @Query("SELECT * FROM PrinterLanguage")
    List<PrinterLanguage> printerLanguageList();

}
