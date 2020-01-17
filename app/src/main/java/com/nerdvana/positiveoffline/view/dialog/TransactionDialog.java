package com.nerdvana.positiveoffline.view.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.TransactionsAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.concurrent.ExecutionException;

public class TransactionDialog extends BaseDialog implements TransactionsContract {

    private TransactionsAdapter transactionsAdapter;

    private String header;
    private EditText search;
    private TextView tvNoData;
    private RecyclerView rvTransactionList;
    private TransactionsViewModel transactionsViewModel;
    private UserViewModel userViewModel;
    private Boolean forVoid;

    private ProgressDialog progressDialog;

    public TransactionDialog(Context context, String header,
                             TransactionsViewModel transactionsViewModel,
                             UserViewModel userViewModel,
                             Boolean forVoid) {
        super(context);
        this.header = header;
        this.transactionsViewModel = transactionsViewModel;
        this.userViewModel = userViewModel;
        this.forVoid = forVoid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_transactions, header);
        initViews();

        setTransactionsAdapter();

        search.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.. please wait");
    }



    private void initViews() {
        search = findViewById(R.id.search);
        initSearch();
        tvNoData = findViewById(R.id.tvNoData);
        rvTransactionList = findViewById(R.id.rvTransactionList);
    }

    private void initSearch() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (transactionsAdapter != null) {
                    transactionsAdapter.getFilter().filter(editable);
                }
            }
        });
    }

    private void setTransactionsAdapter() {
        try {
            if (transactionsViewModel.completedTransactions().size() > 0) {
                showList();
            } else {
                showNoData();
            }
            transactionsAdapter = new TransactionsAdapter(
                    transactionsViewModel.completedTransactions(),
                    getContext(),
                    this,
                    forVoid);
            rvTransactionList.setAdapter(transactionsAdapter);
            rvTransactionList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            transactionsAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void showNoData() {
        tvNoData.setVisibility(View.VISIBLE);
        rvTransactionList.setVisibility(View.GONE);
    }

    private void showList() {
        tvNoData.setVisibility(View.GONE);
        rvTransactionList.setVisibility(View.VISIBLE);
    }

    @Override
    public void clicked(Transactions transactions) {

    }

    @Override
    public void remove(Transactions transactions) {

    }

    @Override
    public void clicked(TransactionWithOrders transactionWithOrders) {
        try {
            if (forVoid) {
                progressDialog.setMessage("VOIDING TRANSACTION ...");
                progressDialog.show();
                //FOR VOID
                Transactions reference = transactionsViewModel.loadedTransactionListViaControlNumber(transactionWithOrders.transactions.getControl_number()).get(0);
                Transactions transactions = new Transactions(
                        reference.getId(),
                        reference.getControl_number(),
                        reference.getUser_id(),
                        true,
                        getUser().getUsername(),
                        reference.getIs_completed(),
                        reference.getIs_completed_by(),
                        reference.getIs_saved(),
                        reference.getIs_saved_by(),
                        reference.getIs_cut_off(),
                        reference.getIs_cut_off_by(),
                        reference.getTrans_name(),
                        reference.getTreg(),
                        reference.getReceipt_number(),
                        reference.getGross_sales(),
                        reference.getNet_sales(),
                        reference.getVatable_sales(),
                        reference.getVat_exempt_sales(),
                        reference.getVat_amount(),
                        reference.getDiscount_amount(),
                        reference.getChange(),
                        Utils.getDateTimeToday(),
                        reference.getCompleted_at(),
                        reference.getSaved_at(),
                        reference.getIs_cut_off_at(),
                        reference.getIs_cancelled(),
                        reference.getIs_cancelled_by(),
                        reference.getIs_cancelled_at(),
                        reference.getTin_number()
                );
                transactions.setRoom_id(reference.getRoom_id());
                transactions.setRoom_number(reference.getRoom_number());
                transactions.setMachine_id(reference.getMachine_id());
                transactions.setIs_sent_to_server(reference.getIs_sent_to_server());
                transactions.setBranch_id(reference.getBranch_id());

                transactionsViewModel.update(transactions);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setTransactionsAdapter();
                        progressDialog.dismiss();
                        Helper.showDialogMessage(getContext(), "VOID SUCCESS", getContext().getString(R.string.header_message));
                    }
                }, 500);
                search.setText("");
            } else {
                progressDialog.setMessage("REPRINTING ...");
                progressDialog.show();
                BusProvider.getInstance().post(new PrintModel("REPRINT_RECEIPT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(transactionWithOrders.transactions.getReceipt_number()))));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 500);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
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

}

