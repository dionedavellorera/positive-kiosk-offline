package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.repository.TransactionsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TransactionsViewModel extends AndroidViewModel {

    private TransactionsRepository transactionsRepository;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);
        transactionsRepository = new TransactionsRepository(application);
    }


    public void insertPayment(List<Payments> paymentList) {
        transactionsRepository.insertPayment(paymentList);
    }


    public void insert(List<Transactions> transactionList) {
        transactionsRepository.insert(transactionList);
    }

    public void update(Transactions transaction) {
        transactionsRepository.update(transaction);
    }

    public void updateOrder(Orders order) {
        transactionsRepository.update(order);
    }

    public void updatePayment(Payments payments) {
        transactionsRepository.updatePayment(payments);
    }

    public void insertOrder(List<Orders> orderList) {
        transactionsRepository.insertOrder(orderList);
    }

//    public void insertOrder(Orders order) {
//        transactionsRepository.update(order);
//    }


    public LiveData<List<Transactions>> transactionLiveData() {
        return transactionsRepository.getTransactions();
    }

    public LiveData<List<Transactions>> savedTransactionLiveData() {
        return transactionsRepository.getSavedTransactions();
    }

    public List<TransactionWithOrders> completedTransactions() throws ExecutionException, InterruptedException {
        return transactionsRepository.getCompletedTransactions();
    }


    public LiveData<List<Orders>> ordersLiveData() {
        return transactionsRepository.getOrders();
    }

    public LiveData<List<Payments>> ldPaymentList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getLdPaymentList(transactionId);
    }


    public List<Payments> paymentList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getPaymentList(transactionId);
    }


    public List<Orders> orderList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getOrderList(transactionId);
    }

    public List<Orders> editingOrderList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getEdigintOrderList(transactionId);
    }


    public List<Transactions> transactionList() throws ExecutionException, InterruptedException {
        return transactionsRepository.getLatestTransaction();
    }

    public List<Transactions> loadedTransactionList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.loadedTransaction(transactionId);
    }

    public List<Transactions> loadedTransactionListViaControlNumber(String controlNumber) throws ExecutionException, InterruptedException {
        return transactionsRepository.loadedTransactionViaControlNumber(controlNumber);
    }




}
