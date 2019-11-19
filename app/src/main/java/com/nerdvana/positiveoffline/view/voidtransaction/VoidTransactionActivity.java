package com.nerdvana.positiveoffline.view.voidtransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.TransactionsAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.concurrent.ExecutionException;

public class VoidTransactionActivity extends AppCompatActivity implements TransactionsContract {

    private TextView tvNoData;
    private RecyclerView rvTransactionList;

    private TransactionsViewModel transactionsViewModel;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void_transaction);


        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTransactionViewModel();
        loadTransactions();

        setTitle(getString(R.string.title_completed_transactions));

        setTransactionsAdapter();
    }

    private void setTransactionsAdapter() {


        try {

            for (TransactionWithOrders tr : transactionsViewModel.completedTransactions()) {
                for (Orders order : tr.ordersList) {
                    Log.d("MYORDERS", order.getName());
                }

            }
            if (transactionsViewModel.completedTransactions().size() > 0) {
                showList();
            } else {
                showNoData();
            }
            TransactionsAdapter transactionsAdapter = new TransactionsAdapter(transactionsViewModel.completedTransactions(), this, this);
            rvTransactionList.setAdapter(transactionsAdapter);
            rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
            transactionsAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initTransactionViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private void loadTransactions() {

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvNoData = findViewById(R.id.tvNoData);
        rvTransactionList = findViewById(R.id.rvTransactionList);
    }

    @Override
    public void clicked(Transactions transactions) {

    }

    @Override
    public void clicked(TransactionWithOrders transactionWithOrders) {
        Toast.makeText(getApplicationContext(), "SHOW ORDER IN DIALOG RECEIPT STYLE", Toast.LENGTH_LONG).show();
    }

    private void showNoData() {
        tvNoData.setVisibility(View.VISIBLE);
        rvTransactionList.setVisibility(View.GONE);
    }

    private void showList() {
        tvNoData.setVisibility(View.GONE);
        rvTransactionList.setVisibility(View.VISIBLE);
    }
}
