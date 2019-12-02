package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Query;

import com.facebook.stetho.common.Util;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.repository.TransactionsRepository;

import java.util.ArrayList;
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

    public void recomputeTransaction(List<Orders> orderList, String transactionId) {
        Double grossSales = 0.00;
        Double netSales = 0.00;
        Double vatableSales = 0.00;
        Double vatExemptSales = 0.00;
        Double vatAmount = 0.00;
        Double discountAmount = 0.00;

        try {
            for (Orders owd : orderList) {

                grossSales += (Utils.roundedOffTwoDecimal(owd.getVatAmount() + owd.getVatable() + owd.getVatExempt()));
                netSales += (Utils.roundedOffTwoDecimal((owd.getVatable() + owd.getVatExempt()) - owd.getDiscountAmount()));
                vatableSales += Utils.roundedOffTwoDecimal(owd.getVatable());
                vatExemptSales += Utils.roundedOffTwoDecimal(owd.getVatExempt());
                vatAmount += Utils.roundedOffTwoDecimal(owd.getVatAmount());
                discountAmount += Utils.roundedOffTwoDecimal(owd.getDiscountAmount());


            }

            if (loadedTransactionList(transactionId).size() > 0) {
                Transactions tr = loadedTransactionList(transactionId).get(0);
                tr.setId(Integer.valueOf(transactionId));
                tr.setGross_sales(Utils.roundedOffTwoDecimal(grossSales));
                tr.setNet_sales(Utils.roundedOffTwoDecimal(netSales));
                tr.setVatable_sales(Utils.roundedOffTwoDecimal(vatableSales));
                tr.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vatExemptSales));
                tr.setVat_amount(Utils.roundedOffTwoDecimal(vatAmount));
                tr.setDiscount_amount(Utils.roundedOffTwoDecimal(discountAmount));
                update(tr);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void recomputeTransactionWithDiscount(String transactionId, DiscountViewModel discountViewModel) {

        Double grossSales = 0.00;
        Double netSales = 0.00;
        Double vatableSales = 0.00;
        Double vatExemptSales = 0.00;
        Double vatAmount = 0.00;
        Double discountAmount = 0.00;

        try {
            if (discountViewModel.getTransactionWithDiscounts(transactionId).size() > 0) {
                for (OrderWithDiscounts owd : discountViewModel.getOrderWithDiscount(transactionId)) {

                    Double remainingAmount = Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12);
                    Double finalAmount = Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12);
                    Double totalDiscountAmount = 0.00;

                    for (OrderDiscounts od : owd.orderWithDiscountList) {
                        if (!od.getIs_void()) {
                            totalDiscountAmount += Utils.roundedOffTwoDecimal(Utils.roundedOffTwoDecimal(remainingAmount) * (od.getValue() / 100));
                            remainingAmount = Utils.roundedOffTwoDecimal(remainingAmount - totalDiscountAmount);
                        }
                    }

                    finalAmount = finalAmount - totalDiscountAmount;

                    Orders ord = new Orders(
                            owd.orders.getTransaction_id(),
                            owd.orders.getCore_id(),
                            owd.orders.getQty(),
                            Utils.roundedOffTwoDecimal(finalAmount),
                            owd.orders.getOriginal_amount(),
                            owd.orders.getName(),
                            owd.orders.getDepartmentId(),
                            Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() - (owd.orders.getOriginal_amount() / 1.12)),
                            0.00,
                            Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12),
                            Utils.roundedOffTwoDecimal(totalDiscountAmount)

                    );
                    ord.setId(owd.orders.getId());
                    updateOrder(ord);
                    grossSales += Utils.roundedOffTwoDecimal((owd.orders.getVatAmount() + owd.orders.getVatable() + owd.orders.getVatExempt()));
                    netSales += Utils.roundedOffTwoDecimal(((owd.orders.getVatable() + owd.orders.getVatExempt()) - owd.orders.getDiscountAmount()));
                    vatableSales += Utils.roundedOffTwoDecimal(owd.orders.getVatable());
                    vatExemptSales += Utils.roundedOffTwoDecimal(owd.orders.getVatExempt());
                    vatAmount += Utils.roundedOffTwoDecimal(owd.orders.getVatAmount());
                    discountAmount += Utils.roundedOffTwoDecimal(totalDiscountAmount);
                }
            } else {
                for (Orders selectedProduct : orderList(transactionId)) {
                    Orders ord = new Orders(
                            Integer.valueOf(transactionId),
                            selectedProduct.getCore_id(),
                            selectedProduct.getQty(),
                            selectedProduct.getOriginal_amount(),
                            selectedProduct.getOriginal_amount(),
                            selectedProduct.getName(),
                            selectedProduct.getDepartmentId(),
                            Utils.roundedOffTwoDecimal(selectedProduct.getOriginal_amount() * .12),
                            Utils.roundedOffTwoDecimal( selectedProduct.getOriginal_amount()),
                            0.00,
                            0.00
                    );
                    ord.setId(selectedProduct.getId());
                    updateOrder(ord);

                    grossSales += Utils.roundedOffTwoDecimal((selectedProduct.getVatAmount() + selectedProduct.getVatable() + selectedProduct.getVatExempt()));
                    netSales += Utils.roundedOffTwoDecimal(((selectedProduct.getVatable() + selectedProduct.getVatExempt()) - selectedProduct.getDiscountAmount()));
                    vatableSales += Utils.roundedOffTwoDecimal(selectedProduct.getVatable());
                    vatAmount += Utils.roundedOffTwoDecimal(selectedProduct.getVatAmount());
                    vatExemptSales += 0.00;
                    discountAmount += 0.00;
                }
            }
            Transactions tr = loadedTransactionList(transactionId).get(0);
            tr.setId(Integer.valueOf(transactionId));
            tr.setGross_sales(Utils.roundedOffTwoDecimal(grossSales));
            tr.setNet_sales(Utils.roundedOffTwoDecimal(netSales));
            tr.setVatable_sales(Utils.roundedOffTwoDecimal(vatableSales));
            tr.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vatExemptSales));
            tr.setVat_amount(Utils.roundedOffTwoDecimal(vatAmount));
            tr.setDiscount_amount(Utils.roundedOffTwoDecimal(discountAmount));
            update(tr);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    public List<Transactions> unCutOffTransactions() throws ExecutionException, InterruptedException {
        return transactionsRepository.getUnCutOffTransactions();
    }

    public Transactions lastTransactionId() throws ExecutionException, InterruptedException {
        return transactionsRepository.getLastTransactionId();
    }

    public Transactions lastOrNumber() throws ExecutionException, InterruptedException {
        return transactionsRepository.getLastOrNumber();
    }

    public List<Transactions> unCutOffTransactions(String userId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getUnCutOffTransactionsByUser(userId);
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
