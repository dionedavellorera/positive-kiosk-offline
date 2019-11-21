package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.background.CheckPasswordAsync;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.intf.PasswordCheckContract;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.view.ProgressButton;
import com.nerdvana.positiveoffline.view.login.LoginActivity;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

public abstract class PasswordDialog extends BaseDialog implements PasswordCheckContract, View.OnClickListener {

    private String header;

    private HidingEditText etUsername;
    private HidingEditText etPassword;
    private ProgressButton btnConfirm;
    private UserViewModel userViewModel;
    private TransactionsViewModel transactionsViewModel;
    public PasswordDialog(Context context, String header, UserViewModel userViewModel, TransactionsViewModel transactionsViewModel) {
        super(context);
        this.header = header;
        this.userViewModel = userViewModel;
        this.transactionsViewModel = transactionsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_password, header);
        setCancelable(false);
        initViews();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    public abstract void success();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (!TextUtils.isEmpty(etUsername.getText().toString()) &&
                    !TextUtils.isEmpty(etPassword.getText().toString())) {
                    btnConfirm.startLoading(view);
                    new CheckPasswordAsync(this,
                            etUsername.getText().toString(),
                            etPassword.getText().toString(),
                            userViewModel,
                            transactionsViewModel).execute();
                } else {
                    Helper.showDialogMessage(getContext(), "Please fill up username and password", "Information");
                }
                break;
        }
    }

    @Override
    public void isValid(String errorMessage) {
        btnConfirm.stopLoading(btnConfirm);

        if (errorMessage.equalsIgnoreCase("success")) {
            success();
            dismiss();
        } else {
            Helper.showDialogMessage(getContext(), errorMessage, getContext().getString(R.string.header_message));
        }

    }
}
