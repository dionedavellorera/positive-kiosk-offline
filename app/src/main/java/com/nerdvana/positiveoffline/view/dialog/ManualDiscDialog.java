package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.List;

public abstract class ManualDiscDialog extends BaseDialog implements View.OnClickListener{

    private HidingEditText etValue;
    private DiscountViewModel discountViewModel;
    private List<Orders> orderList;
    private String transactionId;

    private Button btnConfirm;

    private RadioGroup rgType;

    private TransactionsViewModel transactionsViewModel;
    public ManualDiscDialog(Context context, DiscountViewModel discountViewModel,
                            List<Orders> orderList, String transactionId,
                            TransactionsViewModel transactionsViewModel) {
        super(context);
        this.discountViewModel = discountViewModel;
        this.orderList = orderList;
        this.transactionId = transactionId;
        this.transactionsViewModel = transactionsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_manual_discount, "MANUAL");
        initViews();
        setCancelable(false);



    }

    private void initViews() {
        etValue = findViewById(R.id.etValue);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        rgType = findViewById(R.id.rgType);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                boolean isPercentage = false;
                if (rgType.getCheckedRadioButtonId() != -1) {
                    if (rgType.getCheckedRadioButtonId() == R.id.rbPercentage) {
                        isPercentage = true;
                    } else {
                        isPercentage = false;
                    }
                }


                if (!TextUtils.isEmpty(etValue.getText().toString())) {
                    if (Double.valueOf(etValue.getText().toString()) > 0) {
                        discountViewModel.insertManualDiscount(orderList, transactionId,
                                1000, "MANUAL",
                                isPercentage, Double.valueOf(etValue.getText().toString()));
                        discountSuccess();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                transactionsViewModel.recomputeTransactionWithDiscount(transactionId, discountViewModel);
                            }
                        }, 500);


                        dismiss();

                    } else {
                        Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));
                    }
                } else {
                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));
                }
                break;
        }
    }

    public abstract void discountSuccess();
}
