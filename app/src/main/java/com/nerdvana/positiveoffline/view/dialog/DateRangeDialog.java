package com.nerdvana.positiveoffline.view.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;

import java.util.Calendar;

public abstract class DateRangeDialog extends BaseDialog implements View.OnClickListener{

    private TextView inputDateStart;
    private TextView inputDateEnd;
    private Button btnConfirm;

    private CardView cardDateStart;
    private CardView cardDateEnd;

    public DateRangeDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_date_range, "SELECT DATE RANGE");
        initViews();
        defaults();
    }

    private void defaults() {
        inputDateStart.setText(Utils.getCurrentDate());
        inputDateEnd.setText(Utils.getCurrentDate());
    }

    private void initViews() {
        inputDateStart = findViewById(R.id.inputDateStart);
        inputDateEnd = findViewById(R.id.inputDateEnd);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        cardDateStart = findViewById(R.id.cardDateStart);
        cardDateStart.setOnClickListener(this);
        cardDateEnd = findViewById(R.id.cardDateEnd);
        cardDateEnd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                dateConfirmed(inputDateStart.getText().toString(),
                        inputDateEnd.getText().toString());
                dismiss();
                break;
            case R.id.cardDateStart:
                showDatePickerDialog("from");
                break;
            case R.id.cardDateEnd:
                showDatePickerDialog("to");
                break;
        }
    }
    abstract void dateConfirmed(String dateFrom, String dateTo);

    private void showDatePickerDialog(final String type) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        switch (type) {
                            case "from":
                                inputDateStart.setText(String.format("%s-%s-%s", String.valueOf(year),
                                        monthOfYear < 9 ? "0" + String.valueOf(monthOfYear + 1) : String.valueOf(monthOfYear + 1),
                                        dayOfMonth < 10 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth)));
                                break;
                            case "to":
                                inputDateEnd.setText(String.format("%s-%s-%s", String.valueOf(year),
                                        monthOfYear < 9 ? "0" + String.valueOf(monthOfYear + 1) : String.valueOf(monthOfYear + 1),
                                        dayOfMonth < 10 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth)));
                                break;
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
}
