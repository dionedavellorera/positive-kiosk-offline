package com.nerdvana.positiveoffline.view.sync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.MainActivity;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.SyncDataAdapter;
import com.nerdvana.positiveoffline.apirequests.TestRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchDiscountResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchRoomStatusResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.background.InserUserAsync;
import com.nerdvana.positiveoffline.background.InsertCashDenominationAsync;
import com.nerdvana.positiveoffline.background.InsertCreditCardAsync;
import com.nerdvana.positiveoffline.background.InsertDiscountsAsync;
import com.nerdvana.positiveoffline.background.InsertPaymentTypeAsync;
import com.nerdvana.positiveoffline.background.InsertPrinterLanguageAsync;
import com.nerdvana.positiveoffline.background.InsertPrinterSeriesAsync;
import com.nerdvana.positiveoffline.background.InsertProductAsync;
import com.nerdvana.positiveoffline.background.InsertRoomAsync;
import com.nerdvana.positiveoffline.background.InsertRoomStatusAsync;
import com.nerdvana.positiveoffline.background.InsertThemeSelectionAsync;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends AppCompatActivity implements View.OnClickListener, SyncCallback, SyncDataAdapterListener {

    private RecyclerView rvSyncData;
    private Button btnProceed;

    private UserViewModel userViewModel;
    private ProductsViewModel productsViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private List<DataSync> syncModelList;
    private SyncDataAdapter syncDataAdapter;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Migrating data...");
        initViews();

        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        TestRequest collectionRequest = new TestRequest("test");
        progressDialog.show();
        iUsers.repatchData(collectionRequest.getMapValue()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                initEverything();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                initEverything();
            }
        });



    }

    private void initEverything() {
        initUserViewModel();
        initDataSyncViewModel();
        initProductViewModel();
        initUserListener();
        initDataSyncListener();
        initProductListener();
        initPaymentTypeListener();
        initCreditCardListener();
        initCashDenoListener();
        initDiscountListener();
        initFetchRoomListener();
        initRoomStatusListener();
    }

    private void initDiscountListener() {
        dataSyncViewModel.getDiscountLiveData().observe(this, new Observer<FetchDiscountResponse>() {
            @Override
            public void onChanged(FetchDiscountResponse fetchDiscountResponse) {

                new InsertDiscountsAsync(fetchDiscountResponse.getResult(), SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
            }
        });
    }

    private void initCashDenoListener() {
        dataSyncViewModel.getCashDenoLiveData().observe(this, new Observer<FetchCashDenominationResponse>() {
            @Override
            public void onChanged(FetchCashDenominationResponse fetchCashDenominationResponse) {
                new InsertCashDenominationAsync(fetchCashDenominationResponse.getResultList(), SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
            }
        });

    }

    private void initRoomStatusListener() {
        dataSyncViewModel.getRoomStatusLiveData().observe(this, new Observer<FetchRoomStatusResponse>() {
            @Override
            public void onChanged(FetchRoomStatusResponse fetchRoomStatusResponse) {
                new InsertRoomStatusAsync(fetchRoomStatusResponse.getResult(), SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
            }
        });
    }

    private void initFetchRoomListener() {
        dataSyncViewModel.getFetchRoomLiveData().observe(this, new Observer<FetchRoomResponse>() {
            @Override
            public void onChanged(FetchRoomResponse fetchRoomResponse) {
                new InsertRoomAsync(fetchRoomResponse.getResult(), SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
            }
        });
    }

    private void initPaymentTypeListener() {
        dataSyncViewModel.getPaymentTypeLiveData().observe(this, new Observer<FetchPaymentTypeResponse>() {
            @Override
            public void onChanged(FetchPaymentTypeResponse fetchPaymentTypeResponse) {
                new InsertPaymentTypeAsync(fetchPaymentTypeResponse.getResult(), SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
            }
        });
    }

    private void initCreditCardListener() {
        dataSyncViewModel.getCreditCardLiveData().observe(this, new Observer<FetchCreditCardResponse>() {
            @Override
            public void onChanged(FetchCreditCardResponse fetchCreditCardResponse) {
                new InsertCreditCardAsync(fetchCreditCardResponse.getResult(), SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
            }
        });
    }

    private void initDataSyncListener() {
        dataSyncViewModel.getmSyncData().observe(this, new Observer<List<DataSync>>() {
            @Override
            public void onChanged(List<DataSync> dataSyncList) {
                if (dataSyncList.size() < 1) {
                    syncModelList = new ArrayList<>();
                    syncModelList.add(new DataSync("Users", false));
                    syncModelList.add(new DataSync("Products", false));
                    syncModelList.add(new DataSync("Payment Types", false));
                    syncModelList.add(new DataSync("Credit Cards", false));
                    syncModelList.add(new DataSync("Cash Denomination", false));
                    syncModelList.add(new DataSync("Discount with settings", false));
                    syncModelList.add(new DataSync("Printer Series", false));
                    syncModelList.add(new DataSync("Printer Language", false));
                    syncModelList.add(new DataSync("Rooms", false));
                    syncModelList.add(new DataSync("Room Status", false));
                    syncModelList.add(new DataSync("Theme Selection", false));

//                    syncModelList.add(new DataSync("End of Day", true));
//                    syncModelList.add(new DataSync("Posted Discounts", true));
//                    syncModelList.add(new DataSync("Payments", true));
//                    syncModelList.add(new DataSync("Orders", true));
//                    syncModelList.add(new DataSync("Order Discounts", true));
//                    syncModelList.add(new DataSync("Transactions", true));
//                    syncModelList.add(new DataSync("OR Details", true));
//                    syncModelList.add(new DataSync("Cut Off", true));

//                    syncModelList.add(new DataSync("Printer Language", false));
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

                    if (!syncModelList.get(2).getSynced()) {
                        dataSyncViewModel.requestPaymentType();
                    }

                    if (!syncModelList.get(3).getSynced()) {
                        dataSyncViewModel.requestCreditCards();
                    }

                    if (!syncModelList.get(4).getSynced()) {
                        dataSyncViewModel.requestCashDenomination();
                    }

                    if (!syncModelList.get(5).getSynced()) {
                        dataSyncViewModel.requestDiscounts();
                    }

                    if (!syncModelList.get(6).getSynced()) {
                        new InsertPrinterSeriesAsync(SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
                    }

                    if (!syncModelList.get(7).getSynced()) {
                        new InsertPrinterLanguageAsync(SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
                    }

                    if (!syncModelList.get(8).getSynced()) {
                        dataSyncViewModel.requestRoomsOrTables();
                    }

                    if (!syncModelList.get(9).getSynced()) {
                        dataSyncViewModel.fetchRoomStatus();
                    }

                    if (!syncModelList.get(10).getSynced()) {
                        new InsertThemeSelectionAsync(SyncActivity.this, dataSyncViewModel, SyncActivity.this).execute();
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
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvSyncData.getContext(),
//                llm.getOrientation());
//        rvSyncData.addItemDecoration(dividerItemDecoration);
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
            case "payment_type":
                syncModelList.get(2).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(2));
                break;
            case "credit_card":
                syncModelList.get(3).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(3));
                break;
            case "cash_denomination":
                syncModelList.get(4).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(4));
                break;
            case "discounts":
                syncModelList.get(5).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(5));
                break;
            case "printer_series":
                syncModelList.get(6).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(6));
                break;
            case "printer_language":
                syncModelList.get(7).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(7));
                break;
            case "rooms":
                syncModelList.get(8).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(8));
                break;
            case "room_status":
                syncModelList.get(9).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(9));
                break;
            case "theme_selection":
                syncModelList.get(10).setSynced(true);
                dataSyncViewModel.updateIsSynced(syncModelList.get(10));
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
            case "users":
                syncModelList.get(0).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(0));
                break;
            case "products":
                dataSyncViewModel.truncateProducts();
                syncModelList.get(1).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(1));
                break;
            case "payment types":
                syncModelList.get(2).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(2));
                break;
            case "credit cards":
                syncModelList.get(3).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(3));
                break;
            case "cash denomination":
                syncModelList.get(4).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(4));
                break;
            case "discount with settings":
                syncModelList.get(5).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(5));
                break;
            case "printer series":
                dataSyncViewModel.truncatePrinterSeries();
                syncModelList.get(6).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(6));
                break;
            case "printer language":
                dataSyncViewModel.truncatePrinterLanguage();
                syncModelList.get(7).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(7));
                break;
            case "rooms":
                syncModelList.get(8).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(8));
                break;
            case "room status":
                syncModelList.get(9).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(9));
                break;
            case "theme selection":
                dataSyncViewModel.truncateThemeSelection();
                syncModelList.get(10).setSynced(false);
                dataSyncViewModel.updateIsSynced(syncModelList.get(10));
                break;
        }
    }
}
