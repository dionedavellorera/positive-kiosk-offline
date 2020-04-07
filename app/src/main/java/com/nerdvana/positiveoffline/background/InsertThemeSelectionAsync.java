package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.PrinterSeries;
import com.nerdvana.positiveoffline.entities.ThemeSelection;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsertThemeSelectionAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertThemeSelectionAsync(SyncCallback syncCallback,
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
        List<ThemeSelection> themeSelectionList = new ArrayList<>();
        themeSelectionList.add(new ThemeSelection(100, "LIGHT MODE", true));
        themeSelectionList.add(new ThemeSelection(101, "DARK MODE", false));
        dataSyncViewModel.insertThemeSelection(themeSelectionList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("theme_selection");
    }


}
