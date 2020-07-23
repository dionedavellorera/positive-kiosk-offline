package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.dao.OrDetailsDao;
import com.nerdvana.positiveoffline.dao.OrderDiscountsDao;
import com.nerdvana.positiveoffline.dao.OrdersDao;
import com.nerdvana.positiveoffline.dao.PaymentsDao;
import com.nerdvana.positiveoffline.dao.PayoutDao;
import com.nerdvana.positiveoffline.dao.PostedDiscountsDao;
import com.nerdvana.positiveoffline.dao.ProductsDao;
import com.nerdvana.positiveoffline.dao.SerialNumbersDao;
import com.nerdvana.positiveoffline.dao.TransactionsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.entities.SerialNumbers;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.AsyncSaveContract;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionsRepository {
    private PostedDiscountsDao postedDiscountsDao;
    private OrderDiscountsDao orderDiscountsDao;
    private TransactionsDao transactionsDao;
    private OrdersDao ordersDao;
    private PaymentsDao paymentsDao;
    private OrDetailsDao orDetailsDao;
    private ProductsDao productsDao;
    private PayoutDao payoutDao;
    private SerialNumbersDao serialNumbersDao;
    private MutableLiveData<FetchProductsResponse> fetchProductLiveData;

    public TransactionsRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        transactionsDao = posDatabase.transactionsDao();
        ordersDao = posDatabase.ordersDao();
        productsDao = posDatabase.productsDao();
        paymentsDao = posDatabase.paymentsDao();
        orDetailsDao = posDatabase.orDetailsDao();
        payoutDao = posDatabase.payoutDao();
        serialNumbersDao = posDatabase.serialNumbersDao();

        postedDiscountsDao = posDatabase.postedDiscountsDao();
        orderDiscountsDao = posDatabase.orderDiscountsDao();


    }


    public LiveData<List<Transactions>> getTransactionsTo() {
        return transactionsDao.ldTransactionsListTo();
    }


    public LiveData<List<Transactions>> getTransactions() {
        return transactionsDao.ldTransactionsList();
    }

    public LiveData<List<Transactions>> getSavedTransactions() {
        return transactionsDao.ldSavedTransactionsList();
    }

    public List<BranchGroup> getBranchGroup(final String productId) throws ExecutionException, InterruptedException {
        Callable<List<BranchGroup>> callable = new Callable<List<BranchGroup>>() {
            @Override
            public List<BranchGroup> call() throws Exception {
                return productsDao.getBranchGroup(productId);
            }
        };

        Future<List<BranchGroup>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<BranchGroup> getBranchGroupViaProductIdAndProductGroupId(final String productId, final String productGroupId) throws ExecutionException, InterruptedException {
        Callable<List<BranchGroup>> callable = new Callable<List<BranchGroup>>() {
            @Override
            public List<BranchGroup> call() throws Exception {
                return productsDao.getBranchGroupViaProductIdAndProductGroupId(productId, productGroupId);
            }
        };

        Future<List<BranchGroup>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }



    public List<ProductAlacart> getAlaCartExisting(final String productId, final String productGroupId) throws ExecutionException, InterruptedException {
        Callable<List<ProductAlacart>> callable = new Callable<List<ProductAlacart>>() {
            @Override
            public List<ProductAlacart> call() throws Exception {
                return productsDao.getAlaCartExisting(productId, productGroupId);
            }
        };

        Future<List<ProductAlacart>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<BranchGroup> getFilteredProductsPerCategory(final String branch_group_id) throws ExecutionException, InterruptedException {
        Callable<List<BranchGroup>> callable = new Callable<List<BranchGroup>>() {
            @Override
            public List<BranchGroup> call() throws Exception {
                return productsDao.getFilteredProductsPerCategory(branch_group_id);
            }
        };

        Future<List<BranchGroup>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }




    public List<ProductAlacart> getBranchAlacart(final String productId) throws ExecutionException, InterruptedException {
        Callable<List<ProductAlacart>> callable = new Callable<List<ProductAlacart>>() {
            @Override
            public List<ProductAlacart> call() throws Exception {
                return productsDao.getBranchAlacart(productId);
            }
        };

        Future<List<ProductAlacart>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }




    public List<TransactionWithOrders> transactionListWithRoom() throws ExecutionException, InterruptedException {
        Callable<List<TransactionWithOrders>> callable = new Callable<List<TransactionWithOrders>>() {
            @Override
            public List<TransactionWithOrders> call() throws Exception {
                return transactionsDao.transactionListWithRoom();
            }
        };

        Future<List<TransactionWithOrders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<TransactionWithOrders> savedTransactionsList() throws ExecutionException, InterruptedException {
        Callable<List<TransactionWithOrders>> callable = new Callable<List<TransactionWithOrders>>() {
            @Override
            public List<TransactionWithOrders> call() throws Exception {
                return transactionsDao.savedTransactionsList();
            }
        };

        Future<List<TransactionWithOrders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
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


    public Payout getLastPayoutSeries() throws ExecutionException, InterruptedException {
        Callable<Payout> callable = new Callable<Payout>() {
            @Override
            public Payout call() throws Exception {
                return payoutDao.lastPayoutSeries();
            }
        };

        Future<Payout> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }



    public Transactions getExistingToTransaction(String to_transaction_id, String machine_id) throws ExecutionException, InterruptedException {
        Callable<Transactions> callable = new Callable<Transactions>() {
            @Override
            public Transactions call() throws Exception {
                return transactionsDao.getExistingToTransaction(to_transaction_id, machine_id);
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

    public List<TransactionWithOrders> getCompletedTransactions(final String startDate, final String endDate) throws ExecutionException, InterruptedException {
        Callable<List<TransactionWithOrders>> callable = new Callable<List<TransactionWithOrders>>() {
            @Override
            public List<TransactionWithOrders> call() throws Exception {
                return transactionsDao.completedTransactionList(startDate, endDate);
            }
        };

        Future<List<TransactionWithOrders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public LiveData<List<Payments>> getLdPaymentList(String transactionId) {
        return paymentsDao.ldPaymentList(transactionId);
    }

    public LiveData<List<Payments>> getLdUnredeemedPaymentList() {
        return paymentsDao.ldUnredeemedPaymentList();
    }




    public LiveData<List<Orders>> getOrders() {
        return ordersDao.ldOrderList();
    }



    public List<Payments> getUnredeemedPaymentList() throws ExecutionException, InterruptedException {
        Callable<List<Payments>> callable = new Callable<List<Payments>>() {
            @Override
            public List<Payments> call() throws Exception {
                return paymentsDao.getUnredeemedPaymentList();
            }
        };

        Future<List<Payments>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
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

    public List<Orders> getRoomRateList(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Orders>> callable = new Callable<List<Orders>>() {
            @Override
            public List<Orders> call() throws Exception {
                return ordersDao.roomRateList(transactionId);
            }
        };

        Future<List<Orders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Orders> getBundledItems(final String product_id) throws ExecutionException, InterruptedException {
        Callable<List<Orders>> callable = new Callable<List<Orders>>() {
            @Override
            public List<Orders> call() throws Exception {
                return ordersDao.getBundledItems(product_id);
            }
        };

        Future<List<Orders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public List<Orders> orderListWithoutBundle(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Orders>> callable = new Callable<List<Orders>>() {
            @Override
            public List<Orders> call() throws Exception {
                return ordersDao.orderListWithoutBundle(transactionId);
            }
        };

        Future<List<Orders>> future = Executors.newSingleThreadExecutor().submit(callable);
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


    public List<Orders> getAllOrders() throws ExecutionException, InterruptedException {
        Callable<List<Orders>> callable = new Callable<List<Orders>>() {
            @Override
            public List<Orders> call() throws Exception {
                return ordersDao.getAllOrders();
            }
        };

        Future<List<Orders>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Orders> getOrderListWithAsset(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<Orders>> callable = new Callable<List<Orders>>() {
            @Override
            public List<Orders> call() throws Exception {
                return ordersDao.orderListWithFixedAsset(transactionId);
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

    public List<SerialNumbers> getSerialNumberFromTransaction(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<List<SerialNumbers>> callable = new Callable<List<SerialNumbers>>() {
            @Override
            public List<SerialNumbers> call() throws Exception {
                return serialNumbersDao.serialNumbersList(transactionId);
            }
        };

        Future<List<SerialNumbers>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<SerialNumbers> getSerialNumbers() throws ExecutionException, InterruptedException {
        Callable<List<SerialNumbers>> callable = new Callable<List<SerialNumbers>>() {
            @Override
            public List<SerialNumbers> call() throws Exception {
                return serialNumbersDao.getSerialNumbers();
            }
        };

        Future<List<SerialNumbers>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }



    public List<SerialNumbers> getSerialNumberFromOrderId(final int orderId) throws ExecutionException, InterruptedException {
        Callable<List<SerialNumbers>> callable = new Callable<List<SerialNumbers>>() {
            @Override
            public List<SerialNumbers> call() throws Exception {
                return serialNumbersDao.serialNumbersListOrderId(orderId);
            }
        };

        Future<List<SerialNumbers>> future = Executors.newSingleThreadExecutor().submit(callable);
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

    public List<Payments> getAllPayments() throws ExecutionException, InterruptedException {
        Callable<List<Payments>> callable = new Callable<List<Payments>>() {
            @Override
            public List<Payments> call() throws Exception {
                return paymentsDao.getAllPayments();
            }
        };

        Future<List<Payments>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Payout> getPayoutList() throws ExecutionException, InterruptedException {
        Callable<List<Payout>> callable = new Callable<List<Payout>>() {
            @Override
            public List<Payout> call() throws Exception {
                return payoutDao.getPayoutList();
            }
        };

        Future<List<Payout>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }



    public void updateEditingOrderList(String transaction_id) {
        new TransactionsRepository.updateEditingOrderAsync(ordersDao, transaction_id).execute();
    }

    public void updateSerialNumbers(SerialNumbers serialNumbers) {
        new TransactionsRepository.updateSerialNumbersAsyncTask(serialNumbersDao, serialNumbers).execute();
    }

    public void update(Transactions transactions) {
        new TransactionsRepository.updateAsyncTask(transactionsDao, transactions).execute();
    }

    public Integer updateTransactionDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return new TransactionsRepository
                .updateTransactionDataToSentAsync(
                        transactionsDao,
                        transactionId
        ).execute().get();
    }

    public Integer updateOrdersDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return new TransactionsRepository
                .updateOrdersDataToSentAsync(
                ordersDao,
                transactionId
        ).execute().get();
    }

    public Integer updateOrderDiscountsDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return new TransactionsRepository
                .updateOrderDiscountsDataToSentAsync(
                orderDiscountsDao,
                transactionId
        ).execute().get();
    }



    public Integer updateSerialNumbersToSent(String transactionId) throws ExecutionException, InterruptedException {
        return new TransactionsRepository
                .updateSerialNumbersToSentAsync(
                serialNumbersDao,
                transactionId
        ).execute().get();
    }

    public Integer updatePostedDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return new TransactionsRepository
                .updatePostedDataToSentAsync(
                postedDiscountsDao,
                transactionId
        ).execute().get();
    }


    public Integer updateLong(Transactions transactions) throws ExecutionException, InterruptedException {
        return new TransactionsRepository.updateLongAsyncTask(transactionsDao, transactions).execute().get();
    }


    public void insertOrDetails(OrDetails orDetails) {
        new TransactionsRepository.insertOrDetailsAsyncTask(orDetailsDao).execute(orDetails);
    }
    public void update(Orders orders) {
        new TransactionsRepository.updateOrderAsyncTask(ordersDao, orders).execute();
    }

    public void insert(List<Transactions> transactions) {
        new TransactionsRepository.insertAsyncTask(transactionsDao).execute(transactions);
    }

    public long insertTransactionWaitData(Transactions transactions) throws ExecutionException, InterruptedException {
        return new TransactionsRepository.insertAsyncTaskWaitData(transactionsDao).execute(transactions).get();
    }

    public void insertSerialNumbers(SerialNumbers serialNumbersList) {
        new TransactionsRepository.insertSerialNumbersAsyncTask(serialNumbersDao).execute(serialNumbersList);
    }

    public void insertPayment(List<Payments> payment) {
        new TransactionsRepository.insertPaymentAsyncTask(paymentsDao).execute(payment);
    }

    public void insertPayout(Payout payout) {
        new TransactionsRepository.insertPayoutAsyncTask(payoutDao).execute(payout);
    }

    public void insertOrder(List<Orders> order) {
        new TransactionsRepository.insertAsyncTaskOrder(ordersDao).execute(order);
    }



    public void updatePayment(Payments payments) {
        new TransactionsRepository.updatePaymentAsyncTask(paymentsDao, payments).execute();
    }


    public OrDetails getOrDetails(final String transactionId) throws ExecutionException, InterruptedException {
        Callable<OrDetails> callable = new Callable<OrDetails>() {
            @Override
            public OrDetails call() throws Exception {
                return orDetailsDao.orDetails(transactionId);
            }
        };

        Future<OrDetails> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<OrDetails> getAllOrDetails() throws ExecutionException, InterruptedException {
        Callable<List<OrDetails>> callable = new Callable<List<OrDetails>>() {
            @Override
            public List<OrDetails> call() throws Exception {
                return orDetailsDao.getAllOrDetails();
            }
        };

        Future<List<OrDetails>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
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


    public List<Transactions> transactionListFromTo() throws ExecutionException, InterruptedException {
        Callable<List<Transactions>> callable = new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return transactionsDao.transactionListFromTo();
            }
        };

        Future<List<Transactions>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public List<Transactions> getAllTransactions() throws ExecutionException, InterruptedException {
        Callable<List<Transactions>> callable = new Callable<List<Transactions>>() {
            @Override
            public List<Transactions> call() throws Exception {
                return transactionsDao.getAllTransactions();
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


    public Transactions getTransactionViaTransactionIdOnly(final String transaction_id) throws ExecutionException, InterruptedException {
        Callable<Transactions> callable = new Callable<Transactions>() {
            @Override
            public Transactions call() throws Exception {
                return transactionsDao.getTransactionViaTransactionIdOnly(transaction_id);
            }
        };

        Future<Transactions> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public TransactionCompleteDetails getTransactionViaTransactionId(final String transaction_id) throws ExecutionException, InterruptedException {
        Callable<TransactionCompleteDetails> callable = new Callable<TransactionCompleteDetails>() {
            @Override
            public TransactionCompleteDetails call() throws Exception {
                return transactionsDao.getTransactionViaTransactionId(transaction_id);
            }
        };

        Future<TransactionCompleteDetails> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public Transactions getTransactionViaToTransactionId(final String transaction_id) throws ExecutionException, InterruptedException {
        Callable<Transactions> callable = new Callable<Transactions>() {
            @Override
            public Transactions call() throws Exception {
                return transactionsDao.getTransactionViaToTransactionId(transaction_id);
            }
        };

        Future<Transactions> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }




    public TransactionCompleteDetails getTransaction(final String receiptNumber) throws ExecutionException, InterruptedException {
        Callable<TransactionCompleteDetails> callable = new Callable<TransactionCompleteDetails>() {
            @Override
            public TransactionCompleteDetails call() throws Exception {
                return transactionsDao.getTransactionViaReceiptNumber(receiptNumber);
            }
        };

        Future<TransactionCompleteDetails> future = Executors.newSingleThreadExecutor().submit(callable);
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


    public List<OrDetails> getAllSavedOr() throws ExecutionException, InterruptedException {
        Callable<List<OrDetails>> callable = new Callable<List<OrDetails>>() {
            @Override
            public List<OrDetails> call() throws Exception {
                return orDetailsDao.getAllSavedOr();
            }
        };

        Future<List<OrDetails>> future = Executors.newSingleThreadExecutor().submit(callable);
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


    private static class updateSerialNumbersAsyncTask extends AsyncTask<SerialNumbers, Void, Void> {

            private SerialNumbersDao mAsyncTaskDao;

            private SerialNumbers serialNumbers;
            updateSerialNumbersAsyncTask(SerialNumbersDao dao, SerialNumbers serialNumbers) {
                mAsyncTaskDao = dao;
                this.serialNumbers = serialNumbers;
            }

            @Override
            protected Void doInBackground(final SerialNumbers... params) {
                mAsyncTaskDao.update(serialNumbers);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

    private static class updateLongAsyncTask extends AsyncTask<Transactions, Void, Integer> {
        private TransactionsDao mAsyncTaskDao;
        private Transactions transactions;
        updateLongAsyncTask(TransactionsDao dao, Transactions transactions) {
            mAsyncTaskDao = dao;
            this.transactions = transactions;
        }


        @Override
        protected Integer doInBackground(Transactions... tr) {
            return (int)mAsyncTaskDao.updateLong(transactions);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
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


    private static class updateTransactionDataToSentAsync extends AsyncTask<Void, Void, Integer> {

        private TransactionsDao transactionsDao;
        private String transactionId;
        updateTransactionDataToSentAsync(TransactionsDao transactionsDao, String transactionId) {
            this.transactionsDao = transactionsDao;
            this.transactionId = transactionId;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return transactionsDao.updateSentToServer(transactionId);
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
        }
    }
//2
    private static class updateOrdersDataToSentAsync extends AsyncTask<Void, Void, Integer> {

        private OrdersDao ordersDao;
        private String transactionId;
    updateOrdersDataToSentAsync(OrdersDao ordersDao, String transactionId) {
            this.ordersDao = ordersDao;
            this.transactionId = transactionId;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return ordersDao.updateSentToServer(transactionId);
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    //3
    private static class updateOrderDiscountsDataToSentAsync extends AsyncTask<Void, Void, Integer> {

        private OrderDiscountsDao orderDiscountsDao;
        private String transactionId;
        updateOrderDiscountsDataToSentAsync(OrderDiscountsDao orderDiscountsDao, String transactionId) {
            this.orderDiscountsDao = orderDiscountsDao;
            this.transactionId = transactionId;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return orderDiscountsDao.updateSentToServer(transactionId);
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    //4
    private static class updatePostedDataToSentAsync extends AsyncTask<Void, Void, Integer> {

        private PostedDiscountsDao postedDiscountsDao;
        private String transactionId;
        updatePostedDataToSentAsync(PostedDiscountsDao postedDiscountsDao, String transactionId) {
            this.postedDiscountsDao = postedDiscountsDao;
            this.transactionId = transactionId;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return postedDiscountsDao.updateSentToServer(transactionId);
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    //5
    private static class updateSerialNumbersToSentAsync extends AsyncTask<Void, Void, Integer> {

        private SerialNumbersDao serialNumbersDao;
        private String transactionId;
        updateSerialNumbersToSentAsync(SerialNumbersDao serialNumbersDao, String transactionId) {
            this.serialNumbersDao = serialNumbersDao;
            this.transactionId = transactionId;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return serialNumbersDao.updateSentToServer(transactionId);
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
        }
    }




    private static class insertPayoutAsyncTask extends AsyncTask<Payout, Void, Void> {

        private PayoutDao mAsyncTaskDao;

        insertPayoutAsyncTask(PayoutDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Payout... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }



    private static class insertSerialNumbersAsyncTask extends AsyncTask<SerialNumbers, Void, Void> {

        private SerialNumbersDao mAsyncTaskDao;

        insertSerialNumbersAsyncTask(SerialNumbersDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SerialNumbers... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
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


    private static class insertOrDetailsAsyncTask extends AsyncTask<OrDetails, Void, Void> {

        private OrDetailsDao mAsyncTaskDao;

        insertOrDetailsAsyncTask(OrDetailsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(OrDetails... orDetails) {
            mAsyncTaskDao.insert(orDetails[0]);
            return null;
        }
    }

    private static class updateEditingOrderAsync extends AsyncTask<List<Transactions>, Void, Void> {

        private OrdersDao mAsyncTaskDao;
        private String transactionId;
        updateEditingOrderAsync(OrdersDao dao, String transactionId) {
            mAsyncTaskDao = dao;
            this.transactionId = transactionId;
        }

        @Override
        protected Void doInBackground(final List<Transactions>... params) {
            mAsyncTaskDao.removeEditingOrders(transactionId);
            return null;
        }
    }





    private static class insertAsyncTaskWaitData extends AsyncTask<Transactions, Void, Long> {
        private TransactionsDao mAsyncTaskDao;

        insertAsyncTaskWaitData(TransactionsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(Transactions... transactions) {
            return mAsyncTaskDao.insert(transactions[0]);
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
