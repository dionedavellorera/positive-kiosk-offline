package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.PrinterSeries;

import java.util.List;

@Dao
public interface PrinterSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PrinterSeries> printerSeriesList);

    @Query("SELECT * FROM PrinterSeries")
    List<PrinterSeries> printerSeriesList();


}
