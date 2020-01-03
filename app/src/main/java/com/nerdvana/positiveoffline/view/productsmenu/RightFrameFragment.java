package com.nerdvana.positiveoffline.view.productsmenu;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.ProductsAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.AsyncContract;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.nerdvana.positiveoffline.model.ProductToCheckout;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.ProductsViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class RightFrameFragment extends Fragment implements AsyncContract, ProductsContract {

    private View view;
    private RecyclerView listProducts;
    private HidingEditText search;

    private ProductsAdapter productsAdapter;

    private ProductsViewModel productsViewModel;

    private TransactionsViewModel transactionsViewModel;
    private Products products;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_right_frame, container, false);
        initViews(view);
        initProductsViewModel();
        initTransactionViewModel();
//        initTransactionsViewModelListener();
        return view;
    }

    private void initTransactionViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private void fetchProducts() {
        try {
            setProductAdapter(productsViewModel.getProductsList());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initProductsViewModel() {
        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
    }

    private void initViews(View view) {
        search = view.findViewById(R.id.search);
        addSearchListener();
        listProducts = view.findViewById(R.id.listProducts);
    }

    private void addSearchListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (productsAdapter != null) {
                    productsAdapter.getFilter().filter(editable);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchProducts();
    }

    private void setProductAdapter(List<Products> productsList) {
        //set products adapter with 5 columns (grid layout)
        productsAdapter = new ProductsAdapter(productsList, this, getContext());
        listProducts.setLayoutManager(new GridLayoutManager(getContext(), 4));
        listProducts.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();


    }


    @Override
    public void doneLoading(List list, String isFor) {
        switch (isFor) {
            case "products":
                productsAdapter.addItems(list);
                break;
        }
    }

    @Override
    public void productClicked(Products productsModel) {
        BusProvider.getInstance().post(new ProductToCheckout(productsModel));
//        List<Orders> orderList = new ArrayList<>();
//        products = productsModel;
//        try {
//            if (transactionsList().size() > 0) {
//                orderList.add(new Orders(
//                        transactionsList().get(0).getId(),
//                        productsModel.getCore_id(),
//                        1,
//                        productsModel.getAmount(),
//                        productsModel.getAmount(),
//                        productsModel.getProduct()
//                ));
//                transactionsViewModel.insertOrder(orderList);
//            } else {
//                final int min = 1;
//                final int max = 1000;
//                final int random = new Random().nextInt((max - min) + 1) + min;
//                List<Transactions> tr = new ArrayList<>();
//                tr.add(new Transactions(String.valueOf(random), 01));
//                transactionsViewModel.insert(tr);
//            }
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private List<Transactions> transactionsList() throws ExecutionException, InterruptedException {
        return transactionsViewModel.transactionList();
    }

    private void initTransactionsViewModelListener() {
        transactionsViewModel.transactionLiveData().observe(this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {

                //add order to database
                if (products != null) {
                    if (transactions.size() > 0) {
                        List<Orders> orderList = new ArrayList<>();
                        orderList.add(new Orders(
                                transactions.get(0).getId(),
                                products.getCore_id(),
                                1,
                                products.getAmount(),
                                products.getAmount(),
                                products.getProduct(),
                                products.getDepartmentId(),
                                Utils.roundedOffTwoDecimal(products.getAmount() - (products.getAmount() / 1.12)),
                                Utils.roundedOffTwoDecimal(products.getAmount() / 1.12),
                                0.00,
                                0.00,
                                products.getDepartment(),
                                products.getCategory(),
                                products.getCategoryId()
                        ));
                        transactionsViewModel.insertOrder(orderList);
                    }



                }

                products = null;
            }
        });
    }
}
