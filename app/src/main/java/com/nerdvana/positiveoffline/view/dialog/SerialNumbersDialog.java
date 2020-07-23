package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.SharedTransactionPaymentsAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.SerialNumbers;
import com.nerdvana.positiveoffline.model.SerialNumberToSaveModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class SerialNumbersDialog extends BaseDialog implements View.OnClickListener{
    private Button btnSave;
    private TransactionsViewModel transactionsViewModel;
    private String transactionId;
    private LinearLayout linRow;

    List<SerialNumbers> ordersWithSerialNeeded = new ArrayList<>();
    private List<SerialNumberToSaveModel> serialNumberToSaveModels = new ArrayList<>();
    public SerialNumbersDialog(Context context, TransactionsViewModel transactionsViewModel,
                               String transactionId) {
        super(context);
        this.transactionsViewModel = transactionsViewModel;
        this.transactionId = transactionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_serial_numbers, "Serial Number Encoding");
        initViews();


        addView();

    }

    private void initViews() {
        linRow = (LinearLayout) findViewById(R.id.linRow);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    private void addView() {
        LayoutInflater mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
//            int totalSerialNeeded = transactionsViewModel.serialNumberFromTransaction(Integer.valueOf(transactionId)).size();
//            int serialEncoded = transactionsViewModel.orderListWithFixedAsset(transactionId).size();


            for (Orders ord : transactionsViewModel.orderListWithFixedAsset(transactionId)) {
                boolean hasMatch = false;
                int serialEncodedCount = 0;
                for (SerialNumbers sn : transactionsViewModel.serialNumberFromOrderId(ord.getId())) {
                    if (ord.getCore_id() == sn.getProduct_core_id()) {
                        hasMatch = true;
                        serialEncodedCount += 1;
                    }
                }

                if (hasMatch == false) {
                    for (int i = 0; i < ord.getQty(); i++) {
                        SerialNumbers sn = new SerialNumbers(
                                Integer.valueOf(transactionId), "",
                                Utils.getDateTimeToday(), ord.getCore_id(),
                                ord.getName(),ord.getId(),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                SharedPreferenceManager
                                        .getString(null, AppConstants.SELECTED_SYSTEM_MODE)
                                        .equalsIgnoreCase("to")
                                        ? Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID))
                                        : 0
                        );
                        ordersWithSerialNeeded.add(sn);
                    }
                } else {
                    for (int i = 0; i < ord.getQty() - serialEncodedCount; i++) {
                        SerialNumbers sn = new SerialNumbers(
                                Integer.valueOf(transactionId), "",
                                Utils.getDateTimeToday(), ord.getCore_id(),
                                ord.getName(),ord.getId(),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                SharedPreferenceManager
                                        .getString(null, AppConstants.SELECTED_SYSTEM_MODE)
                                        .equalsIgnoreCase("to")
                                        ? Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID))
                                        : 0
                        );
                        ordersWithSerialNeeded.add(sn);
                    }
                }
            }

            for (SerialNumbers sn : transactionsViewModel.serialNumberFromTransaction(Integer.valueOf(transactionId))) {
                sn.setFor_update(1);
                ordersWithSerialNeeded.add(sn);
            }


        } catch (Exception e) {

        }
        Log.d("MYINPUTCOUNT", String.valueOf(ordersWithSerialNeeded.size()));
        for (SerialNumbers sn : ordersWithSerialNeeded) {


            final View child = mLayoutInflater.inflate(R.layout.list_item_serial_numbers, null, false);
//                child.setTag(counter1);
            EditText etInput = child.findViewById(R.id.etInput);
            etInput.setText(sn.getSerial_number());
            TextView tvProduct = child.findViewById(R.id.tvProduct);
            tvProduct.setText(sn.getProduct_name());
            linRow.addView(child);

            serialNumberToSaveModels.add(new SerialNumberToSaveModel((EditText) etInput, sn));
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:

                for (SerialNumberToSaveModel sntmsn : serialNumberToSaveModels) {

                    if (sntmsn.getSerialNumbers().getFor_update() == 0) {
                        sntmsn.getSerialNumbers().setSerial_number(sntmsn.getEditText().getText().toString());
                        transactionsViewModel.insertSerialNumbers(sntmsn.getSerialNumbers());
                    } else {
                        sntmsn.getSerialNumbers().setSerial_number(sntmsn.getEditText().getText().toString());
                        transactionsViewModel.updateSerialNumbers(sntmsn.getSerialNumbers());
                    }
                    submitted();
                    dismiss();
                }
                break;
        }
    }

    public abstract void submitted();
}
