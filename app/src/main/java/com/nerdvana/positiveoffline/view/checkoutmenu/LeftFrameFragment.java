package com.nerdvana.positiveoffline.view.checkoutmenu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.facebook.stetho.common.Util;
import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.CheckoutAdapter;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.entities.SerialNumbers;
import com.nerdvana.positiveoffline.entities.ThemeSelection;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.OrdersContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.ProductToCheckout;
import com.nerdvana.positiveoffline.model.StPaymentsModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.view.dialog.AlacartCompositionDialog;
import com.nerdvana.positiveoffline.view.dialog.BundleCompositionDialog;
import com.nerdvana.positiveoffline.view.dialog.ChangeQtyDialog;
import com.nerdvana.positiveoffline.view.dialog.CollectionDialog;
import com.nerdvana.positiveoffline.view.dialog.CutOffMenuDialog;
import com.nerdvana.positiveoffline.view.dialog.DiscountMenuDialog;
import com.nerdvana.positiveoffline.view.dialog.InputDialog;
import com.nerdvana.positiveoffline.view.dialog.IntransitDialog;
import com.nerdvana.positiveoffline.view.dialog.OpenPriceDialog;
import com.nerdvana.positiveoffline.view.dialog.PasswordDialog;
import com.nerdvana.positiveoffline.view.dialog.PaymentDialog;
import com.nerdvana.positiveoffline.view.dialog.PayoutDialog;
import com.nerdvana.positiveoffline.view.dialog.SerialNumbersDialog;
import com.nerdvana.positiveoffline.view.dialog.ShareTransactionDialog;
import com.nerdvana.positiveoffline.view.dialog.TransactionDialog;
import com.nerdvana.positiveoffline.view.resumetransaction.ResumeTransactionActivity;
import com.nerdvana.positiveoffline.view.rooms.RoomsActivity;
import com.nerdvana.positiveoffline.view.settings.SettingsActivity;
import com.nerdvana.positiveoffline.viewmodel.CutOffViewModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.RoomsViewModel;
import com.nerdvana.positiveoffline.viewmodel.ServiceChargeViewModel;
import com.nerdvana.positiveoffline.viewmodel.SettingsViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LeftFrameFragment extends Fragment implements OrdersContract, View.OnClickListener {

    private List<ProductAlacart> mAlacartList = new ArrayList<>();
    private int mQty = 1;
    private List<BranchGroup> mBranchGroupList = new ArrayList<>();

    private boolean isDarkMode = false;

    int lastIncrementalId = 0;
    //regiondialogs
    private ShareTransactionDialog shareTransactionDialog;
    private CollectionDialog collectionDialog;
    private PayoutDialog payoutDialog;
    private IntransitDialog intransitDialog;
    private DiscountMenuDialog discountMenuDialog;
    private PaymentDialog paymentDialog;
    private PasswordDialog passwordDialog;
    private ChangeQtyDialog changeQtyDialog;
    private InputDialog inputDialog;
    private CutOffMenuDialog cutOffMenuDialog;
    private TransactionDialog transactionDialog;
    private OpenPriceDialog openPriceDialog;
    //endregion

    //regionview models
    private RoomsViewModel roomsViewModel;
    private TransactionsViewModel transactionsViewModel;
    private UserViewModel userViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private DiscountViewModel discountViewModel;
    private CutOffViewModel cutOffViewModel;
    private SettingsViewModel settingsViewModel;
    private ServiceChargeViewModel serviceChargeViewModel;
    //endregion

    private final int RESUME_TRANS_RETURN = 100;
    private final int ROOM_SELECTED_RETURN = 101;

    private Products selectedProduct;
    private RoomRates selectedRoomRate;
    private Rooms selectedTable;
    private int pRoomId = 0;

    private RelativeLayout rootRel;
    private CardView rootCard;
    private View view;
    private TextView subTotalValue;
    private TextView totalValue;
    private TextView discountValue;
    private RecyclerView listCheckoutItems;

//    private TextView header;
    private TextView cardHeader;
    private TextView cardHeaderRoot;
    private TextView subTotal;
    private TextView deposit;
    private TextView discount;
    private TextView depositLabel;
    private TextView discountLabel;
    private TextView subTotalLabel;
    private TextView totalLabel;
    private TextView listItemName;
    private TextView listItemQty;
    private TextView listItemPrice;
    private TextView tvRoomTableNumber;
    private LinearLayout lin00;
    private Button btnRemoveRoomTable;



    private String transactionId = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_left_frame, container, false);
        BusProvider.getInstance().register(this);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        btnRemoveRoomTable = view.findViewById(R.id.btnRemoveRoomTable);
        btnRemoveRoomTable.setOnClickListener(this);
        rootRel = view.findViewById(R.id.rootRel);
        lin00 = view.findViewById(R.id.lin00);
        tvRoomTableNumber = view.findViewById(R.id.tvRoomTableNumber);
        rootCard = view.findViewById(R.id.rootCard);
        listItemName = view.findViewById(R.id.listItemName);
        listItemQty = view.findViewById(R.id.listItemQty);
        listItemPrice = view.findViewById(R.id.listItemPrice);

        discountValue = view.findViewById(R.id.discountValue);
        listCheckoutItems = view.findViewById(R.id.listCheckoutItems);
        subTotalValue = view.findViewById(R.id.subTotalValue);
        totalValue = view.findViewById(R.id.totalValue);

//        header = view.findViewById(R.id.header);
        deposit = view.findViewById(R.id.depositValue);
        discount = view.findViewById(R.id.discountValue);

        depositLabel = view.findViewById(R.id.depositLabel);
        discountLabel = view.findViewById(R.id.discountLabel);
        subTotalLabel = view.findViewById(R.id.subTotalLabel);
        totalLabel = view.findViewById(R.id.totalLabel);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initServiceChargeViewModel();
        initUserViewModel();
        initRoomViewModel();
        initTransactionsViewModel();
        initTransactionsViewModelListener();
        initDiscountViewModel();
        initOrdersListener();
        initDataSyncViewModel();
        initCutOffViewModel();
        initSettingsViewModel();
        try {
            if (transactionsList().size() > 0) {
                setOrderAdapter(transactionsViewModel.orderList(transactionId));
            }
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }
    }

    private void initServiceChargeViewModel() {
        serviceChargeViewModel = new ViewModelProvider(this).get(ServiceChargeViewModel.class);
    }

    private void initRoomViewModel() {
        roomsViewModel = new ViewModelProvider(this).get(RoomsViewModel.class);
    }

    private void initSettingsViewModel() {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    private void initCutOffViewModel() {
        cutOffViewModel = new ViewModelProvider(this).get(CutOffViewModel.class);
    }

    private void initDiscountViewModel() {
        discountViewModel = new ViewModelProvider(this).get(DiscountViewModel.class);
    }

    private void initDataSyncViewModel() {
        dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void initTransactionsViewModelListener() {
        transactionsViewModel.transactionLiveData().observe(this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {


                if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
                    lin00.setVisibility(View.GONE);
                } else {
                    if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
                        lin00.setVisibility(View.GONE);
                    } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                        lin00.setVisibility(View.VISIBLE);
                    } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                        lin00.setVisibility(View.VISIBLE);
                    }
                }



                try {
                    if (!TextUtils.isEmpty(transactionId)) {
                        if (selectedTable != null) {
                            transactionId = String.valueOf(transactions.get(transactions.size() - 1).getId());
                            selectedTable.setTransaction_id(transactionId);
                            if (selectedTable.getCheck_in_time().isEmpty()) {
                                selectedTable.setCheck_in_time(Utils.getDateTimeToday());
                            }
                            if (!TextUtils.isEmpty(selectedTable.getCheck_in_time())) {
                                tvRoomTableNumber.setText("TABLE:" + selectedTable.getRoom_name() + "-" + Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), selectedTable.getCheck_in_time()));
                            } else {
                                tvRoomTableNumber.setText("TABLE:NA");
                            }


                            roomsViewModel.update(selectedTable);
                        } else {
                            if (roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)) != null) {
                                if (!TextUtils.isEmpty(roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time())) {
                                    tvRoomTableNumber.setText("TABLE:" + roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getRoom_name() + "-" + Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time()));
                                } else {
                                    tvRoomTableNumber.setText("TABLE:" + "NA");
                                }

                            } else {
                                tvRoomTableNumber.setText("TABLE:" + "NA");
                            }

                        }
                        setOrderAdapter(transactionsViewModel.orderList(transactionId));
                    } else {

                        if (transactions.size() > 0) {
                        
                            transactionId = String.valueOf(transactions.get(0).getId());

                            if (!TextUtils.isEmpty(transactions.get(0).getCheck_in_time())) {
                                tvRoomTableNumber.setText("TABLE:" + transactions.get(0).getRoom_number() + "-" + Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), transactions.get(0).getCheck_in_time()));
                            } else {
                                tvRoomTableNumber.setText("TABLE:NA");
                            }


                            if (selectedTable != null) {
                                selectedTable.setTransaction_id(transactionId);
                                if (selectedTable.getCheck_in_time().isEmpty()) {
                                    selectedTable.setCheck_in_time(Utils.getDateTimeToday());
                                }
                                roomsViewModel.update(selectedTable);
                            } else {
                                if (roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)) != null) {

                                    if (!TextUtils.isEmpty(roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time())) {
                                        tvRoomTableNumber.setText("TABLE:" + roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getRoom_name() + "-" + Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time()));
                                    } else {
                                        tvRoomTableNumber.setText("TABLE:" + "NA");
                                    }

                                } else {
                                    tvRoomTableNumber.setText("TABLE:" + "NA");
                                }
                            }

                            if (selectedProduct != null) {
                                insertSelectedOrder();
                                selectedProduct = null;
                            }

                            if (selectedRoomRate != null) {

                                Rooms rooms = roomsViewModel.getRoomViaId(selectedRoomRate.getRoom_id());
                                rooms.setTransaction_id(transactionId);
                                roomsViewModel.update(rooms);


                                Transactions tmpTr = transactionsViewModel.loadedTransactionList(String.valueOf(transactionId)).get(0);
                                tmpTr.setId(Integer.valueOf(transactionId));
                                tmpTr.setRoom_id(rooms.getRoom_id());
                                tmpTr.setRoom_number(rooms.getRoom_name());
                                tmpTr.setIs_sent_to_server(0);

                                tmpTr.setCheck_in_time(Utils.getDateTimeToday());

                                transactionsViewModel.update(tmpTr);

                                insertSelectedRoomRate();

                            }
                            setOrderAdapter(transactionsViewModel.orderList(transactionId));
                        } else {
                            defaults();
                        }
                    }


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initTransactionsViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private List<Transactions> transactionsList() throws ExecutionException, InterruptedException {
        return transactionsViewModel.transactionList();
    }

    private List<Transactions> loadedTransactionsList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsViewModel.loadedTransactionList(transactionId);
    }



    private void initOrdersListener() {

        transactionsViewModel.ordersLiveData().observe(this, new Observer<List<Orders>>() {
            @Override
            public void onChanged(List<Orders> orders) {

                try {
                    setOrderAdapter(transactionsViewModel.orderList(transactionId));

                    transactionsViewModel.recomputeTransaction(transactionsViewModel.orderList(transactionId), transactionId);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setOrderAdapter(List<Orders> orderList) {

        computeTotal(orderList);

        CheckoutAdapter checkoutAdapter = new CheckoutAdapter(orderList, this, getContext(), isDarkMode);
        listCheckoutItems.setAdapter(checkoutAdapter);
        listCheckoutItems.setLayoutManager(new LinearLayoutManager(getContext()));
        checkoutAdapter.notifyDataSetChanged();

        selectedProduct = null;


    }

    private void computeTotal(List<Orders> orderList) {
        Double subTotal = 0.00;
        Double totalValue = 0.00;
        Double discountAmount = 0.00;
        for (Orders order : orderList) {
            totalValue += order.getAmount() * order.getQty();
            subTotal += order.getOriginal_amount() * order.getQty();
            discountAmount += order.getDiscountAmount();
        }

        discountValue.setText(Utils.digitsWithComma(discountAmount));
        subTotalValue.setText(Utils.digitsWithComma(subTotal));
        this.totalValue.setText(Utils.digitsWithComma(totalValue));
    }


    private void saveTransaction(Transactions transactions, String name) {
        transactionId = "";

        Transactions tr = null;
        try {

            tr = new Transactions(
                    transactions.getId(),
                    transactions.getControl_number(),
                    transactions.getUser_id(),
                    transactions.getIs_void(),
                    transactions.getIs_void_by(),
                    transactions.getIs_completed(),
                    transactions.getIs_completed_by(),
                    true,
                    getUser().getUsername(),
                    transactions.getIs_cut_off(),
                    transactions.getIs_cut_off_by(),
                    name,
                    transactions.getTreg(),
                    transactions.getReceipt_number(),
                    transactions.getGross_sales() == null ? 0.00 : transactions.getGross_sales(),
                    transactions.getNet_sales() == null ? 0.00 : transactions.getNet_sales(),
                    transactions.getVatable_sales() == null ? 0.00 :transactions.getVatable_sales(),
                    transactions.getVat_exempt_sales() == null ? 0.00 : transactions.getVat_exempt_sales(),
                    transactions.getVatable_sales() == null ? 0.00 : transactions.getVatable_sales(),
                    transactions.getDiscount_amount() == null ? 0.00 : transactions.getDiscount_amount(),
                    0.00,
                    transactions.getVoid_at(),
                    transactions.getCompleted_at(),
                    Utils.getDateTimeToday(),
                    transactions.getIs_cut_off_at(),
                    transactions.getIs_cancelled(),
                    transactions.getIs_cancelled_by(),
                    transactions.getIs_cancelled_at(),
                    transactions.getTin_number(),
                    transactions.getIs_backed_out(),
                    transactions.getIs_backed_out_by(),
                    transactions.getIs_backed_out_at()
            );
            tr.setRoom_id(transactions.getRoom_id());
            tr.setRoom_number(transactions.getRoom_number());
            tr.setMachine_id(transactions.getMachine_id());
            tr.setIs_sent_to_server(0);
            tr.setBranch_id(transactions.getBranch_id());
            tr.setCheck_in_time(transactions.getCheck_in_time());
            tr.setCheck_out_time(transactions.getCheck_out_time());


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transactionsViewModel.update(tr);
    }

    private void changeRoomStatus(Rooms room, int status_id, boolean is_transfer) {
        try {
            if (room != null) {
                RoomStatus roomStatus = roomsViewModel.getRoomStatusViaId(status_id);
                room.setStatus_id(roomStatus.getCore_id());
                room.setStatus_description(roomStatus.getRoom_status());
                room.setHex_color(roomStatus.getHex_color());

                if (is_transfer) {
                    room.setTransaction_id("");
                }
                roomsViewModel.update(room);
            }

        } catch (Exception e) {

        }
    }
    private void doPayoutFunction(String amount, String user) {
    }



    @Subscribe
    public void menuClicked(ButtonsModel buttonsModel) throws ExecutionException, InterruptedException {
        switch (buttonsModel.getId()) {
            case 120://SET SERIAL NUMBER
                if (!TextUtils.isEmpty(transactionId)) {
                    if (transactionsViewModel.orderListWithFixedAsset(transactionId).size() > 0) {
                        SerialNumbersDialog serialNumbersDialog = new SerialNumbersDialog(getContext(), transactionsViewModel, transactionId) {
                            @Override
                            public void submitted() {

                            }
                        };
                        serialNumbersDialog.show();
                    } else {
                        Helper.showDialogMessage(getActivity(), "No fixed asset on ordered list", "Information");
                    }
                } else {
                    Helper.showDialogMessage(getActivity(), "No transaction yet", "Information");
                }
                break;
            case 112://PER SHARE TRANSACTION
                if (shareTransactionDialog == null) {
                    if (!TextUtils.isEmpty(transactionId)) {
                        List<Orders> orderList =  transactionsViewModel.orderListWithoutBundle(transactionId);
//                        List<Orders> orderList =  transactionsViewModel.orderList(transactionId);
                        if (orderList.size() > 0) {
                            int orderCount = 0;
                            for (Orders tmpOrd : orderList) {
                                orderCount += tmpOrd.getQty();
                            }
                            if (orderCount > 1) {
                                shareTransactionDialog = new ShareTransactionDialog(getActivity(), orderList,
                                        orderCount, dataSyncViewModel) {
                                    @Override
                                    public void confirmPayments(List<StPaymentsModel> list) {
                                        //create transaction step 1
                                        if (!TextUtils.isEmpty(transactionId)) {
                                            try {
                                                Transactions mTrans = transactionsViewModel.loadedTransactionList(transactionId).get(0);

                                                mTrans.setIs_shared(1);

                                                transactionsViewModel.update(mTrans);
                                            } catch (Exception e) {

                                            }

                                        }


                                        for (StPaymentsModel model : list) {
                                            synchronized (model) {
                                                try {
                                                    Transactions tr = new Transactions(
                                                            generatedControlNumber(),
                                                            getUser().getUsername(),
                                                            Utils.getDateTimeToday(),
                                                            0,
                                                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                                    );
                                                    tr.setIs_shared(0);
                                                    tr.setIs_completed(true);
                                                    tr.setIs_completed_by(getUser().getUsername());
                                                    tr.setCompleted_at(Utils.getDateTimeToday());
                                                    tr.setReceipt_number(generatedReceiptNumber());


                                                    Double tendered = 0.00;
                                                    Double amountDue = 0.00;
                                                    boolean isPercentage = false;
                                                    Double serviceCharge = 0.00;

                                                    Double grossSales = 0.00;
                                                    Double netSales = 0.00;
                                                    Double vatableSales = 0.00;
                                                    Double vatExemptSales = 0.00;
                                                    Double vatAmount = 0.00;
                                                    Double discountAmount = 0.00;


                                                    for (Orders ord : model.getOrdersList()) {
                                                        //create order step 2
                                                        amountDue += Utils.roundedOffTwoDecimal(ord.getAmount()) * ord.getQty();

                                                        grossSales += Utils.roundedOffTwoDecimal((ord.getVatAmount() + ord.getVatable() + ord.getVatExempt()));
                                                        netSales += Utils.roundedOffTwoDecimal(((ord.getVatable() + ord.getVatExempt()) - ord.getDiscountAmount()));
                                                        vatableSales += Utils.roundedOffTwoDecimal(ord.getVatable());
                                                        vatAmount += Utils.roundedOffTwoDecimal(ord.getVatAmount());
                                                        vatExemptSales += ord.getVatExempt();
                                                        discountAmount += ord.getDiscountAmount();
                                                    }

                                                    tr.setGross_sales(Utils.roundedOffTwoDecimal(grossSales));
                                                    tr.setNet_sales(Utils.roundedOffTwoDecimal(netSales));
                                                    tr.setVatable_sales(Utils.roundedOffTwoDecimal(vatableSales));
                                                    tr.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vatExemptSales));
                                                    tr.setVat_amount(Utils.roundedOffTwoDecimal(vatAmount));
                                                    tr.setDiscount_amount(Utils.roundedOffTwoDecimal(discountAmount));

                                                    if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel") ||
                                                            SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {

                                                        if (serviceChargeViewModel.getActiveServiceCharge() != null) {
                                                            if (serviceChargeViewModel.getActiveServiceCharge().isIs_percentage()) {
                                                                isPercentage = true;
                                                                serviceCharge = amountDue * (serviceChargeViewModel.getActiveServiceCharge().getValue() / 100);
                                                            } else {
                                                                isPercentage = false;
                                                                serviceCharge = serviceChargeViewModel.getActiveServiceCharge().getValue();
                                                            }
                                                            amountDue += serviceCharge;
                                                        }

                                                    }



                                                    //step 3
                                                    Double change = 0.00;
                                                    for (Payments pym : model.getPaymentsList()) {
                                                        //create payment
                                                        tendered += Utils.roundedOffTwoDecimal(pym.getAmount());
                                                    }

                                                    change = (Utils.roundedOffTwoDecimal(tendered) - Utils.roundedOffTwoDecimal(amountDue) < 1 ? 0.00 : Utils.roundedOffTwoDecimal(tendered) - Utils.roundedOffTwoDecimal(amountDue));

                                                    tr.setService_charge_is_percentage(isPercentage);
                                                    tr.setService_charge_value(serviceCharge);

                                                    tr.setCheck_out_time(!TextUtils.isEmpty(tr.getCheck_in_time()) ? Utils.getDateTimeToday() : "");
                                                    tr.setChange(change);



                                                    long returnTransId = transactionsViewModel.insertTransactionWaitData(tr);

//                                                transactionsViewModel.recomputeTransaction(model.getOrdersList(), String.valueOf(returnTransId));
                                                    //step 2


                                                    for (Orders ord : model.getOrdersList()) {
                                                        //create order step 2
                                                        ord.setTransaction_id((int)returnTransId);

                                                    }

                                                    //step 3
                                                    for (Payments pym : model.getPaymentsList()) {
                                                        //create payment
                                                        pym.setTransaction_id((int) returnTransId);
                                                    }



                                                    transactionsViewModel.insertOrder(model.getOrdersList());
                                                    transactionsViewModel.insertPayment(model.getPaymentsList());



                                                    //print receipt per transaction



                                                    BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT", GsonHelper.getGson().toJson(tr.getReceipt_number())));





                                                } catch (Exception e) {

                                                }


                                            }



                                        }

                                        defaults();


                                        //checkout
                                    }
                                };

                                shareTransactionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        shareTransactionDialog = null;
                                    }
                                });
                                shareTransactionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        shareTransactionDialog = null;
                                    }
                                });
                                shareTransactionDialog.show();
                            } else {
                                Helper.showDialogMessage(getActivity(), "Cannot share a single item transaction", getContext().getString(R.string.header_message));
                            }

                        } else {
                            Helper.showDialogMessage(getActivity(), "No transaction to share", getContext().getString(R.string.header_message));
                        }

                    } else {
                        Helper.showDialogMessage(getActivity(), "No transaction to share", getContext().getString(R.string.header_message));
                    }

                }
                break;
            case 111://DINE IN OR TAKE OUT TOGGLE
                if (!TextUtils.isEmpty(transactionId)) {
                    boolean hasRoom = false;
                    try {
                        Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                        if (tmpRm != null) {
                            hasRoom = true;
                        }

                        if (!hasRoom) {
                            Helper.showDialogMessage(getActivity(), "No table, cannot proceed to dine in, please select table first", getContext().getString(R.string.header_message));
                        } else {
                            if (getEditingOrderList().size() > 0) {
                                for (Orders ord : getEditingOrderList()) {
                                    if (ord.getIs_take_out() == 1) {
                                        ord.setIs_take_out(0);
                                    } else {
                                        ord.setIs_take_out(1);
                                    }
                                    transactionsViewModel.updateOrder(ord);
                                }
                            } else {
                                Helper.showDialogMessage(getActivity(), "No item selected", getContext().getString(R.string.header_message));
                            }

                        }
                    } catch (Exception e) {

                    }



                } else {
                    Helper.showDialogMessage(getActivity(), "No transaction", getContext().getString(R.string.header_message));
                }
                break;
            case 108://BACKOUT
                if (!TextUtils.isEmpty(transactionId)) {
                    try {
                        if (transactionsViewModel.loadedTransactionList(transactionId).size() > 0) {
                            Transactions mTmpTr = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                            mTmpTr.setCheck_in_time("");
                            mTmpTr.setCheck_out_time("");
                            mTmpTr.setRoom_id(0);
                            mTmpTr.setRoom_number("");
                            mTmpTr.setIs_backed_out(true);
                            mTmpTr.setIs_backed_out_by(userViewModel.searchLoggedInUser().get(0).getUsername());
                            mTmpTr.setIs_backed_out_at(Utils.getDateTimeToday());
                            transactionsViewModel.update(mTmpTr);
                        }

                        Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                        if (tmpRm != null) {
                            tmpRm.setCheck_in_time("");
                        }


                        changeRoomStatus(tmpRm, 1, true);

                        final TransactionCompleteDetails tr = transactionsViewModel.getTransactionViaTransactionId(transactionId);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BusProvider.getInstance().post(new PrintModel("BACKOUT", GsonHelper.getGson().toJson(tr)));

                            }
                        }, 300);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    defaults();
                    Helper.showDialogMessage(getActivity(), "back out success", getContext().getString(R.string.header_message));
                } else {
                    Helper.showDialogMessage(getActivity(), "No transaction to backout", getContext().getString(R.string.header_message));
                }
                break;
            case 200://PAYOUT
                if (payoutDialog == null) {
                    payoutDialog = new PayoutDialog(getActivity()) {
                        @Override
                        public void payoutSuccess(final String amount, final String reason) {
                            try {
                                String payoutNumber = "";
                                if (transactionsViewModel.lastPayoutSeriesNumber() == null) {
                                    payoutNumber = Utils.getPayoutSeriesFormat("1");
                                } else {
                                    if (TextUtils.isEmpty(transactionsViewModel.lastPayoutSeriesNumber().getSeries_number())) {
                                        payoutNumber = Utils.getPayoutSeriesFormat("1");
                                    } else {
                                        payoutNumber =
                                                Utils.getPayoutSeriesFormat(
                                                        String.valueOf(
                                                                Integer.valueOf(
                                                                        transactionsViewModel.lastPayoutSeriesNumber().getSeries_number().replaceFirst("0", "")) + 1));
                                    }

                                }

                                if (passwordDialog == null) {
                                    final String finalPayoutNumber = payoutNumber;
                                    passwordDialog = new PasswordDialog(getActivity(), "Payout Confirmation", userViewModel, transactionsViewModel) {
                                        @Override
                                        public void success(String username) {
                                            try {

                                                payoutDialog.dismiss();
                                                Payout payout =
                                                        new Payout(finalPayoutNumber,
                                                                userViewModel.searchLoggedInUser().get(0).getUsername(),
                                                                Double.valueOf(amount),
                                                                reason,
                                                                username,
                                                                Utils.getDateTimeToday(),
                                                                0,
                                                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)));

                                                transactionsViewModel.insertPayoutData(payout);

                                                //add payout printout here
                                                BusProvider.getInstance().post(new PrintModel("PRINT_PAYOUT", GsonHelper.getGson().toJson(payout)));

                                            } catch (Exception e) {

                                            }

                                        }
                                    };

                                    passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            passwordDialog = null;
                                        }
                                    });

                                    passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            passwordDialog = null;
                                        }
                                    });
                                    passwordDialog.show();
                                }







                            } catch (Exception e) {

                            }




                        }
                    };
                    payoutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            payoutDialog = null;
                        }
                    });
                    payoutDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            payoutDialog = null;
                        }
                    });
                    payoutDialog.show();
                }
                break;
            case 124://INTRANSIT
                if (intransitDialog == null) {
                    intransitDialog = new IntransitDialog(getActivity(), transactionsViewModel);
                    intransitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            intransitDialog = null;
                        }
                    });
                    intransitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            intransitDialog = null;
                        }
                    });
                    intransitDialog.show();
                }
                break;
            case 125://SPOT AUDIT
                if (collectionDialog == null) {
                    collectionDialog = new CollectionDialog(getActivity(), dataSyncViewModel) {
                        @Override
                        public void cutOffSuccess(Double totalCash) {

                            try {
                                CutOff cutOff = new CutOff(
                                        0,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0,
                                        Utils.getDateTimeToday(),
                                        "",
                                        "",
                                        0,
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                                        0.00
                                );
                                cutOff.setId(996699);
                                        /*
                                        cutOffViewModel.insertData(new CutOff(
                                        0,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0,
                                        Utils.getDateTimeToday(),
                                        "",
                                        "",
                                        0,
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                ));
                                         */

                                List<Transactions> transactionsList = transactionsViewModel.unCutOffTransactions(userViewModel.searchLoggedInUser().get(0).getUsername());
                                int number_of_transaction = 0;
                                Double gross_sales = 0.00;
                                Double net_sales = 0.00;
                                Double vatable_sales = 0.00;
                                Double vat_exempt_sales = 0.00;
                                Double vat_amount = 0.00;
                                Double void_amount = 0.00;

                                Double total_cash_payments = 0.00;
                                Double total_card_payments = 0.00;
                                Double total_change = 0.00;
                                Double total_service_charge = 0.00;
                                int seniorCount = 0;
                                Double seniorAmount = 0.00;
                                int pwdCount = 0;
                                Double pwdAmount = 0.00;
                                int othersCount = 0;
                                Double othersAmount = 0.00;


                                String begOrNo = "";
                                String endOrNo = "";


                                if (transactionsList.size() > 0) {
                                    begOrNo = transactionsList.get(0).getReceipt_number();
                                    endOrNo = transactionsList.get(transactionsList.size() - 1).getReceipt_number();
                                    for (Transactions tr : transactionsList) {
                                        if (tr.getIs_void()) {
                                            void_amount += tr.getNet_sales();
                                        } else {
                                            total_service_charge += tr.getService_charge_value();
                                            gross_sales += tr.getGross_sales();
                                            net_sales += tr.getNet_sales();
                                            vatable_sales += tr.getVatable_sales();
                                            vat_exempt_sales += tr.getVat_exempt_sales();
                                            vat_amount += tr.getVat_amount();
                                            total_change += tr.getChange();
                                        }
                                        number_of_transaction += 1;

                                        tr.setService_charge_value(total_service_charge);
                                        tr.setIs_sent_to_server(0);
                                        tr.setIs_cut_off(true);
                                        tr.setIs_cut_off_by(userViewModel.searchLoggedInUser().get(0).getUsername());
                                        tr.setCut_off_id(996699);
                                        tr.setIs_cut_off_at(Utils.getDateTimeToday());
//                                        transactionsViewModel.update(tr);
                                    }
                                    dismiss();
                                } else {
                                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_no_transaction_cutoff), getContext().getString(R.string.header_message));
                                }


                                for (Payments payments : cutOffViewModel.getAllPayments()) {
//                                    payments.setIs_sent_to_server(0);
//                                    payments.setCut_off_id((int) cut_off_id);
//                                    cutOffViewModel.update(payments);
                                    switch (payments.getCore_id()) {
                                        case 1://CASH
                                            total_cash_payments += payments.getAmount();
                                            break;
                                        case 2://CARD
                                            total_card_payments += payments.getAmount();
                                    }
                                }

                                for (PostedDiscounts postedDiscounts : cutOffViewModel.getUnCutOffPostedDiscount()) {
//                                    postedDiscounts.setCut_off_id((int)cut_off_id);
//                                    postedDiscounts.setIs_sent_to_server(0);
                                    if (postedDiscounts.getDiscount_name().equalsIgnoreCase("SENIOR")) {
                                        seniorCount += 1;
                                        seniorAmount += postedDiscounts.getAmount();
                                    } else if (postedDiscounts.getDiscount_name().equalsIgnoreCase("PWD")) {
                                        pwdCount += 1;
                                        pwdAmount += postedDiscounts.getAmount();
                                    } else {
                                        othersCount += 1;
                                        othersAmount += postedDiscounts.getAmount();
                                    }
                                }

//                                CutOff cutOff = cutOffViewModel.getCutOff(cut_off_id);
                                cutOff.setIs_sent_to_server(0);
                                cutOff.setTotal_change(Utils.roundedOffTwoDecimal(total_change));
                                cutOff.setGross_sales(Utils.roundedOffTwoDecimal(gross_sales));
                                cutOff.setNet_sales(Utils.roundedOffTwoDecimal(net_sales));
                                cutOff.setVatable_sales(Utils.roundedOffTwoDecimal(vatable_sales));
                                cutOff.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vat_exempt_sales));
                                cutOff.setVat_amount(Utils.roundedOffTwoDecimal(vat_amount));
                                cutOff.setNumber_of_transactions(number_of_transaction);
                                cutOff.setVoid_amount(Utils.roundedOffTwoDecimal(void_amount));
                                cutOff.setTotal_cash_amount(Utils.roundedOffTwoDecimal(totalCash));
                                cutOff.setTotal_cash_payments(Utils.roundedOffTwoDecimal(total_cash_payments));
                                cutOff.setTotal_card_payments(Utils.roundedOffTwoDecimal(total_card_payments));

                                cutOff.setSeniorCount(seniorCount);
                                cutOff.setSeniorAmount(seniorAmount);
                                cutOff.setPwdCount(pwdCount);
                                cutOff.setPwdAmount(pwdAmount);
                                cutOff.setOthersCount(othersCount);
                                cutOff.setOthersAmount(othersAmount);

                                cutOff.setBegOrNo(begOrNo);
                                cutOff.setEndOrNo(endOrNo);

                                BusProvider.getInstance().post(new PrintModel("PRINT_SPOT_AUDIT", GsonHelper.getGson().toJson(cutOff)));

//                                cutOffViewModel.update(cutOff);
                                dismiss();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    };
                    collectionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            collectionDialog = null;
                        }
                    });
                    collectionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            collectionDialog = null;
                        }
                    });
                    collectionDialog.show();
                }
                break;
            case 122:// DISCOUNT EXEMPT
                if (getEditingOrderList().size() > 0) {
                    diDiscountExempt(getEditingOrderList(), true);
                } else {
                    Helper.showDialogMessage(getActivity(), "No item to discount exempt", "Information");
                }
                break;
            case 110:// TEST PRINT

                BusProvider.getInstance().post(new PrintModel("CHEAT", "123131"));

                break;
            case 109://PRINT SOA
                if (Utils.isPasswordProtected(userViewModel, "123")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "SOA", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doSoaFunction();
                            }
                        };

                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });

                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }

                } else {
                    doSoaFunction();
                }




                break;
            case 107://TRANSFER ROOM
                if (Utils.isPasswordProtected(userViewModel, "69")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(),"TRANSFER TABLE", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doTransferRoomFunction();
                            }
                        };
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });

                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doTransferRoomFunction();
                }
                break;
            case 106://OPEN ROOM OR TABLES DEPENDING ON SYSTEM TYPE

                Intent roomsActivityIntent = new Intent(getContext(), RoomsActivity.class);
                roomsActivityIntent.putExtra(AppConstants.TRANS_ID, TextUtils.isEmpty(transactionId) ? 0 : Integer.valueOf(transactionId));
                roomsActivityIntent.putExtra(AppConstants.TRANSFER, "n");
                roomsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(roomsActivityIntent, ROOM_SELECTED_RETURN);
                break;
            case 996://OPEN VIEW RECEIPT
                if (Utils.isPasswordProtected(userViewModel, "125")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "VIEW RECEIPT", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doViewReceiptFunction();
                            }
                        };
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });

                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doViewReceiptFunction();
                }

                break;
            case 129://OPEN SETTINGS
                if (Utils.isPasswordProtected(userViewModel, "124")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "SETTINGS", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                Intent intent = new Intent(getContext(), SettingsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        };
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });

                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                    }
                } else {
                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;
            case 115://OPEN DISCOUNT DIALOG
                if (Utils.isPasswordProtected(userViewModel, "62")) {
                    if (passwordDialog == null) {

                        passwordDialog = new PasswordDialog(getActivity(), "DISCOUNT", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doDiscountFunction();
                            }
                        };

                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });

                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doDiscountFunction();
                }
                break;
            case 133://OPEN SHIFT CUT OFF DIALOG
                if (cutOffMenuDialog == null) {
                    cutOffMenuDialog = new CutOffMenuDialog(getActivity(), transactionsViewModel, userViewModel, dataSyncViewModel, cutOffViewModel);
                    cutOffMenuDialog.show();

                    cutOffMenuDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            cutOffMenuDialog = null;
                        }
                    });

                    cutOffMenuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            cutOffMenuDialog = null;
                        }
                    });
                }
                break;
            case 113://VOID TRANSACTION
                if (Utils.isPasswordProtected(userViewModel, "67")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "POST VOID", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doVoidTransactionFunction();
                            }
                        };
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doVoidTransactionFunction();
                }

//                startActivity(new Intent(getContext(), TransactionActivity.class));
                break;
            case 105://PAYMENT



                if (Utils.isPasswordProtected(userViewModel, "129")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "PAYMENT", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doPaymentFunction();
                            }
                        };
                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doPaymentFunction();
                }


                break;
            case 101://ITEM VOID
                if (getEditingOrderList().size() > 0) {
                    if (Utils.isPasswordProtected(userViewModel, "68")) {
                        if (passwordDialog == null) {
                            passwordDialog = new PasswordDialog(getActivity(),
                                    getContext().getString(R.string.item_void_password_dialog),
                                    userViewModel,
                                    transactionsViewModel) {
                                @Override
                                public void success(String username) {
                                    doItemVoidFunction();
                                }
                            };
                            passwordDialog.show();

                            passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    passwordDialog = null;
                                }
                            });

                            passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    passwordDialog = null;
                                }
                            });
                        }
                    } else {
                        doItemVoidFunction();
                    }


                } else {
                    Helper.showDialogMessage(getActivity(), "No item to void", "Information");
                }
                break;
            case 99://CHANGE QUANTITY
                if (getEditingOrderList().size() > 0) {
                    if (changeQtyDialog == null) {
                        changeQtyDialog = new ChangeQtyDialog(getActivity(), getEditingOrderList().get(0).getQty()) {
                            @Override
                            public void success(int newQty) {
                                try {
                                    for (Orders order : getEditingOrderList()) {

                                        if (transactionsViewModel.getBundledItems(String.valueOf(order.getId())).size() < 1) {
                                            if (newQty > order.getQty()) {
                                                order.setVatAmount(((newQty - order.getQty()) * order.getVatAmount()) + order.getVatAmount());
                                                order.setVatable(((newQty - order.getQty()) * order.getVatable()) + order.getVatable());
                                                order.setVatExempt(((newQty - order.getQty()) * order.getVatExempt()) + order.getVatExempt());
                                                order.setDiscountAmount(((newQty - order.getQty()) * order.getDiscountAmount()) + order.getDiscountAmount());
                                                order.setIs_sent_to_server(0);
                                                order.setQty(newQty);
                                            } else if (newQty < order.getQty()) {
                                                if (order.getIs_fixed_asset() == 0) {
                                                    order.setVatAmount((order.getVatAmount() / order.getQty()) * newQty);
                                                    order.setVatable((order.getVatable() / order.getQty()) * newQty);
                                                    order.setVatExempt((order.getVatExempt() / order.getQty()) * newQty);
                                                    order.setDiscountAmount((order.getDiscountAmount() / order.getQty()) * newQty);
                                                    order.setIs_sent_to_server(0);
                                                    order.setQty(newQty);
                                                } else {
                                                    Toast.makeText(getContext(), "Cannot subtract quantity from a fixed asset as it has serial number, void the item instead", Toast.LENGTH_LONG).show();
                                                }

                                            }


                                        }
                                        order.setIs_editing(false);
                                        transactionsViewModel.updateOrder(order);
                                    }
                                    transactionsViewModel.recomputeTransaction(transactionsViewModel.orderList(transactionId), transactionId);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }
                        };
                        changeQtyDialog.show();

                        changeQtyDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                changeQtyDialog = null;
                            }
                        });

                        changeQtyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                changeQtyDialog = null;
                            }
                        });
                    }

                } else {
                    Helper.showDialogMessage(getActivity(), "No item to change quantity", "Information");
                }
                break;
            case 100://SAVE TRANSACTION
                if (Utils.isPasswordProtected(userViewModel, "127")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "SAVE TRANSACTION", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doSaveTransactionFunction();
                            }
                        };
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doSaveTransactionFunction();
                }

                break;
            case 9988://RESUME TRANSACTION
                if (Utils.isPasswordProtected(userViewModel, "128")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "RESUME TRANSACTION", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doResumeTransaction();
                            }
                        };
                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doResumeTransaction();
                }

                break;
            case 116://CANCEL BACK TO DEFAULTS
                if (transactionsList().size() > 0) {
                    transactionId = String.valueOf(transactionsList().get(0).getId());
                    setOrderAdapter(transactionsViewModel.orderList(transactionId));
                } else {
                    defaults();
                }
                break;
        }
    }

    private String generatedReceiptNumber() throws ExecutionException, InterruptedException {
        String receiptNumber = "";
        if (transactionsViewModel.lastOrNumber() == null) {
            receiptNumber = Utils.getOrFormat("1");
        } else {
            if (TextUtils.isEmpty(transactionsViewModel.lastOrNumber().getReceipt_number())) {
                receiptNumber = Utils.getOrFormat("1");
            } else {
                receiptNumber =
                        Utils.getOrFormat(
                                String.valueOf(
                                        Integer.valueOf(
                                                transactionsViewModel.lastOrNumber().getReceipt_number().replaceFirst("0", "")) + 1));
            }

        }
        return receiptNumber;
    }

    private String generatedControlNumber() {
        String controlNumber = "";
        try {
            if (transactionsViewModel.lastTransactionId() == null) {
                controlNumber = Utils.getCtrlNumberFormat("1");
            } else {
                if (TextUtils.isEmpty(transactionsViewModel.lastTransactionId().getControl_number())) {
                    controlNumber = Utils.getCtrlNumberFormat("1");
                } else {
                    controlNumber = Utils.getCtrlNumberFormat(String.valueOf(Integer.valueOf(transactionsViewModel.lastTransactionId().getControl_number().replaceFirst("0", "")) + 1));
                }

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return controlNumber;
    }

    private void diDiscountExempt(List<Orders> ordersList, boolean willDiscountExempt) {
        for (Orders orders : ordersList) {
            if (willDiscountExempt) {
                orders.setIs_discount_exempt(1);
            } else {
                orders.setIs_discount_exempt(0);
            }
            orders.setIs_editing(false);
            transactionsViewModel.updateOrder(orders);
        }
    }

    private void doResumeTransaction() {

        Intent resumeTransactionIntent = new Intent(getContext(), ResumeTransactionActivity.class);
        resumeTransactionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(resumeTransactionIntent, RESUME_TRANS_RETURN);
    }

    private void doSaveTransactionFunction() {
        try {
            if (transactionsList().size() > 0) {
                if (inputDialog == null) {
                    inputDialog = new InputDialog(getActivity(), "Enter name of guest" , "") {
                        @Override
                        public void confirm(String str) {

                            try {
                                saveTransaction(transactionsList().get(0), str);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    inputDialog.show();

                    inputDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            inputDialog = null;
                        }
                    });

                    inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            inputDialog = null;
                        }
                    });
                }


            } else {
                if (loadedTransactionsList(transactionId).size() > 0) {

                    saveTransaction(loadedTransactionsList(transactionId).get(0), "");

                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doItemVoidFunction() {
        try {

            int incrementalId = 0;
            for (Orders order : getEditingOrderList()) {
                order.setIs_sent_to_server(0);
                order.setIs_void(true);
                order.setIs_editing(false);

                transactionsViewModel.updateOrder(order);

                incrementalId = order.getId();
                for (Orders bundle : transactionsViewModel.getBundledItems(String.valueOf(incrementalId))) {
                    bundle.setIs_void(true);
                    transactionsViewModel.updateOrder(bundle);
                }

                for (SerialNumbers sn : transactionsViewModel.serialNumberFromOrderId(order.getId())) {
                    sn.setIs_void(true);
                    sn.setIs_void_at(Utils.getDateTimeToday());
                    transactionsViewModel.updateSerialNumbers(sn);
                }
            }



            Transactions currentTrans = transactionsViewModel.loadedTransactionList(transactionId).get(0);
            if (currentTrans.getRoom_id() != 0) {
                List<Orders> currentPunchedRoomRate = transactionsViewModel.roomRateList(transactionId);
                if (currentPunchedRoomRate.size() < 1) {
                    currentTrans.setIs_sent_to_server(0);
                    currentTrans.setRoom_number("");
                    currentTrans.setCheck_in_time("");
                    currentTrans.setCheck_out_time("");
                    currentTrans.setRoom_id(0);
                    transactionsViewModel.update(currentTrans);
                    changeRoomStatus(roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)), 3, true);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doPaymentFunction() {
        if (!TextUtils.isEmpty(transactionId)) {
            try {
                int serialNeededCount = 0;
                int serialNumberEncodedCount = 0;
                for (Orders ord : transactionsViewModel.orderListWithFixedAsset(transactionId)) {
                    serialNeededCount += ord.getQty();
                }

                for (SerialNumbers sn : transactionsViewModel.serialNumberFromTransaction(Integer.valueOf(transactionId))) {
                    if (!TextUtils.isEmpty(sn.getSerial_number())) {
                        serialNumberEncodedCount += 1;
                    }
                }
                if (serialNeededCount ==
                serialNumberEncodedCount) {

                    if (transactionsViewModel.orderList(transactionId).size() > 0) {
                        if (paymentDialog == null) {
                            paymentDialog = new PaymentDialog(getActivity(), dataSyncViewModel,
                                    transactionsViewModel, transactionId,
                                    userViewModel, roomsViewModel,
                                    serviceChargeViewModel) {
                                @Override
                                public void completed(String receiptNumber) {


                                    try {
                                        BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(receiptNumber))));
                                        //                                        BusProvider.getInstance().post(new PrintModel("CHEAT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(receiptNumber))));
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        if (transactionsList().size() > 0) {
                                            selectedTable = null;
                                            transactionId = String.valueOf(transactionsList().get(0).getId());
                                            setOrderAdapter(transactionsViewModel.orderList(transactionId));


                                        } else {
                                            defaults();
                                        }
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            paymentDialog.show();

                            paymentDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    paymentDialog = null;
                                }
                            });

                            paymentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    paymentDialog = null;
                                }
                            });
                        }

                    } else {
                        Helper.showDialogMessage(getActivity(),
                                getContext().getString(R.string.error_no_items),
                                getContext().getString(R.string.header_message));
                    }


                } else {


                    SerialNumbersDialog serialNumbersDialog = new SerialNumbersDialog(getContext(), transactionsViewModel, transactionId) {
                        @Override
                        public void submitted() {

                            try {
                                int snc = 0;
                                int sec = 0;
                                for (Orders ord : transactionsViewModel.orderListWithFixedAsset(transactionId)) {
                                    snc += ord.getQty();
                                }

                                for (SerialNumbers sn : transactionsViewModel.serialNumberFromTransaction(Integer.valueOf(transactionId))) {
                                    if (!TextUtils.isEmpty(sn.getSerial_number())) {
                                        sec += 1;
                                    }
                                }
                                if (snc ==
                                        sec) {

                                    if (transactionsViewModel.orderList(transactionId).size() > 0) {
                                        if (paymentDialog == null) {
                                            paymentDialog = new PaymentDialog(getActivity(), dataSyncViewModel,
                                                    transactionsViewModel, transactionId,
                                                    userViewModel, roomsViewModel,
                                                    serviceChargeViewModel) {
                                                @Override
                                                public void completed(String receiptNumber) {


                                                    try {
                                                        BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(receiptNumber))));
                                                        //                                        BusProvider.getInstance().post(new PrintModel("CHEAT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(receiptNumber))));
                                                    } catch (ExecutionException e) {
                                                        e.printStackTrace();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        if (transactionsList().size() > 0) {
                                                            selectedTable = null;
                                                            transactionId = String.valueOf(transactionsList().get(0).getId());
                                                            setOrderAdapter(transactionsViewModel.orderList(transactionId));


                                                        } else {
                                                            defaults();
                                                        }
                                                    } catch (ExecutionException e) {
                                                        e.printStackTrace();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            paymentDialog.show();

                                            paymentDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialogInterface) {
                                                    paymentDialog = null;
                                                }
                                            });

                                            paymentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    paymentDialog = null;
                                                }
                                            });
                                        }

                                    } else {
                                        Helper.showDialogMessage(getActivity(),
                                                getContext().getString(R.string.error_no_items),
                                                getContext().getString(R.string.header_message));
                                    }

                                }
                            } catch (Exception e) {

                            }

                        }
                    };
                    serialNumbersDialog.show();
                    Toast.makeText(getContext(), "Please enter serial number for fixed asset items", Toast.LENGTH_LONG).show();
                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            Helper.showDialogMessage(getActivity(),
                    getContext().getString(R.string.error_no_transaction),
                    getContext().getString(R.string.header_message));
        }
    }

    private void doVoidTransactionFunction() {
        if (transactionDialog == null) {
            transactionDialog = new TransactionDialog(getActivity(),
                    getContext().getString(R.string.title_completed_transactions),
                    transactionsViewModel,
                    userViewModel,
                    true);
            transactionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    transactionDialog = null;
                }
            });
            transactionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    transactionDialog = null;
                }
            });

            transactionDialog.show();
        }
    }

    private void doDiscountFunction() {
        if (!TextUtils.isEmpty(transactionId)) {
            try {

                if (transactionsViewModel.orderList(transactionId).size() > 0) {

                    if (getEditingOrderList().size() > 0) {
                        if (discountMenuDialog == null) {
                            boolean hasItemToDiscount = false;
                            for (Orders ord : transactionsViewModel.orderList(transactionId)) {
                                if (ord.getIs_discount_exempt() == 0) {
                                    hasItemToDiscount = true;
                                    break;
                                }
                            }
                            if (hasItemToDiscount) {
                                discountMenuDialog = new DiscountMenuDialog(getActivity(), discountViewModel,
                                        transactionsViewModel, transactionId);
                                discountMenuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        discountMenuDialog = null;
                                    }
                                });

                                discountMenuDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        discountMenuDialog = null;
                                    }
                                });
                                discountMenuDialog.show();
                            } else {
                                Helper.showDialogMessage(getActivity(),
                                        getContext().getString(R.string.no_item_to_discount_disc_exempt),
                                        getContext().getString(R.string.header_message));
                            }

                        }
                    } else {
                        Helper.showDialogMessage(getActivity(),
                                getContext().getString(R.string.no_item_to_discount),
                                getContext().getString(R.string.header_message));
                    }




                } else {
                    Helper.showDialogMessage(getActivity(),
                            getContext().getString(R.string.error_no_items_disc),
                            getContext().getString(R.string.header_message));
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            Helper.showDialogMessage(getActivity(),
                    getContext().getString(R.string.error_no_transaction_disc),
                    getContext().getString(R.string.header_message));
        }
    }

    private void doViewReceiptFunction() {
        if (transactionDialog == null) {
            transactionDialog = new TransactionDialog(getActivity(),
                    getContext().getString(R.string.title_reprint_transactions),
                    transactionsViewModel,
                    userViewModel,
                    false);
            transactionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    transactionDialog = null;
                }
            });
            transactionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    transactionDialog = null;
                }
            });

            transactionDialog.show();
        }
    }

    private void doTransferRoomFunction() {
        try {
            if (transactionsViewModel.loadedTransactionList(transactionId).size() > 0) {
                Transactions currentTrans = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                if (currentTrans.getRoom_id() != 0) {
                    Rooms rooms = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                    if (rooms != null) {
                        Intent roomsActivityIntent = new Intent(getContext(), RoomsActivity.class);
                        roomsActivityIntent.putExtra(AppConstants.TRANS_ID, TextUtils.isEmpty(transactionId) ? 0 : Integer.valueOf(transactionId));
                        roomsActivityIntent.putExtra(AppConstants.TRANSFER, "y");
                        roomsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(roomsActivityIntent, ROOM_SELECTED_RETURN);
                    } else {
                        Helper.showDialogMessage(getContext(), "No room attached to orders", getString(R.string.header_message));
                    }
                } else {
                    Helper.showDialogMessage(getActivity(), "No room attached to transaction", getString(R.string.header_message));
                }
            } else {
                Helper.showDialogMessage(getActivity(), "No room attached to transaction", getString(R.string.header_message));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doSoaFunction() {
        try {
            if (transactionsViewModel.loadedTransactionList(transactionId).size() > 0) {
                Transactions currentTrans = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                if (currentTrans.getRoom_id() != 0) {
                    Rooms rooms = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                    if (rooms != null) {
                        BusProvider.getInstance().post(new PrintModel("SOA", GsonHelper.getGson().toJson(transactionsViewModel.getTransactionViaTransactionId(rooms.getTransaction_id()))));
                    } else {
                        Helper.showDialogMessage(getContext(), "No room/table attached to orders", getString(R.string.header_message));
                    }
                } else {
                    Helper.showDialogMessage(getActivity(), "No room/table attached to transaction", getString(R.string.header_message));
                }
            } else {
                Helper.showDialogMessage(getActivity(), "No room/table attached to transaction", getString(R.string.header_message));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ROOM_SELECTED_RETURN:
                if (resultCode == Activity.RESULT_OK) {
                    switch (data.getExtras().getString("type")) {
                        case "table":
                            Rooms mSelectedRoom = GsonHelper.getGson().fromJson(data.getExtras().getString("selected_room"), Rooms.class);
                            selectedTable = mSelectedRoom;
                            switch (data.getExtras().getString("case")) {
                                case "transfer_table":
                                    selectedTable.setTransaction_id(transactionId);
                                    selectedTable.setCheck_in_time(Utils.getDateTimeToday());
                                    roomsViewModel.update(mSelectedRoom);
                                    if (!TextUtils.isEmpty(mSelectedRoom.getCheck_in_time())) {
                                        tvRoomTableNumber.setText("TABLE:" + mSelectedRoom.getRoom_name() +"-" + Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), mSelectedRoom.getCheck_in_time()));
                                    } else {
                                        tvRoomTableNumber.setText("TABLE:NA");
                                    }

                                    try {
                                        List<Transactions> tmpTrList = loadedTransactionsList(transactionId);
                                        if (tmpTrList.size() > 0) {
                                            Transactions tr = tmpTrList.get(0);
                                            tr.setRoom_number(mSelectedRoom.getRoom_name());
                                            tr.setRoom_id(mSelectedRoom.getRoom_id());
                                            transactionsViewModel.update(tr);
                                        }
                                    } catch (Exception e) {

                                    }

                                    break;
                                case "load_existing_data":
                                    try {
                                        transactionId = mSelectedRoom.getTransaction_id();

                                        if (roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)) != null) {
                                            if (!TextUtils.isEmpty(roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time())) {
                                                tvRoomTableNumber.setText("TABLE:" + roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getRoom_name() + "-" + Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time()));
                                            } else {
                                                tvRoomTableNumber.setText("TABLE:NA");
                                            }

                                        } else {
                                            tvRoomTableNumber.setText("TABLE:" + "NA");
                                        }

                                        setOrderAdapter(transactionsViewModel.orderList(transactionId));
                                    }catch (Exception e) {

                                    }

                                    break;
                                case "new_trans_with_room":

                                    String controlNumber = "";
                                    try {
                                        if (transactionsViewModel.lastTransactionId() == null) {
                                            controlNumber = Utils.getCtrlNumberFormat("1");
                                        } else {
                                            if (TextUtils.isEmpty(transactionsViewModel.lastTransactionId().getControl_number())) {
                                                controlNumber = Utils.getCtrlNumberFormat("1");
                                            } else {
                                                controlNumber = Utils.getCtrlNumberFormat(String.valueOf(Integer.valueOf(transactionsViewModel.lastTransactionId().getControl_number().replaceFirst("0", "")) + 1));
                                            }

                                        }
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                    List<Transactions> tr = new ArrayList<>();

                                    try {
                                        Transactions trs = new Transactions(
                                                controlNumber,
                                                getUser().getUsername(),
                                                Utils.getDateTimeToday(),
                                                0,
                                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                        );
                                        trs.setRoom_number(selectedTable.getRoom_name());
                                        trs.setRoom_id(selectedTable.getRoom_id());
                                        trs.setCheck_in_time(Utils.getDateTimeToday());
                                        tr.add(trs);

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    transactionsViewModel.insert(tr);

                                    break;
                                case "existing_trans_new_room":
                                    try {
                                        Rooms rooms = roomsViewModel.getRoomViaId(mSelectedRoom.getRoom_id());
                                        rooms.setTransaction_id(transactionId);
                                        rooms.setCheck_in_time(Utils.getDateTimeToday());
                                        roomsViewModel.update(rooms);

                                        List<Orders> tmpOrds = transactionsViewModel.orderList(transactionId);
                                        if (tmpOrds.size() > 0) {
                                            for (Orders ord : tmpOrds) {
                                                ord.setIs_take_out(0);
                                                transactionsViewModel.updateOrder(ord);
                                            }
                                        }
                                    }catch (Exception e) {

                                    }
                                    break;
                            }

                            break;
                        case "room":

                            selectedRoomRate = GsonHelper.getGson().fromJson(data.getExtras().getString("selected_room"), RoomRates.class);

                            try {
                                if (!TextUtils.isEmpty(transactionId)) {

                                    Rooms rooms = roomsViewModel.getRoomViaId(selectedRoomRate.getRoom_id());
                                    rooms.setTransaction_id(transactionId);
                                    roomsViewModel.update(rooms);

                                    insertSelectedRoomRate();
                                } else {
                                    if (transactionsList().size() > 0) {

                                        Rooms rooms = roomsViewModel.getRoomViaId(selectedRoomRate.getRoom_id());
                                        rooms.setTransaction_id(transactionId);
                                        roomsViewModel.update(rooms);


                                        insertSelectedRoomRate();
                                    } else {
                                        String controlNumber = "";
                                        try {
                                            if (transactionsViewModel.lastTransactionId() == null) {
                                                controlNumber = Utils.getCtrlNumberFormat("1");
                                            } else {
                                                if (TextUtils.isEmpty(transactionsViewModel.lastTransactionId().getControl_number())) {
                                                    controlNumber = Utils.getCtrlNumberFormat("1");
                                                } else {
                                                    controlNumber = Utils.getCtrlNumberFormat(String.valueOf(Integer.valueOf(transactionsViewModel.lastTransactionId().getControl_number().replaceFirst("0", "")) + 1));
                                                }

                                            }
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }


                                        List<Transactions> tr = new ArrayList<>();

                                        tr.add(new Transactions(
                                                controlNumber,
                                                getUser().getUsername(),
                                                Utils.getDateTimeToday(),
                                                0,
                                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                        ));

                                        transactionsViewModel.insert(tr);


                                    }
                                }

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }



                            break;
                    }


                }
                break;
            case RESUME_TRANS_RETURN:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        transactionId = data.getExtras().getString(AppConstants.TRANS_ID);
                        setOrderAdapter(transactionsViewModel.orderList(transactionId));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (transactionsList().size() > 0) {
                            transactionId = String.valueOf(transactionsList().get(0).getId());
                            setOrderAdapter(transactionsViewModel.orderList(transactionId));
                        } else {
                            defaults();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void defaults() {
        selectedTable = null;
        transactionId = "";
        tvRoomTableNumber.setText("TABLE:NA");
        setOrderAdapter(new ArrayList<Orders>());
    }

    @Subscribe
    public void productToCheckout(final ProductToCheckout productsModel) {
        mQty = 1;
        try {
            final List<ProductAlacart> alacartList = transactionsViewModel.getBranchAlacart(String.valueOf(productsModel.getProducts().getCore_id()));
            final List<BranchGroup> branchGroupList = transactionsViewModel.getBranchGroup(String.valueOf(productsModel.getProducts().getCore_id()));

            if (branchGroupList.size() > 0) { //SHOW SELECTION OF PRODUCT FROM GROUP AND INCLUDED IN ALACART
                //show dialog for bundle

                ChangeQtyDialog changeQtyDialog = new ChangeQtyDialog(getActivity(), 1) {
                    @Override
                    public void success(final int newQty) {
                        BundleCompositionDialog bundleCompositionDialog =
                                new BundleCompositionDialog(getActivity(), alacartList,
                                        branchGroupList, transactionsViewModel,
                                        newQty) {
                                    @Override
                                    public void bundleCompleted(List<BranchGroup> bgList) {
                                        mQty = newQty;
                                        mAlacartList = alacartList;
                                        mBranchGroupList = bgList;


                                        selectedProduct = productsModel.getProducts();
                                        if (!TextUtils.isEmpty(transactionId)) {
                                            insertSelectedOrder();
                                        } else {
                                            try {
                                                if (transactionsList().size() > 0) {
                                                    insertSelectedOrder();
                                                } else {
                                                    String controlNumber = "";
                                                    try {
                                                        if (transactionsViewModel.lastTransactionId() == null) {
                                                            controlNumber = Utils.getCtrlNumberFormat("1");
                                                        } else {
                                                            if (TextUtils.isEmpty(transactionsViewModel.lastTransactionId().getControl_number())) {
                                                                controlNumber = Utils.getCtrlNumberFormat("1");
                                                            } else {
                                                                controlNumber = Utils.getCtrlNumberFormat(String.valueOf(Integer.valueOf(transactionsViewModel.lastTransactionId().getControl_number().replaceFirst("0", "")) + 1));
                                                            }

                                                        }
                                                    } catch (ExecutionException e) {
                                                        e.printStackTrace();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }


                                                    List<Transactions> tr = new ArrayList<>();

                                                    tr.add(new Transactions(
                                                            controlNumber,
                                                            getUser().getUsername(),
                                                            Utils.getDateTimeToday(),
                                                            0,
                                                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                                    ));
                                                    transactionsViewModel.insert(tr);


                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }
                                };
                        bundleCompositionDialog.show();
                    }
                };
                changeQtyDialog.show();

            } else if (alacartList.size() > 0){ //SHOW INCLUDED IN ALACART
                //show dialog for alacart confirmation
                ChangeQtyDialog changeQtyDialog = new ChangeQtyDialog(getContext(), 1) {
                    @Override
                    public void success(int newQty) {
                        mAlacartList = alacartList;
                        mQty = newQty;
                        AlacartCompositionDialog alacartCompositionDialog = new AlacartCompositionDialog(getActivity(), alacartList) {
                            @Override
                            public void confirmed() {
                                selectedProduct = productsModel.getProducts();
                                if (!TextUtils.isEmpty(transactionId)) {
                                    insertSelectedOrder();
                                } else {
                                    try {
                                        if (transactionsList().size() > 0) {
                                            insertSelectedOrder();
                                        } else {
                                            String controlNumber = "";
                                            try {
                                                if (transactionsViewModel.lastTransactionId() == null) {
                                                    controlNumber = Utils.getCtrlNumberFormat("1");
                                                } else {
                                                    if (TextUtils.isEmpty(transactionsViewModel.lastTransactionId().getControl_number())) {
                                                        controlNumber = Utils.getCtrlNumberFormat("1");
                                                    } else {
                                                        controlNumber = Utils.getCtrlNumberFormat(String.valueOf(Integer.valueOf(transactionsViewModel.lastTransactionId().getControl_number().replaceFirst("0", "")) + 1));
                                                    }

                                                }
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }


                                            List<Transactions> tr = new ArrayList<>();

                                            tr.add(new Transactions(
                                                    controlNumber,
                                                    getUser().getUsername(),
                                                    Utils.getDateTimeToday(),
                                                    0,
                                                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                            ));
                                            transactionsViewModel.insert(tr);


                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        };
                        alacartCompositionDialog.show();
                    }
                };
                changeQtyDialog.show();

            } else {
                selectedProduct = productsModel.getProducts();
                if (!TextUtils.isEmpty(transactionId)) {
                    insertSelectedOrder();
                } else {
                    if (transactionsList().size() > 0) {
                        insertSelectedOrder();
                    } else {
                        String controlNumber = "";
                        try {
                            if (transactionsViewModel.lastTransactionId() == null) {
                                controlNumber = Utils.getCtrlNumberFormat("1");
                            } else {
                                if (TextUtils.isEmpty(transactionsViewModel.lastTransactionId().getControl_number())) {
                                    controlNumber = Utils.getCtrlNumberFormat("1");
                                } else {
                                    controlNumber = Utils.getCtrlNumberFormat(String.valueOf(Integer.valueOf(transactionsViewModel.lastTransactionId().getControl_number().replaceFirst("0", "")) + 1));
                                }

                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        List<Transactions> tr = new ArrayList<>();

                        tr.add(new Transactions(
                                controlNumber,
                                getUser().getUsername(),
                                Utils.getDateTimeToday(),
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                        ));
                        transactionsViewModel.insert(tr);


                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (productsModel.getProducts().getIs_fixed_asset() == 1) {
            if (!TextUtils.isEmpty(transactionId)) {
                SerialNumbersDialog serialNumbersDialog = new SerialNumbersDialog(getContext(), transactionsViewModel, transactionId) {
                    @Override
                    public void submitted() {

                    }
                };
                serialNumbersDialog.show();
            }

        }
    }

    private void insertSelectedRoomRate() {
        List<Orders> orderList = new ArrayList<>();
        if (!TextUtils.isEmpty(transactionId)) {
            if (selectedRoomRate != null) {
                orderList.add(new Orders(
                        Integer.valueOf(transactionId),
                        selectedRoomRate.getRoom_rate_price_id(),
                        1,
                        selectedRoomRate.getAmount(),
                        selectedRoomRate.getAmount(),
                        selectedRoomRate.getRoom_rate_description(),
                        selectedRoomRate.getRoom_type_id(),
                        Utils.roundedOffTwoDecimal((selectedRoomRate.getAmount() / 1.12) * .12),
                        Utils.roundedOffTwoDecimal(selectedRoomRate.getAmount() / 1.12),
                        0.00,
                        0.00,
                        "ROOM RATE",
                        "ROOM RATE",
                        0,
                        0,
                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                        Utils.getDateTimeToday(),
                        1,
                        "",
                        0,
                        0
                ));
                transactionsViewModel.insertOrder(orderList);
            }

        }

        selectedRoomRate = null;
    }



    private void insertSelectedOrder() {
        int is_to = 1;
        if (!TextUtils.isEmpty(transactionId)) {
            if (selectedProduct != null) {


                List<Orders> orderList = new ArrayList<>();

                try {
                    Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                    if (tmpRm != null) is_to = 0;
                } catch (Exception e) {

                }

                TypeToken<List<FetchProductsResponse.ProductPromo>> token = new TypeToken<List<FetchProductsResponse.ProductPromo>>() {};
                List<FetchProductsResponse.ProductPromo> promo = GsonHelper.getGson().fromJson(selectedProduct.getJson_promo(), token.getType());

                Double productFinalAmount = selectedProduct.getAmount();
                DateTime dateToday = new DateTime(Utils.getCurrentDate());
                if (promo.size() > 0) {
                    for (FetchProductsResponse.ProductPromo pp : promo) {
                        if (TextUtils.isEmpty(pp.getEndDate()) && TextUtils.isEmpty(pp.getEndTime())) {
                            //PRICE CHANGE
                           DateTime promoDateStart =  new DateTime(pp.getStartDate());
                           if (dateToday.isAfter(promoDateStart)) {
                               if (pp.getIsPercentage() == 1) {
                                    productFinalAmount = selectedProduct.getAmount() - (selectedProduct.getAmount() * (pp.getValue() / 100));
                               } else {
                                   productFinalAmount = pp.getValue();
                               }
                           }

                        }
                    }

                    for (FetchProductsResponse.ProductPromo pp : promo) {
                        if (!TextUtils.isEmpty(pp.getStartDate()) && !TextUtils.isEmpty(pp.getEndDate())) {
                            DateTime promoDateStart =  new DateTime(pp.getStartDate());
                            DateTime promoDateEnd =  new DateTime(pp.getEndDate());
                            //PROMO
                            if (dateToday.isAfter(promoDateStart) && dateToday.isBefore(promoDateEnd)) {
                                if (pp.getIsPercentage() == 1) {
                                    productFinalAmount = productFinalAmount - (productFinalAmount * (pp.getValue() / 100));
                                } else {
                                    productFinalAmount = pp.getValue();
                                }
                            }
                        }
                    }
                }

                Orders orders = new Orders(
                        Integer.valueOf(transactionId),
                        selectedProduct.getCore_id(),
                        1 * mQty,
                        productFinalAmount,
                        productFinalAmount,
                        selectedProduct.getProduct(),
                        selectedProduct.getDepartmentId(),
                        Utils.roundedOffTwoDecimal((selectedProduct.getAmount() / 1.12) * .12),
                        Utils.roundedOffTwoDecimal(selectedProduct.getAmount() / 1.12),
                        0.00,
                        0.00,
                        selectedProduct.getDepartment(),
                        selectedProduct.getCategory(),
                        selectedProduct.getCategoryId(),
                        0,
                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                        Utils.getDateTimeToday(),
                        0,
                        "",
                        is_to,
                        selectedProduct.getIs_fixed_asset()
                );
//                orders.setIs_editing(true);
                orderList.add(orders);
                transactionsViewModel.insertOrder(orderList);


//                transactionsViewModel.updateEditingOrderList(transactionId);


            }

            if (mAlacartList.size() > 0) {
                final Handler handler = new Handler();
                final int finalIs_to = is_to;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (transactionsViewModel.orderList(transactionId).size() > 0) {
                                Orders lastOrder = transactionsViewModel.orderList(transactionId).get(transactionsViewModel.orderList(transactionId).size() - 1);
                                lastIncrementalId = lastOrder.getId();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                        for (ProductAlacart palac : mAlacartList) {
                            List<Orders> orderList = new ArrayList<>();


                            Orders orders = new Orders(
                                    Integer.valueOf(transactionId),
                                    palac.getProduct_id(),
                                    (int) palac.getQty() * mQty,
                                    0.00,
                                    0.00,
                                    palac.getProduct(),
                                    0,
                                    0.00,
                                    0.00,
                                    0.00,
                                    0.00,
                                    "",
                                    "",
                                    0,
                                    0,
                                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                                    Utils.getDateTimeToday(),
                                    0,
                                    "",
                                    finalIs_to,
                                    0
                            );
                            orders.setProduct_alacart_id(palac.getProduct_alacart_id());
                            orders.setIs_editing(false);
                            orders.setOrders_incremental_id(lastIncrementalId);
                            orderList.add(orders);
                            transactionsViewModel.insertOrder(orderList);


                        }

                        mAlacartList = new ArrayList<>();

                    }
                }, 300);
            }

            if (mAlacartList.size() == 0) lastIncrementalId = 0;

            if (mBranchGroupList.size() > 0) {
                final Handler handler = new Handler();
                final int finalIs_to1 = is_to;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (transactionsViewModel.orderList(transactionId).size() > 0) {
                                if (lastIncrementalId == 0) {
                                    Orders lastOrder = transactionsViewModel.orderList(transactionId).get(transactionsViewModel.orderList(transactionId).size() - 1);
                                    lastIncrementalId = lastOrder.getId();
                                }

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                        for (BranchGroup palac : mBranchGroupList) {
                            List<Orders> orderList = new ArrayList<>();


                            Orders orders = new Orders(
                                    Integer.valueOf(transactionId),
                                    palac.getProduct_id(),
                                    (int) palac.getSelectedQty(),
                                    palac.getPrice(),
                                    palac.getPrice(),
                                    palac.getProduct(),
                                    0,
                                    0.00,
                                    0.00,
                                    0.00,
                                    0.00,
                                    "",
                                    "",
                                    0,
                                    0,
                                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                                    Utils.getDateTimeToday(),
                                    0,
                                    "",
                                    finalIs_to1,
                                    0
                            );
                            orders.setProduct_alacart_id(palac.getProduct_group_id());
                            orders.setIs_editing(false);
                            orders.setOrders_incremental_id(lastIncrementalId);
                            orderList.add(orders);
                            transactionsViewModel.insertOrder(orderList);
                        }
                        mBranchGroupList = new ArrayList<>();
                    }
                }, 300);
            }



        }

    }

    @Override
    public void longClicked(final Orders orders, View v) {

        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionOpenPrice:
                        if (openPriceDialog == null) {
                            openPriceDialog = new OpenPriceDialog(getActivity(), transactionsViewModel, orders) {
                                @Override
                                public void openPriceSuccess(Orders orders) {
                                    transactionsViewModel.recomputeTransactionWithDiscount(transactionId, discountViewModel);
                                }
                            };

                            openPriceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    openPriceDialog = null;
                                }
                            });

                            openPriceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    openPriceDialog = null;
                                }
                            });

                            openPriceDialog.show();
                        }


                        return true;
                    case R.id.actionItemModifier:

                        if (inputDialog == null) {
                            inputDialog = new InputDialog(getActivity(), "Item request modifier for " + orders.getName() , orders.getNotes()) {
                                @Override
                                public void confirm(String str) {

                                    orders.setNotes(str);

                                    transactionsViewModel.updateOrder(orders);
                                }
                            };
                            inputDialog.show();

                            inputDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    inputDialog = null;
                                }
                            });

                            inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    inputDialog = null;
                                }
                            });
                        }



                        return true;
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_checkout_item, popup.getMenu());
        popup.show();


    }

    @Override
    public void clicked(Orders orders) {
        if (orders.getIs_editing()) {
            orders.setIs_editing(false);
        } else {
            orders.setIs_editing(true);
        }

        transactionsViewModel.updateOrder(orders);
    }

    private List<Orders> getEditingOrderList() throws ExecutionException, InterruptedException {
        return transactionsViewModel.editingOrderList(transactionId);
    }

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initThemeSelectionListener();
    }

    private void initThemeSelectionListener() {
        DataSyncViewModel dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
        dataSyncViewModel.getThemeSelectionLiveData().observe(this, new Observer<List<ThemeSelection>>() {
            @Override
            public void onChanged(List<ThemeSelection> themeSelectionList) {
                for (ThemeSelection tsl : themeSelectionList) {
                    if (tsl.getIs_selected()) {
                        if (tsl.getTheme_id() == 100) { // LIGHT MODE
                            isDarkMode = false;
                            listCheckoutItems.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                            header.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            subTotalValue.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            deposit.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            discountValue.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            depositLabel.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            discountLabel.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            subTotalLabel.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            totalLabel.setBackgroundColor(getResources().getColor(R.color.colorLtGrey));
                            totalValue.setBackgroundColor(getResources().getColor(R.color.colorLtGrey));
                            listItemName.setBackgroundColor(getResources().getColor(R.color.colorLtGrey));
                            listItemQty.setBackgroundColor(getResources().getColor(R.color.colorLtGrey));
                            listItemPrice.setBackgroundColor(getResources().getColor(R.color.colorLtGrey));
                            rootRel.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            rootCard.setBackgroundColor(getResources().getColor(R.color.colorWhite));



//                            header.setTextColor(getResources().getColor(R.color.colorBlack));

                            listItemName.setTextColor(getResources().getColor(R.color.colorBlack));
                            listItemQty.setTextColor(getResources().getColor(R.color.colorBlack));
                            listItemPrice.setTextColor(getResources().getColor(R.color.colorBlack));

                            subTotalValue.setTextColor(getResources().getColor(R.color.colorBlack));
                            discountValue.setTextColor(getResources().getColor(R.color.colorBlack));
                            deposit.setTextColor(getResources().getColor(R.color.colorBlack));
                            totalValue.setTextColor(getResources().getColor(R.color.colorBlack));
                            depositLabel.setTextColor(getResources().getColor(R.color.colorBlack));
                            discountLabel.setTextColor(getResources().getColor(R.color.colorBlack));
                            subTotalLabel.setTextColor(getResources().getColor(R.color.colorBlack));
                            totalLabel.setTextColor(getResources().getColor(R.color.colorBlack));

                            try {
                                if (transactionsList().size() > 0) {
                                    transactionId = String.valueOf(transactionsList().get(0).getId());
                                    setOrderAdapter(transactionsViewModel.orderList(transactionId));
                                } else {
                                    defaults();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }


                            break;
                        } else { // DARK MODE
                            isDarkMode = true;
                            listCheckoutItems.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
//                            header.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            subTotalValue.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            deposit.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            discountValue.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            depositLabel.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            discountLabel.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            subTotalLabel.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            totalLabel.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            totalValue.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            listItemName.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            listItemQty.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            listItemPrice.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            rootRel.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            rootCard.setBackgroundColor(getResources().getColor(R.color.colorBlack));



//                            header.setTextColor(getResources().getColor(R.color.colorWhite));
                            listItemName.setTextColor(getResources().getColor(R.color.colorWhite));
                            listItemQty.setTextColor(getResources().getColor(R.color.colorWhite));
                            listItemPrice.setTextColor(getResources().getColor(R.color.colorWhite));
                            subTotalValue.setTextColor(getResources().getColor(R.color.colorWhite));
                            discountValue.setTextColor(getResources().getColor(R.color.colorWhite));
                            deposit.setTextColor(getResources().getColor(R.color.colorWhite));
                            totalValue.setTextColor(getResources().getColor(R.color.colorWhite));
                            depositLabel.setTextColor(getResources().getColor(R.color.colorWhite));
                            discountLabel.setTextColor(getResources().getColor(R.color.colorWhite));
                            subTotalLabel.setTextColor(getResources().getColor(R.color.colorWhite));
                            totalLabel.setTextColor(getResources().getColor(R.color.colorWhite));

                            try {
                                if (transactionsList().size() > 0) {
                                    transactionId = String.valueOf(transactionsList().get(0).getId());
                                    setOrderAdapter(transactionsViewModel.orderList(transactionId));
                                } else {
                                    defaults();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }

        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRemoveRoomTable:
                try {
                    if (!TextUtils.isEmpty(transactionId)) {
                        Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                        List<Transactions> tmpTrLst = transactionsViewModel.loadedTransactionList(transactionId);


                        if (tmpRm != null) {
                            if (!TextUtils.isEmpty(roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time())) {
                                tvRoomTableNumber.setText("TABLE:" + roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getRoom_name() + "-" + Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)).getCheck_in_time()));
                            } else {
                                tvRoomTableNumber.setText("TABLE:NA");
                            }
                            tmpRm.setTransaction_id("");
                            tmpRm.setCheck_in_time("");
                            tmpRm.setReservation_name("");
                            tmpRm.setReservation_time("");
                            tmpRm.setTime_reservation_made("");
                            changeRoomStatus(tmpRm, 1 , false);

                            List<Orders> tmpOrds = transactionsViewModel.orderList(transactionId);
                            if (tmpOrds.size() > 0) {
                                for (Orders ord : tmpOrds) {
                                    ord.setIs_take_out(1);
                                    transactionsViewModel.updateOrder(ord);
                                }
                            }
                        } else {
                            tvRoomTableNumber.setText("TABLE:" + "NA");
                        }


                        if (tmpTrLst.size() > 0) {
                            Transactions tns = tmpTrLst.get(0);
                            tns.setCheck_in_time("");
                            tns.setRoom_id(0);
                            tns.setRoom_number("");
                            transactionsViewModel.update(tns);
                        }

                        selectedTable = null;


                    } else {
                        Helper.showDialogMessage(getActivity(), "No Table to remove", getString(R.string.header_message));
                    }

                } catch (Exception e) {

                }



                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
            lin00.setVisibility(View.GONE);
        } else {
            if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
                lin00.setVisibility(View.GONE);
            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                lin00.setVisibility(View.VISIBLE);
            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                lin00.setVisibility(View.VISIBLE);
            }
        }


        try {
            if (transactionsList().size() > 0) {
                setOrderAdapter(transactionsViewModel.orderList(transactionId));
            }
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }


    }
}
