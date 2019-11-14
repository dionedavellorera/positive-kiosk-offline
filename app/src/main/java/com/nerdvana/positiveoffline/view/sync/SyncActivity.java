package com.nerdvana.positiveoffline.view.sync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.MainActivity;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.SyncDataAdapter;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.background.InserUserAsync;
import com.nerdvana.positiveoffline.background.InsertProductAsync;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.intf.SyncDataAdapterListener;
import com.nerdvana.positiveoffline.model.ErrorDataModel;
import com.nerdvana.positiveoffline.view.login.LoginActivity;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.ProductsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SyncActivity extends AppCompatActivity implements View.OnClickListener, SyncCallback, SyncDataAdapterListener {

    private RecyclerView rvSyncData;
    private Button btnProceed;

    private UserViewModel userViewModel;
    private ProductsViewModel productsViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private List<DataSync> syncModelList;
    private SyncDataAdapter syncDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        initViews();
        initUserViewModel();
        initDataSyncViewModel();
        initProductViewModel();

        initUserListener();
        initDataSyncListener();
        initProductListener();

    }

    private void initDataSyncListener() {
        dataSyncViewModel.getmSyncData().observe(this, new Observer<List<DataSync>>() {
            @Override
            public void onChanged(List<DataSync> dataSyncList) {
                if (dataSyncList.size() < 1) {
                    syncModelList = new ArrayList<>();
                    syncModelList.add(new DataSync("Users", false));
                    syncModelList.add(new DataSync("Products", false));
                    dataSyncViewModel.insertData(syncModelList);
                } else {
                    syncModelList = dataSyncList;
                    setAdapter(dataSyncList);

                    if (!syncModelList.get(0).getSynced()) {
                        userViewModel.fetchUserRequest();
                    }

                    if (!syncModelList.get(1).getSynced()) {
                        productsViewModel.fetchProductsRequest();
                    }
                }

            }
        });
    }

    private void initDataSyncViewModel() {
        dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
    }

    private void initProductListener() {
        productsViewModel.getProductsLiveData().observe(this, new Observer<FetchProductsResponse>() {
            @Override
            public void onChanged(FetchProductsResponse fetchProductsResponse) {
                new InsertProductAsync(fetchProductsResponse.getResult(), SyncActivity.this, productsViewModel, SyncActivity.this).execute();
            }
        });
    }


    private void initProductViewModel() {
        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
    }

    private void initUserListener() {
        userViewModel.getUserResponseMutableData().observe(this, new Observer<FetchUserResponse>() {
            @Override
            public void onChanged(FetchUserResponse fetchUserResponse) {
                new InserUserAsync(fetchUserResponse.getResultList(), SyncActivity.this, userViewModel).execute();
            }
        });

    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void initViews() {
        btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);
        rvSyncData = findViewById(R.id.rvSyncData);

        syncModelList = new ArrayList<>();
    }

    private void setAdapter(List<DataSync> list) {
        LinearLayoutManager llm = new LinearLayoutManager(SyncActivity.this);
        syncDataAdapter = new SyncDataAdapter(list, this);
        rvSyncData.setAdapter(syncDataAdapter);
        rvSyncData.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvSyncData.getContext(),
                llm.getOrientation());
        rvSyncData.addItemDecoration(dividerItemDecoration);
        syncDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProceed:
                if (errorDataModel().isValid()) {
                    if (bundleString(AppConstants.ORIGIN).equalsIgnoreCase(AppConstants.ACTIVITY_LOGIN)) {
                        startActivity(new Intent(SyncActivity.this, MainActivity.class));
                        finish();
                    } else if (bundleString(AppConstants.ORIGIN).equalsIgnoreCase(AppConstants.VERIFY_MACHINE)) {
                        startActivity(new Intent(SyncActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    Helper.showDialogMessage(SyncActivity.this, errorDataModel().getErrorMessage(), getString(R.string.header_message));
                }
                break;
        }
    }

    @Override
    public void finishedLoading(String str) {
        switch (str) {
            case "user":
                syncModelList.get(0).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(0));
                break;
            case "products":
                syncModelList.get(1).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(1));
                break;
        }
    }

    private ErrorDataModel errorDataModel() {
        boolean isValid = true;
        String errorMessage = "Success";
        try {
            if (dataSyncViewModel.getUnsyncedData().size() > 0) {
                isValid = false;
                errorMessage = getApplicationContext().getString(R.string.error_message_data_not_sync);
            }

        } catch (ExecutionException e) {
            isValid = false;
            errorMessage = e.getLocalizedMessage();
        } catch (InterruptedException e) {
            isValid = false;
            errorMessage = e.getLocalizedMessage();
        }

        return new ErrorDataModel(errorMessage, isValid);
    }

    private String bundleString(String key) {
        String extra = "";
        if (getIntent().getExtras() != null) {
            extra = getIntent().getExtras().getString(key, "");
        }
        return extra;
    }

    @Override
    public void clicked(DataSync dataSync) {
        switch (dataSync.getTable().toLowerCase()) {
            case "user":
                syncModelList.get(0).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(0));
                break;
            case "products":
                syncModelList.get(1).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(1));
                break;
        }
    }
}
