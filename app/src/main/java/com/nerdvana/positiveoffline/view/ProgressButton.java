package com.nerdvana.positiveoffline.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.nerdvana.positiveoffline.R;

public class ProgressButton extends CoordinatorLayout {
    private TextView tv;
    private ProgressBar progressBar;
    public ProgressButton(Context context) {
        super(context);
        initView(context, null);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView(Context c, @Nullable AttributeSet set) {
        LayoutInflater mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.inflate(R.layout.view_progress_button, null, true);

        tv = v.findViewById(R.id.name);
        tv.setTextColor(Color.RED);
        progressBar = v.findViewById(R.id.progress);



        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);



        TypedArray a = c.obtainStyledAttributes(set, R.styleable.ProgressButtonView);
        tv.setText(a.getString(R.styleable.ProgressButtonView_text));
        tv.setTextColor(a.getColor(R.styleable.ProgressButtonView_textColor, getResources().getColor(R.color.colorWhite)));
        a.recycle();
        addView(v);
    }

    public void stopLoading(final View v) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(GONE);
                tv.setVisibility(VISIBLE);
                v.setEnabled(true);
            }
        }, 1000);
    }

    public void startLoading(View v) {
        v.setEnabled(false);
        tv.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }
}
