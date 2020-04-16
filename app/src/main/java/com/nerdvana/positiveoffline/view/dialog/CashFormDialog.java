package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Payments;

import java.util.ArrayList;
import java.util.List;

public abstract class CashFormDialog extends BaseDialog implements View.OnClickListener{
    private EditText cashAmount;
    private Button addCash;

    public CashFormDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_cash_form, "Cash Form");
        initViews();
    }

    private void initViews() {
        addCash = findViewById(R.id.addCash);
        addCash.setOnClickListener(this);
        cashAmount = findViewById(R.id.amount);

        cashAmount.requestFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCash:
                if (!TextUtils.isEmpty(cashAmount.getText().toString())) {
                    if (Double.valueOf(cashAmount.getText().toString()) > 0) {
                        confirmPayment(cashAmount.getText().toString());
                        dismiss();
                    } else {
                        Helper.showDialogMessage(getContext(), "Amount cannot be zero(0)", getContext().getString(R.string.header_message));
                    }
                } else {
                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));
                }
                break;
        }
    }

    public abstract void confirmPayment(String cashAmount);
}
