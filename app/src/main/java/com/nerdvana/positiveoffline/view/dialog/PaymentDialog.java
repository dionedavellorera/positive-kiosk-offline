package com.nerdvana.positiveoffline.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.PaymentTypeAdapter;
import com.nerdvana.positiveoffline.adapter.PaymentsAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.PaymentTypeContract;
import com.nerdvana.positiveoffline.intf.PaymentsContract;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.view.ProgressButton;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.view.View.GONE;

public abstract class PaymentDialog extends BaseDialog implements PaymentTypeContract, View.OnClickListener, PaymentsContract {

    private RecyclerView listPayments;
    private RelativeLayout formCash;
    private LinearLayout formCard;
    private LinearLayout formOnline;
    private LinearLayout formVoucher;
    private LinearLayout formForex;
    private LinearLayout formGuestInfo;
    private Button add;
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

    private RadioGroup rgCards;

    private DataSyncViewModel dataSyncViewModel;
    private TransactionsViewModel transactionsViewModel;
    private String transactionId;
    private PaymentTypes paymentTypes;
    private Context context;
    public PaymentDialog(Context context, DataSyncViewModel dataSyncViewModel,
                         TransactionsViewModel transactionsViewModel, String transactionId) {
        super(context);
        this.context = context;
        this.dataSyncViewModel = dataSyncViewModel;
        this.transactionsViewModel = transactionsViewModel;
        this.transactionId =transactionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_payment, "PAYMENT");
        initViews();
        setPaymentTypeAdapter();
        setCancelable(false);
        showForm("1");
        loadPayments();
        loadCreditCardTypes();

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

    private void loadCreditCardTypes() {
        try {
            for (CreditCards card : dataSyncViewModel.getCreditCardList()) {
                RadioButton rb = new RadioButton(getContext());
                rb.setId(card.getCore_id());
                rb.setText(card.getCredit_card().toUpperCase());
                rgCards.addView(rb);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                tendered += payments.getAmount();
            }

            for (Orders order : transactionsViewModel.orderList(transactionId)) {
                amountDue += order.getAmount() * order.getQty();
            }

            change = (tendered - amountDue < 1 ? 0.00 : tendered - amountDue);

            totalPayment.setText(Utils.digitsWithComma(tendered));
            totalAmountDue.setText(Utils.digitsWithComma(tendered >= amountDue ? 0.00 : amountDue - tendered));
            totalChange.setText(Utils.digitsWithComma(change));

            cashAmount.setText(String.valueOf(tendered >= amountDue ? 0.00 : amountDue - tendered));
            creditCardAmount.setText(String.valueOf(tendered >= amountDue ? 0.00 : amountDue - tendered));

            if (tendered >= amountDue) {
                pay.setBackgroundResource(R.drawable.button_selector);
            } else {
                pay.setBackgroundResource(R.drawable.button_selector_red);
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
        rgCards = findViewById(R.id.rgCards);
        cardNumber = findViewById(R.id.cardNumber);
        cardHoldersName = findViewById(R.id.cardHoldersName);
        expiration = findViewById(R.id.expiration);
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
        cashAmount = findViewById(R.id.amount);
        totalAmountDue = findViewById(R.id.totalAmountDue);
        listPostedPayments = findViewById(R.id.listPostedPayments);
        totalChange = findViewById(R.id.totalChange);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Dialog dialog = this;
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
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
                pay.startLoading(pay);
                try {

                    Double tendered = 0.00;
                    Double amountDue = 0.00;
                    Double change = 0.00;
                    for (Payments payments : transactionsViewModel.paymentList(transactionId)) {
                        tendered += payments.getAmount();
                    }

                    for (Orders order : transactionsViewModel.orderList(transactionId)) {
                        amountDue += order.getAmount() * order.getQty();
                    }

                    change = (tendered - amountDue < 1 ? 0.00 : tendered - amountDue);

                    totalPayment.setText(Utils.digitsWithComma(tendered));
                    totalAmountDue.setText(Utils.digitsWithComma(tendered >= amountDue ? 0.00 : amountDue - tendered));
                    totalChange.setText(Utils.digitsWithComma(change));

                    if (tendered >= amountDue) {
                        Transactions transactions = new Transactions(
                                transactionsViewModel.loadedTransactionList(transactionId).get(0).getId(),
                                transactionsViewModel.loadedTransactionList(transactionId).get(0).getControl_number(),
                                transactionsViewModel.loadedTransactionList(transactionId).get(0).getUser_id(),
                                transactionsViewModel.loadedTransactionList(transactionId).get(0).getIs_void(),
                                true,
                                transactionsViewModel.loadedTransactionList(transactionId).get(0).getIs_saved()
                        );
                        transactionsViewModel.update(transactions);
                        dismiss();
                        completed();
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
            case R.id.add:
                if (paymentTypes.getCore_id() == 1) { //cash
                    if (!TextUtils.isEmpty(cashAmount.getText().toString())) {
                        if (Double.valueOf(cashAmount.getText().toString()) > 0) {
                            List<Payments> cashPayment = new ArrayList<>();
                            cashPayment.add(new Payments(
                                    Integer.valueOf(transactionId), paymentTypes.getCore_id(),
                                    Double.valueOf(cashAmount.getText().toString()), paymentTypes.getPayment_type()));
                            transactionsViewModel.insertPayment(cashPayment);
                        }
                    }

                } else if (paymentTypes.getCore_id() == 2) { //card

                    if (!TextUtils.isEmpty(cardNumber.getText().toString()) &&
                    !TextUtils.isEmpty(cardHoldersName.getText().toString()) &&
                    !TextUtils.isEmpty(expiration.getText().toString()) &&
                    !TextUtils.isEmpty(authorization.getText().toString()) &&
                    !TextUtils.isEmpty(creditCardAmount.getText().toString())) {

                        Map<String, String> cardMap = new HashMap<>();
                        cardMap.put("card_number", cardNumber.getText().toString());
                        cardMap.put("cardholder_name", cardHoldersName.getText().toString());
                        cardMap.put("expiration", expiration.getText().toString());
                        cardMap.put("authorization", authorization.getText().toString());
                        cardMap.put("credit_card_amount", creditCardAmount.getText().toString());
                        cardMap.put("remarks", remarks.getText().toString());
                        cardMap.put("core_id", String.valueOf(rgCards.getCheckedRadioButtonId()));
                        cardMap.put("card_type", ((RadioButton)findViewById(rgCards.getCheckedRadioButtonId())).getText().toString());

                        List<Payments> cardPayment = new ArrayList<>();
                        Payments p = new Payments(
                                Integer.valueOf(transactionId), paymentTypes.getCore_id(),
                                Double.valueOf(creditCardAmount.getText().toString()), paymentTypes.getPayment_type());
                        p.setOther_data(GsonHelper.getGson().toJson(cardMap));
                        cardPayment.add(p);
                        transactionsViewModel.insertPayment(cardPayment);
                    } else {
                        Helper.showDialogMessage(getContext(), context.getString(R.string.error_message_fill_up_all_fields), context.getString(R.string.header_message));
                    }

                } else if (paymentTypes.getCore_id() == 3) { //online

                } else if (paymentTypes.getCore_id() == 5) { //voucher

                } else if (paymentTypes.getCore_id() == 6) { //forex

                } else if (paymentTypes.getCore_id() == 999) {

                }
                break;
        }
    }

    @Override
    public void clicked(Payments payments) {
        payments.setIs_void(true);
        transactionsViewModel.updatePayment(payments);
    }

    public abstract void completed();
}
