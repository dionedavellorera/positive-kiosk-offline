package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.nerdvana.positiveoffline.model.SettingsMenuModel;

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
       return list;
    }
}
