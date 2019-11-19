package com.nerdvana.positiveoffline.view.resumetransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.ResumeTransactionAdapter;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.List;

public class ResumeTransactionActivity extends AppCompatActivity implements TransactionsContract {

    private TextView tvNoData;
    private RecyclerView rvTransactionList;
    private Toolbar toolbar;

    private TransactionsViewModel transactionsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_transaction);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Select transaction to resume");
        initTransViewModel();
        initTransViewModelListener();
    }

    private void initTransViewModelListener() {
        transactionsViewModel.savedTransactionLiveData().observe(this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {
                if (transactions.size() > 0) {
                    rvTransactionList.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                } else {
                    rvTransactionList.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
                setTransactionAdapter(transactions);
            }
        });
    }

    private void initTransViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvNoData = findViewById(R.id.tvNoData);
        rvTransactionList = findViewById(R.id.rvTransactionList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    void returnSuccessData(String transactionId) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.TRANS_ID, transactionId);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setTransactionAdapter(List<Transactions> transactionsList) {
        ResumeTransactionAdapter resumeTransactionAdapter = new ResumeTransactionAdapter(transactionsList, this, this);
        rvTransactionList.setAdapter(resumeTransactionAdapter);
        rvTransactionList.setLayoutManager(new GridLayoutManager(this, 5));
        resumeTransactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void clicked(Transactions transactions) {
        returnSuccessData(String.valueOf(transactions.getId()));
    }

    @Override
    public void clicked(TransactionWithOrders transactionWithOrders) {

    }
}
