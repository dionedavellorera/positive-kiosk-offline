package com.nerdvana.positiveoffline.background;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.Utils;
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
import com.nerdvana.positiveoffline.model.ReprintReceiptData;
import com.nerdvana.positiveoffline.model.ServerConnectionTest;
import com.nerdvana.positiveoffline.repository.TransactionsRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

                if (secsOfDate % 20 == 0) {
                    BusProvider.getInstance().post(new ReprintReceiptData(""));
                }

                if (secsOfDate % 10000000 == 0) { //process sending of data to server
                    final PosDatabase posDatabase = DatabaseHelper.getDatabase(TimerService.this);
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            final EndOfDayDao endOfDayDao = posDatabase.endOfDayDao();
                            final CutOffDao cutOffDao = posDatabase.cutOffDao();
                            final TransactionsDao transactionsDao = posDatabase.transactionsDao();
                            final OrDetailsDao orDetailsDao = posDatabase.orDetailsDao();
                            final PostedDiscountsDao postedDiscountsDao = posDatabase.postedDiscountsDao();
                            final PaymentsDao paymentsDao = posDatabase.paymentsDao();
                            final OrdersDao ordersDao = posDatabase.ordersDao();
                            final OrderDiscountsDao orderDiscountsDao = posDatabase.orderDiscountsDao();

                            Map<String, String> tmpMap = new HashMap<>();
                            final List<EndOfDay> unsyncedEndOfDay = endOfDayDao.unsyncedEndOfDay();
                            tmpMap.put("end_of_day", GsonHelper.getGson().toJson(unsyncedEndOfDay));
                            final List<CutOff> unsyncedCutOff = cutOffDao.unsyncedCutOff();
                            tmpMap.put("cut_off", GsonHelper.getGson().toJson(unsyncedCutOff));
                            final List<Transactions> unsyncedTransactions = transactionsDao.unsyncedTransactions();
                            tmpMap.put("transactions", GsonHelper.getGson().toJson(unsyncedTransactions));
                            final List<OrDetails> unsyncedOrDetails = orDetailsDao.unsyncedOrDetails();
                            tmpMap.put("or_details", GsonHelper.getGson().toJson(unsyncedOrDetails));
                            final List<PostedDiscounts> unsyncedPostedDiscounts = postedDiscountsDao.unsyncedPostedDiscounts();
                            tmpMap.put("posted_discounts", GsonHelper.getGson().toJson(unsyncedPostedDiscounts));
                            final List<Payments> unsynedPayments = paymentsDao.unsyncedPayments();
                            tmpMap.put("payments", GsonHelper.getGson().toJson(unsynedPayments));
                            final List<Orders> unsyncedOrders = ordersDao.unsyncedOrders();
                            tmpMap.put("orders", GsonHelper.getGson().toJson(unsyncedOrders));
                            final List<OrderDiscounts> unsyncedOrderDiscounts = orderDiscountsDao.unsyncedOrderDiscounts();
                            tmpMap.put("order_discounts", GsonHelper.getGson().toJson(unsyncedOrderDiscounts));

                            Map<String, Object> wholeData = new HashMap<>();

                            if (unsyncedEndOfDay.size() > 0) {
                                wholeData.put("data", GsonHelper.getGson().toJson(tmpMap));

                                IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
                                SendDataRequest req = new SendDataRequest(wholeData);

                                Call<ResponseBody> call = iUsers.sendData(req.getMapValue());

                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
//                                        if (unsyncedEndOfDay.size() > 0) {
//                                            for (EndOfDay endOfDay : unsyncedEndOfDay) {
//                                                message += "END OF DAY ID - " + endOfDay.getId() + "PROCESSED\n";
//                                                endOfDay.setIs_sent_to_server(1);
//                                                endOfDayDao.update(endOfDay);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
//
//                                        if (unsyncedCutOff.size() > 0) {
//                                            for (CutOff cutOff : unsyncedCutOff) {
//                                                message += "CUTOFF ID - " + cutOff.getId() + "PROCESSED\n";
//                                                cutOff.setIs_sent_to_server(1);
//                                                cutOffDao.update(cutOff);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
//
//                                        if (unsyncedTransactions.size() > 0) {
//                                            for (Transactions transactions : unsyncedTransactions) {
//                                                message += "TRANSACTIONS ID - " + transactions.getId() + "PROCESSED\n";
//                                                transactions.setIs_sent_to_server(1);
//                                                transactionsDao.update(transactions);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
//
//
//                                        if (unsyncedOrDetails.size() > 0) {
//                                            for (OrDetails orDetails : unsyncedOrDetails) {
//                                                message += "ORDETAILS TRANSID - " + orDetails.getTransaction_id() + "PROCESSED\n";
//                                                orDetails.setIs_sent_to_server(1);
//                                                orDetailsDao.update(orDetails);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
//
//                                        if (unsyncedPostedDiscounts.size() > 0) {
//                                            for (PostedDiscounts postedDiscounts : unsyncedPostedDiscounts) {
//                                                message += "POSTED DISCOUNTS ID - " + postedDiscounts.getId() + "PROCESSED\n";
//                                                postedDiscounts.setIs_sent_to_server(1);
//                                                postedDiscountsDao.update(postedDiscounts);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
//
//                                        if (unsynedPayments.size() > 0) {
//                                            for (Payments payments : unsynedPayments) {
//                                                message += "PAYMENTS ID - " + payments.getId() + "PROCESSED\n";
//                                                payments.setIs_sent_to_server(1);
//                                                paymentsDao.update(payments);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
//
//                                        if (unsyncedOrders.size() > 0) {
//                                            for (Orders orders : unsyncedOrders) {
//                                                message += "ORDERS ID - " + orders.getId() + "PROCESSED\n";
//                                                orders.setIs_sent_to_server(1);
//                                                ordersDao.update(orders);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
//
//                                        if (unsyncedOrderDiscounts.size() > 0) {
//                                            for (OrderDiscounts orderDiscounts : unsyncedOrderDiscounts) {
//                                                message += "ORDER DISCOUNTS ID - " + orderDiscounts.getId() + "PROCESSED\n";
//                                                orderDiscounts.setIs_sent_to_server(1);
//                                                orderDiscountsDao.update(orderDiscounts);
//                                            }
//                                            saveToTextFile(message);
//                                            message = "";
//                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                        saveToTextFile(t.getLocalizedMessage());
                                    }
                                });

                            } else {

//                                saveToTextFile("NO DATA TO SEND");

                            }

                            return null;
                        }
                    }.execute();

                }
            }
        }.start();


        return super.onStartCommand(intent, flags, startId);
    }

    private void saveToTextFile(String message) {
        File root = new File(Environment.getExternalStorageDirectory(), "POS/");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, Utils.getCurrentDate() +"_error.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(gpxfile, true);
            writer.append(message + " " + Utils.getCurrentDate());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

