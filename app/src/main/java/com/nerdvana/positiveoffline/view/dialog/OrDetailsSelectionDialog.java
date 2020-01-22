package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.OrDetailsSelectionAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.intf.OrDetailsContract;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.List;

public abstract class OrDetailsSelectionDialog extends BaseDialog implements OrDetailsContract {
    private HidingEditText etSearch;
    private TextView noData;
    private RecyclerView rvOrDetails;
    private TransactionsViewModel transactionsViewModel;
    private OrDetailsSelectionAdapter orDetailsSelectionAdapter;
    public OrDetailsSelectionDialog(Context context, TransactionsViewModel transactionsViewModel) {
        super(context);
        this.transactionsViewModel = transactionsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_or_details_selection, "SELECT OR DETAIL");
        initViews();
        setCancelable(false);
        setAdapter();


    }

    private void setAdapter() {
        try {
            List<OrDetails> orDetailsList = transactionsViewModel.getAllSavedOr();
            if (orDetailsList.size() > 0) {
                noData.setVisibility(View.GONE);
                orDetailsSelectionAdapter =
                        new OrDetailsSelectionAdapter(transactionsViewModel.getAllSavedOr(), OrDetailsSelectionDialog.this, getContext());
                rvOrDetails.setAdapter(orDetailsSelectionAdapter);
                rvOrDetails.setLayoutManager(new LinearLayoutManager(getContext()));
                orDetailsSelectionAdapter.notifyDataSetChanged();
            } else {

                noData.setVisibility(View.VISIBLE);

            }

        } catch (Exception e) {
            noData.setVisibility(View.VISIBLE);
        }

    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        rvOrDetails = findViewById(R.id.rvOrDetails);
        noData = findViewById(R.id.noData);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do search here for or, address, name, tin
                try {
                    if (transactionsViewModel.getAllSavedOr().size() > 0) {
                        orDetailsSelectionAdapter.getFilter().filter(editable);
                    }
                }catch (Exception e) {
                }

            }
        });
    }

    @Override
    public void clicked(OrDetails orDetails) {
        setOr(orDetails);
        dismiss();
    }

    public abstract void setOr(OrDetails or);
}
