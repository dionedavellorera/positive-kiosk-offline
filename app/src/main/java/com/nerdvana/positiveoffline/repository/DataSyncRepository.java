package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.apirequests.FetchCreditCardRequest;
import com.nerdvana.positiveoffline.apirequests.FetchPaymentTypeRequest;
import com.nerdvana.positiveoffline.apirequests.FetchProductsRequest;
import com.nerdvana.positiveoffline.apirequests.FetchUserRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.dao.CreditCardsDao;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.PaymentTypeDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSyncRepository {

    private DataSyncDao dataSyncDao;
    private PaymentTypeDao paymentTypeDao;
    private CreditCardsDao creditCardsDao;
    private LiveData<List<DataSync>> allSyncList;

    private List<DataSync> syncList = new ArrayList<>();

    private MutableLiveData<FetchPaymentTypeResponse> fetchPaymentTypeLiveData;
    private MutableLiveData<FetchCreditCardResponse> fetchCreditCarDLiveData;
    public DataSyncRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        dataSyncDao = posDatabase.dataSyncDao();
        paymentTypeDao = posDatabase.paymentTypeDao();
        creditCardsDao = posDatabase.creditCardsDao();
        allSyncList = dataSyncDao.syncList();

        fetchPaymentTypeLiveData = new MutableLiveData<>();
        fetchCreditCarDLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<FetchPaymentTypeResponse> getFetchPaymentTypeLiveData() {
        return fetchPaymentTypeLiveData;
    }

    public MutableLiveData<FetchCreditCardResponse> getFetchCreditCarDLiveData() {
        return fetchCreditCarDLiveData;
    }

    public LiveData<List<DataSync>> getAllSyncList() {
        return allSyncList;
    }

    public List<CreditCards> getCreditCardList() throws ExecutionException, InterruptedException {
        Callable<List<CreditCards>> callable = new Callable<List<CreditCards>>() {
            @Override
            public List<CreditCards> call() throws Exception {
                return creditCardsDao.creditCardList();
            }
        };

        Future<List<CreditCards>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public List<PaymentTypes> getPaymentTypeList() throws ExecutionException, InterruptedException {
        Callable<List<PaymentTypes>> callable = new Callable<List<PaymentTypes>>() {
            @Override
            public List<PaymentTypes> call() throws Exception {
                return paymentTypeDao.paymentTypeList();
            }
        };

        Future<List<PaymentTypes>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<DataSync> getUnsyncedList() throws ExecutionException, InterruptedException {
        Callable<List<DataSync>> callable = new Callable<List<DataSync>>() {
            @Override
            public List<DataSync> call() throws Exception {
                return dataSyncDao.unsyncList();
            }
        };

        Future<List<DataSync>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public void insertPaymentType(List<PaymentTypes> paymentTypes) {
        new DataSyncRepository.insertPaymentTypeAsyncTask(paymentTypeDao).execute(paymentTypes);
    }

    public void insertCreditCard(List<CreditCards> creditCards) {
        new DataSyncRepository.insertCreditCardAsync(creditCardsDao).execute(creditCards);
    }

    public void insert(List<DataSync> dataSync) {
        new DataSyncRepository.insertAsyncTask(dataSyncDao).execute(dataSync);
    }

    public void update(DataSync dataSync) {
        new DataSyncRepository.updateAsyncTask(dataSyncDao, dataSync).execute();
    }

    private static class UnsyncedAsyncTask extends AsyncTask<List<DataSync>, Void, List<DataSync>> {

        private DataSyncDao mAsyncTaskDao;

        UnsyncedAsyncTask(DataSyncDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected List<DataSync> doInBackground(List<DataSync>... lists) {
            return mAsyncTaskDao.unsyncList();
        }
    }

    private static class insertCreditCardAsync extends AsyncTask<List<CreditCards>, Void, Void> {

        private CreditCardsDao mAsyncTaskDao;

        insertCreditCardAsync(CreditCardsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<CreditCards>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertPaymentTypeAsyncTask extends AsyncTask<List<PaymentTypes>, Void, Void> {

        private PaymentTypeDao mAsyncTaskDao;

        insertPaymentTypeAsyncTask(PaymentTypeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<PaymentTypes>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<List<DataSync>, Void, Void> {

        private DataSyncDao mAsyncTaskDao;

        insertAsyncTask(DataSyncDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<DataSync>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class updateAsyncTask extends AsyncTask<List<DataSync>, Void, Void> {

        private DataSyncDao mAsyncTaskDao;

        private DataSync dataSync;
        updateAsyncTask(DataSyncDao dao, DataSync dataSync) {
            mAsyncTaskDao = dao;
            this.dataSync = dataSync;
        }

        @Override
        protected Void doInBackground(final List<DataSync>... params) {
            mAsyncTaskDao.update(dataSync);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }



    public void fetchPaymentTypeRequest() {
        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        FetchPaymentTypeRequest req = new FetchPaymentTypeRequest();

        Call<FetchPaymentTypeResponse> call = iUsers.fetchPaymentTypeRequest(req.getMapValue());
        call.enqueue(new Callback<FetchPaymentTypeResponse>() {
            @Override
            public void onResponse(Call<FetchPaymentTypeResponse> call, Response<FetchPaymentTypeResponse> response) {
                fetchPaymentTypeLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<FetchPaymentTypeResponse> call, Throwable t) {

            }
        });
    }

    public void fetchCreditCardRequest() {
        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        FetchCreditCardRequest req = new FetchCreditCardRequest();

        Call<FetchCreditCardResponse> call = iUsers.fetchCreditCardRequest(req.getMapValue());
        call.enqueue(new Callback<FetchCreditCardResponse>() {
            @Override
            public void onResponse(Call<FetchCreditCardResponse> call, Response<FetchCreditCardResponse> response) {
                fetchCreditCarDLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<FetchCreditCardResponse> call, Throwable t) {

            }
        });
    }


}
