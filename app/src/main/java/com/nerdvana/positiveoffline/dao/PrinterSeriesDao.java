package com.nerdvana.positiveoffline.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.PrinterSeries;

import java.util.List;

@Dao
public interface PrinterSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<PrinterSeries> printerSeriesList);

    @Query("SELECT * FROM PrinterSeries")
    List<PrinterSeries> printerSeriesList();

    @Query("SELECT * FROM PrinterSeries WHERE is_selected = '1' LIMIT 1")
    PrinterSeries activePrinterSeries();

    @Query("DELETE FROM PrinterSeries")
    void truncatePrinterSeries();

    @Update
    void update(PrinterSeries printerSeries);


}
