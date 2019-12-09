package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.PrinterLanguage;
import com.nerdvana.positiveoffline.entities.PrinterSeries;

import java.util.List;

@Dao
public interface PrinterLanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PrinterLanguage> printerLanguageList);

    @Query("SELECT * FROM PrinterLanguage")
    List<PrinterLanguage> printerLanguageList();

    @Query("SELECT * FROM PrinterLanguage WHERE is_selected = '1' LIMIT 1")
    PrinterLanguage activePrinterLanguage();

    @Query("DELETE FROM PrinterLanguage")
    void truncatePrinterLanguage();


    @Update
    void update(PrinterLanguage printerLanguage);

}
