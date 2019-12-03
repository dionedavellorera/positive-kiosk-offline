package com.nerdvana.positiveoffline.view.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.SettingsMenuAdapter;
import com.nerdvana.positiveoffline.intf.SettingsMenuContract;
import com.nerdvana.positiveoffline.model.SettingsMenuModel;
import com.nerdvana.positiveoffline.view.printersettings.PrinterConnectionFragment;
import com.nerdvana.positiveoffline.view.printersettings.PrinterSetupFragment;
import com.nerdvana.positiveoffline.view.printersettings.ReceiptSetupFragment;
import com.nerdvana.positiveoffline.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SettingsMenuContract {

    private SettingsMenuAdapter settingsMenuAdapter;
    private RecyclerView rvSettingMenu;
    private List<SettingsMenuModel> settingsMenuModelList;
    private SettingsViewModel settingsViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        initSettingsViewModel();
        setMenuAdapter();

        openFragment(R.id.settingFrame, new PrinterSetupFragment());

    }

    private void initSettingsViewModel() {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    private void setMenuAdapter() {
        settingsMenuModelList = new ArrayList<>(settingsViewModel.settingsList());
        settingsMenuAdapter = new SettingsMenuAdapter(settingsMenuModelList, SettingsActivity.this, this);
        rvSettingMenu.setAdapter(settingsMenuAdapter);
        rvSettingMenu.setLayoutManager(new LinearLayoutManager(SettingsActivity.this));
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
                switch (smm.getId()) {
                    case 1://PRINTER SETUP
                        openFragment(R.id.settingFrame, new PrinterSetupFragment());
                        break;
                    case 2://PRINTER CONNECTION
                        openFragment(R.id.settingFrame, new PrinterConnectionFragment());
                        break;
                    case 3://RECEIPT SETUP
                        openFragment(R.id.settingFrame, new ReceiptSetupFragment());
                        break;
                }
                smm.setActive(true);
            } else {
                smm.setActive(false);
            }
            index++;
        }
        settingsMenuAdapter.notifyDataSetChanged();

    }

    private void openFragment(int container, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
    }


}
