package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.PrinterLanguage;
import com.nerdvana.positiveoffline.entities.PrinterSeries;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsertPrinterLanguageAsync extends AsyncTask<Void, Void, Void> {
    private SyncCallback syncCallback;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertPrinterLanguageAsync(SyncCallback syncCallback,
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
        List<PrinterLanguage> printerLanguagesList = new ArrayList<>();

        printerLanguagesList.add(new PrinterLanguage(context.getString(R.string.lang_ank), Printer.MODEL_ANK, false));
        printerLanguagesList.add(new PrinterLanguage(context.getString(R.string.lang_japanese), Printer.MODEL_JAPANESE, false));
        printerLanguagesList.add(new PrinterLanguage(context.getString(R.string.lang_chinese), Printer.MODEL_CHINESE, false));
        printerLanguagesList.add(new PrinterLanguage(context.getString(R.string.lang_taiwan), Printer.MODEL_TAIWAN, false));
        printerLanguagesList.add(new PrinterLanguage(context.getString(R.string.lang_korean), Printer.MODEL_KOREAN, false));
        printerLanguagesList.add(new PrinterLanguage(context.getString(R.string.lang_thai), Printer.MODEL_THAI, false));
        printerLanguagesList.add(new PrinterLanguage(context.getString(R.string.lang_southasia), Printer.MODEL_SOUTHASIA, true));

        dataSyncViewModel.insertPrinterLanguage(printerLanguagesList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("printer_language");
    }

}
