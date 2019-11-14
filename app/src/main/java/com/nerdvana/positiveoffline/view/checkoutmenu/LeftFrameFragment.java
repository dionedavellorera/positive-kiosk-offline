package com.nerdvana.positiveoffline.view.checkoutmenu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.CheckoutAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LeftFrameFragment extends Fragment implements ProductsContract {

    private View view;

    private TransactionsViewModel transactionsViewModel;

    private RecyclerView listCheckoutItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_left_frame, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        listCheckoutItems = view.findViewById(R.id.listCheckoutItems);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTransactionsViewModel();
        initTransactionsViewModelListener();


        try {
//            Transactions tr = new Transactions(
//                    transactionsList().get(0).getId(),
//                    transactionsList().get(0).getControl_number(),
//                    transactionsList().get(0).getUser_id(),
//                    transactionsList().get(0).getIs_void(),
//                    transactionsList().get(0).getIs_completed(),
//                    true);
//            transactionsViewModel.update(tr);
            if (transactionsList().size() > 0) {
                initOrdersListener(String.valueOf(transactionsList().get(0).getId()));
                setOrderAdapter(transactionsViewModel.orderList(String.valueOf(transactionsList().get(0).getId())));
            }
        } catch (ExecutionException e) {
            Log.d("DEDEDE", e.getLocalizedMessage());
        } catch (InterruptedException e) {
            Log.d("DEDEDE", e.getLocalizedMessage());
        }

    }

    private void initTransactionsViewModelListener() {
        transactionsViewModel.transactionLiveData().observe(this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {

                try {
                    if (transactions.size() > 0) {
                        initOrdersListener(String.valueOf(transactions.get(0).getId()));
                        setOrderAdapter(transactionsViewModel.orderList(String.valueOf(transactions.get(0).getId())));
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTransactionsViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private List<Transactions> transactionsList() throws ExecutionException, InterruptedException {
        return transactionsViewModel.transactionList();
    }

    private void initOrdersListener(String id) {
        transactionsViewModel.ordersLiveData(id).observe(this, new Observer<List<Orders>>() {
            @Override
            public void onChanged(List<Orders> orders) {
                setOrderAdapter(orders);
            }
        });
    }

    private void setOrderAdapter(List<Orders> orderList) {
        CheckoutAdapter checkoutAdapter = new CheckoutAdapter(orderList, this, getContext());
        listCheckoutItems.setAdapter(checkoutAdapter);
        listCheckoutItems.setLayoutManager(new LinearLayoutManager(getContext()));
        checkoutAdapter.notifyDataSetChanged();
    }


    @Override
    public void productClicked(Products productsModel) {

    }
}
