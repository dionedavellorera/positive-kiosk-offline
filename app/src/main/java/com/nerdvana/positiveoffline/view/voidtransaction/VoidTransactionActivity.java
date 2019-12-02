package com.nerdvana.positiveoffline.view.voidtransaction;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.TransactionsAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.concurrent.ExecutionException;

public class VoidTransactionActivity extends AppCompatActivity implements TransactionsContract {
    private TransactionsAdapter transactionsAdapter;
    private TextView tvNoData;
    private RecyclerView rvTransactionList;

    private TransactionsViewModel transactionsViewModel;
    private UserViewModel userViewModel;


    private Toolbar toolbar;
    private EditText search;
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void_transaction);


        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTransactionViewModel();
        initUsersViewModel();
        loadTransactions();

        setTitle();

        setTransactionsAdapter();
    }

    private void initUsersViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }

    private void setTitle() {
        title.setText(getString(R.string.title_completed_transactions));
    }

    private void setTransactionsAdapter() {


        try {
            if (transactionsViewModel.completedTransactions().size() > 0) {
                showList();
            } else {
                showNoData();
            }
            transactionsAdapter = new TransactionsAdapter(transactionsViewModel.completedTransactions(), this, this);
            rvTransactionList.setAdapter(transactionsAdapter);
            rvTransactionList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            transactionsAdapter.notifyDataSetChanged();

            Log.d("ERERERe-1", String.valueOf(transactionsViewModel.completedTransactions().size()));
        } catch (ExecutionException e) {

            Log.d("ERERERe", e.getLocalizedMessage());

            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("ERERERe-1", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void initTransactionViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private void loadTransactions() {

    }

    private void initViews() {
        title = findViewById(R.id.title);
        search = findViewById(R.id.search);
        initSearch();
        toolbar = findViewById(R.id.toolbar);
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

    @Override
    public void clicked(Transactions transactions) {

    }

    @Override
    public void clicked(TransactionWithOrders transactionWithOrders) {
        try {
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
                    reference.getCreated_at(),
                    reference.getReceipt_number(),
                    reference.getGross_sales(),
                    reference.getNet_sales(),
                    reference.getVatable_sales(),
                    reference.getVat_exempt_sales(),
                    reference.getVat_amount(),
                    reference.getDiscount_amount(),
                    reference.getChange()
            );


            transactionsViewModel.update(transactions);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTransactionsAdapter();
                }
            }, 500);

            search.setText("");


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

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
    }

}
