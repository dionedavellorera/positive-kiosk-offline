package com.nerdvana.positiveoffline.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.CreditCardAdapter;
import com.nerdvana.positiveoffline.adapter.PaymentTypeAdapter;
import com.nerdvana.positiveoffline.adapter.PaymentsAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.CreditCardContract;
import com.nerdvana.positiveoffline.intf.PaymentTypeContract;
import com.nerdvana.positiveoffline.intf.PaymentsContract;
import com.nerdvana.positiveoffline.model.CreditCardListModel;
import com.nerdvana.positiveoffline.model.SettingsMenuModel;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.view.ProgressButton;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.RoomsViewModel;
import com.nerdvana.positiveoffline.viewmodel.ServiceChargeViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.view.View.GONE;

public abstract class PaymentDialog extends BaseDialog implements PaymentTypeContract, View.OnClickListener, PaymentsContract {
    private int selectedCreditCardId = 0;
    private String selectedCreditCardName = "";
    private CreditCardAdapter creditCardAdapter;
    private RecyclerView listPayments;
    private RelativeLayout formCash;
    private LinearLayout formCard;
    private LinearLayout formOnline;
    private LinearLayout formVoucher;
    private LinearLayout formForex;
    private LinearLayout formGuestInfo;
    private Button add;
    private Button addCash;
    private Button addCard;
    private Button addGuestInfo;
    private ProgressButton pay;
    private HidingEditText cashAmount;
    private RecyclerView listPostedPayments;
    private TextView totalPayment;
    private TextView totalAmountDue;
    private TextView totalChange;

    private HidingEditText cardNumber;
    private HidingEditText cardHoldersName;
    private HidingEditText expiration;
    private HidingEditText authorization;
    private HidingEditText creditCardAmount;
    private HidingEditText remarks;

    private HidingEditText guestNameInput;
    private HidingEditText guestAddressinput;
    private HidingEditText guestTinInput;
    private HidingEditText guestBusinessStyle;

    private TextView guestName;
    private TextView guestAddress;
    private TextView guestTin;
    private TextView displayBusinessStyle;
    private TextView guestDetailsHeader;

    private RecyclerView rvCreditCard;

    private DataSyncViewModel dataSyncViewModel;
    private RoomsViewModel roomsViewModel;
    private TransactionsViewModel transactionsViewModel;
    private ServiceChargeViewModel serviceChargeViewModel;
    private UserViewModel userViewModel;
    private String transactionId;
    private PaymentTypes paymentTypes;
    private Context context;




    public PaymentDialog(Context context, DataSyncViewModel dataSyncViewModel,
                         TransactionsViewModel transactionsViewModel, String transactionId,
                         UserViewModel userViewModel, RoomsViewModel roomsViewModel,
                         ServiceChargeViewModel serviceChargeViewModel) {
        super(context);
        this.context = context;
        this.dataSyncViewModel = dataSyncViewModel;
        this.transactionsViewModel = transactionsViewModel;
        this.transactionId =transactionId;
        this.userViewModel = userViewModel;
        this.roomsViewModel = roomsViewModel;
        this.serviceChargeViewModel = serviceChargeViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_payment, "PAYMENT");

        initViews();
        setGuestDetailsClickListener();
        setPaymentTypeAdapter();
        setCancelable(false);
        showForm("1");
        loadPayments();
        loadCreditCardTypes();
        loadOrDetails(transactionId);
        try {
            transactionsViewModel.ldPaymentList(transactionId).observe((LifecycleOwner) context, new Observer<List<Payments>>() {
                @Override
                public void onChanged(List<Payments> paymentsList) {
                    loadPayments();
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setGuestDetailsClickListener() {
        guestDetailsHeader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= guestDetailsHeader.getRight() - guestDetailsHeader.getTotalPaddingRight()) {
                        OrDetailsSelectionDialog orDetailsSelectionDialog = new OrDetailsSelectionDialog(getContext(), transactionsViewModel) {
                            @Override
                            public void setOr(OrDetails or) {
                                guestNameInput.setText(or.getName());
                                guestAddressinput.setText(or.getAddress());
                                guestTinInput.setText(or.getTin_number());
                                guestBusinessStyle.setText(or.getBusiness_style());
                            }
                        };
                        orDetailsSelectionDialog.show();
                        return true;
                    }
                }
                return true;
            }
        });
    }

    private void loadCreditCardTypes() {
        final List<CreditCardListModel> ccArray = new ArrayList<>();

        try {
            for (CreditCards card : dataSyncViewModel.getCreditCardList()) {
                ccArray.add(new CreditCardListModel(card.getCore_id(), card.getCredit_card(), false, 0));
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        CreditCardContract creditCardContract = new CreditCardContract() {
            @Override
            public void clicked(CreditCardListModel creditCardListModel, int position) {
                for (CreditCardListModel ccm : ccArray) {

                    if (ccm.getCore_id() == ccArray.get(position).getCore_id()) {
                        ccm.setIs_selected(true);
                        selectedCreditCardName = String.valueOf(ccm.getCredit_card());
                        selectedCreditCardId = ccm.getCore_id();
                    } else {
                        ccm.setIs_selected(false);
                    }
                    if (creditCardAdapter != null) {
                        creditCardAdapter.notifyDataSetChanged();
                    }
                }

            }
        };
        creditCardAdapter = new CreditCardAdapter(ccArray, creditCardContract);
        rvCreditCard.setAdapter(creditCardAdapter);
        rvCreditCard.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        creditCardAdapter.notifyDataSetChanged();





    }

    private void loadPayments() {
        try {

            PaymentsAdapter paymentsAdapter = new PaymentsAdapter(
                    transactionsViewModel.paymentList(transactionId),
                    context, PaymentDialog.this);
            listPostedPayments.setAdapter(paymentsAdapter);
            listPostedPayments.setLayoutManager(new LinearLayoutManager(getContext()));
            paymentsAdapter.notifyDataSetChanged();

            Double tendered = 0.00;
            Double amountDue = 0.00;
            Double change = 0.00;
            for (Payments payments : transactionsViewModel.paymentList(transactionId)) {
                tendered += Utils.roundedOffTwoDecimal(payments.getAmount());
            }

            for (Orders order : transactionsViewModel.orderList(transactionId)) {
                amountDue += Utils.roundedOffTwoDecimal(order.getAmount()) * order.getQty();
            }
            Double surCharge = 0.00;

            if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel") ||
                    SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {

                if (serviceChargeViewModel.getActiveServiceCharge() != null) {

                    if (serviceChargeViewModel.getActiveServiceCharge().isIs_percentage()) {
                        surCharge = amountDue * (serviceChargeViewModel.getActiveServiceCharge().getValue() / 100);
                    } else {
                        surCharge = serviceChargeViewModel.getActiveServiceCharge().getValue();
                    }
                    amountDue += surCharge;
                }

            }




            change = (tendered - amountDue < 1 ? 0.00 : tendered - amountDue);

            totalPayment.setText(Utils.digitsWithComma(tendered));

            if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel") ||
                    SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                if (serviceChargeViewModel.getActiveServiceCharge() != null) {
                    totalAmountDue.setText(Utils.digitsWithComma(tendered >= amountDue ? 0.00 : (amountDue - tendered)) +"(" + PrinterUtils.returnWithTwoDecimal(String.valueOf(surCharge)) + ")");
                } else {
                    totalAmountDue.setText(Utils.digitsWithComma(tendered >= amountDue ? 0.00 : amountDue - tendered));
                }
            } else {
                totalAmountDue.setText(Utils.digitsWithComma(tendered >= amountDue ? 0.00 : amountDue - tendered));
            }



//            totalAmountDue.setText(Utils.digitsWithComma(tendered >= amountDue ? 0.00 : amountDue - tendered));
            totalChange.setText(Utils.digitsWithComma(change));

            cashAmount.setText(String.valueOf(tendered >= amountDue ? 0.00 : Utils.roundedOffTwoDecimal(amountDue - tendered)));
            creditCardAmount.setText(String.valueOf(tendered >= amountDue ? 0.00 : Utils.roundedOffTwoDecimal(amountDue - tendered)));

            if (Utils.roundedOffTwoDecimal(tendered) >= Utils.roundedOffTwoDecimal(amountDue)) {
                pay.setBackgroundResource(R.drawable.button_selector);
//                pay.setEnabled(true);
            } else {
                pay.setBackgroundResource(R.drawable.button_selector_disabled);
//                pay.setEnabled(false);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setPaymentTypeAdapter() {
        try {
            PaymentTypeAdapter paymentTypeAdapter = new PaymentTypeAdapter(dataSyncViewModel.getPaymentTypeList(), getContext(),
                    PaymentDialog.this);
            listPayments.setLayoutManager(new LinearLayoutManager(getContext()));
            listPayments.setAdapter(paymentTypeAdapter);
            paymentTypeAdapter.notifyDataSetChanged();

            paymentTypes = dataSyncViewModel.getPaymentTypeList().get(0);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private void initViews() {
        guestDetailsHeader = findViewById(R.id.guestDetailsHeader);
        rvCreditCard = findViewById(R.id.rvCreditCard);
        cardNumber = findViewById(R.id.cardNumber);
        cardHoldersName = findViewById(R.id.cardHoldersName);
        expiration = findViewById(R.id.expiration);

        expiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        expiration.setText(String.format("%s/%s", String.valueOf(selectedMonth), String.valueOf(selectedYear)));
                    }
                }, 2020, 01);

                builder.setActivatedMonth(Calendar.JULY)
                        .setMinYear(1990)
                        .setActivatedYear(2020)
                        .setMaxYear(2040)
                        .setTitle("CARD EXPIRATION")
                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                            @Override
                            public void onMonthChanged(int selectedMonth) {

                            }
                        })
                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                            @Override
                            public void onYearChanged(int year) {

                            }
                        })
                        .build().show();

            }
        });
        authorization = findViewById(R.id.authorization);
        creditCardAmount = findViewById(R.id.creditCardAmount);
        remarks = findViewById(R.id.remarks);
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(this);
        totalPayment = findViewById(R.id.totalPayment);
        listPayments = findViewById(R.id.listPayments);
        formGuestInfo = findViewById(R.id.formGuestInfo);
        formCash = findViewById(R.id.formCash);
        formCard = findViewById(R.id.formCard);
        formOnline = findViewById(R.id.formOnline);
        formVoucher = findViewById(R.id.formGiftCheck);
        formForex = findViewById(R.id.formForex);
        add = findViewById(R.id.add);
        add.setOnClickListener(this);

        addCash = findViewById(R.id.addCash);
        addCash.setOnClickListener(this);

        addCard = findViewById(R.id.addCard);
        addCard.setOnClickListener(this);

        addGuestInfo = findViewById(R.id.addGuestInfo);
        addGuestInfo.setOnClickListener(this );

        cashAmount = findViewById(R.id.amount);
        totalAmountDue = findViewById(R.id.totalAmountDue);
        listPostedPayments = findViewById(R.id.listPostedPayments);
        totalChange = findViewById(R.id.totalChange);

        guestNameInput = findViewById(R.id.guestNameInput);
        guestAddressinput = findViewById(R.id.guestAddressinput);
        guestTinInput = findViewById(R.id.guestTinInput);
        guestBusinessStyle = findViewById(R.id.guestBusinessStyle);

        guestName = findViewById(R.id.guestName);
        guestAddress = findViewById(R.id.guestAddress);
        guestTin = findViewById(R.id.guestTin);
        displayBusinessStyle = findViewById(R.id.displayBusinessStyle);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Dialog dialog = this;
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
//            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }



    @Override
    public void clicked(PaymentTypes paymentTypes) {

        this.paymentTypes = paymentTypes;

        switch (paymentTypes.getCore_id()) {
            case 1://CASH
                showForm("1");
                break;
            case 2://CREDIT CARD
                showForm("2");
                break;
            case 999://GUEST INFO
                showForm("999");
        }
    }



    private void showForm(String coreId) {
        if (coreId.equalsIgnoreCase("1")) { //cash
            formCash.setVisibility(View.VISIBLE);
            formCard.setVisibility(GONE);
            formVoucher.setVisibility(GONE);
            formOnline.setVisibility(GONE);
            formForex.setVisibility(GONE);
            formGuestInfo.setVisibility(GONE);
        } else if (coreId.equalsIgnoreCase("2")) { //card
            formCash.setVisibility(GONE);
            formCard.setVisibility(View.VISIBLE);
            formVoucher.setVisibility(GONE);
            formOnline.setVisibility(GONE);
            formForex.setVisibility(GONE);
            formGuestInfo.setVisibility(GONE);
        } else if (coreId.equalsIgnoreCase("3")) { //online
            formCash.setVisibility(View.GONE);
            formCard.setVisibility(GONE);
            formVoucher.setVisibility(GONE);
            formOnline.setVisibility(View.VISIBLE);
            formForex.setVisibility(GONE);
            formGuestInfo.setVisibility(GONE);
        } else if (coreId.equalsIgnoreCase("5")) { //voucher
            formCash.setVisibility(View.GONE);
            formCard.setVisibility(GONE);
            formVoucher.setVisibility(View.VISIBLE);
            formOnline.setVisibility(GONE);
            formForex.setVisibility(GONE);
            formGuestInfo.setVisibility(GONE);
        } else if (coreId.equalsIgnoreCase("6")) { //forex
            formCash.setVisibility(View.GONE);
            formCard.setVisibility(GONE);
            formVoucher.setVisibility(GONE);
            formOnline.setVisibility(GONE);
            formForex.setVisibility(View.VISIBLE);
            formGuestInfo.setVisibility(GONE);
        } else if (coreId.equalsIgnoreCase("999")) {
            formGuestInfo.setVisibility(View.VISIBLE);
            formCash.setVisibility(View.GONE);
            formCard.setVisibility(GONE);
            formVoucher.setVisibility(GONE);
            formOnline.setVisibility(GONE);
            formForex.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay:

                try {
                    boolean isPercentage = false;
                    Double serviceCharge = 0.00;
                    Double tendered = 0.00;
                    Double amountDue = 0.00;
                    Double change = 0.00;
                    for (Payments payments : transactionsViewModel.paymentList(transactionId)) {
                        tendered += Utils.roundedOffTwoDecimal(payments.getAmount());
                    }

                    for (Orders order : transactionsViewModel.orderList(transactionId)) {
                        amountDue += Utils.roundedOffTwoDecimal(order.getAmount()) * order.getQty();
                    }


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





                    change = (Utils.roundedOffTwoDecimal(tendered) - Utils.roundedOffTwoDecimal(amountDue) < 1 ? 0.00 : Utils.roundedOffTwoDecimal(tendered) - Utils.roundedOffTwoDecimal(amountDue));

                    totalPayment.setText(Utils.digitsWithComma(tendered));
                    totalAmountDue.setText(Utils.digitsWithComma(tendered >= amountDue ? 0.00 : Utils.roundedOffTwoDecimal(amountDue - tendered)));
                    totalChange.setText(Utils.digitsWithComma(change));


                    if (Utils.roundedOffTwoDecimal(tendered) >= Utils.roundedOffTwoDecimal(amountDue)) {

                        pay.startLoading(pay);


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
                                                                transactionsViewModel.lastOrNumber().getReceipt_number().split("-")[1].replaceFirst("0", "")) + 1));
                            }

                        }

                        Transactions tmp = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                        Transactions transactions = new Transactions(
                                tmp.getId(),
                                tmp.getControl_number(),
                                tmp.getUser_id(),
                                tmp.getIs_void(),
                                tmp.getIs_void_by(),
                                true,
                                getUser().getUsername(),
                                tmp.getIs_saved(),
                                tmp.getIs_saved_by(),
                                tmp.getIs_cut_off(),
                                tmp.getIs_cut_off_by(),
                                tmp.getTrans_name(),
                                tmp.getTreg(),
                                receiptNumber,
                                tmp.getGross_sales(),
                                tmp.getNet_sales(),
                                tmp.getVatable_sales(),
                                tmp.getVat_exempt_sales(),
                                tmp.getVat_amount(),
                                tmp.getDiscount_amount(),
                                Utils.roundedOffTwoDecimal(change),
                                tmp.getVoid_at(),
                                Utils.getDateTimeToday(),
                                tmp.getSaved_at(),
                                tmp.getIs_cut_off_at(),
                                tmp.getIs_cancelled(),
                                tmp.getIs_cancelled_by(),
                                tmp.getIs_cancelled_at(),
                                tmp.getTin_number(),
                                tmp.getIs_backed_out(),
                                tmp.getIs_backed_out_by(),
                                tmp.getIs_backed_out_at()

                        );

                        transactions.setService_charge_is_percentage(isPercentage);
                        transactions.setService_charge_value(serviceCharge);

                        transactions.setRoom_id(tmp.getRoom_id());
                        transactions.setRoom_number(tmp.getRoom_number());
                        transactions.setMachine_id(tmp.getMachine_id());
                        transactions.setIs_sent_to_server(0);
                        transactions.setBranch_id(tmp.getBranch_id());
                        transactions.setCheck_in_time(tmp.getCheck_in_time());
                        transactions.setCheck_out_time(!TextUtils.isEmpty(tmp.getCheck_in_time()) ? Utils.getDateTimeToday() : "");


                        transactions.setHas_special(tmp.getHas_special());
                        transactionsViewModel.update(transactions);
                        dismiss();

                        RoomStatus roomStatus = roomsViewModel.getRoomStatusViaId(3);

                        Rooms rooms = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                        if (rooms != null) {
                            rooms.setStatus_id(roomStatus.getCore_id());
                            rooms.setHex_color(roomStatus.getHex_color());
                            rooms.setStatus_description(roomStatus.getRoom_status());
                            rooms.setTransaction_id("");
                            rooms.setCheck_in_time("");

                            rooms.setTime_reservation_made("");
                            rooms.setReservation_time("");
                            rooms.setReservation_name("");
                            roomsViewModel.update(rooms);
                        }
                        completed(receiptNumber);
                        pay.stopLoading(pay);
                    } else {
                        pay.stopLoading(pay);
                        Helper.showDialogMessage(getContext(), context.getString(R.string.error_has_balance), context.getString(R.string.header_message));
                    }
                } catch (ExecutionException e) {
                    pay.stopLoading(pay);
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    pay.stopLoading(pay);
                    e.printStackTrace();
                }



                break;
            case R.id.addCash:

                if (!TextUtils.isEmpty(cashAmount.getText().toString())) {
                    if (Double.valueOf(cashAmount.getText().toString()) > 0) {
                        List<Payments> cashPayment = new ArrayList<>();
                        Payments p = new Payments(
                                Integer.valueOf(transactionId), paymentTypes.getCore_id(),
                                Double.valueOf(cashAmount.getText().toString()), paymentTypes.getPayment_type(),
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                Utils.getDateTimeToday());
                        p.setOther_data("");
                        cashPayment.add(p);

                        transactionsViewModel.insertPayment(cashPayment);
                    } else {
                        Helper.showDialogMessage(getContext(), "Amount cannot be zero(0)", context.getString(R.string.header_message));
                    }
                } else {
                    Helper.showDialogMessage(getContext(), context.getString(R.string.error_message_fill_up_all_fields), context.getString(R.string.header_message));
                }


                break;
            case R.id.addCard:
                if (!TextUtils.isEmpty(cardNumber.getText().toString()) &&
                        !TextUtils.isEmpty(cardHoldersName.getText().toString()) &&
                        !TextUtils.isEmpty(expiration.getText().toString()) &&
                        !TextUtils.isEmpty(authorization.getText().toString()) &&
                        !TextUtils.isEmpty(creditCardAmount.getText().toString())) {
                    //fix validation for card
                    if (selectedCreditCardId != 0) {
                        Map<String, String> cardMap = new HashMap<>();
                        cardMap.put("card_number", cardNumber.getText().toString());
                        cardMap.put("cardholder_name", cardHoldersName.getText().toString());
                        cardMap.put("expiration", expiration.getText().toString());
                        cardMap.put("authorization", authorization.getText().toString());
                        cardMap.put("credit_card_amount", creditCardAmount.getText().toString());
                        cardMap.put("remarks", remarks.getText().toString());
                        cardMap.put("core_id", String.valueOf(selectedCreditCardId));
                        cardMap.put("card_type", selectedCreditCardName);

                        List<Payments> cardPayment = new ArrayList<>();
                        Payments p = new Payments(
                                Integer.valueOf(transactionId), paymentTypes.getCore_id(),
                                Double.valueOf(creditCardAmount.getText().toString()), paymentTypes.getPayment_type(),
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                Utils.getDateTimeToday());
                        p.setOther_data(GsonHelper.getGson().toJson(cardMap));
                        cardPayment.add(p);
                        transactionsViewModel.insertPayment(cardPayment);
                    } else {
                        Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_select_card), getContext().getString(R.string.header_message));
                    }

                } else {
                    Helper.showDialogMessage(getContext(), context.getString(R.string.error_message_fill_up_all_fields), context.getString(R.string.header_message));
                }


                break;
            case R.id.addGuestInfo:

                if (!TextUtils.isEmpty(guestNameInput.getText().toString()) &&
                        !TextUtils.isEmpty(guestAddressinput.getText().toString()) &&
                        !TextUtils.isEmpty(guestTinInput.getText().toString()) &&
                        !TextUtils.isEmpty(guestBusinessStyle.getText().toString())) {

                    OrDetails orDetails = new OrDetails();
                    orDetails.setName(guestNameInput.getText().toString());
                    orDetails.setAddress(guestAddressinput.getText().toString());
                    orDetails.setTin_number(guestTinInput.getText().toString());
                    orDetails.setBusiness_style(guestBusinessStyle.getText().toString());
                    orDetails.setTransaction_id(Integer.valueOf(transactionId));
                    orDetails.setMachine_id(Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)));
                    orDetails.setBranch_id(Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)));
                    orDetails.setIs_sent_to_server(0);
                    orDetails.setTreg(Utils.getDateTimeToday());

                    transactionsViewModel.insertOrDetails(orDetails);

                    try {
                        Transactions transactions = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                        transactions.setTin_number(guestTinInput.getText().toString());
                        transactionsViewModel.update(transactions);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Helper.showDialogMessage(getContext(), context.getString(R.string.message_or_details_success), context.getString(R.string.header_message));

                    loadOrDetails(transactionId);

                } else {
                    Helper.showDialogMessage(getContext(), context.getString(R.string.error_message_fill_up_all_fields), context.getString(R.string.header_message));
                }


                break;

            case R.id.add:
                if (paymentTypes.getCore_id() == 1) { //cash
                    if (!TextUtils.isEmpty(cashAmount.getText().toString())) {
                        if (Double.valueOf(cashAmount.getText().toString()) > 0) {
                            List<Payments> cashPayment = new ArrayList<>();
                            Payments p = new Payments(
                                    Integer.valueOf(transactionId), paymentTypes.getCore_id(),
                                    Double.valueOf(cashAmount.getText().toString()), paymentTypes.getPayment_type(),
                                    0,
                                    Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                    Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                    Utils.getDateTimeToday());
                            p.setOther_data("");
                            cashPayment.add(p);

                            transactionsViewModel.insertPayment(cashPayment);
                        }
                    }

                } else if (paymentTypes.getCore_id() == 2) { //card

                    if (!TextUtils.isEmpty(cardNumber.getText().toString()) &&
                    !TextUtils.isEmpty(cardHoldersName.getText().toString()) &&
                    !TextUtils.isEmpty(expiration.getText().toString()) &&
                    !TextUtils.isEmpty(authorization.getText().toString()) &&
                    !TextUtils.isEmpty(creditCardAmount.getText().toString())) {
                        //fix validation for card
                        if (selectedCreditCardId != 0) {
                            Map<String, String> cardMap = new HashMap<>();
                            cardMap.put("card_number", cardNumber.getText().toString());
                            cardMap.put("cardholder_name", cardHoldersName.getText().toString());
                            cardMap.put("expiration", expiration.getText().toString());
                            cardMap.put("authorization", authorization.getText().toString());
                            cardMap.put("credit_card_amount", creditCardAmount.getText().toString());
                            cardMap.put("remarks", remarks.getText().toString());
                            cardMap.put("core_id", String.valueOf(selectedCreditCardId));
                            cardMap.put("card_type", selectedCreditCardName);

                            List<Payments> cardPayment = new ArrayList<>();
                            Payments p = new Payments(
                                    Integer.valueOf(transactionId), paymentTypes.getCore_id(),
                                    Double.valueOf(creditCardAmount.getText().toString()), paymentTypes.getPayment_type(),
                                    0,
                                    Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                    Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                    Utils.getDateTimeToday());
                            p.setOther_data(GsonHelper.getGson().toJson(cardMap));
                            cardPayment.add(p);
                            transactionsViewModel.insertPayment(cardPayment);
                        } else {
                            Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_select_card), getContext().getString(R.string.header_message));
                        }
                        
                    } else {
                        Helper.showDialogMessage(getContext(), context.getString(R.string.error_message_fill_up_all_fields), context.getString(R.string.header_message));
                    }

                } else if (paymentTypes.getCore_id() == 3) { //online

                } else if (paymentTypes.getCore_id() == 5) { //voucher

                } else if (paymentTypes.getCore_id() == 6) { //forex

                } else if (paymentTypes.getCore_id() == 999) {

                    if (!TextUtils.isEmpty(guestNameInput.getText().toString()) &&
                        !TextUtils.isEmpty(guestAddressinput.getText().toString()) &&
                        !TextUtils.isEmpty(guestTinInput.getText().toString()) &&
                        !TextUtils.isEmpty(guestBusinessStyle.getText().toString())) {

                        OrDetails orDetails = new OrDetails();
                        orDetails.setName(guestNameInput.getText().toString());
                        orDetails.setAddress(guestAddressinput.getText().toString());
                        orDetails.setTin_number(guestTinInput.getText().toString());
                        orDetails.setBusiness_style(guestBusinessStyle.getText().toString());
                        orDetails.setTransaction_id(Integer.valueOf(transactionId));
                        orDetails.setMachine_id(Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)));
                        orDetails.setBranch_id(Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)));
                        orDetails.setIs_sent_to_server(0);
                        orDetails.setTreg(Utils.getDateTimeToday());

                        transactionsViewModel.insertOrDetails(orDetails);

                        try {
                            Transactions transactions = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                            transactions.setTin_number(guestTinInput.getText().toString());
                            transactionsViewModel.update(transactions);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Helper.showDialogMessage(getContext(), context.getString(R.string.message_or_details_success), context.getString(R.string.header_message));

                        loadOrDetails(transactionId);

                    } else {
                        Helper.showDialogMessage(getContext(), context.getString(R.string.error_message_fill_up_all_fields), context.getString(R.string.header_message));
                    }
                }
                break;
        }
    }

    @Override
    public void clicked(Payments payments) {
        payments.setIs_sent_to_server(0);
        payments.setIs_void(true);
        transactionsViewModel.updatePayment(payments);
    }

    public abstract void completed(String receiptNumber);

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
    }

    private void loadOrDetails(final String transactionId)  {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OrDetails orDetails = null;
                try {
                    orDetails = transactionsViewModel.getOrDetails(transactionId);
                    if (orDetails != null) {
                        guestName.setText(String.format("name:%s", orDetails.getName().toUpperCase()));
                        guestAddress.setText(String.format("address:%s", orDetails.getAddress().toUpperCase()));
                        guestTin.setText(String.format("tin#:%s", orDetails.getTin_number().toUpperCase()));
                        displayBusinessStyle.setText(String.format("business style:%s", orDetails.getBusiness_style().toUpperCase()));
                        guestNameInput.setText(orDetails.getName());
                        guestAddressinput.setText(orDetails.getAddress());
                        guestTinInput.setText(orDetails.getTin_number());
                        guestBusinessStyle.setText(orDetails.getBusiness_style());
                    } else {
                        guestName.setText(String.format("name:%s", "N/A"));
                        guestAddress.setText(String.format("address:%s", "N/A"));
                        guestTin.setText(String.format("tin#:%s", "N/A"));
                        displayBusinessStyle.setText(String.format("business style:%s", "N/A"));
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, 300);


    }

}
