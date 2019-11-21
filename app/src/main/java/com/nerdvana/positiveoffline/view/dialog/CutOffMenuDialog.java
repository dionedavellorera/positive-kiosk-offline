package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CutOffMenuDialog extends BaseDialog implements View.OnClickListener{
    private Button btnXReading;
    private Button btnZReading;
    private Button btnReprintXReading;
    private Button btnReprintZReading;
    private UserViewModel userViewModel;
    private TransactionsViewModel transactionsViewModel;
    private DataSyncViewModel dataSyncViewModel;
    public CutOffMenuDialog(Context context, TransactionsViewModel transactionsViewModel,
                            UserViewModel userViewModel, DataSyncViewModel dataSyncViewModel) {
        super(context);
        this.transactionsViewModel = transactionsViewModel;
        this.userViewModel = userViewModel;
        this.dataSyncViewModel = dataSyncViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_cutoff_menu, "CUT OFF MENU");
        initViews();
    }

    private void initViews() {
        btnXReading = findViewById(R.id.btnXReading);
        btnXReading.setOnClickListener(this);
        btnZReading = findViewById(R.id.btnZReading);
        btnZReading.setOnClickListener(this);
        btnReprintXReading = findViewById(R.id.btnReprintXRead);
        btnReprintXReading.setOnClickListener(this);
        btnReprintZReading = findViewById(R.id.btnReprintZRead);
        btnReprintZReading.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnXReading:

                CollectionDialog collectionDialog = new CollectionDialog(getContext(),dataSyncViewModel);
                collectionDialog.show();
//                try {
//                    List<Transactions> transactionsList = transactionsViewModel.unCutOffTransactions(userViewModel.searchLoggedInUser().get(0).getUsername());
//
//                    if (transactionsList.size() > 0) {
//                        Toast.makeText(getContext(), "Please cutoff", Toast.LENGTH_SHORT).show();
//                        for (Transactions tr : transactionsList) {
//                            tr.setIs_cut_off(true);
//                            tr.setIs_cut_off_by(userViewModel.searchLoggedInUser().get(0).getUsername());
//                            transactionsViewModel.update(tr);
//                        }
//
//                        dismiss();
//                    } else {
//                        Toast.makeText(getContext(), "No transaction to cutoff", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.btnZReading:
                break;
            case R.id.btnReprintXRead:
                break;
            case R.id.btnReprintZRead:
                break;
        }
    }
}
