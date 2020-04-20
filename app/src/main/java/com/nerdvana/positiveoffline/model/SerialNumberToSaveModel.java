package com.nerdvana.positiveoffline.model;

import android.widget.EditText;

import com.nerdvana.positiveoffline.entities.SerialNumbers;

public class SerialNumberToSaveModel {
    private EditText editText;
    private SerialNumbers serialNumbers;

    public SerialNumberToSaveModel(EditText editText, SerialNumbers serialNumbers) {
        this.editText = editText;
        this.serialNumbers = serialNumbers;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public SerialNumbers getSerialNumbers() {
        return serialNumbers;
    }

    public void setSerialNumbers(SerialNumbers serialNumbers) {
        this.serialNumbers = serialNumbers;
    }
}
