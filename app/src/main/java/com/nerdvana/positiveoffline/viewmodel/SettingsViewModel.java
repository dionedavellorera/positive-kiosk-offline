package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.model.SettingsMenuModel;
import com.nerdvana.positiveoffline.view.rooms.RoomsActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingsViewModel extends AndroidViewModel {

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public List<SettingsMenuModel> settingsList() {
       List<SettingsMenuModel> list = new ArrayList<>();
       list.add(new SettingsMenuModel(1, "PRINTER SETUP", true));
       list.add(new SettingsMenuModel(2, "PRINTER CONNECTION", false));
       list.add(new SettingsMenuModel(3, "RECEIPT SETUP", false));
       list.add(new SettingsMenuModel(4, "SYSTEM TYPE", false));

        Log.d("QWEQEQ", SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE));
        if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
            list.add(new SettingsMenuModel(5, "SERVICE CHARGE SETUP", false));
        } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
            list.add(new SettingsMenuModel(5, "SERVICE CHARGE SETUP", false));
        }

       return list;
    }
}
