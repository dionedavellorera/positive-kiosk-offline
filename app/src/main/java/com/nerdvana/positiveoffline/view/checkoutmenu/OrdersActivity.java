package com.nerdvana.positiveoffline.view.checkoutmenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClientCompany;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.adapter.ToOrdersAdapter;
import com.nerdvana.positiveoffline.apirequests.ServerDataRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchPosToTransactions;
import com.nerdvana.positiveoffline.apiresponses.OrderDiscountsServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.OrdersServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.PostingDiscountServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.SerialNumbersServerDataResponse;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.SerialNumbers;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.ToTransactionsContract;
import com.nerdvana.positiveoffline.model.RefreshCashierOrderList;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ToTransactionsContract {
    private SwipeRefreshLayout swipeRefreshData;
    private RecyclerView rvOrdersList;
    private TextView tvNoData;
    private TransactionsViewModel transactionsViewModel;
    private DiscountViewModel discountViewModel;
    private TextView title;
    private HidingEditText search;
    private ToOrdersAdapter toOrdersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        requestData();
        initTransactionsViewModel();
        title.setText("Orders");
        initSearchListener();
    }

    private void initViews() {
        search = findViewById(R.id.search);
        title = findViewById(R.id.title);
        tvNoData = findViewById(R.id.tvNoData);
        rvOrdersList = findViewById(R.id.rvOrdersList);
        swipeRefreshData = findViewById(R.id.swipeRefreshData);
        swipeRefreshData.setOnRefreshListener(this);
    }


    private void initTransactionsViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
        discountViewModel = new ViewModelProvider(this).get(DiscountViewModel.class);
    }

    private void requestData() {
        swipeRefreshData.setRefreshing(true);
        IUsers iUsers = PosClientCompany.mRestAdapter.create(IUsers.class);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("company_code", SharedPreferenceManager.getString(OrdersActivity.this, AppConstants.BRANCH));
        dataMap.put("branch_code", SharedPreferenceManager.getString(OrdersActivity.this, AppConstants.CODE));
        dataMap.put("machine_id", SharedPreferenceManager.getString(OrdersActivity.this, AppConstants.MACHINE_ID));
        ServerDataRequest data = new ServerDataRequest(dataMap);


        Call<FetchPosToTransactions> request = iUsers.posToTransactionsRequest(data.getMapValue());
        request.enqueue(new Callback<FetchPosToTransactions>() {
            @Override
            public void onResponse(Call<FetchPosToTransactions> call, Response<FetchPosToTransactions> response) {


                if (response.body().getData().size() > 0) {
                    tvNoData.setVisibility(View.GONE);
//                    swipeRefreshData.setVisibility(View.VISIBLE);
                } else {
                    tvNoData.setVisibility(View.VISIBLE);
//                    swipeRefreshData.setVisibility(View.GONE);
                    swipeRefreshData.setRefreshing(false);
                }
                setAdapter(response.body().getData());

            }

            @Override
            public void onFailure(Call<FetchPosToTransactions> call, Throwable t) {
                swipeRefreshData.setRefreshing(false);
//                swipeRefreshData.setVisibility(View.GONE);
//                tvNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    void returnSuccessData(String transactionId) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.TRANS_ID, transactionId);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setAdapter(List<FetchPosToTransactions.ToTransactionsData> data) {
        toOrdersAdapter = new ToOrdersAdapter(data, OrdersActivity.this, this);
        rvOrdersList.setAdapter(toOrdersAdapter);
        rvOrdersList.setLayoutManager(new GridLayoutManager(OrdersActivity.this, 6));
        toOrdersAdapter.notifyDataSetChanged();



        for (FetchPosToTransactions.ToTransactionsData list : data) {
            try {
                Transactions tmpTr = transactionsViewModel.existingToTransaction(String.valueOf(list.getTransactions().getId()), String.valueOf(list.getTransactions().getMachineId()));
                if (tmpTr == null) {

                    //insert data from server
                    //regioninsert transaction
                    Transactions tr = new Transactions();
//                            tr.setId(list.getId()); // allow auto increment for this
                    tr.setControl_number(list.getTransactions().getControlNumber());
                    tr.setUser_id(list.getTransactions().getUserId());
                    tr.setIs_void(list.getTransactions().getIsVoid() == 1 ? true : false);
                    tr.setIs_void_by(list.getTransactions().getIsVoidBy() == null ? "" : list.getTransactions().getIsVoidBy().toString());
                    tr.setVoid_at(list.getTransactions().getVoidAt() == null ? "" : list.getTransactions().getVoidAt().toString());
                    tr.setIs_completed(list.getTransactions().getIsCompleted() == 1 ? true : false);
                    tr.setIs_completed_by(list.getTransactions().getIsCompletedBy() == null ? "" : list.getTransactions().getIsCompletedBy().toString());
                    tr.setCompleted_at(list.getTransactions().getCompletedAt() == null ? "" : list.getTransactions().getCompletedAt().toString());
                    tr.setIs_saved(list.getTransactions().getIsSaved() == 1 ? true : false);
                    tr.setIs_saved_by(list.getTransactions().getIsSavedBy() == null ? "" : list.getTransactions().getIsSavedBy().toString());
                    tr.setSaved_at(list.getTransactions().getSavedAt() == null ? "" : list.getTransactions().getSavedAt().toString());
                    tr.setIs_cut_off(list.getTransactions().getIsCutOff() == 1 ? true : false);
                    tr.setIs_cut_off_by(list.getTransactions().getIsCutOffBy() == null ? "" : list.getTransactions().getIsCutOffBy().toString());
                    tr.setIs_cut_off_at(list.getTransactions().getIsCutOffAt() == null ? "" : list.getTransactions().getIsCutOffAt().toString());
                    tr.setIs_backed_out(list.getTransactions().getIsBackedOut() == 1 ? true : false);
                    tr.setIs_backed_out_by(list.getTransactions().getIsBackedOutBy() == null ? "" : list.getTransactions().getIsBackedOutBy().toString());
                    tr.setIs_backed_out_at(list.getTransactions().getIsBackedOutAt() == null ? "" : list.getTransactions().getIsBackedOutAt().toString());
                    tr.setTrans_name(list.getTransactions().getTransName() == null ? "" : list.getTransactions().getTransName().toString());
                    tr.setTreg(list.getTransactions().getTreg());
                    tr.setReceipt_number(list.getTransactions().getReceiptNumber() == null ? "" : list.getTransactions().getReceiptNumber());
                    tr.setGross_sales(list.getTransactions().getGrossSales());
                    tr.setNet_sales(list.getTransactions().getNetSales());
                    tr.setVatable_sales(list.getTransactions().getVatableSales());
                    tr.setVat_exempt_sales(list.getTransactions().getVatExemptSales());
                    tr.setVat_amount(list.getTransactions().getVatAmount());
                    tr.setDiscount_amount(list.getTransactions().getDiscountAmount());
                    tr.setChange(list.getTransactions().getChange());
                    tr.setCut_off_id(list.getTransactions().getCutOffId());
                    tr.setHas_special(list.getTransactions().getHasSpecial());
                    tr.setIs_cancelled(list.getTransactions().getIsBackedOut() == 1 ? true : false);
                    tr.setIs_cancelled_by(list.getTransactions().getIsCancelledBy() == null ? "" : list.getTransactions().getIsCancelledBy().toString());
                    tr.setIs_cancelled_at(list.getTransactions().getIsCancelledAt() == null ? "" : list.getTransactions().getIsCancelledAt().toString());
                    tr.setTin_number(list.getTransactions().getTinNumber() == null ? "" : list.getTransactions().getTinNumber());
                    tr.setIs_sent_to_server(list.getTransactions().getIsSentToServer());
//                    tr.setMachine_id(Integer.valueOf(SharedPreferenceManager.getString(OrdersActivity.this, AppConstants.MACHINE_ID)));
                    tr.setMachine_id(list.getTransactions().getMachineId());
                    tr.setBranch_id(list.getTransactions().getBranchId());
                    tr.setRoom_id(list.getTransactions().getRoomId());
                    tr.setRoom_number(list.getTransactions().getRoomNumber() == null ? "" : list.getTransactions().getRoomNumber().toString());
                    tr.setCheck_in_time(list.getTransactions().getCheckInTime() == null ? "" : list.getTransactions().getCheckInTime().toString());
                    tr.setCheck_out_time(list.getTransactions().getCheckOutTime() == null ? "" : list.getTransactions().getCheckOutTime().toString());
                    tr.setService_charge_value(Double.valueOf(list.getTransactions().getServiceChargeValue()));
                    tr.setService_charge_is_percentage(list.getTransactions().getServiceChargeIsPercentage() == 1 ? true : false);
                    tr.setIs_shared(list.getTransactions().getIsShared());
                    tr.setDelivery_to(list.getTransactions().getDeliveryTo());
                    tr.setDelivery_address(list.getTransactions().getDeliveryAddress());
                    tr.setTo_id(list.getTransactions().getToId());

                    tr.setTo_transaction_id(list.getTransactions().getId());
                    tr.setIs_temp(1);
                    tr.setTo_control_number(list.getTransactions().getControlNumber());
                    tr.setShift_number(list.getTransactions().getShiftNumber());
                    Log.d("TMPTRTRACING", "BEFORE SAVING");
                    long transId = transactionsViewModel.insertTransactionWaitData(tr);
                    Log.d("TMPTRTRACING", "SAVED - TRID" + String.valueOf(transId));
                    if (transId > 0) {
                        List<Orders> orderList = new ArrayList<>();
                        for (OrdersServerDataResponse.Order ord : list.getOrders()) {
                            Orders orders = new Orders();
                            orders.setTransaction_id((int)transId);
//                                    orders.setId(ord.getId());
                            orders.setCore_id(ord.getCoreId());
                            orders.setQty(ord.getQty());
                            orders.setAmount(ord.getAmount());
                            orders.setOriginal_amount(ord.getOriginalAmount());
                            orders.setName(ord.getName());
                            orders.setIs_void(ord.getIsVoid() == 1 ? true : false);
                            orders.setIs_editing(ord.getIsEditing() == 1 ? true : false);
                            orders.setDepartmentId(ord.getDepartmentId());
                            orders.setVatAmount(ord.getVatAmount());
                            orders.setVatable(ord.getVatable());
                            orders.setVatExempt(ord.getVatExempt());
                            orders.setDiscountAmount(ord.getDiscountAmount());
                            orders.setDepartmentName(ord.getDepartmentName());
                            orders.setCategoryName(ord.getCategoryName());
                            orders.setCategoryId(ord.getCategoryId());
                            orders.setIs_sent_to_server(ord.getIsSentToServer());
                            orders.setMachine_id(ord.getMachineId());
                            orders.setBranch_id(ord.getBranchId());
                            orders.setTreg(ord.getTreg());
                            orders.setIs_room_rate(ord.getIsRoomRate());
                            orders.setIs_discount_exempt(ord.getIsDiscountExempt());
                            orders.setProduct_alacart_id(ord.getProductAlacartId());
                            orders.setProduct_group_id(ord.getProductGroupId());
                            orders.setOrders_incremental_id(ord.getOrdersIncrementalId());
                            orders.setNotes(ord.getNotes() == null ? "" : ord.getNotes().toString());
                            orders.setIs_take_out(ord.getIsTakeOut());
                            orders.setSerial_number(ord.getSerialNumber());
                            orders.setIs_fixed_asset(ord.getIsFixedAsset());
                            orders.setTo_id(ord.getToId());
                            orders.setName_initials(ord.getName_initials());
                            orderList.add(orders);


                        }

                        Log.d("TMPTRTRACING", "ORDERSIZ" + orderList.size());
                        if (orderList.size() > 0) {
                            transactionsViewModel.insertOrder(orderList);
                            Log.d("TMPTRTRACING", "ORDER SAVED");
                        }


                        List<OrderDiscounts> orderDiscountsList = new ArrayList<>();
                        for (OrderDiscountsServerDataResponse.OrderDiscount ordDiscount : list.getOrderDiscount()) {
                            OrderDiscounts orderDiscounts = new OrderDiscounts();

//                                    orderDiscounts.setId(list.getId());
                            orderDiscounts.setTransaction_id((int)transId);
                            orderDiscounts.setProduct_id(ordDiscount.getProductId());
                            orderDiscounts.setIs_percentage(ordDiscount.getIsPercentage() == 1 ? true : false);
                            orderDiscounts.setValue(ordDiscount.getValue());
                            orderDiscounts.setOrder_id(ordDiscount.getOrderId());
                            orderDiscounts.setDiscount_name(ordDiscount.getDiscountName());
                            orderDiscounts.setPosted_discount_id(ordDiscount.getPostedDiscountId());
                            orderDiscounts.setIs_void(ordDiscount.getIsVoid() == 1 ? true : false);
                            orderDiscounts.setIs_sent_to_server(ordDiscount.getIsSentToServer());
                            orderDiscounts.setMachine_id(ordDiscount.getMachineId());
                            orderDiscounts.setBranch_id(ordDiscount.getBranchId());
                            orderDiscounts.setTreg(ordDiscount.getTreg());
                            orderDiscounts.setTo_id(ordDiscount.getToId());
                            orderDiscountsList.add(orderDiscounts);
                        }

                        if (orderDiscountsList.size() > 0) {
                            discountViewModel.insertOrderDiscount(orderDiscountsList);
                        }

                        for (PostingDiscountServerDataResponse.PostingDiscount postedDisc : list.getPostingDiscount()) {
                            PostedDiscounts postedDiscounts = new PostedDiscounts();
//                                    postedDiscounts.setId(list.getId());
                            postedDiscounts.setTransaction_id((int)transId);
                            postedDiscounts.setDiscount_id(postedDisc.getDiscountId());
                            postedDiscounts.setDiscount_name(postedDisc.getDiscountName());
                            postedDiscounts.setIs_void(postedDisc.getIsVoid() == 1 ? true : false);
                            postedDiscounts.setCard_number(postedDisc.getCardNumber());
                            postedDiscounts.setName(postedDisc.getName());
                            postedDiscounts.setAddress(postedDisc.getAddress());

                            postedDiscounts.setCut_off_id(postedDisc.getCutOffId());
                            postedDiscounts.setEnd_of_day_id(postedDisc.getEndOfDayId());
                            postedDiscounts.setAmount(postedDisc.getAmount());

                            postedDiscounts.setIs_percentage(postedDisc.getIsPercentage() == 1 ? true : false);
                            postedDiscounts.setDiscount_value(postedDisc.getDiscountValue());
                            postedDiscounts.setIs_sent_to_server(postedDisc.getIsSentToServer());
                            postedDiscounts.setMachine_id(postedDisc.getMachineId());
                            postedDiscounts.setBranch_id(postedDisc.getBranchId());
                            postedDiscounts.setTreg(postedDisc.getTreg());
                            postedDiscounts.setTo_id(postedDisc.getToId());
                            discountViewModel.insertPostedDiscount(postedDiscounts);
                        }


                        for (SerialNumbersServerDataResponse.SerialNumber sn : list.getSerialNumberList()) {
                            SerialNumbers serialNumbers = new SerialNumbers();
//                            serialNumbers.setId(list.getId());
                            serialNumbers.setTransaction_id((int)transId);
                            serialNumbers.setSerial_number(sn.getSerialNumber());
                            serialNumbers.setTreg(sn.getTreg());
                            serialNumbers.setIs_void(sn.getIsVoid()== 1 ? true : false);
                            serialNumbers.setIs_void_at(sn.getIsVoidAt());
                            serialNumbers.setProduct_core_id(sn.getProductCoreId());
                            serialNumbers.setProduct_name(sn.getProductName());
                            serialNumbers.setFor_update(sn.getForUpdate());
                            serialNumbers.setIs_sent_to_server(sn.getIsSentToServer());
                            serialNumbers.setOrder_id(sn.getOrderId());
                            serialNumbers.setMachine_id(sn.getMachineId());
                            serialNumbers.setBranch_id(sn.getBranchId());
                            serialNumbers.setTo_id(sn.getToId());
                            transactionsViewModel.insertSerialNumbers(serialNumbers);
                        }





                    }
                    //endregion
                } else {
                    //data already existing on local machine
                    Log.d("TMPTRTRACING", "NOT NULL");
                }

                swipeRefreshData.setRefreshing(false);

            } catch (ExecutionException e) {
                Log.d("TMPTRTRACING", e.getMessage());
                swipeRefreshData.setRefreshing(false);
                e.printStackTrace();
            } catch (InterruptedException e) {
                swipeRefreshData.setRefreshing(false);
                Log.d("TMPTRTRACING", e.getMessage());
                e.printStackTrace();
            }
        }


    }


    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void clicked(FetchPosToTransactions.ToTransactionsData transactions) {
        try {
            Transactions existingToTransaction = transactionsViewModel.getTransactionViaToTransactionId(String.valueOf(transactions.getTransactions().getId()));
            if (existingToTransaction != null) {

                if (existingToTransaction.getIs_completed()) {
                    Toast.makeText(getApplicationContext(),"Transaction already completed", Toast.LENGTH_SHORT).show();
                } else {
                    returnSuccessData(String.valueOf(transactions.getTransactions().getId()));
                }

            } else {
                Toast.makeText(getApplicationContext(),"Data not existing on local yet", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }


    }

    private void initSearchListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (toOrdersAdapter != null) {
                    toOrdersAdapter.getFilter().filter(editable);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void refreshList(RefreshCashierOrderList refreshCashierOrderList) {
        Log.d("RECEIVEDEMIT", "PLEASE REFRESH");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestData();
            }
        });

    }
}
