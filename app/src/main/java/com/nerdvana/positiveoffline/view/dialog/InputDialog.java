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

public abstract class InputDialog extends BaseDialog implements View.OnClickListener{

    private Button btnSave;
    private EditText input;
    private String header;
    private String defaultText;
    public InputDialog(Context context, String header, String defaultText) {
        super(context);
        this.header = header;
        this.defaultText = defaultText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_input_name, header);

        initViews();

        input.setText(defaultText);
    }

    private void initViews() {
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        input = findViewById(R.id.input);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (!TextUtils.isEmpty(input.getText().toString())) {
                    confirm(input.getText().toString().trim());
                    dismiss();
                } else {
                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));
                }
                break;
        }
    }

    public abstract void confirm(String str);
}
