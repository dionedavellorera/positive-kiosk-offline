package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.SettingsMenuAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.intf.SettingsMenuContract;
import com.nerdvana.positiveoffline.model.SettingsMenuModel;
import com.nerdvana.positiveoffline.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsDialog extends BaseDialog implements SettingsMenuContract {

    private SettingsMenuAdapter settingsMenuAdapter;
    private RecyclerView rvSettingMenu;
    private List<SettingsMenuModel> settingsMenuModelList;
    private SettingsViewModel settingsViewModel;
    public SettingsDialog(Context context, SettingsViewModel settingsViewModel) {
        super(context);
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_settings, "SETTINGS");
        initViews();
        setMenuAdapter();
    }

    private void setMenuAdapter() {
        settingsMenuModelList = new ArrayList<>(settingsViewModel.settingsList());
        settingsMenuAdapter = new SettingsMenuAdapter(settingsMenuModelList, SettingsDialog.this, getContext());
        rvSettingMenu.setAdapter(settingsMenuAdapter);
        rvSettingMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        settingsMenuAdapter.notifyDataSetChanged();
    }


    private void initViews() {
        rvSettingMenu = findViewById(R.id.rvSettingMenu);
        settingsMenuModelList = new ArrayList<>();
    }


    @Override
    public void clicked(SettingsMenuModel settingsMenuModel, int position) {
        int index = 0;
        for (SettingsMenuModel smm : settingsMenuModelList) {
            if (index == position) {
                smm.setActive(true);
            } else {
                smm.setActive(false);
            }
            index++;
        }
        settingsMenuAdapter.notifyDataSetChanged();

    }
}
