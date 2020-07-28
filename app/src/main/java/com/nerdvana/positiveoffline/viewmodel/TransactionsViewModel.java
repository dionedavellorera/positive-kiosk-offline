package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.entities.SerialNumbers;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.DiscountComputeModel;
import com.nerdvana.positiveoffline.model.OdWithPd;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
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

    public Integer updateTransactionDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.updateTransactionDataToSent(transactionId);
    }

    public Integer updateOrdersDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.updateOrdersDataToSent(transactionId);
    }

    public Integer updateOrderDiscountsDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.updateOrderDiscountsDataToSent(transactionId);
    }

    public Integer updatePostedDataToSent(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.updatePostedDataToSent(transactionId);
    }

    public Integer updateSerialNumbersToSent(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsRepository.updateSerialNumbersToSent(transactionId);
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

                grossSales += (owd.getVatAmount() + owd.getVatable() + owd.getVatExempt());
                if (owd.getVatExempt() > 0) {
                    netSales += (owd.getVatExempt()) - owd.getDiscountAmount();
                } else {
                    netSales += ((owd.getVatAmount() + owd.getVatable() + owd.getVatExempt()) - owd.getDiscountAmount());
                }
                vatableSales += owd.getVatable();
                vatExemptSales += owd.getVatExempt();
                vatAmount += owd.getVatAmount();
                discountAmount += owd.getDiscountAmount();


            }

            if (loadedTransactionList(transactionId).size() > 0) {
                Transactions tr = loadedTransactionList(transactionId).get(0);
                tr.setId(Integer.valueOf(transactionId));
                tr.setGross_sales(grossSales);
                tr.setNet_sales(netSales);
                tr.setVatable_sales(vatableSales);
                tr.setVat_exempt_sales(vatExemptSales);
                tr.setVat_amount(vatAmount);
                tr.setDiscount_amount(discountAmount);
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
                                if (od.getIs_special() == 1) {
                                    hasSpecial = 1;
                                }
//                                if (od.getDiscount_name().equalsIgnoreCase("senior") ||
//                                        od.getDiscount_name().equalsIgnoreCase("senior citizen") ||
//                                        od.getDiscount_name().equalsIgnoreCase("pwd")) {
//                                    hasSpecial = 1;
//                                }

                            }
                        }
                        //endregion

                        Double remainingAmount = 0.00;
                        Double finalAmount = 0.00;
                        Double finalAmountSpec = 0.00;
                        Double totalDiscountAmount = 0.00; ;


//                        remainingAmount = owd.orders.getOriginal_amount();
//                        finalAmount = owd.orders.getOriginal_amount();
//                        totalDiscountAmount = 0.00;

                        if (hasSpecial == 1) {
                            remainingAmount = owd.orders.getOriginal_amount() / 1.12;
                            finalAmount = owd.orders.getOriginal_amount() / 1.12;
                            totalDiscountAmount = 0.00;
                        } else {
                            remainingAmount = owd.orders.getOriginal_amount();
                            finalAmount = owd.orders.getOriginal_amount();
                            totalDiscountAmount = 0.00;
                        }

                        //region apply discounting

                        List<OrderDiscounts> sortedDiscounts = new ArrayList<>();

                        int otherCounter = 0;


                        for (OrderDiscounts od : owd.orderWithDiscountList) {
                            if (!od.getIs_void()) {

                                if (od.getIs_special() == 1 ) {
                                    sortedDiscounts.add(otherCounter, od);
                                } else {
                                    if (od.getIs_percentage()) {
                                        sortedDiscounts.add(0, od);
                                    } else {
                                        sortedDiscounts.add(od);
                                    }

                                    otherCounter += 1;
                                }

//                                if (od.getDiscount_name().equalsIgnoreCase("senior") ||
//                                        od.getDiscount_name().equalsIgnoreCase("senior citizen") ||
//                                        od.getDiscount_name().equalsIgnoreCase("pwd")) {
//
//                                    sortedDiscounts.add(otherCounter, od);
//
//                                } else {
//
//                                    if (od.getIs_percentage()) {
//                                        sortedDiscounts.add(0, od);
//                                    } else {
//                                        sortedDiscounts.add(od);
//                                    }
//
//                                    otherCounter += 1;
//                                }
                            }
                        }

                        Double currentDiscountAmount = 0.00;

                        for (OrderDiscounts od : sortedDiscounts) {
                            if (!od.getIs_void()) {

                                if (od.getIs_percentage()) {
                                    totalDiscountAmount += remainingAmount * (od.getValue() / 100);
                                    currentDiscountAmount = (remainingAmount * (od.getValue() / 100));
                                } else {
                                    totalDiscountAmount += od.getValue();
                                    currentDiscountAmount = od.getValue();
                                }

                                dcm.add(new DiscountComputeModel((int)od.getPosted_discount_id(), currentDiscountAmount, owd.orders.getQty()));

//                                remainingAmount = Utils.roundedOffFourDecimal(remainingAmount - totalDiscountAmount);
                                remainingAmount = remainingAmount - totalDiscountAmount;
                            }
                        }

                        //endregion
                        finalAmountSpec = finalAmount;
                        finalAmount = finalAmount - totalDiscountAmount;

                        Orders ord = new Orders(
                                owd.orders.getTransaction_id(),
                                owd.orders.getCore_id(),
                                owd.orders.getQty(),
                                finalAmount,
                                owd.orders.getOriginal_amount(),
                                owd.orders.getName(),
                                owd.orders.getDepartmentId(),
                                hasSpecial == 1 ? 0.00 : ((finalAmount/1.12) * .12) * owd.orders.getQty(),
                                hasSpecial == 1 ? 0.00 : ((finalAmount * owd.orders.getQty()) / 1.12),
                                hasSpecial == 1 ? ((finalAmountSpec * owd.orders.getQty())) : 0.00,
                                totalDiscountAmount * owd.orders.getQty(),
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
                                owd.orders.getIs_fixed_asset(),
                                owd.orders.getTo_id(),
                                owd.orders.getName_initials(),
                                owd.orders.getUnit_id(),
                                owd.orders.getUnit_id_description()

                        );

                        ord.setIs_void(owd.orders.getIs_void());

                        ord.setId(owd.orders.getId());
                        updateOrder(ord);

                        if (owd.transactions.getHas_special() == 1) {
                            grossSales += (ord.getVatAmount() + ord.getVatable() + ord.getVatExempt()) - owd.transactions.getDiscount_amount() ;
                            Log.d("MYGROSSALES", String.valueOf(grossSales));
                        } else {
                            Log.d("MYGROSSALES", String.valueOf(grossSales));

                            grossSales += ord.getVatAmount() + ord.getVatable() + ord.getVatExempt();
                        }

//                        grossSales += (ord.getVatAmount() + ord.getVatable() + ord.getVatExempt()) ;

                        netSales += ((ord.getVatable() + ord.getVatExempt()) - ord.getDiscountAmount());
                        vatableSales += ord.getVatable();
                        vatExemptSales += ord.getVatExempt();
                        vatAmount += ord.getVatAmount();
                        discountAmount += totalDiscountAmount;
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
                        pepe.add(new DiscountComputeModel(dcm.get(i).getPostedDiscountId(), dcm.get(i).getDiscountAmount() * dcm.get(i).getQty(), dcm.get(i).getQty()));
                    } else {
                        pepe.get(index).setDiscountAmount(pepe.get(index).getDiscountAmount() + (dcm.get(i).getDiscountAmount() * dcm.get(i).getQty()));
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
                                ((selectedProduct.getOriginal_amount()/1.12) * .12) * selectedProduct.getQty(),
                                (selectedProduct.getOriginal_amount() / 1.12) * selectedProduct.getQty(),
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
                                selectedProduct.getIs_fixed_asset(),
                                selectedProduct.getTo_id(),
                                selectedProduct.getName_initials(),
                                selectedProduct.getUnit_id(),
                                selectedProduct.getUnit_id_description()
                        );
                        ord.setId(selectedProduct.getId());
                        updateOrder(ord);

                        grossSales += selectedProduct.getVatAmount() + selectedProduct.getVatable() + selectedProduct.getVatExempt();
                        netSales += (selectedProduct.getVatable() + selectedProduct.getVatExempt()) - selectedProduct.getDiscountAmount();
                        vatableSales += selectedProduct.getVatable();
                        vatAmount += selectedProduct.getVatAmount();
                        vatExemptSales += 0.00;
                        discountAmount += 0.00;
                    }

                }
            }

            if (loadedTransactionList(transactionId).size() > 0) {
                Transactions tr = loadedTransactionList(transactionId).get(0);
                tr.setId(Integer.valueOf(transactionId));
                tr.setGross_sales(grossSales);
                tr.setNet_sales(netSales);
                tr.setVatable_sales(vatableSales);
                tr.setVat_exempt_sales(vatExemptSales);
                tr.setVat_amount(vatAmount);
                tr.setDiscount_amount(discountAmount);
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



    public LiveData<List<Transactions>> transactionLiveDataTo() {
        return transactionsRepository.getTransactionsTo();
    }


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

    public Transactions existingToTransaction(String to_transaction_id, String machine_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getExistingToTransaction(to_transaction_id, machine_id);
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


    public List<Transactions> transactionListFromTo() throws ExecutionException, InterruptedException {
        return transactionsRepository.transactionListFromTo();
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

    public Transactions getTransactionViaTransactionIdOnly(String transaction_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getTransactionViaTransactionIdOnly(transaction_id);
    }

    public Transactions getTransactionViaToTransactionId(String transaction_id) throws ExecutionException, InterruptedException {
        return transactionsRepository.getTransactionViaToTransactionId(transaction_id);
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
