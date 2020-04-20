package com.nerdvana.positiveoffline.view.productsmenu;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.ProductsAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.ThemeSelection;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.AsyncContract;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.ProductToCheckout;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.ProductsViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class RightFrameFragment extends Fragment implements AsyncContract, ProductsContract, View.OnClickListener {

    private Timer timer;
    private boolean isDarkMode = false;
    private View view;
    private RecyclerView listProducts;
    private CardView cardSearch;
    private HidingEditText search;
    private ConstraintLayout rightFrameConstraint;

    private ProductsAdapter productsAdapter;

    private ProductsViewModel productsViewModel;

    private TransactionsViewModel transactionsViewModel;
    private Products products;
    private ImageView clearText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_right_frame, container, false);
        initViews(view);
        initProductsViewModel();
        initTransactionViewModel();
//        initTransactionsViewModelListener();

        BusProvider.getInstance().register(this);
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
        cardSearch = view.findViewById(R.id.cardSearch);
        clearText = view.findViewById(R.id.clearText);
        clearText.setOnClickListener(this);
        addSearchListener();
        listProducts = view.findViewById(R.id.listProducts);
        rightFrameConstraint = view.findViewById(R.id.rightFrameConstraint);
    }


    private void addSearchListener() {



        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (search.length() > 0) {
                    if (actionId == 0) {
                        try {
                            Products searchedProduct = productsViewModel.findProductViaBarcode(search.getText().toString());
                            if (searchedProduct != null) {
                                BusProvider.getInstance().post(new ProductToCheckout(searchedProduct));
                            } else {
                                Helper.showDialogMessage(getActivity(), "Product not existing", getString(R.string.header_message));
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        search.setText("");
                    }
                }
                return true;
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }

            }

            @Override
            public void afterTextChanged(final Editable editable) {

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (productsAdapter != null) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        productsAdapter.getFilter().filter(editable);
                                    }
                                });
                            }


                        }

                    }
                }, 500);



            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchProducts();

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchProducts();

    }

    private void setProductAdapter(List<Products> productsList) {
        productsAdapter = new ProductsAdapter(productsList,
                this,
                getContext(),
                isDarkMode);
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
        search.setText("");
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
                                products.getCategoryId(),
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                                Utils.getDateTimeToday(),
                                0,
                                "",
                                0,
                                products.getIs_fixed_asset()
                        ));
                        transactionsViewModel.insertOrder(orderList);
                    }



                }

                products = null;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearText:
                search.setText("");
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initThemeSelectionListener();
    }

    private void initThemeSelectionListener() {
        DataSyncViewModel dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
        dataSyncViewModel.getThemeSelectionLiveData().observe(this, new Observer<List<ThemeSelection>>() {
            @Override
            public void onChanged(List<ThemeSelection> themeSelectionList) {
                for (ThemeSelection tsl : themeSelectionList) {
                    if (tsl.getIs_selected()) {
                        if (tsl.getTheme_id() == 100) { // LIGHT MODE
                            search.setTextColor(getResources().getColor(R.color.colorBlack));
                            search.setHintTextColor(getResources().getColor(R.color.colorBlack));
                            search.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            cardSearch.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            listProducts.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            rightFrameConstraint.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                            isDarkMode = false;
                            break;
                        } else { // DARK MODE
                            isDarkMode = true;
                            search.setTextColor(getResources().getColor(R.color.colorWhite));
                            search.setHintTextColor(getResources().getColor(R.color.colorWhite));
                            search.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            cardSearch.setBackgroundColor(getResources().getColor(R.color.colorDarkLighter));
                            listProducts.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            rightFrameConstraint.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            break;
                        }
                    }
                }

                try {
                    setProductAdapter(productsViewModel.getProductsList());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
    }

}
