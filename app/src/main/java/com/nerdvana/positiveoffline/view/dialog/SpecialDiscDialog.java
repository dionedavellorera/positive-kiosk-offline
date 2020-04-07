package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.SpecialDiscountInfo;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class SpecialDiscDialog extends BaseDialog implements View.OnClickListener{

    private String header;

    private Button btnConfirm;
    private HidingEditText etCardNumber;
    private HidingEditText etName;
    private HidingEditText etAddress;

    private TextView tvCardNumber;

    private DiscountWithSettings discountWithSettings;
    private List<Orders> ordersList;
    private TransactionsViewModel transactionsViewModel;
    private DiscountViewModel discountViewModel;
    private String transactionId;
    public SpecialDiscDialog(Context context,
                             String header,
                             DiscountWithSettings discountWithSettings,
                             List<Orders> orderList,
                             TransactionsViewModel transactionsViewModel,
                             DiscountViewModel discountViewModel,
                             String transactionId) {
        super(context);
        this.header = header;
        this.discountWithSettings = discountWithSettings;
        this.ordersList = orderList;
        this.transactionsViewModel = transactionsViewModel;
        this.discountViewModel = discountViewModel;
        this.transactionId = transactionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_specialdiscount, header);
        setCancelable(false);
        initViews();
//        tvCardNumber.setText(String.format("%s NUMBER", header));
    }

    private void initViews() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        etCardNumber = findViewById(R.id.etCardNumber);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
//        tvCardNumber = findViewById(R.id.tvCardNumber);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (!TextUtils.isEmpty(etCardNumber.getText().toString()) &&
                    !TextUtils.isEmpty(etName.getText().toString()) &&
                    !TextUtils.isEmpty(etAddress.getText().toString())) {

                    SpecialDiscountInfo specialDiscountInfo = new SpecialDiscountInfo(
                            etCardNumber.getText().toString().trim(),
                            etName.getText().toString().trim(),
                            etAddress.getText().toString().trim()
                    );


                    discountViewModel.insertDiscount(ordersList, discountWithSettings, transactionId, specialDiscountInfo);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            transactionsViewModel.recomputeTransactionWithDiscount(transactionId, discountViewModel);
                        }
                    }, 500);


                    seniorSucceeded();
                    dismiss();
                } else {

                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));

                }

                break;
        }
    }

    public abstract void seniorSucceeded();
}
