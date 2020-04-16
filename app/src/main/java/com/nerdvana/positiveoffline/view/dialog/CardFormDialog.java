package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.CreditCardAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.intf.CreditCardContract;
import com.nerdvana.positiveoffline.model.CreditCardListModel;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class CardFormDialog extends BaseDialog implements View.OnClickListener{

    private HidingEditText cardNumber;
    private HidingEditText cardHoldersName;
    private HidingEditText expiration;
    private HidingEditText authorization;
    private HidingEditText creditCardAmount;
    private HidingEditText remarks;


    private int selectedCreditCardId = 0;
    private String selectedCreditCardName = "";
    private CreditCardAdapter creditCardAdapter;
    private RecyclerView rvCreditCard;
    private DataSyncViewModel dataSyncViewModel;

    private Button addCard;

    public CardFormDialog(Context context, DataSyncViewModel dataSyncViewModel) {
        super(context);
        this.dataSyncViewModel = dataSyncViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_card_form, "Card Form");
        initViews();

        loadCreditCardTypes();
    }

    private void initViews() {
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
                        .setMinYear(2015)
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

        addCard = findViewById(R.id.addCard);
        addCard.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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


                        cardPaymentAdded(creditCardAmount.getText().toString(), GsonHelper.getGson().toJson(cardMap));
                        dismiss();

                    } else {
                        Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_select_card), getContext().getString(R.string.header_message));
                    }

                } else {
                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));
                }
                break;
        }
    }



    public abstract void cardPaymentAdded(String creditCardAmount, String cardJsonData);

}
