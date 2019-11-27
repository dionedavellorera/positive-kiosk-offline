package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.OrdersDao;
import com.nerdvana.positiveoffline.dao.PaymentsDao;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionsRepository {

    private TransactionsDao transactionsDao;
    private OrdersDao ordersDao;
    private PaymentsDao paymentsDao;
    private MutableLiveData<FetchProductsResponse> fetchProductLiveData;

    public TransactionsRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        transactionsDao = posDatabase.transactionsDao();
        ordersDao = posDatabase.ordersDao();
        paymentsDao = posDatabase.paymentsDao();

    }

    public LiveData<List<Transactions>> getTransactions() {
        return transactionsDao.ldTransactionsList();
    }

    public LiveData<List<Transactions>> getSavedTransactions() {
        return transactionsDao.ldSavedTransactionsList();
    }

    public List<Transactions> getUnCutOffTransactions() throws ExecutionException, InterruptedException {
        Callable<List<Transactions>> callable = new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return transactionsDao.unCutOffTransactions();
            }
        };

        Future<List<Transactions>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public Transactions getLastTransactionId() throws ExecutionException, InterruptedException {
        Callable<Transactions> callable = new Callable<Transactions>() {
            @Override
            public Transactions call() throws Exception {
                return transactionsDao.lastTransactionId();
            }
        };

        Future<Transactions> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public Transactions getLastOrNumber() throws ExecutionException, InterruptedException {
        Callable<Transactions> callable = new Callable<Transactions>() {
            @Override
            public Transactions call() throws Exception {
                return transactionsDao.lastOrNumber();
            }
        };

        Future<Transactions> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Transactions> getUnCutOffTransactionsByUser(final String userId) throws ExecutionException, InterruptedException {
        Callable<List<Transactions>> callable = new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return transactionsDao.unCutOffTransactionsByUser(userId);
            }
        };

        Future<List<Transactions>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<TransactionWithOrders> getCompletedTransactions() throws ExecutionException, InterruptedException {
        Callable<List<TransactionWithOrders>> callable = new Callable<List<TransactionWithOrders>>() {
            @Override
            public List<TransactionWithOrders> call() throws Exception {
                return transactionsDao.completedTransactionList();
            }
        };

        Future<List<TransactionWithOrders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

        public LiveData<List<Payments>> getLdPaymentList(String transactionId) {
        return paymentsDao.ldPaymentList(transactionId);
    }

    public LiveData<List<Orders>> getOrders() {
        return ordersDao.ldOrderList();
    }


    public List<Payments> paymentsList(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Payments>> callable = new Callable<List<Payments>>() {
            @Override
            public List<Payments> call() throws Exception {
                return paymentsDao.paymentList(transactionId);
            }
        };

        Future<List<Payments>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
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

    public List<Orders> getEdigintOrderList(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Orders>> callable = new Callable<List<Orders>>() {
            @Override
            public List<Orders> call() throws Exception {
                return ordersDao.editingOrderList(transactionId);
            }
        };

        Future<List<Orders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public List<Payments> getPaymentList(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Payments>> callable = new Callable<List<Payments>>() {
            @Override
            public List<Payments> call() throws Exception {
                return paymentsDao.paymentList(transactionId);
            }
        };

        Future<List<Payments>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public void update(Transactions transactions) {
        new TransactionsRepository.updateAsyncTask(transactionsDao, transactions).execute();
    }

    public void update(Orders orders) {
        new TransactionsRepository.updateOrderAsyncTask(ordersDao, orders).execute();
    }

    public void insert(List<Transactions> transactions) {
        new TransactionsRepository.insertAsyncTask(transactionsDao).execute(transactions);
    }

    public void insertPayment(List<Payments> payment) {
        new TransactionsRepository.insertPaymentAsyncTask(paymentsDao).execute(payment);
    }

    public void insertOrder(List<Orders> order) {
        new TransactionsRepository.insertAsyncTaskOrder(ordersDao).execute(order);
    }

    public void updatePayment(Payments payments) {
        new TransactionsRepository.updatePaymentAsyncTask(paymentsDao, payments).execute();
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

    public List<Transactions> loadedTransactionViaControlNumber(final String controlNumber) throws ExecutionException, InterruptedException {
        Callable<List<Transactions>> callable = new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return transactionsDao.loadedTransactionListViaControlNumber(controlNumber);
            }
        };

        Future<List<Transactions>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Transactions> loadedTransaction(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Transactions>> callable = new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return transactionsDao.loadedTransactionList(transactionId);
            }
        };

        Future<List<Transactions>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    private static class updateOrderAsyncTask extends AsyncTask<Transactions, Void, Void> {

        private OrdersDao mAsyncTaskDao;

        private Orders order;
        updateOrderAsyncTask(OrdersDao dao, Orders order) {
            mAsyncTaskDao = dao;
            this.order = order;
        }

        @Override
        protected Void doInBackground(final Transactions... params) {
            mAsyncTaskDao.update(order);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    private static class updatePaymentAsyncTask extends AsyncTask<Payments, Void, Void> {

        private PaymentsDao mAsyncTaskDao;

        private Payments payments;
        updatePaymentAsyncTask(PaymentsDao dao, Payments payments) {
            mAsyncTaskDao = dao;
            this.payments = payments;
        }

        @Override
        protected Void doInBackground(final Payments... params) {
            mAsyncTaskDao.update(payments);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
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


    private static class insertPaymentAsyncTask extends AsyncTask<List<Payments>, Void, Void> {

        private PaymentsDao mAsyncTaskDao;

        insertPaymentAsyncTask(PaymentsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Payments>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
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
