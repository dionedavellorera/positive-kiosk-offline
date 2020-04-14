package com.nerdvana.positiveoffline.view.dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;

import java.util.Calendar;

public abstract class InputWithTimeDialog extends BaseDialog implements View.OnClickListener{
    private String header;
    private String defaultText;

    private Button btnSave;
    private Button btnSetReservationTime;
    private EditText input;
    private String selectedTime = "";
    public InputWithTimeDialog(Context context, String header, String defaultText) {
        super(context);
        this.header = header;
        this.defaultText = defaultText;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_input_with_time, header);
        initViews();
        input.setText(defaultText);
    }

    private void initViews() {
        btnSetReservationTime = findViewById(R.id.btnSetReservationTime);
        btnSetReservationTime.setOnClickListener(this);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        input = findViewById(R.id.input);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetReservationTime:
                showTimePicker();
                break;
            case R.id.btnSave:
                if (!TextUtils.isEmpty(input.getText().toString())) {
                    confirm(input.getText().toString().trim(), selectedTime);
                    dismiss();
                } else if (TextUtils.isEmpty(selectedTime)) {
                    Helper.showDialogMessage(getContext(), "Please select time of reservation", getContext().getString(R.string.header_message));
                } else {
                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));
                }
                break;
        }
    }

    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                selectedTime = (selectedHour < 10 ? "0" + selectedHour : selectedHour)
                        + ":" +
                        (selectedMinute < 10 ? "0" + selectedMinute : selectedMinute);
                btnSetReservationTime.setText(selectedTime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public abstract void confirm(String str1, String str2);
}
