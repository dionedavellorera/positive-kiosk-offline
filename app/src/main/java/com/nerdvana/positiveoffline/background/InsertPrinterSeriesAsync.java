package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.PrinterSeries;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsertPrinterSeriesAsync extends AsyncTask<Void, Void, Void> {
    private SyncCallback syncCallback;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertPrinterSeriesAsync(SyncCallback syncCallback,
                                    DataSyncViewModel dataSyncViewModel, Context context) {
        this.syncCallback = syncCallback;
        this.dataSyncViewModel = dataSyncViewModel;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<PrinterSeries> printerSeriesList = new ArrayList<>();

        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_m10), Printer.TM_M10, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_m30), Printer.TM_M30, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_p20), Printer.TM_P20, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_p60), Printer.TM_P60, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_p60ii), Printer.TM_P60II, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_p80), Printer.TM_P80, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t20), Printer.TM_T20, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t70), Printer.TM_T70, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t81), Printer.TM_T81, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t82), Printer.TM_T82, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t83), Printer.TM_T83, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t88), Printer.TM_T88, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t90), Printer.TM_T90, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_t90kp), Printer.TM_T90KP, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_u220), Printer.TM_U220, true));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_u330), Printer.TM_U330, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_l90), Printer.TM_L90, false));
        printerSeriesList.add(new PrinterSeries(context.getString(R.string.printerseries_h6000), Printer.TM_H6000, false));
        dataSyncViewModel.insertPrinterSeries(printerSeriesList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("printer_series");
    }
}
