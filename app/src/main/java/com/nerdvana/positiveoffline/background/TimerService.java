package com.nerdvana.positiveoffline.background;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.apirequests.FetchDiscountRequest;
import com.nerdvana.positiveoffline.apirequests.SendDataRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchDiscountResponse;
import com.nerdvana.positiveoffline.dao.CutOffDao;
import com.nerdvana.positiveoffline.dao.EndOfDayDao;
import com.nerdvana.positiveoffline.dao.OrDetailsDao;
import com.nerdvana.positiveoffline.dao.OrderDiscountsDao;
import com.nerdvana.positiveoffline.dao.OrdersDao;
import com.nerdvana.positiveoffline.dao.PaymentsDao;
import com.nerdvana.positiveoffline.dao.PostedDiscountsDao;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.ServerConnectionTest;
import com.nerdvana.positiveoffline.repository.TransactionsRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerService extends Service {
    long secsOfDate = 0;
    private static String currentDate = "";

    CountDownTimer countUpTimer;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        countUpTimer = new CountUpTimer(999999999) {
            @Override
            public void onTick(int second) {
                secsOfDate+= 1;

                if (secsOfDate % 5 == 0) {
                    BusProvider.getInstance().post(new ServerConnectionTest(""));
                }

                Log.d("DIONEDATA", String.valueOf(secsOfDate));

                if (secsOfDate % 30 == 0) { //process sending of data to server
                    final PosDatabase posDatabase = DatabaseHelper.getDatabase(TimerService.this);
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            EndOfDayDao endOfDayDao = posDatabase.endOfDayDao();
                            CutOffDao cutOffDao = posDatabase.cutOffDao();
                            TransactionsDao transactionsDao = posDatabase.transactionsDao();
                            OrDetailsDao orDetailsDao = posDatabase.orDetailsDao();
                            PostedDiscountsDao postedDiscountsDao = posDatabase.postedDiscountsDao();
                            PaymentsDao paymentsDao = posDatabase.paymentsDao();
                            OrdersDao ordersDao = posDatabase.ordersDao();
                            OrderDiscountsDao orderDiscountsDao = posDatabase.orderDiscountsDao();

                            Map<String, String> tmpMap = new HashMap<>();
                            List<EndOfDay> unsyncedEndOfDay = endOfDayDao.unsyncedEndOfDay();
                            tmpMap.put("end_of_day", GsonHelper.getGson().toJson(unsyncedEndOfDay));
                            List<CutOff> unsyncedCutOff = cutOffDao.unsyncedCutOff();
                            tmpMap.put("cut_off", GsonHelper.getGson().toJson(unsyncedCutOff));
                            List<Transactions> unsyncedTransactions = transactionsDao.unsyncedTransactions();
                            tmpMap.put("transactions", GsonHelper.getGson().toJson(unsyncedTransactions));
                            List<OrDetails> unsyncedOrDetails = orDetailsDao.unsyncedOrDetails();
                            tmpMap.put("or_details", GsonHelper.getGson().toJson(unsyncedOrDetails));
                            List<PostedDiscounts> unsyncedPostedDiscounts = postedDiscountsDao.unsyncedPostedDiscounts();
                            tmpMap.put("posted_discounts", GsonHelper.getGson().toJson(unsyncedPostedDiscounts));
                            List<Payments> unsynedPayments = paymentsDao.unsyncedPayments();
                            tmpMap.put("payments", GsonHelper.getGson().toJson(unsynedPayments));
                            List<Orders> unsyncedOrders = ordersDao.unsyncedOrders();
                            tmpMap.put("orders", GsonHelper.getGson().toJson(unsyncedOrders));
                            List<OrderDiscounts> unsyncedOrderDiscounts = orderDiscountsDao.unsyncedOrderDiscounts();
                            tmpMap.put("order_discounts", GsonHelper.getGson().toJson(unsyncedOrderDiscounts));

                            Map<String, Object> wholeData = new HashMap<>();

                            if (unsyncedEndOfDay.size() > 0) {
                                wholeData.put("data", GsonHelper.getGson().toJson(tmpMap));
//                                wholeData.put("cut_off", GsonHelper.getGson().toJson(unsyncedCutOff));
//                                wholeData.put("transactions", GsonHelper.getGson().toJson(unsyncedTransactions));
//                                wholeData.put("or_details", GsonHelper.getGson().toJson(unsyncedOrDetails));
//                                wholeData.put("posted_discounts", GsonHelper.getGson().toJson(unsyncedPostedDiscounts));
//                                wholeData.put("payments", GsonHelper.getGson().toJson(unsynedPayments));
//                                wholeData.put("orders", GsonHelper.getGson().toJson(unsyncedOrders));
//                                wholeData.put("order_discounts", GsonHelper.getGson().toJson(unsyncedOrderDiscounts));

                                IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
                                SendDataRequest req = new SendDataRequest(wholeData);

//                                Call<ResponseBody> call = iUsers.sendData(req.getMapValue());
//
//                                call.enqueue(new Callback<ResponseBody>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                    }
//                                });



                                Log.d("SYNCDATA_ENDOFDAY", "SEND DATA TO SERVER");
                                Log.d("SYNCDATA_ENDOFDAY", wholeData.toString());
                            } else {
                                Log.d("SYNCDATA_ENDOFDAY", "NO DATA TO SEND");
                            }

                            return null;
                        }
                    }.execute();

                }
            }
        }.start();


        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countUpTimer != null) {
            countUpTimer.cancel();
            countUpTimer = null;
        }
    }


    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

