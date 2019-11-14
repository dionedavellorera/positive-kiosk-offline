package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.OrdersDao;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionsRepository {

    private TransactionsDao transactionsDao;
    private OrdersDao ordersDao;
    private MutableLiveData<FetchProductsResponse> fetchProductLiveData;

    public TransactionsRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        transactionsDao = posDatabase.transactionsDao();
        ordersDao = posDatabase.ordersDao();

    }

    public LiveData<List<Transactions>> getTransactions() {
        return transactionsDao.ldTransactionsList();
    }

    public LiveData<List<Orders>> getOrders(String transactionId) {
        return ordersDao.ldOrderList(transactionId);
    }

    public List<Orders> getOrderList(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Orders>> callable = new Callable<List<Orders>>() {
            @Override
            public List<Orders> call() throws Exception {
                return ordersDao.orderList(transactionId);
            }
        };

        Future<List<Orders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public void update(Transactions transactions) {

        Log.d("ASYNC", String.valueOf(transactions.getIs_saved()));

        new TransactionsRepository.updateAsyncTask(transactionsDao, transactions).execute();
    }

    public void insert(List<Transactions> transactions) {
        new TransactionsRepository.insertAsyncTask(transactionsDao).execute(transactions);
    }

    public void insertOrder(List<Orders> order) {
        new TransactionsRepository.insertAsyncTaskOrder(ordersDao).execute(order);
    }

    public List<Transactions> getLatestTransaction() throws ExecutionException, InterruptedException {
        Callable<List<Transactions>> callable = new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return transactionsDao.transactionsList();
            }
        };

        Future<List<Transactions>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    private static class updateAsyncTask extends AsyncTask<Transactions, Void, Void> {

        private TransactionsDao mAsyncTaskDao;

        private Transactions transactions;
        updateAsyncTask(TransactionsDao dao, Transactions transactions) {
            mAsyncTaskDao = dao;
            this.transactions = transactions;
        }

        @Override
        protected Void doInBackground(final Transactions... params) {
            mAsyncTaskDao.update(transactions);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }



    private static class insertAsyncTask extends AsyncTask<List<Transactions>, Void, Void> {

        private TransactionsDao mAsyncTaskDao;

        insertAsyncTask(TransactionsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Transactions>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertAsyncTaskOrder extends AsyncTask<List<Orders>, Void, Void> {

        private OrdersDao mAsyncTaskDao;

        insertAsyncTaskOrder(OrdersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Orders>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }



}
