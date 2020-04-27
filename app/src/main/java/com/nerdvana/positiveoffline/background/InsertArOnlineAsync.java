package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.nerdvana.positiveoffline.apiresponses.ArOnlineResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomStatusResponse;
import com.nerdvana.positiveoffline.entities.ArOnline;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsertArOnlineAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<ArOnlineResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertArOnlineAsync(List<ArOnlineResponse.Result> list, SyncCallback syncCallback,
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
        List<ArOnline> arOnlines = new ArrayList<>();
        for (ArOnlineResponse.Result result :list) {
            arOnlines.add(new ArOnline(
                    result.getCoreId(),
                    result.getArOnline()));
        }
        dataSyncViewModel.insertArOnline(arOnlines);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("ar_online");
    }


}
