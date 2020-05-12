package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Query;

import com.facebook.stetho.common.Util;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.SerialNumbers;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.model.DiscountComputeModel;
import com.nerdvana.positiveoffline.model.OdWithPd;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.repository.DiscountsRepository;
import com.nerdvana.positiveoffline.repository.TransactionsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TransactionsViewModel extends AndroidViewModel {

    private TransactionsRepository transactionsRepository;
    private DiscountsRepository discountsRepository;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);
        transactionsRepository = new TransactionsRepository(application);
        discountsRepository = new DiscountsRepository(application);
    }



    public void insertPayment(List<Payments> paymentList) {
        transactionsRepository.insertPayment(paymentList);
    }

    public void insertSerialNumbers(SerialNumbers serialNumbersList) {
        transactionsRepository.insertSerialNumbers(serialNumbersList);
    }

    public void updateSerialNumbers(SerialNumbers serialNumbers) {
        transactionsRepository.updateSerialNumbers(serialNumbers);
    }

    public List<SerialNumbers> serialNumberFromTransaction(int transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getSerialNumberFromTransaction(String.valueOf(transactionId));
    }

    public List<SerialNumbers> getSerialNumbers() throws ExecutionException, InterruptedException {
        return transactionsRepository.getSerialNumbers();
    }

    public List<SerialNumbers> serialNumberFromOrderId(int orderId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getSerialNumberFromOrderId(orderId);
    }





    public void insertPayoutData(Payout payout) {
        transactionsRepository.insertPayout(payout);
    }


    public void insert(List<Transactions> transactionList) {
        transactionsRepository.insert(transactionList);
    }

    public long insertTransactionWaitData(Transactions transactions) throws ExecutionException, InterruptedException {
        return transactionsRepository.insertTransactionWaitData(transactions);
    }

    public void update(Transactions transaction) {
        transactionsRepository.update(transaction);
    }

    public Integer updateLong(Transactions transaction) throws ExecutionException, InterruptedException {
        return transactionsRepository.updateLong(transaction);
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
                if (owd.getVatExempt() > 0) {
                    netSales += (Utils.roundedOffTwoDecimal((owd.getVatExempt()) - owd.getDiscountAmount()));
                } else {
                    netSales += (Utils.roundedOffTwoDecimal((owd.getVatAmount() + owd.getVatable() + owd.getVatExempt()) - owd.getDiscountAmount()));
                }
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
                tr.setIs_sent_to_server(0);
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
        int hasSpecial = 0;
        try {
            if (discountViewModel.getTransactionWithDiscounts(transactionId).size() > 0) {
                List<DiscountComputeModel> dcm = new ArrayList<>();
                for (OrderWithDiscounts owd : discountViewModel.getOrderWithDiscount(transactionId)) {

                    if (owd.orders.getIs_discount_exempt() == 0) {
                        //region check if special
                        for (OrderDiscounts od : owd.orderWithDiscountList) {
                            if (!od.getIs_void()) {
                                if (od.getDiscount_name().equalsIgnoreCase("senior") ||
                                        od.getDiscount_name().equalsIgnoreCase("pwd")) {
                                    hasSpecial = 1;
                                }

                            }
                        }
                        //endregion

                        Double remainingAmount = 0.00;
                        Double finalAmount = 0.00;
                        Double totalDiscountAmount = 0.00; ;

                        if (hasSpecial == 1) {
                            remainingAmount = Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12);
                            finalAmount = Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12);
                            totalDiscountAmount = 0.00;
                        } else {
                            remainingAmount = Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount());
                            finalAmount = Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount());
                            totalDiscountAmount = 0.00;
                        }

                        //region apply discounting

                        List<OrderDiscounts> sortedDiscounts = new ArrayList<>();

                        int otherCounter = 0;


                        for (OrderDiscounts od : owd.orderWithDiscountList) {
                            if (!od.getIs_void()) {

                                if (od.getDiscount_name().equalsIgnoreCase("senior") ||
                                        od.getDiscount_name().equalsIgnoreCase("pwd")) {

                                    sortedDiscounts.add(otherCounter, od);

                                } else {

                                    if (od.getIs_percentage()) {
                                        sortedDiscounts.add(0, od);
                                    } else {
                                        sortedDiscounts.add(od);
                                    }

                                    otherCounter += 1;
                                }
                            }
                        }

                        Double currentDiscountAmount = 0.00;

                        for (OrderDiscounts od : sortedDiscounts) {
                            if (!od.getIs_void()) {

                                if (od.getIs_percentage()) {
                                    totalDiscountAmount += Utils.roundedOffTwoDecimal(Utils.roundedOffTwoDecimal(remainingAmount) * (od.getValue() / 100));
                                    currentDiscountAmount = Utils.roundedOffTwoDecimal(Utils.roundedOffTwoDecimal(remainingAmount) * (od.getValue() / 100));
                                } else {
                                    totalDiscountAmount += Utils.roundedOffTwoDecimal(od.getValue());
                                    currentDiscountAmount = Utils.roundedOffTwoDecimal(od.getValue());
                                }

                                dcm.add(new DiscountComputeModel((int)od.getPosted_discount_id(), currentDiscountAmount));

                                remainingAmount = Utils.roundedOffTwoDecimal(remainingAmount - totalDiscountAmount);
                            }
                        }

                        //endregion

                        finalAmount = finalAmount - totalDiscountAmount;

                        Orders ord = new Orders(
                                owd.orders.getTransaction_id(),
                                owd.orders.getCore_id(),
                                owd.orders.getQty(),
                                Utils.roundedOffTwoDecimal(finalAmount),
                                owd.orders.getOriginal_amount(),
                                owd.orders.getName(),
                                owd.orders.getDepartmentId(),
                                hasSpecial == 1 ? Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() - (owd.orders.getOriginal_amount() / 1.12)) : Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() * .12),
                                hasSpecial == 1 ? 0.00 : owd.orders.getOriginal_amount() / 1.12,
                                hasSpecial == 1 ? Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12) : 0.00,
                                Utils.roundedOffTwoDecimal(totalDiscountAmount),
                                owd.orders.getDepartmentName(),
                                owd.orders.getCategoryName(),
                                owd.orders.getCategoryId(),
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                Utils.getDateTimeToday(),
                                owd.orders.getIs_room_rate(),
                                owd.orders.getNotes(),
                                owd.orders.getIs_take_out(),
                                owd.orders.getIs_fixed_asset()

                        );

                        ord.setIs_void(owd.orders.getIs_void());

                        ord.setId(owd.orders.getId());
                        updateOrder(ord);
                        grossSales += Utils.roundedOffTwoDecimal((owd.orders.getVatAmount() + owd.orders.getVatable() + owd.orders.getVatExempt()));
                        netSales += Utils.roundedOffTwoDecimal(((owd.orders.getVatable() + owd.orders.getVatExempt()) - owd.orders.getDiscountAmount()));
                        vatableSales += Utils.roundedOffTwoDecimal(owd.orders.getVatable());
                        vatExemptSales += Utils.roundedOffTwoDecimal(owd.orders.getVatExempt());
                        vatAmount += Utils.roundedOffTwoDecimal(owd.orders.getVatAmount());
                        discountAmount += Utils.roundedOffTwoDecimal(totalDiscountAmount);
                    }


                }

                List<DiscountComputeModel> pepe = new ArrayList<>();
                for (int i = 0; i < dcm.size(); i++) {
                    boolean isExisting = false;
                    int index = 0;
                    for (int j = 0; j < pepe.size(); j++) {
                        if (pepe.get(j).getPostedDiscountId() == dcm.get(i).getPostedDiscountId()) {
                            index = j;
                            isExisting = true;
                        }
                    }

                    if (!isExisting) {
                        pepe.add(new DiscountComputeModel(dcm.get(i).getPostedDiscountId(), dcm.get(i).getDiscountAmount()));
                    } else {
                        pepe.get(index).setDiscountAmount(pepe.get(index).getDiscountAmount() + dcm.get(i).getDiscountAmount());
                    }

                }
                for (DiscountComputeModel mudel : pepe) {
                    PostedDiscounts pd = discountViewModel.getPostedDiscount(mudel.getPostedDiscountId());
                    pd.setId(mudel.getPostedDiscountId());
                    pd.setAmount(mudel.getDiscountAmount());
                    discountViewModel.updatePostedDiscount(pd);
                }
            } else {
                for (Orders selectedProduct : orderList(transactionId)) {
                    if (selectedProduct.getIs_discount_exempt() == 0) {
                        Orders ord = new Orders(
                                Integer.valueOf(transactionId),
                                selectedProduct.getCore_id(),
                                selectedProduct.getQty(),
                                selectedProduct.getOriginal_amount(),
                                selectedProduct.getOriginal_amount(),
                                selectedProduct.getName(),
                                selectedProduct.getDepartmentId(),
                                Utils.roundedOffTwoDecimal((selectedProduct.getOriginal_amount()/1.12) * .12),
                                Utils.roundedOffTwoDecimal( selectedProduct.getOriginal_amount() / 1.12),
                                0.00,
                                0.00,
                                selectedProduct.getDepartmentName(),
                                selectedProduct.getCategoryName(),
                                selectedProduct.getCategoryId(),
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                Utils.getDateTimeToday(),
                                selectedProduct.getIs_room_rate(),
                                selectedProduct.getNotes(),
                                selectedProduct.getIs_take_out(),
                                selectedProduct.getIs_fixed_asset()
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
                tr.setHas_special(hasSpecial);
                update(tr);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public List<OdWithPd> odWithPd(int transactionId) throws ExecutionException, InterruptedException {
        return discountsRepository.odWithPd(transactionId);
    }

    public void insertOrDetails(OrDetails orDetails) {
        transactionsRepository.insertOrDetails(orDetails);
    }

    public OrDetails getOrDetails(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getOrDetails(transactionId);
    }

    public List<OrDetails> getAllOrDetails() throws ExecutionException, InterruptedException {
        return transactionsRepository.getAllOrDetails();
    }



    public void updateEditingOrderList(String transactionId) {
        transactionsRepository.updateEditingOrderList(transactionId);
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

    public Payout lastPayoutSeriesNumber() throws ExecutionException, InterruptedException {
        return transactionsRepository.getLastPayoutSeries();
    }

    public List<Transactions> unCutOffTransactions(String userId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getUnCutOffTransactionsByUser(userId);
    }

    public List<TransactionWithOrders> completedTransactions(String startDate, String endDate) throws ExecutionException, InterruptedException {
        return transactionsRepository.getCompletedTransactions(startDate, endDate);
    }

    public LiveData<List<Orders>> ordersLiveData() {
        return transactionsRepository.getOrders();
    }

    public LiveData<List<Payments>> ldPaymentList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getLdPaymentList(transactionId);
    }

    public LiveData<List<Payments>> ldUnredeemedPaymentList() throws ExecutionException, InterruptedException {
        return transactionsRepository.getLdUnredeemedPaymentList();
    }


    public List<Payments> getUnredeemedPaymentList() throws ExecutionException, InterruptedException {
        return transactionsRepository.getUnredeemedPaymentList();
    }

    public List<Payments> paymentList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getPaymentList(transactionId);
    }

    public List<Payments> getAllPayments() throws ExecutionException, InterruptedException {
        return transactionsRepository.getAllPayments();
    }

    public List<Payout> payoutList() throws ExecutionException, InterruptedException {
        return transactionsRepository.getPayoutList();
    }

    public List<Orders> roomRateList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getRoomRateList(transactionId);
    }

    public List<Orders> getBundledItems(String product_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getBundledItems(product_id);
    }


    public List<Orders> orderList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getOrderList(transactionId);
    }

    public List<Orders> getAllOrders() throws ExecutionException, InterruptedException {
        return transactionsRepository.getAllOrders();
    }

    public List<Orders> orderListWithFixedAsset(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getOrderListWithAsset(transactionId);
    }

    public List<Orders> orderListWithoutBundle(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.orderListWithoutBundle(transactionId);
    }

    public List<Orders> editingOrderList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.getEdigintOrderList(transactionId);
    }


    public List<Transactions> transactionList() throws ExecutionException, InterruptedException {
        return transactionsRepository.getLatestTransaction();
    }

    public List<Transactions> getAllTransactions() throws ExecutionException, InterruptedException {
        return transactionsRepository.getAllTransactions();
    }

    public List<Transactions> loadedTransactionList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.loadedTransaction(transactionId);
    }



    public List<Transactions> loadedTransactionListViaControlNumber(String controlNumber) throws ExecutionException, InterruptedException {
        return transactionsRepository.loadedTransactionViaControlNumber(controlNumber);
    }

    public TransactionCompleteDetails getTransaction(String receiptNumber) throws ExecutionException, InterruptedException {
        return transactionsRepository.getTransaction(receiptNumber);
    }

    public TransactionCompleteDetails getTransactionViaTransactionId(String transaction_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getTransactionViaTransactionId(transaction_id);
    }

    public List<OrDetails> getAllSavedOr() throws ExecutionException, InterruptedException {
        return transactionsRepository.getAllSavedOr();
    }

    public List<TransactionWithOrders> savedTransactionsList() throws ExecutionException, InterruptedException {
        return transactionsRepository.savedTransactionsList();
    }

    public List<TransactionWithOrders> transactionListWithRoom() throws ExecutionException, InterruptedException {
        return transactionsRepository.transactionListWithRoom();
    }

    public List<ProductAlacart> getBranchAlacart(String product_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getBranchAlacart(product_id);
    }

    public List<BranchGroup> getBranchGroup(String product_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getBranchGroup(product_id);
    }

    public List<BranchGroup> getFilteredProductsPerCategory(String branch_group_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getFilteredProductsPerCategory(branch_group_id);
    }






}
