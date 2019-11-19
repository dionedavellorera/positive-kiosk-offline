package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.view.ProgressButton;

public abstract class ChangeQtyDialog extends BaseDialog implements View.OnClickListener{

    private EditText etQty;
    private ProgressButton btnConfirm;
    private int qty;
    public ChangeQtyDialog(Context context, int quantity) {
        super(context);
        this.qty = quantity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_change_qty,
                getContext().getString(R.string.dialog_header_change_qty));
        initViews();
    }

    private void initViews() {
        etQty = findViewById(R.id.etQuantity);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(etQty.getText().toString())) {
            if (Integer.valueOf(etQty.getText().toString()) > 0) {
                success(Integer.valueOf(etQty.getText().toString()));
                dismiss();
            } else {
                Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_zero_quantity), getContext().getString(R.string.header_message));
            }
        } else {
            Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_zero_quantity), getContext().getString(R.string.header_message));
        }
    }

    public abstract void success(int newQty);
}
