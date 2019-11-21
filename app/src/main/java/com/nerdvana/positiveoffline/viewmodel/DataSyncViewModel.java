package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.repository.DataSyncRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataSyncViewModel extends AndroidViewModel {

    private DataSyncRepository mRepository;
    private LiveData<List<DataSync>> mSyncData;

    public DataSyncViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataSyncRepository(application);
        mSyncData = mRepository.getAllSyncList();
    }


    public LiveData<List<DataSync>> getmSyncData() {
        return mSyncData;
    }

    public List<DataSync> getUnsyncedData() throws ExecutionException, InterruptedException {
        return mRepository.getUnsyncedList();
    }

    public void updateIsSynced(DataSync dataSync) {
        mRepository.update(dataSync);
    }

    public void insertData(List<DataSync> list) {
        mRepository.insert(list);
    }

    public void requestCashDenomination() {
        mRepository.fetchCashDenominationRequest();
    }

    public MutableLiveData<FetchCashDenominationResponse> getCashDenoLiveData() {
        return mRepository.getFetchCashDenominationLiveData();
    }

    public List<CashDenomination> getCashDeno() throws ExecutionException, InterruptedException {
        return mRepository.getCashDenoList();
    }


    public void requestPaymentType() {
        mRepository.fetchPaymentTypeRequest();
    }

    public void requestCreditCards() {
        mRepository.fetchCreditCardRequest();
    }

    public MutableLiveData<FetchPaymentTypeResponse> getPaymentTypeLiveData() {
        return mRepository.getFetchPaymentTypeLiveData();
    }

    public MutableLiveData<FetchCreditCardResponse> getCreditCardLiveData() {
        return mRepository.getFetchCreditCarDLiveData();
    }

    public List<PaymentTypes> getPaymentTypeList() throws ExecutionException, InterruptedException {
        return mRepository.getPaymentTypeList();
    }

    public List<CreditCards> getCreditCardList() throws ExecutionException, InterruptedException {
        return mRepository.getCreditCardList();
    }


    public void insertPaymentType(List<PaymentTypes> list) {
        mRepository.insertPaymentType(list);
    }

    public void insertCreditCard(List<CreditCards> list) {
        mRepository.insertCreditCard(list);
    }

    public void insertCashDenomination(List<CashDenomination> list) {
        mRepository.insertCashDenomination(list);
    }

}
