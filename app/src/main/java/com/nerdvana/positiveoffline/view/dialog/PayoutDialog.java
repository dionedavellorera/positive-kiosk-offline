package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;

public abstract class PayoutDialog extends BaseDialog implements View.OnClickListener{

    private Button btnProceed;
    private EditText etPayoutAmount;
    private EditText etReason;

    public PayoutDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_payout, "Payout");
        initViews();
    }

    private void initViews() {
        btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(this);
        etPayoutAmount = findViewById(R.id.etPayoutAmount);
        etReason = findViewById(R.id.etReason);
    }

    public abstract void payoutSuccess(String amount, String payoutReason);

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnProceed:
                if (!TextUtils.isEmpty(etPayoutAmount.getText().toString()) && !TextUtils.isEmpty(etReason.getText().toString())) {
                    Double etPayoutValue = Double.valueOf(etPayoutAmount.getText().toString());
                    if (etPayoutValue > 0) {
                        payoutSuccess(etPayoutAmount.getText().toString(), etReason.getText().toString());
                    } else {
                        Helper.showDialogMessage(getContext(), "Payout amount cannot be 0", "Information");
                    }
                } else {
                    Helper.showDialogMessage(getContext(), "Please enter amount for payout and payout reason", "Information");
                }
                break;
        }
    }
}
