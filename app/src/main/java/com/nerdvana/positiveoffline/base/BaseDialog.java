package com.nerdvana.positiveoffline.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nerdvana.positiveoffline.R;

public class BaseDialog extends Dialog {

    protected ImageView backButton;
    protected TextView headerText;


    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected void setDialogLayout(int layout, String header) {
        setContentView(layout);
        backButton = findViewById(R.id.backButton);
        headerText = findViewById(R.id.displayHeader);
        headerText.setText(header);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
}
