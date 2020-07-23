package com.nerdvana.positiveoffline.background;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
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
import com.nerdvana.positiveoffline.model.PaymentSelectionModel;
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

            List<PaymentSelectionModel> selectionModelList = new ArrayList<>();

            for (FetchPaymentTypeResponse.OnlinePayment op : r.getOnlinePaymentList()) {
                selectionModelList.add(new PaymentSelectionModel(
                        op.getPaymentId(), op.getOnlinePaymentId(),
                        op.getOnlinePayment(), op.getImageFile()));
            }

            for (FetchPaymentTypeResponse.AccountReceivable ar : r.getAccountReceivableList()) {
                selectionModelList.add(new PaymentSelectionModel(
                        ar.getPaymentId(), ar.getArPaymentId(),
                        ar.getAccountReceivablePayment(), ar.getImageFile()));
            }

            for (FetchPaymentTypeResponse.MobilePayment mp : r.getMobilePaymentList()) {
                selectionModelList.add(new PaymentSelectionModel(
                        mp.getPaymentId(), mp.getMobilePaymentId(),
                        mp.getMobilePayment(), mp.getImageFile()));
            }

            Log.d("PAYMENTSELEC", r.getPaymentType() + "-"+String.valueOf(selectionModelList.size()) + "-"  +r.getCoreId() + "-" + String.valueOf(r.getId()));
            Log.d("PAYMENTSELEC-MP", r.getPaymentType() + "-"+String.valueOf(r.getMobilePaymentList().size()));
            Log.d("PAYMENTSELEC-AR", r.getPaymentType() + "-"+String.valueOf(r.getAccountReceivableList().size()));
            Log.d("PAYMENTSELEC-OL", r.getPaymentType() + "-"+String.valueOf(r.getOnlinePaymentList().size()));

            paymentType = new PaymentTypes(
                    Integer.valueOf(r.getCoreId()),
                    r.getImage() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
                    r.getPaymentType(),
                    GsonHelper.getGson().toJson(selectionModelList));

//            if (r.getCoreId().equalsIgnoreCase("3")) { //ONLINE
//                paymentType = new PaymentTypes(
//                        Integer.valueOf(r.getCoreId()),
//                        r.getImage() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
//                        r.getPaymentType(),
//                        GsonHelper.getGson().toJson(r.getOnlinePaymentList()));
//            } else if (r.getCoreId().equalsIgnoreCase("8")) {
//                paymentType = new PaymentTypes(
//                        Integer.valueOf(r.getCoreId()),
//                        r.getImage() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
//                        r.getPaymentType(),
//                        GsonHelper.getGson().toJson(r.getAccountReceivableList()));
//            } else if (r.getCoreId().equalsIgnoreCase("9")) {
//                paymentType = new PaymentTypes(
//                        Integer.valueOf(r.getCoreId()),
//                        r.getImage() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
//                        r.getPaymentType(),
//                        GsonHelper.getGson().toJson(r.getMobilePaymentList()));
//            } else {
//                paymentType = new PaymentTypes(
//                        Integer.valueOf(r.getCoreId()),
//                        r.getImage() != null ? String.valueOf(r.getCoreId()) + ".jpg" : "",
//                        r.getPaymentType(),
//                        GsonHelper.getGson().toJson(new ArrayList<>()));
//            }

            if (r.getMobilePaymentList().size() > 0) {
                for (FetchPaymentTypeResponse.MobilePayment mobilePayment : r.getMobilePaymentList()) {
                    if (mobilePayment.getImageFile() != null) {
                        File direct = new File(Environment.getExternalStorageDirectory()
                                + "/POS/MOBILEPAYMENT");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }
                        File directory = Environment.getExternalStorageDirectory();
                        File file = new File(directory, "/POS/MOBILEPAYMENT/" + mobilePayment.getMobilePaymentId() + ".jpg");

                        if (!file.exists()) {

                            if (!TextUtils.isEmpty(r.getImage())) {
                                DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

//                        Uri downloadUri = Uri.parse(SharedPreferenceManager.getString(null, AppConstants.HOST) + "/uploads/icon/" + r.getImage());
                                Uri downloadUri = Uri.parse(mobilePayment.getImageFile());
                                DownloadManager.Request request = new DownloadManager.Request(
                                        downloadUri);

                                request.setAllowedNetworkTypes(
                                        DownloadManager.Request.NETWORK_WIFI
                                                | DownloadManager.Request.NETWORK_MOBILE)
                                        .setAllowedOverRoaming(false).setTitle(mobilePayment.getMobilePayment())
//                        .setDescription(r.)
                                        .setDestinationInExternalPublicDir("/POS/MOBILEPAYMENT", String.valueOf(mobilePayment.getMobilePaymentId()) + ".jpg");

                                mgr.enqueue(request);
                            }
                        }
                    }
                }
            }

            if (r.getOnlinePaymentList().size() > 0) {
                for (FetchPaymentTypeResponse.OnlinePayment onlinePayment : r.getOnlinePaymentList()) {
                    if (onlinePayment.getImageFile() != null) {
                        File direct = new File(Environment.getExternalStorageDirectory()
                                + "/POS/ONLINEPAYMENT");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }
                        File directory = Environment.getExternalStorageDirectory();
                        File file = new File(directory, "/POS/ONLINEPAYMENT/" + onlinePayment.getOnlinePaymentId() + ".jpg");

                        if (!file.exists()) {

                            if (!TextUtils.isEmpty(r.getImage())) {
                                DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

//                        Uri downloadUri = Uri.parse(SharedPreferenceManager.getString(null, AppConstants.HOST) + "/uploads/icon/" + r.getImage());
                                Uri downloadUri = Uri.parse(onlinePayment.getImageFile());
                                DownloadManager.Request request = new DownloadManager.Request(
                                        downloadUri);

                                request.setAllowedNetworkTypes(
                                        DownloadManager.Request.NETWORK_WIFI
                                                | DownloadManager.Request.NETWORK_MOBILE)
                                        .setAllowedOverRoaming(false).setTitle(onlinePayment.getOnlinePayment())
//                        .setDescription(r.)
                                        .setDestinationInExternalPublicDir("/POS/ONLINEPAYMENT", String.valueOf(onlinePayment.getOnlinePaymentId()) + ".jpg");

                                mgr.enqueue(request);
                            }
                        }
                    }
                }
            }

            if (r.getAccountReceivableList().size() > 0) {
                for (FetchPaymentTypeResponse.AccountReceivable accountReceivable : r.getAccountReceivableList()) {
                    if (accountReceivable.getImageFile() != null) {
                        File direct = new File(Environment.getExternalStorageDirectory()
                                + "/POS/ARONLINE");
                        if (!direct.exists()) {
                            direct.mkdirs();
                        }
                        File directory = Environment.getExternalStorageDirectory();
                        File file = new File(directory, "/POS/ARONLINE/" + accountReceivable.getArPaymentId() + ".jpg");

                        if (!file.exists()) {

                            if (!TextUtils.isEmpty(r.getImage())) {
                                DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

//                        Uri downloadUri = Uri.parse(SharedPreferenceManager.getString(null, AppConstants.HOST) + "/uploads/icon/" + r.getImage());
                                Uri downloadUri = Uri.parse(accountReceivable.getImageFile());
                                DownloadManager.Request request = new DownloadManager.Request(
                                        downloadUri);

                                request.setAllowedNetworkTypes(
                                        DownloadManager.Request.NETWORK_WIFI
                                                | DownloadManager.Request.NETWORK_MOBILE)
                                        .setAllowedOverRoaming(false).setTitle(accountReceivable.getAccountReceivablePayment())
//                        .setDescription(r.)
                                        .setDestinationInExternalPublicDir("/POS/ARONLINE", String.valueOf(accountReceivable.getArPaymentId()) + ".jpg");

                                mgr.enqueue(request);
                            }
                        }
                    }
                }
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

                    if (!TextUtils.isEmpty(r.getImage())) {
                        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

//                        Uri downloadUri = Uri.parse(SharedPreferenceManager.getString(null, AppConstants.HOST) + "/uploads/icon/" + r.getImage());
                        Uri downloadUri = Uri.parse(r.getImage());
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
            }



            paymentList.add(paymentType);
        }

        PaymentTypes pTypeMobile = new PaymentTypes(
                998,
                "9.jpg",
                "DELIVERY DETAILS",
                GsonHelper.getGson().toJson(new ArrayList<>()));

        paymentList.add(pTypeMobile);

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
