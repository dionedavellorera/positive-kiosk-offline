package com.nerdvana.positiveoffline.background;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.ProductsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InsertPaymentTypeAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<FetchPaymentTypeResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertPaymentTypeAsync(List<FetchPaymentTypeResponse.Result> list, SyncCallback syncCallback,
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
        List<PaymentTypes> paymentList = new ArrayList<>();
        for (final FetchPaymentTypeResponse.Result r : list) {
            PaymentTypes paymentType = null;
            if (r.getCoreId().equalsIgnoreCase("8")) {
                paymentType = new PaymentTypes(
                        Integer.valueOf(r.getCoreId()),
                        r.getImage() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
                        "AR",
                        GsonHelper.getGson().toJson(r.getMobilePaymentList()));
            } else {
                paymentType = new PaymentTypes(
                        Integer.valueOf(r.getCoreId()),
                        r.getImage() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
                        r.getPaymentType(),
                        GsonHelper.getGson().toJson(r.getMobilePaymentList()));
            }



            if (r.getImage() != null) {
                File direct = new File(Environment.getExternalStorageDirectory()
                        + "/POS/PAYMENT_TYPE");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                File directory = Environment.getExternalStorageDirectory();
                File file = new File(directory, "/POS/PAYMENT_TYPE/" + r.getCoreId() + ".jpg");

                if (!file.exists()) {

                    DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                    Uri downloadUri = Uri.parse(SharedPreferenceManager.getString(null, AppConstants.HOST) + "/uploads/icon/" + r.getImage());
                    DownloadManager.Request request = new DownloadManager.Request(
                            downloadUri);

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI
                                    | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(r.getPaymentType())
//                        .setDescription(r.)
                            .setDestinationInExternalPublicDir("/POS/PAYMENT_TYPE", String.valueOf(r.getCoreId()) + ".jpg");

                    mgr.enqueue(request);


                }

            }



            paymentList.add(paymentType);
        }

//        PaymentTypes pTypeMobile = new PaymentTypes(
//                9,
//                "9.jpg",
//                "MOBILE PAYMENT");
//
//        paymentList.add(pTypeMobile);

        PaymentTypes pTypeGuest = new PaymentTypes(
                999,
                "8.jpg",
                "GUEST",
                GsonHelper.getGson().toJson(new ArrayList<>()));

        paymentList.add(pTypeGuest);

        dataSyncViewModel.insertPaymentType(paymentList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("payment_type");
    }




}
