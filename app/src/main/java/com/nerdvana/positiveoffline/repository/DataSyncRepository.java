package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.apirequests.FetchUserRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.DataSync;
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
    private LiveData<List<DataSync>> allSyncList;

    private List<DataSync> syncList = new ArrayList<>();
    public DataSyncRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        dataSyncDao = posDatabase.dataSyncDao();
        allSyncList = dataSyncDao.syncList();

    }


    public LiveData<List<DataSync>> getAllSyncList() {
        return allSyncList;
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



}
