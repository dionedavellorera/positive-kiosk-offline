package com.nerdvana.positiveoffline.view.settings;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.SettingsMenuAdapter;
import com.nerdvana.positiveoffline.intf.SettingsMenuContract;
import com.nerdvana.positiveoffline.model.RefreshMenuModel;
import com.nerdvana.positiveoffline.model.SettingsMenuModel;
import com.nerdvana.positiveoffline.view.printersettings.PrinterConnectionFragment;
import com.nerdvana.positiveoffline.view.printersettings.PrinterConnectionFragmentV2;
import com.nerdvana.positiveoffline.view.printersettings.PrinterSetupFragment;
import com.nerdvana.positiveoffline.view.printersettings.ReceiptSetupFragment;
import com.nerdvana.positiveoffline.view.servicechargesetting.ServiceChargeFragment;
import com.nerdvana.positiveoffline.viewmodel.ServiceChargeViewModel;
import com.nerdvana.positiveoffline.viewmodel.SettingsViewModel;
import com.squareup.otto.Subscribe;

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
        setTitle("Settings");
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
                        smm.setActive(true);
                        break;
                    case 2://SUNMI SETUP
                        if (!isEmulator()) {
                            openFragment(R.id.settingFrame, new PrinterConnectionFragmentV2());
                            smm.setActive(true);
                        } else {
                            Helper.showDialogMessage(SettingsActivity.this, "Please use a real device for this setting", "Information");
                        }


                        break;
                    case 3://RECEIPT SETUP
                        openFragment(R.id.settingFrame, new ReceiptSetupFragment());
                        smm.setActive(true);
                        break;
                    case 4://POS SYSTEM TYPE
                        openFragment(R.id.settingFrame, new SystemTypeFragment());
                        smm.setActive(true);
                        break;
                    case 5://SERVICE CHARGE SETUP
                        openFragment(R.id.settingFrame, new ServiceChargeFragment());
                        smm.setActive(true);
                        break;
                    case 6://PRINTER CONNECTION
                        if (!isEmulator()) {
                            openFragment(R.id.settingFrame, new PrinterConnectionFragment());
                            smm.setActive(true);
                        }  else {
                            Helper.showDialogMessage(SettingsActivity.this, "Please use a real device for this setting", "Information");
                        }

                        break;
                }

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

    private boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }


    @Subscribe
    public void refreshMenu(RefreshMenuModel refreshMenuModel) {
        setMenuAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
