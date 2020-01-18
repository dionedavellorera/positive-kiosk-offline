package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.List;

public abstract class OpenPriceDialog extends BaseDialog implements View.OnClickListener{

    private TransactionsViewModel transactionsViewModel;
    private Orders selectedItem;

    private TextView tvMessage;
    private Button btnConfirm;
    private EditText etNewPrice;
    public OpenPriceDialog(Context context, TransactionsViewModel transactionsViewModel,
                           Orders selectedItem) {
        super(context);
        this.transactionsViewModel = transactionsViewModel;
        this.selectedItem = selectedItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_open_price, "OPEN PRICE");
        initViews();
        tvMessage.setText(String.format("%s - %s", selectedItem.getName(), String.valueOf(selectedItem.getOriginal_amount())));
        etNewPrice.setText(String.valueOf(selectedItem.getOriginal_amount()));
    }

    private void initViews() {
        etNewPrice = findViewById(R.id.etNewPrice);
        tvMessage = findViewById(R.id.tvMessage);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    public abstract void openPriceSuccess(Orders orders);

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (TextUtils.isEmpty(etNewPrice.getText().toString())) {
                    selectedItem.setOriginal_amount(0.00);
                    selectedItem.setAmount(0.00);
                    transactionsViewModel.updateOrder(selectedItem);

                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            openPriceSuccess(selectedItem);
                        }
                    }, 500);



                    dismiss();
                } else {
                    if (Double.valueOf(etNewPrice.getText().toString()) < 0) {
                        Helper.showDialogMessage(getContext(), "Invalid amount set",getContext().getString(R.string.header_message));
                    } else {
                        selectedItem.setOriginal_amount(Double.valueOf(etNewPrice.getText().toString()));
                        selectedItem.setAmount(Double.valueOf(etNewPrice.getText().toString()));
                        transactionsViewModel.updateOrder(selectedItem);
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openPriceSuccess(selectedItem);
                            }
                        }, 500);
                        dismiss();
                    }
                }



                break;
        }
    }
}
