package com.nerdvana.positiveoffline.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;

public abstract class ChooseQtyDialog extends Dialog implements View.OnClickListener{

    private TextView tvQty;
    private String maxQty;

    private Button btnAdd;
    private Button btnMinus;
    private Button btnConfirm;

    public ChooseQtyDialog(Context context, String maxQty) {
        super(context);
        this.maxQty = maxQty;
    }

    public ChooseQtyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChooseQtyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_qty);
        initViews();
        tvQty.setText(maxQty);
    }

    private void initViews() {
        tvQty = findViewById(R.id.tvQty);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnMinus = findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(this);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    public abstract void confirmQty(int qty);

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                confirmQty(Integer.valueOf(tvQty.getText().toString()));
                dismiss();
                break;
            case R.id.btnAdd:
                if (Integer.valueOf(tvQty.getText().toString()) + 1 <= Integer.valueOf(maxQty)) {
                    tvQty.setText(String.valueOf(Integer.valueOf(tvQty.getText().toString()) + 1));
                }
                break;
            case R.id.btnMinus:
                if (Integer.valueOf(tvQty.getText().toString()) - 1 > 0) {
                    tvQty.setText(String.valueOf(Integer.valueOf(tvQty.getText().toString()) - 1));
                }
                break;
        }
    }
}
