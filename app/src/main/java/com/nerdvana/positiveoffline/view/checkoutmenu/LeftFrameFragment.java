package com.nerdvana.positiveoffline.view.checkoutmenu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.CheckoutAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.OrdersContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.ProductToCheckout;
import com.nerdvana.positiveoffline.view.dialog.ChangeQtyDialog;
import com.nerdvana.positiveoffline.view.dialog.CutOffMenuDialog;
import com.nerdvana.positiveoffline.view.dialog.DiscountMenuDialog;
import com.nerdvana.positiveoffline.view.dialog.InputDialog;
import com.nerdvana.positiveoffline.view.dialog.PasswordDialog;
import com.nerdvana.positiveoffline.view.dialog.PaymentDialog;
import com.nerdvana.positiveoffline.view.dialog.TransactionDialog;
import com.nerdvana.positiveoffline.view.resumetransaction.ResumeTransactionActivity;
import com.nerdvana.positiveoffline.view.settings.SettingsActivity;
import com.nerdvana.positiveoffline.viewmodel.CutOffViewModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.SettingsViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LeftFrameFragment extends Fragment implements OrdersContract {

    //regiondialogs
    private DiscountMenuDialog discountMenuDialog;
    private PaymentDialog paymentDialog;
    private PasswordDialog passwordDialog;
    private ChangeQtyDialog changeQtyDialog;
    private InputDialog inputDialog;
    private CutOffMenuDialog cutOffMenuDialog;
    private TransactionDialog transactionDialog;
    //endregion

    //regionview models
    private TransactionsViewModel transactionsViewModel;
    private UserViewModel userViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private DiscountViewModel discountViewModel;
    private CutOffViewModel cutOffViewModel;
    private SettingsViewModel settingsViewModel;
    //endregion

    private final int RESUME_TRANS_RETURN = 100;

    private Products selectedProduct;

    private View view;
    private TextView subTotalValue;
    private TextView totalValue;
    private TextView discountValue;



    private RecyclerView listCheckoutItems;



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
        discountValue = view.findViewById(R.id.discountValue);
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
        initDiscountViewModel();
        initOrdersListener();
        initDataSyncViewModel();
        initCutOffViewModel();
        initSettingsViewModel();
        try {
            if (transactionsList().size() > 0) {
                setOrderAdapter(transactionsViewModel.orderList(transactionId));
            }
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }



    }

    private void initSettingsViewModel() {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    private void initCutOffViewModel() {
        cutOffViewModel = new ViewModelProvider(this).get(CutOffViewModel.class);
    }

    private void initDiscountViewModel() {
        discountViewModel = new ViewModelProvider(this).get(DiscountViewModel.class);
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
                    if (!TextUtils.isEmpty(transactionId)) {
                        setOrderAdapter(transactionsViewModel.orderList(transactionId));
                    } else {
                        if (transactions.size() > 0) {
                            transactionId = String.valueOf(transactions.get(0).getId());

                            if (selectedProduct != null) {
                                insertSelectedOrder();
                                selectedProduct = null;
                            }
                            setOrderAdapter(transactionsViewModel.orderList(transactionId));
                        } else {
                            defaults();
                        }
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
                    transactionsViewModel.recomputeTransaction(transactionsViewModel.orderList(transactionId), transactionId);
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

        selectedProduct = null;


    }

    private void computeTotal(List<Orders> orderList) {
        Double subTotal = 0.00;
        Double totalValue = 0.00;
        Double discountAmount = 0.00;
        for (Orders order : orderList) {
            totalValue += order.getAmount() * order.getQty();
            subTotal += order.getOriginal_amount() * order.getQty();
            discountAmount += order.getDiscountAmount();
        }

        discountValue.setText(Utils.digitsWithComma(discountAmount));
        subTotalValue.setText(Utils.digitsWithComma(subTotal));
        this.totalValue.setText(Utils.digitsWithComma(totalValue));
    }


    private void saveTransaction(Transactions transactions, String name) {
        transactionId = "";

        Transactions tr = null;
        try {

            tr = new Transactions(
                    transactions.getId(),
                    transactions.getControl_number(),
                    transactions.getUser_id(),
                    transactions.getIs_void(),
                    transactions.getIs_void_by(),
                    transactions.getIs_completed(),
                    transactions.getIs_completed_by(),
                    true,
                    getUser().getUsername(),
                    transactions.getIs_cut_off(),
                    transactions.getIs_cut_off_by(),
                    name,
                    transactions.getCreated_at(),
                    transactions.getReceipt_number(),
                    transactions.getGross_sales() == null ? 0.00 : transactions.getGross_sales(),
                    transactions.getNet_sales() == null ? 0.00 : transactions.getNet_sales(),
                    transactions.getVatable_sales() == null ? 0.00 :transactions.getVatable_sales(),
                    transactions.getVat_exempt_sales() == null ? 0.00 : transactions.getVat_exempt_sales(),
                    transactions.getVatable_sales() == null ? 0.00 : transactions.getVatable_sales(),
                    transactions.getDiscount_amount() == null ? 0.00 : transactions.getDiscount_amount(),
                    0.00,
                    transactions.getVoid_at(),
                    transactions.getCompleted_at(),
                    Utils.getDateTimeToday(),
                    transactions.getIs_cut_off_at(),
                    transactions.getIs_cancelled(),
                    transactions.getIs_cancelled_by(),
                    transactions.getIs_cancelled_at()
            );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transactionsViewModel.update(tr);
    }



    @Subscribe
    public void menuClicked(ButtonsModel buttonsModel) throws ExecutionException, InterruptedException {
        switch (buttonsModel.getId()) {
            case 996://OPEN VIEW RECEIPT
                if (transactionDialog == null) {
                    transactionDialog = new TransactionDialog(getActivity(),
                            getContext().getString(R.string.title_reprint_transactions),
                            transactionsViewModel,
                            userViewModel,
                            false);
                    transactionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            transactionDialog = null;
                        }
                    });
                    transactionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            transactionDialog = null;
                        }
                    });

                    transactionDialog.show();
                }
                break;
            case 129://OPEN SETTINGS
                startActivity(new Intent(getContext(), SettingsActivity.class));
                break;
            case 115://OPEN DISCOUNT DIALOG
                if (!TextUtils.isEmpty(transactionId)) {
                    if (transactionsViewModel.orderList(transactionId).size() > 0) {
                        if (discountMenuDialog == null) {
                            discountMenuDialog = new DiscountMenuDialog(getActivity(), discountViewModel,
                                    transactionsViewModel, transactionId);
                            discountMenuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    discountMenuDialog = null;
                                }
                            });

                            discountMenuDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    discountMenuDialog = null;
                                }
                            });
                            discountMenuDialog.show();
                        }

                    } else {
                        Helper.showDialogMessage(getActivity(),
                                getContext().getString(R.string.error_no_items_disc),
                                getContext().getString(R.string.header_message));
                    }

                } else {
                    Helper.showDialogMessage(getActivity(),
                            getContext().getString(R.string.error_no_transaction_disc),
                            getContext().getString(R.string.header_message));
                }



                break;
            case 133://OPEN SHIFT CUT OFF DIALOG
                if (cutOffMenuDialog == null) {
                    cutOffMenuDialog = new CutOffMenuDialog(getActivity(), transactionsViewModel, userViewModel, dataSyncViewModel, cutOffViewModel);
                    cutOffMenuDialog.show();

                    cutOffMenuDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            cutOffMenuDialog = null;
                        }
                    });

                    cutOffMenuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            cutOffMenuDialog = null;
                        }
                    });
                }
                break;
            case 113://VOID TRANSACTION
                if (transactionDialog == null) {
                    transactionDialog = new TransactionDialog(getActivity(),
                            getContext().getString(R.string.title_completed_transactions),
                            transactionsViewModel,
                            userViewModel,
                            true);
                    transactionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            transactionDialog = null;
                        }
                    });
                    transactionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            transactionDialog = null;
                        }
                    });

                    transactionDialog.show();
                }
//                startActivity(new Intent(getContext(), TransactionActivity.class));
                break;
            case 105://PAYMENT
                if (!TextUtils.isEmpty(transactionId)) {
                    if (transactionsViewModel.orderList(transactionId).size() > 0) {
                        if (paymentDialog == null) {
                            paymentDialog = new PaymentDialog(getActivity(), dataSyncViewModel,
                                    transactionsViewModel, transactionId,
                                    userViewModel) {
                                @Override
                                public void completed(String receiptNumber) {


//                                    try {
//                                        BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(receiptNumber))));
//                                    } catch (ExecutionException e) {
//                                        Log.d("EXECEPT", e.getLocalizedMessage());
//                                        e.printStackTrace();
//                                    } catch (InterruptedException e) {
//                                        Log.d("EXECEPT", e.getLocalizedMessage());
//                                        e.printStackTrace();
//                                    }

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

                            paymentDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    paymentDialog = null;
                                }
                            });

                            paymentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    paymentDialog = null;
                                }
                            });
                        }

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
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(),
                                getContext().getString(R.string.item_void_password_dialog),
                                userViewModel,
                                transactionsViewModel) {
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

                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });

                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                    }

                } else {
                    Helper.showDialogMessage(getActivity(), "No item to void", "Information");
                }
                break;
            case 99://CHANGE QUANTITY
                if (getEditingOrderList().size() > 0) {
                    if (changeQtyDialog == null) {
                        changeQtyDialog = new ChangeQtyDialog(getActivity(), getEditingOrderList().get(0).getQty()) {
                            @Override
                            public void success(int newQty) {
                                try {
                                    for (Orders order : getEditingOrderList()) {

                                        if (newQty > order.getQty()) {
                                            order.setVatAmount(((newQty - order.getQty()) * order.getVatAmount()) + order.getVatAmount());
                                            order.setVatable(((newQty - order.getQty()) * order.getVatable()) + order.getVatable());
                                            order.setVatExempt(((newQty - order.getQty()) * order.getVatExempt()) + order.getVatExempt());
                                            order.setDiscountAmount(((newQty - order.getQty()) * order.getDiscountAmount()) + order.getDiscountAmount());
                                        } else if (newQty < order.getQty()) {
                                            order.setVatAmount((order.getVatAmount() / order.getQty()) * newQty);
                                            order.setVatable((order.getVatable() / order.getQty()) * newQty);
                                            order.setVatExempt((order.getVatExempt() / order.getQty()) * newQty);
                                            order.setDiscountAmount((order.getDiscountAmount() / order.getQty()) * newQty);
                                        }
                                        order.setQty(newQty);
                                        order.setIs_editing(false);
                                        transactionsViewModel.updateOrder(order);

                                    }

                                    transactionsViewModel.recomputeTransaction(transactionsViewModel.orderList(transactionId), transactionId);

                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }
                        };
                        changeQtyDialog.show();

                        changeQtyDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                changeQtyDialog = null;
                            }
                        });

                        changeQtyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                changeQtyDialog = null;
                            }
                        });
                    }

                } else {
                    Helper.showDialogMessage(getActivity(), "No item to change quantity", "Information");
                }
                break;
            case 100://SAVE TRANSACTION
                try {
                    if (transactionsList().size() > 0) {
                        if (inputDialog == null) {
                            inputDialog = new InputDialog(getActivity()) {
                                @Override
                                public void confirm(String str) {

                                    try {
                                        saveTransaction(transactionsList().get(0), str);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            inputDialog.show();

                            inputDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    inputDialog = null;
                                }
                            });

                            inputDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    inputDialog = null;
                                }
                            });
                        }


                    } else {
                        if (loadedTransactionsList(transactionId).size() > 0) {

                            saveTransaction(loadedTransactionsList(transactionId).get(0), "");

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
                } else {
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
                break;
        }
    }

    private void defaults() {
        transactionId = "";
        setOrderAdapter(new ArrayList<Orders>());
    }

    @Subscribe
    public void productToCheckout(ProductToCheckout productsModel) {

        try {
            selectedProduct = productsModel.getProducts();
            if (!TextUtils.isEmpty(transactionId)) {
                insertSelectedOrder();
            } else {
                if (transactionsList().size() > 0) {
                    insertSelectedOrder();
                } else {
                    String controlNumber = "";
                    try {
                        if (transactionsViewModel.lastTransactionId() == null) {
                            controlNumber = Utils.getCtrlNumberFormat("1");
                        } else {
                            if (TextUtils.isEmpty(transactionsViewModel.lastTransactionId().getControl_number())) {
                                controlNumber = Utils.getCtrlNumberFormat("1");
                            } else {
                                controlNumber = Utils.getCtrlNumberFormat(String.valueOf(Integer.valueOf(transactionsViewModel.lastTransactionId().getControl_number().split("-")[1].replaceFirst("0", "")) + 1));
                            }

                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    List<Transactions> tr = new ArrayList<>();

                    tr.add(new Transactions(controlNumber, getUser().getUsername(), Utils.getDateTimeToday()));
                    transactionsViewModel.insert(tr);


                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void insertSelectedOrder() {
        List<Orders> orderList = new ArrayList<>();
        if (!TextUtils.isEmpty(transactionId)) {
            orderList.add(new Orders(
                    Integer.valueOf(transactionId),
                    selectedProduct.getCore_id(),
                    1,
                    selectedProduct.getAmount(),
                    selectedProduct.getAmount(),
                    selectedProduct.getProduct(),
                    selectedProduct.getDepartmentId(),
                    Utils.roundedOffTwoDecimal((selectedProduct.getAmount() / 1.12) * .12),
                    Utils.roundedOffTwoDecimal(selectedProduct.getAmount() / 1.12),
                    0.00,
                    0.00,
                    selectedProduct.getDepartment(),
                    selectedProduct.getCategory(),
                    selectedProduct.getCategoryId()
            ));
            transactionsViewModel.insertOrder(orderList);
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

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
    }

}
