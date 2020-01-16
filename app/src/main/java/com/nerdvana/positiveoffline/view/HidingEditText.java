package com.nerdvana.positiveoffline.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class HidingEditText extends AppCompatEditText {


    public HidingEditText(Context context) {
        super(context);
        hideKeyboard();
    }

    public HidingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        hideKeyboard();
    }

    public HidingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hideKeyboard();
    }

    private void hideKeyboard() {
        setTextIsSelectable(true);
    }

//    @Override
//    public boolean onCheckIsTextEditor() {
//        return false;
//    }

}
