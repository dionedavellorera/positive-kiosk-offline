package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.nerdvana.positiveoffline.apiresponses.ArOnlineResponse;
import com.nerdvana.positiveoffline.apiresponses.TakasTypeResponse;
import com.nerdvana.positiveoffline.entities.ArOnline;
import com.nerdvana.positiveoffline.entities.Takas;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsertTakasAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<TakasTypeResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertTakasAsync(List<TakasTypeResponse.Result> list, SyncCallback syncCallback,
                            DataSyncViewModel dataSyncViewModel, Context context) {
        this.syncCallback = syncCallback;
        this.list = list;
        this.dataSyncViewModel = dataSyncViewModel;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Takas> takasList = new ArrayList<>();
        for (TakasTypeResponse.Result result :list) {
            takasList.add(new Takas(
                    result.getId(),
                    result.getTakas()));
        }
        dataSyncViewModel.insertTakas(takasList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("takas_type");
    }


}
