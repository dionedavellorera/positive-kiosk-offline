package com.nerdvana.positiveoffline.view.checkoutmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.CheckoutAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.OrdersContract;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.ProductToCheckout;
import com.nerdvana.positiveoffline.view.dialog.ChangeQtyDialog;
import com.nerdvana.positiveoffline.view.dialog.PasswordDialog;
import com.nerdvana.positiveoffline.view.dialog.PaymentDialog;
import com.nerdvana.positiveoffline.view.resumetransaction.ResumeTransactionActivity;
import com.nerdvana.positiveoffline.view.voidtransaction.VoidTransactionActivity;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class LeftFrameFragment extends Fragment implements OrdersContract {

    private final int RESUME_TRANS_RETURN = 100;

    private View view;
    private TextView subTotalValue;
    private TextView totalValue;

    private TransactionsViewModel transactionsViewModel;

    private RecyclerView listCheckoutItems;


    private UserViewModel userViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private String transactionId = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_left_frame, container, false);
        BusProvider.getInstance().register(this);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        listCheckoutItems = view.findViewById(R.id.listCheckoutItems);
        subTotalValue = view.findViewById(R.id.subTotalValue);
        totalValue = view.findViewById(R.id.totalValue);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUserViewModel();
        initTransactionsViewModel();
        initTransactionsViewModelListener();
        initOrdersListener();
        initDataSyncViewModel();

        try {
            if (transactionsList().size() > 0) {
                setOrderAdapter(transactionsViewModel.orderList(transactionId));
            }
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }

    }

    private void initDataSyncViewModel() {
        dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void initTransactionsViewModelListener() {
        transactionsViewModel.transactionLiveData().observe(this, new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {

                try {
                    if (transactions.size() > 0) {
                        transactionId = String.valueOf(transactions.get(0).getId());
                        setOrderAdapter(transactionsViewModel.orderList(transactionId));
                    } else {
                        defaults();
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

    private List<Transactions> loadedTransactionsList(String transactionId) throws ExecutionException, InterruptedException {
        return transactionsViewModel.loadedTransactionList(transactionId);
    }



    private void initOrdersListener() {

        transactionsViewModel.ordersLiveData().observe(this, new Observer<List<Orders>>() {
            @Override
            public void onChanged(List<Orders> orders) {

                try {
                    setOrderAdapter(transactionsViewModel.orderList(transactionId));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setOrderAdapter(List<Orders> orderList) {

        computeTotal(orderList);

        CheckoutAdapter checkoutAdapter = new CheckoutAdapter(orderList, this, getContext());
        listCheckoutItems.setAdapter(checkoutAdapter);
        listCheckoutItems.setLayoutManager(new LinearLayoutManager(getContext()));
        checkoutAdapter.notifyDataSetChanged();
    }

    private void computeTotal(List<Orders> orderList) {
        Double subTotal = 0.00;
        Double totalValue = 0.00;
        for (Orders order : orderList) {
            totalValue += order.getAmount() * order.getQty();
            subTotal += order.getOriginal_amount() * order.getQty();
        }

        subTotalValue.setText(Utils.digitsWithComma(subTotal));
        this.totalValue.setText(Utils.digitsWithComma(totalValue));
    }


    private void saveTransaction(Transactions transactions) {
        transactionId = "";
            Transactions tr = new Transactions(
                    transactions.getId(),
                    transactions.getControl_number(),
                    transactions.getUser_id(),
                    transactions.getIs_void(),
                    transactions.getIs_completed(),
            true);
            transactionsViewModel.update(tr);
    }

    private void voidTransaction(Transactions transactions) {
        Transactions tr = new Transactions(
                transactions.getId(),
                transactions.getControl_number(),
                transactions.getUser_id(),
                true,
                true,
                transactions.getIs_saved());
        transactionsViewModel.update(tr);
    }


    @Subscribe
    public void menuClicked(ButtonsModel buttonsModel) throws ExecutionException, InterruptedException {
        switch (buttonsModel.getId()) {
            case 113://VOID TRANSACTION
                startActivity(new Intent(getContext(), VoidTransactionActivity.class));
                break;
            case 105://PAYMENT
                if (!TextUtils.isEmpty(transactionId)) {
                    if (transactionsViewModel.orderList(transactionId).size() > 0) {
                        PaymentDialog paymentDialog = new PaymentDialog(getActivity(), dataSyncViewModel, transactionsViewModel, transactionId) {
                            @Override
                            public void completed() {

                                Helper.showDialogMessage(getActivity(), getContext().getString(R.string.transaction_completed), getContext().getString(R.string.header_message));

                                try {
                                    if (transactionsList().size() > 0) {
                                        transactionId = String.valueOf(transactionsList().get(0).getId());
                                        setOrderAdapter(transactionsViewModel.orderList(transactionId));
                                    } else {
                                        defaults();
                                    }
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        paymentDialog.show();
                    } else {
                        Helper.showDialogMessage(getActivity(),
                                getContext().getString(R.string.error_no_items),
                                getContext().getString(R.string.header_message));
                    }

                } else {
                    Helper.showDialogMessage(getActivity(),
                            getContext().getString(R.string.error_no_transaction),
                            getContext().getString(R.string.header_message));
                }

                break;
            case 101://ITEM VOID
                if (getEditingOrderList().size() > 0) {
                    PasswordDialog passwordDialog = new PasswordDialog(getActivity(), getContext().getString(R.string.item_void_password_dialog), userViewModel) {
                        @Override
                        public void success() {

                            try {
                                for (Orders order : getEditingOrderList()) {
                                    order.setIs_void(true);
                                    order.setIs_editing(false);
                                    transactionsViewModel.updateOrder(order);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    };
                    passwordDialog.show();
                } else {
                    Helper.showDialogMessage(getActivity(), "No item to void", "Information");
                }
                break;
            case 99://CHANGE QUANTITY
                if (getEditingOrderList().size() > 0) {
                    ChangeQtyDialog changeQtyDialog = new ChangeQtyDialog(getActivity(), getEditingOrderList().get(0).getQty()) {
                        @Override
                        public void success(int newQty) {
                            try {
                                for (Orders order : getEditingOrderList()) {
                                    order.setQty(newQty);
                                    order.setIs_editing(false);
                                    transactionsViewModel.updateOrder(order);
                                }

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    changeQtyDialog.show();
                } else {
                    Helper.showDialogMessage(getActivity(), "No item to change quantity", "Information");
                }
                break;
            case 100://SAVE TRANSACTION
                try {
                    if (transactionsList().size() > 0) {
                        saveTransaction(transactionsList().get(0));
                    } else {
                        if (loadedTransactionsList(transactionId).size() > 0) {
                            saveTransaction(loadedTransactionsList(transactionId).get(0));
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 9988://RESUME TRANSACTION
                Intent resumeTransactionIntent = new Intent(getContext(), ResumeTransactionActivity.class);
                startActivityForResult(resumeTransactionIntent, RESUME_TRANS_RETURN);
                break;
            case 116://CANCEL BACK TO DEFAULTS
                if (transactionsList().size() > 0) {
                    transactionId = String.valueOf(transactionsList().get(0).getId());
                    setOrderAdapter(transactionsViewModel.orderList(transactionId));
                } else {
                    defaults();
                }
                break;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESUME_TRANS_RETURN:
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        transactionId = data.getExtras().getString(AppConstants.TRANS_ID);
                        setOrderAdapter(transactionsViewModel.orderList(transactionId));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void defaults() {
        transactionId = "";
        setOrderAdapter(new ArrayList<Orders>());
    }

    @Subscribe
    public void productToCheckout(ProductToCheckout productsModel) {
        List<Orders> orderList = new ArrayList<>();
        try {
            if (!TextUtils.isEmpty(transactionId)) {
                orderList.add(new Orders(
                        Integer.valueOf(transactionId),
                        productsModel.getProducts().getCore_id(),
                        1,
                        productsModel.getProducts().getAmount(),
                        productsModel.getProducts().getAmount(),
                        productsModel.getProducts().getProduct()
                ));
                transactionsViewModel.insertOrder(orderList);
            } else {
                if (transactionsList().size() > 0) {
                    orderList.add(new Orders(
                            transactionsList().get(0).getId(),
                            productsModel.getProducts().getCore_id(),
                            1,
                            productsModel.getProducts().getAmount(),
                            productsModel.getProducts().getAmount(),
                            productsModel.getProducts().getProduct()
                    ));
                    transactionsViewModel.insertOrder(orderList);
                } else {
                    final int min = 1;
                    final int max = 1000;
                    final int random = new Random().nextInt((max - min) + 1) + min;
                    List<Transactions> tr = new ArrayList<>();
                    tr.add(new Transactions(String.valueOf(random), 01));
                    transactionsViewModel.insert(tr);
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clicked(Orders orders) {
        if (orders.getIs_editing()) {
            orders.setIs_editing(false);
        } else {
            orders.setIs_editing(true);
        }

        transactionsViewModel.updateOrder(orders);
    }

    private List<Orders> getEditingOrderList() throws ExecutionException, InterruptedException {
        return transactionsViewModel.editingOrderList(transactionId);
    }


}
