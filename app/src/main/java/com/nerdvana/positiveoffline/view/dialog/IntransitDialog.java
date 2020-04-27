package com.nerdvana.positiveoffline.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.IntransitAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.concurrent.ExecutionException;

public class IntransitDialog extends BaseDialog implements View.OnClickListener{

    private TransactionsViewModel transactionsViewModel;
    private RecyclerView rvIntransit;

    private FloatingActionButton fabPrintIntransit;
    public IntransitDialog(Context context, TransactionsViewModel transactionsViewModel) {
        super(context);
        this.transactionsViewModel = transactionsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_intransit, "Intransit");
        rvIntransit = findViewById(R.id.rvIntransit);
        fabPrintIntransit = findViewById(R.id.fabPrintIntransit);
        fabPrintIntransit.setOnClickListener(this);
        setIntransitAdapter();
    }

    private void setIntransitAdapter() {
        try {
            if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
                IntransitAdapter intransitAdapter = new IntransitAdapter(transactionsViewModel.savedTransactionsList(), getContext());
                rvIntransit.setAdapter(intransitAdapter);
                rvIntransit.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
                    IntransitAdapter intransitAdapter = new IntransitAdapter(transactionsViewModel.savedTransactionsList(), getContext());
                    rvIntransit.setAdapter(intransitAdapter);
                    rvIntransit.setLayoutManager(new LinearLayoutManager(getContext()));
                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                    //
                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {

                    IntransitAdapter intransitAdapter = new IntransitAdapter(transactionsViewModel.transactionListWithRoom(), getContext());
                    rvIntransit.setAdapter(intransitAdapter);
                    rvIntransit.setLayoutManager(new LinearLayoutManager(getContext()));

                }
            }




        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabPrintIntransit:
                try {
                    Helper.showDialogMessage(getContext(), "Printing intransit ..", "Information");
                    BusProvider.getInstance().post(new PrintModel("PRINT_INTRANSIT", GsonHelper.getGson().toJson(transactionsViewModel.savedTransactionsList())));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
