package com.nerdvana.positiveoffline.background;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomStatusResponse;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InsertRoomStatusAsync extends AsyncTask<Void, Void, Void> {
    private SyncCallback syncCallback;
    private List<FetchRoomStatusResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertRoomStatusAsync(List<FetchRoomStatusResponse.Result> list, SyncCallback syncCallback,
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
        List<RoomStatus> roomStatusList = new ArrayList<>();
        for (FetchRoomStatusResponse.Result result :list) {
            roomStatusList.add(new RoomStatus(
                    result.getCoreId(),
                    result.getRoomStatus(),
                    result.getColor(),
                    result.getIsBlink(),
                    result.getIsTimer(),
                    result.getIsName(),
                    result.getIsBuddy(),
                    result.getIsCancel()));
        }
        dataSyncViewModel.insertRoomStatus(roomStatusList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("room_status");
    }



}
