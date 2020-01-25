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
import com.nerdvana.positiveoffline.apirequests.AddCutOffOfflineRequest;
import com.nerdvana.positiveoffline.apirequests.AddEndOfDayOfflineRequest;
import com.nerdvana.positiveoffline.apirequests.AddOrDetailsOfflineRequest;
import com.nerdvana.positiveoffline.apirequests.AddOrderDiscountsOfflineRequest;
import com.nerdvana.positiveoffline.apirequests.AddOrdersOfflineRequest;
import com.nerdvana.positiveoffline.apirequests.AddPaymentsOfflineRequest;
import com.nerdvana.positiveoffline.apirequests.AddPostedDiscountsOfflineRequest;
import com.nerdvana.positiveoffline.apirequests.AddTransactionsOfflineRequest;
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
import com.nerdvana.positiveoffline.model.ShiftUpdateModel;
import com.nerdvana.positiveoffline.model.TimerUpdateModel;

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

                BusProvider.getInstance().post(new TimerUpdateModel(""));

                if (secsOfDate % 3 == 0) {
                    BusProvider.getInstance().post(new ShiftUpdateModel(""));
                }

                if (secsOfDate % 5 == 0) {
                    BusProvider.getInstance().post(new ServerConnectionTest(""));
                }

                if (secsOfDate % 99999999 == 0) {
                    BusProvider.getInstance().post(new ReprintReceiptData(""));
                }

                if (secsOfDate % 30 == 0) { //process sending of data to server
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

                            final List<EndOfDay> unsyncedEndOfDay = endOfDayDao.unsyncedEndOfDay();
                            final List<CutOff> unsyncedCutOff = cutOffDao.unsyncedCutOff();
                            final List<Transactions> unsyncedTransactions = transactionsDao.unsyncedTransactions();
                            final List<OrDetails> unsyncedOrDetails = orDetailsDao.unsyncedOrDetails();
                            final List<PostedDiscounts> unsyncedPostedDiscounts = postedDiscountsDao.unsyncedPostedDiscounts();
                            final List<Payments> unsynedPayments = paymentsDao.unsyncedPayments();
                            final List<Orders> unsyncedOrders = ordersDao.unsyncedOrders();
                            final List<OrderDiscounts> unsyncedOrderDiscounts = orderDiscountsDao.unsyncedOrderDiscounts();


                            IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);

                            /*
                                POSTED DISCOUNT TESTED AND OKAY
                             */
                            //region posted discounts
                            if (unsyncedPostedDiscounts.size() > 0) {
                                Map<String, String> postedDiscountMap = new HashMap<>();
                                postedDiscountMap.put("posted_discounts", GsonHelper.getGson().toJson(unsyncedPostedDiscounts));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(postedDiscountMap));
                                AddPostedDiscountsOfflineRequest req = new AddPostedDiscountsOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addPostedDiscountsOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
                                        for (final PostedDiscounts postedDiscounts : unsyncedPostedDiscounts) {
                                            message += "POSTED DISCOUNTS ID - " + postedDiscounts.getId() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    postedDiscounts.setIs_sent_to_server(1);
                                                    postedDiscountsDao.update(postedDiscounts);
                                                }
                                            });
                                        }
                                        saveToTextFile(message);
                                        message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            //endregion


                            /*
                                END OF DAY TESTED OKAY
                             */
                            //region end of day
                            if (unsyncedEndOfDay.size() > 0) {
                                Map<String, String> endOfDayMap = new HashMap<>();
                                endOfDayMap.put("end_of_day", GsonHelper.getGson().toJson(unsyncedEndOfDay));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(endOfDayMap));
                                AddEndOfDayOfflineRequest req = new AddEndOfDayOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addEndOfDayOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";

                                        for (final EndOfDay endOfDay : unsyncedEndOfDay) {

                                            message += "END OF DAY ID - " + endOfDay.getId() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    endOfDay.setIs_sent_to_server(1);
                                                    endOfDayDao.update(endOfDay);
                                                }
                                            });

                                        }
                                        saveToTextFile(message);
                                        message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            } else {
                                Log.d("ASYNCDATA", "NO ENTRY FOR EOD");
                            }
                            //endregion

                            /*
                                CUTOFF TESTED OKAY
                             */
                            //region cutoff
                            if (unsyncedCutOff.size() > 0) {
                                Map<String, String> cutOffMap = new HashMap<>();
                                cutOffMap.put("cut_off", GsonHelper.getGson().toJson(unsyncedCutOff));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(cutOffMap));
                                AddCutOffOfflineRequest req = new AddCutOffOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addCutOffOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
                                        for (final CutOff cutOff : unsyncedCutOff) {
                                            message += "CUTOFF ID - " + cutOff.getId() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    cutOff.setIs_sent_to_server(1);
                                                    cutOffDao.update(cutOff);
                                                }
                                            });
                                        }
                                            saveToTextFile(message);
                                            message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            //endregion

                            /*
                                TRANSACTIONS TESTED OKAY
                             */
                            //region transactions
                            if (unsyncedTransactions.size() > 0) {
                                Map<String, String> transactionMap = new HashMap<>();
                                transactionMap.put("transactions", GsonHelper.getGson().toJson(unsyncedTransactions));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(transactionMap));
                                AddTransactionsOfflineRequest req = new AddTransactionsOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addTransactionsOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
                                        for (final Transactions transactions : unsyncedTransactions) {
                                            message += "TRANSACTIONS ID - " + transactions.getId() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    transactions.setIs_sent_to_server(1);
                                                    transactionsDao.update(transactions);
                                                }
                                            });
                                        }
                                        saveToTextFile(message);
                                        message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            //endregion


                            /*
                                OR DETAILS TESTED
                             */
                            //region or details
                            if (unsyncedOrDetails.size() > 0) {
                                Map<String, String> orDetailsMap = new HashMap<>();
                                orDetailsMap.put("or_details", GsonHelper.getGson().toJson(unsyncedOrDetails));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(orDetailsMap));
                                AddOrDetailsOfflineRequest req = new AddOrDetailsOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addOrDetailsOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
                                        for (final OrDetails orDetails : unsyncedOrDetails) {
                                            message += "ORDETAILS TRANSID - " + orDetails.getTransaction_id() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    orDetails.setIs_sent_to_server(1);
                                                    orDetailsDao.update(orDetails);
                                                }
                                            });
                                        }
                                        saveToTextFile(message);
                                        message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
//                            //endregion

                            /*
                                PAYMENTS TESTED OKAY
                             */
                            //region payments
                            if (unsynedPayments.size() > 0) {
                                Map<String, String> paymentsMap = new HashMap<>();
                                paymentsMap.put("payments", GsonHelper.getGson().toJson(unsynedPayments));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(paymentsMap));
                                AddPaymentsOfflineRequest req = new AddPaymentsOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addPaymentsOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
                                        for (final Payments payments : unsynedPayments) {
                                            message += "PAYMENTS ID - " + payments.getId() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    payments.setIs_sent_to_server(1);
                                                    paymentsDao.update(payments);
                                                }
                                            });
                                        }
                                        saveToTextFile(message);
                                        message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            //endregion

                            /*
                                ORDERS FOR TESTING
                             */
                            //region orders
                            if (unsyncedOrders.size() > 0) {
                                Map<String, String> ordersMap = new HashMap<>();
                                ordersMap.put("orders", GsonHelper.getGson().toJson(unsyncedOrders));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(ordersMap));
                                AddOrdersOfflineRequest req = new AddOrdersOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addOrdersOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
                                        for (final Orders orders : unsyncedOrders) {
                                            message += "ORDERS ID - " + orders.getId() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    orders.setIs_sent_to_server(1);
                                                    ordersDao.update(orders);
                                                }
                                            });
                                        }
                                        saveToTextFile(message);
                                        message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            //endregion

                            /*
                                ORDER DISCOUNTS TESTED
                             */
                            //regionorder discounts
                            if (unsyncedOrderDiscounts.size() > 0) {
                                Map<String, String> orderDiscountMap = new HashMap<>();
                                orderDiscountMap.put("order_discounts", GsonHelper.getGson().toJson(unsyncedOrderDiscounts));

                                Map<String, Object> wholeData = new HashMap<>();
                                wholeData.put("data", GsonHelper.getGson().toJson(orderDiscountMap));
                                AddOrderDiscountsOfflineRequest req = new AddOrderDiscountsOfflineRequest(wholeData);

                                Call<ResponseBody> call = iUsers.addOrderDiscountsOffline(req.getMapValue());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        String message = "";
                                        for (final OrderDiscounts orderDiscounts : unsyncedOrderDiscounts) {
                                            message += "ORDER DISCOUNTS ID - " + orderDiscounts.getId() + "PROCESSED\n";
                                            AsyncTask.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    orderDiscounts.setIs_sent_to_server(1);
                                                    orderDiscountsDao.update(orderDiscounts);
                                                }
                                            });
                                        }
                                        saveToTextFile(message);
                                        message = "";
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            //endregion


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
        File gpxfile = new File(root, Utils.getCurrentDate() +"_logs.txt");
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

