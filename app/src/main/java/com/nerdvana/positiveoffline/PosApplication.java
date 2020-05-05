package com.nerdvana.positiveoffline;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.squareup.otto.Bus;

import net.danlew.android.joda.JodaTimeAndroid;

public class PosApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Bus mBus = BusProvider.getInstance();
        PosManager autopilotManager = new PosManager(this, mBus);
        mBus.register(autopilotManager);
        new UserServices(this);
        new SharedPreferenceManager(this);
        JodaTimeAndroid.init(this);
        new GsonHelper();
        Stetho.initializeWithDefaults(this);

        FirebaseAnalytics.getInstance(this);
    }
}
