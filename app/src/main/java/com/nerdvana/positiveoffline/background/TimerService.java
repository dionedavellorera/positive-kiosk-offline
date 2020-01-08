package com.nerdvana.positiveoffline.background;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.model.ServerConnectionTest;
import com.nerdvana.positiveoffline.repository.TransactionsRepository;

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

                if (secsOfDate % 60 == 0) { //process sending of data to server
                    PosDatabase posDatabase = DatabaseHelper.getDatabase(TimerService.this);
                    TransactionsDao transactionsDao = posDatabase.transactionsDao();
//                    Log.d("DIONEDATA", String.valueOf(transactionsDao.completedTransactionList().size()));
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

