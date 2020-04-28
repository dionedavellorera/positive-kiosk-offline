package com.nerdvana.positiveoffline.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.StringAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.intf.StringSelectionContract;
import com.nerdvana.positiveoffline.model.StringModel;
import com.nerdvana.positiveoffline.view.HidingEditText;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class StringSelectionDialog extends BaseDialog implements StringSelectionContract {
    private Timer timer;
    private HidingEditText etSearchStringSt;
    private RecyclerView rvStringSelection;
    private StringAdapter stringAdapter;
    private String header;
    private List<StringModel> stringList;
    private Button btnSearch;
    private TextView displayHeader;
    public StringSelectionDialog(Context context, String header,
                                 List<StringModel> stringList) {
        super(context);
        this.header = header;
        this.stringList = stringList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_string_selection, header);
        initViews();
        setAdapter();
    }


    private void setAdapter() {

        stringAdapter = new StringAdapter(stringList, this, getContext(), false);
        rvStringSelection.setAdapter(stringAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 7);
        rvStringSelection.setLayoutManager(gridLayoutManager);
        stringAdapter.notifyDataSetChanged();
    }

    private void initViews() {
//        btnSearch = findViewById(R.id.btnSearch);
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("CHANGING", "TES");
//                Log.d("CHANGING", etSearchStringSt.getText().toString());
//            }
//        });
        etSearchStringSt = findViewById(R.id.search);

//        etSearchStringSt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("CHANGING", "BEFIRE TEXT CHANGED");
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("CHANGING", "TEXT CHANGED");
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Log.d("CHANGING", "AFTER TEXT CHANGED");
//            }
//        });



        rvStringSelection = findViewById(R.id.rvStringSelection);
    }


    public abstract void stringSelected(StringModel selected);

    @Override
    protected void onStart() {
        super.onStart();
        Dialog dialog = this;
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void clicked(StringModel stringModel) {
        stringSelected(stringModel);
        dismiss();
    }
}
