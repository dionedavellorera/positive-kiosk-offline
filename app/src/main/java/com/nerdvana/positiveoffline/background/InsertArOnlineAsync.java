package com.nerdvana.positiveoffline.background;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.nerdvana.positiveoffline.apiresponses.ArOnlineResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomStatusResponse;
import com.nerdvana.positiveoffline.entities.ArOnline;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.io.File;
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
                    result.getArOnline(),
                    !TextUtils.isEmpty(result.getImage_file()) ? String.valueOf(result.getId()) + ".jpg" : ""));

            if (!TextUtils.isEmpty(result.getImage_file())) {
                File direct = new File(Environment.getExternalStorageDirectory()
                        + "/POS/ARONLINE");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                File directory = Environment.getExternalStorageDirectory();
                File file = new File(directory, "/POS/ARONLINE/" + result.getId() + ".jpg");

                if (!file.exists()) {

                    if (!TextUtils.isEmpty(result.getImage_file())) {
                        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

//                        Uri downloadUri = Uri.parse(SharedPreferenceManager.getString(null, AppConstants.HOST) + "/uploads/icon/" + r.getImage());
                        Uri downloadUri = Uri.parse(result.getImage_file());
                        DownloadManager.Request request = new DownloadManager.Request(
                                downloadUri);

                        request.setAllowedNetworkTypes(
                                DownloadManager.Request.NETWORK_WIFI
                                        | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false).setTitle(result.getArOnline())
//                        .setDescription(r.)
                                .setDestinationInExternalPublicDir("/POS/ARONLINE", String.valueOf(result.getId()) + ".jpg");

                        mgr.enqueue(request);
                    }



                }

            }

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
