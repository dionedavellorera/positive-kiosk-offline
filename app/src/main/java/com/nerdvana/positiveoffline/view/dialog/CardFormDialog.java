package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;

public class CardFormDialog extends BaseDialog {
    public CardFormDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_card_form, "Card Form");
    }
}
