package com.nerdvana.positiveoffline.view.checkoutmenu;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.CheckoutAdapter;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.OrdersContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.ProductToCheckout;
import com.nerdvana.positiveoffline.view.dialog.ChangeQtyDialog;
import com.nerdvana.positiveoffline.view.dialog.CollectionDialog;
import com.nerdvana.positiveoffline.view.dialog.CutOffMenuDialog;
import com.nerdvana.positiveoffline.view.dialog.DiscountMenuDialog;
import com.nerdvana.positiveoffline.view.dialog.InputDialog;
import com.nerdvana.positiveoffline.view.dialog.IntransitDialog;
import com.nerdvana.positiveoffline.view.dialog.OpenPriceDialog;
import com.nerdvana.positiveoffline.view.dialog.PasswordDialog;
import com.nerdvana.positiveoffline.view.dialog.PaymentDialog;
import com.nerdvana.positiveoffline.view.dialog.TransactionDialog;
import com.nerdvana.positiveoffline.view.resumetransaction.ResumeTransactionActivity;
import com.nerdvana.positiveoffline.view.rooms.RoomsActivity;
import com.nerdvana.positiveoffline.view.settings.SettingsActivity;
import com.nerdvana.positiveoffline.viewmodel.CutOffViewModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.RoomsViewModel;
import com.nerdvana.positiveoffline.viewmodel.SettingsViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LeftFrameFragment extends Fragment implements OrdersContract {

    //regiondialogs
    private CollectionDialog collectionDialog;
    private IntransitDialog intransitDialog;
    private DiscountMenuDialog discountMenuDialog;
    private PaymentDialog paymentDialog;
    private PasswordDialog passwordDialog;
    private ChangeQtyDialog changeQtyDialog;
    private InputDialog inputDialog;
    private CutOffMenuDialog cutOffMenuDialog;
    private TransactionDialog transactionDialog;
    private OpenPriceDialog openPriceDialog;
    //endregion

    //regionview models
    private RoomsViewModel roomsViewModel;
    private TransactionsViewModel transactionsViewModel;
    private UserViewModel userViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private DiscountViewModel discountViewModel;
    private CutOffViewModel cutOffViewModel;
    private SettingsViewModel settingsViewModel;
    //endregion

    private final int RESUME_TRANS_RETURN = 100;
    private final int ROOM_SELECTED_RETURN = 101;

    private Products selectedProduct;
    private RoomRates selectedRoomRate;

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
        initRoomViewModel();
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

    private void initRoomViewModel() {
        roomsViewModel = new ViewModelProvider(this).get(RoomsViewModel.class);
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

                            if (selectedRoomRate != null) {

                                Rooms rooms = roomsViewModel.getRoomViaId(selectedRoomRate.getRoom_id());
                                rooms.setTransaction_id(transactionId);
                                roomsViewModel.update(rooms);


                                Transactions tmpTr = transactionsViewModel.loadedTransactionList(String.valueOf(transactionId)).get(0);
                                tmpTr.setId(Integer.valueOf(transactionId));
                                tmpTr.setRoom_id(rooms.getRoom_id());
                                tmpTr.setRoom_number(rooms.getRoom_name());
                                tmpTr.setIs_sent_to_server(0);

                                tmpTr.setCheck_in_time(Utils.getDateTimeToday());

                                transactionsViewModel.update(tmpTr);

                                insertSelectedRoomRate();

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
                    transactions.getTreg(),
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
                    transactions.getIs_cancelled_at(),
                    transactions.getTin_number()
            );
            tr.setRoom_id(transactions.getRoom_id());
            tr.setRoom_number(transactions.getRoom_number());
            tr.setMachine_id(transactions.getMachine_id());
            tr.setIs_sent_to_server(0);
            tr.setBranch_id(transactions.getBranch_id());
            tr.setCheck_in_time(transactions.getCheck_in_time());
            tr.setCheck_out_time(transactions.getCheck_out_time());


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        transactionsViewModel.update(tr);
    }

    private void changeRoomStatus(Rooms room, int status_id, boolean is_transfer) {
        try {

            RoomStatus roomStatus = roomsViewModel.getRoomStatusViaId(status_id);
            room.setStatus_id(roomStatus.getCore_id());
            room.setStatus_description(roomStatus.getRoom_status());
            room.setHex_color(roomStatus.getHex_color());

            if (is_transfer) {
                room.setTransaction_id("");
            }
            roomsViewModel.update(room);
        } catch (Exception e) {

        }
    }




    @Subscribe
    public void menuClicked(ButtonsModel buttonsModel) throws ExecutionException, InterruptedException {
        switch (buttonsModel.getId()) {
            case 124://INTRANSIT
                if (intransitDialog == null) {
                    intransitDialog = new IntransitDialog(getActivity(), transactionsViewModel);
                    intransitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            intransitDialog = null;
                        }
                    });
                    intransitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            intransitDialog = null;
                        }
                    });
                    intransitDialog.show();
                }
                break;
            case 125://SPOT AUDIT
                if (collectionDialog == null) {
                    collectionDialog = new CollectionDialog(getActivity(), dataSyncViewModel) {
                        @Override
                        public void cutOffSuccess(Double totalCash) {

                            try {
                                CutOff cutOff = new CutOff(
                                        0,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0,
                                        Utils.getDateTimeToday(),
                                        "",
                                        "",
                                        0,
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                );
                                cutOff.setId(996699);
                                        /*
                                        cutOffViewModel.insertData(new CutOff(
                                        0,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0.00,
                                        0,
                                        Utils.getDateTimeToday(),
                                        "",
                                        "",
                                        0,
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                ));
                                         */

                                List<Transactions> transactionsList = transactionsViewModel.unCutOffTransactions(userViewModel.searchLoggedInUser().get(0).getUsername());
                                int number_of_transaction = 0;
                                Double gross_sales = 0.00;
                                Double net_sales = 0.00;
                                Double vatable_sales = 0.00;
                                Double vat_exempt_sales = 0.00;
                                Double vat_amount = 0.00;
                                Double void_amount = 0.00;

                                Double total_cash_payments = 0.00;
                                Double total_card_payments = 0.00;
                                Double total_change = 0.00;

                                int seniorCount = 0;
                                Double seniorAmount = 0.00;
                                int pwdCount = 0;
                                Double pwdAmount = 0.00;
                                int othersCount = 0;
                                Double othersAmount = 0.00;

                                String begOrNo = "";
                                String endOrNo = "";


                                if (transactionsList.size() > 0) {
                                    begOrNo = transactionsList.get(0).getReceipt_number();
                                    endOrNo = transactionsList.get(transactionsList.size() - 1).getReceipt_number();
                                    for (Transactions tr : transactionsList) {
                                        if (tr.getIs_void()) {
                                            void_amount += tr.getNet_sales();
                                        } else {

                                            gross_sales += tr.getGross_sales();
                                            net_sales += tr.getNet_sales();
                                            vatable_sales += tr.getVatable_sales();
                                            vat_exempt_sales += tr.getVat_exempt_sales();
                                            vat_amount += tr.getVat_amount();
                                            total_change += tr.getChange();
                                        }
                                        number_of_transaction += 1;

                                        tr.setIs_sent_to_server(0);
                                        tr.setIs_cut_off(true);
                                        tr.setIs_cut_off_by(userViewModel.searchLoggedInUser().get(0).getUsername());
                                        tr.setCut_off_id(996699);
                                        tr.setIs_cut_off_at(Utils.getDateTimeToday());
//                                        transactionsViewModel.update(tr);
                                    }
                                    dismiss();
                                } else {
                                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_no_transaction_cutoff), getContext().getString(R.string.header_message));
                                }


                                for (Payments payments : cutOffViewModel.getAllPayments()) {
//                                    payments.setIs_sent_to_server(0);
//                                    payments.setCut_off_id((int) cut_off_id);
//                                    cutOffViewModel.update(payments);
                                    switch (payments.getCore_id()) {
                                        case 1://CASH
                                            total_cash_payments += payments.getAmount();
                                            break;
                                        case 2://CARD
                                            total_card_payments += payments.getAmount();
                                    }
                                }

                                for (PostedDiscounts postedDiscounts : cutOffViewModel.getUnCutOffPostedDiscount()) {
//                                    postedDiscounts.setCut_off_id((int)cut_off_id);
//                                    postedDiscounts.setIs_sent_to_server(0);
                                    if (postedDiscounts.getDiscount_name().equalsIgnoreCase("SENIOR")) {
                                        seniorCount += 1;
                                        seniorAmount += postedDiscounts.getAmount();
                                    } else if (postedDiscounts.getDiscount_name().equalsIgnoreCase("PWD")) {
                                        pwdCount += 1;
                                        pwdAmount += postedDiscounts.getAmount();
                                    } else {
                                        othersCount += 1;
                                        othersAmount += postedDiscounts.getAmount();
                                    }
                                }

//                                CutOff cutOff = cutOffViewModel.getCutOff(cut_off_id);
                                cutOff.setIs_sent_to_server(0);
                                cutOff.setTotal_change(Utils.roundedOffTwoDecimal(total_change));
                                cutOff.setGross_sales(Utils.roundedOffTwoDecimal(gross_sales));
                                cutOff.setNet_sales(Utils.roundedOffTwoDecimal(net_sales));
                                cutOff.setVatable_sales(Utils.roundedOffTwoDecimal(vatable_sales));
                                cutOff.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vat_exempt_sales));
                                cutOff.setVat_amount(Utils.roundedOffTwoDecimal(vat_amount));
                                cutOff.setNumber_of_transactions(number_of_transaction);
                                cutOff.setVoid_amount(Utils.roundedOffTwoDecimal(void_amount));
                                cutOff.setTotal_cash_amount(Utils.roundedOffTwoDecimal(totalCash));
                                cutOff.setTotal_cash_payments(Utils.roundedOffTwoDecimal(total_cash_payments));
                                cutOff.setTotal_card_payments(Utils.roundedOffTwoDecimal(total_card_payments));

                                cutOff.setSeniorCount(seniorCount);
                                cutOff.setSeniorAmount(seniorAmount);
                                cutOff.setPwdCount(pwdCount);
                                cutOff.setPwdAmount(pwdAmount);
                                cutOff.setOthersCount(othersCount);
                                cutOff.setOthersAmount(othersAmount);

                                cutOff.setBegOrNo(begOrNo);
                                cutOff.setEndOrNo(endOrNo);

                                BusProvider.getInstance().post(new PrintModel("PRINT_SPOT_AUDIT", GsonHelper.getGson().toJson(cutOff)));

//                                cutOffViewModel.update(cutOff);
                                dismiss();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    };
                    collectionDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            collectionDialog = null;
                        }
                    });
                    collectionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            collectionDialog = null;
                        }
                    });
                    collectionDialog.show();
                }
                break;
            case 122:// DISCOUNT EXEMPT
                if (getEditingOrderList().size() > 0) {
                    diDiscountExempt(getEditingOrderList(), true);
                } else {
                    Helper.showDialogMessage(getActivity(), "No item to discount exempt", "Information");
                }
                break;
            case 110:// TEST PRINT

                BusProvider.getInstance().post(new PrintModel("CHEAT", "123131"));

                break;
            case 109://PRINT SOA
                if (Utils.isPasswordProtected(userViewModel, "123")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "SOA", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doSoaFunction();
                            }
                        };

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
                        passwordDialog.show();
                    }

                } else {
                    doSoaFunction();
                }




                break;
            case 107://TRANSFER ROOM
                if (Utils.isPasswordProtected(userViewModel, "69")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(),"TRANSFER ROOM", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doTransferRoomFunction();
                            }
                        };
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
                        passwordDialog.show();
                    }
                } else {
                    doTransferRoomFunction();
                }
                break;
            case 106://OPEN ROOM OR TABLES DEPENDING ON SYSTEM TYPE
                Intent roomsActivityIntent = new Intent(getContext(), RoomsActivity.class);
                roomsActivityIntent.putExtra(AppConstants.TRANS_ID, TextUtils.isEmpty(transactionId) ? 0 : Integer.valueOf(transactionId));
                roomsActivityIntent.putExtra(AppConstants.TRANSFER, "n");
                startActivityForResult(roomsActivityIntent, ROOM_SELECTED_RETURN);
                break;
            case 996://OPEN VIEW RECEIPT
                if (Utils.isPasswordProtected(userViewModel, "125")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "VIEW RECEIPT", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doViewReceiptFunction();
                            }
                        };
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
                        passwordDialog.show();
                    }
                } else {
                    doViewReceiptFunction();
                }

                break;
            case 129://OPEN SETTINGS
                if (Utils.isPasswordProtected(userViewModel, "124")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "SETTINGS", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                startActivity(new Intent(getContext(), SettingsActivity.class));
                            }
                        };
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
                    startActivity(new Intent(getContext(), SettingsActivity.class));
                }

                break;
            case 115://OPEN DISCOUNT DIALOG
                if (Utils.isPasswordProtected(userViewModel, "62")) {
                    if (passwordDialog == null) {

                        passwordDialog = new PasswordDialog(getActivity(), "DISCOUNT", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doDiscountFunction();
                            }
                        };

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
                        passwordDialog.show();
                    }
                } else {
                    doDiscountFunction();
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
                if (Utils.isPasswordProtected(userViewModel, "67")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "POST VOID", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doVoidTransactionFunction();
                            }
                        };
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
                        passwordDialog.show();
                    }
                } else {
                    doVoidTransactionFunction();
                }

//                startActivity(new Intent(getContext(), TransactionActivity.class));
                break;
            case 105://PAYMENT
                if (Utils.isPasswordProtected(userViewModel, "129")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "PAYMENT", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doPaymentFunction();
                            }
                        };
                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doPaymentFunction();
                }


                break;
            case 101://ITEM VOID
                if (getEditingOrderList().size() > 0) {
                    if (Utils.isPasswordProtected(userViewModel, "68")) {
                        if (passwordDialog == null) {
                            passwordDialog = new PasswordDialog(getActivity(),
                                    getContext().getString(R.string.item_void_password_dialog),
                                    userViewModel,
                                    transactionsViewModel) {
                                @Override
                                public void success() {
                                    doItemVoidFunction();
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
                        doItemVoidFunction();
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
                                        order.setIs_sent_to_server(0);
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
                if (Utils.isPasswordProtected(userViewModel, "127")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "SAVE TRANSACTION", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doSaveTransactionFunction();
                            }
                        };
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
                        passwordDialog.show();
                    }
                } else {
                    doSaveTransactionFunction();
                }

                break;
            case 9988://RESUME TRANSACTION
                if (Utils.isPasswordProtected(userViewModel, "128")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getActivity(), "RESUME TRANSACTION", userViewModel, transactionsViewModel) {
                            @Override
                            public void success() {
                                doResumeTransaction();
                            }
                        };
                        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doResumeTransaction();
                }

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

    private void diDiscountExempt(List<Orders> ordersList, boolean willDiscountExempt) {
        for (Orders orders : ordersList) {
            if (willDiscountExempt) {
                orders.setIs_discount_exempt(1);
            } else {
                orders.setIs_discount_exempt(0);
            }
            orders.setIs_editing(false);
            transactionsViewModel.updateOrder(orders);
        }
    }

    private void doResumeTransaction() {
        Intent resumeTransactionIntent = new Intent(getContext(), ResumeTransactionActivity.class);
        startActivityForResult(resumeTransactionIntent, RESUME_TRANS_RETURN);
    }

    private void doSaveTransactionFunction() {
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

    }

    private void doItemVoidFunction() {
        try {


            for (Orders order : getEditingOrderList()) {
                order.setIs_sent_to_server(0);
                order.setIs_void(true);
                order.setIs_editing(false);
                transactionsViewModel.updateOrder(order);
            }


            Transactions currentTrans = transactionsViewModel.loadedTransactionList(transactionId).get(0);
            if (currentTrans.getRoom_id() != 0) {
                List<Orders> currentPunchedRoomRate = transactionsViewModel.roomRateList(transactionId);
                if (currentPunchedRoomRate.size() < 1) {
                    currentTrans.setIs_sent_to_server(0);
                    currentTrans.setRoom_number("");
                    currentTrans.setCheck_in_time("");
                    currentTrans.setCheck_out_time("");
                    currentTrans.setRoom_id(0);
                    transactionsViewModel.update(currentTrans);
                    changeRoomStatus(roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId)), 3, true);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doPaymentFunction() {
        if (!TextUtils.isEmpty(transactionId)) {
            try {
                if (transactionsViewModel.orderList(transactionId).size() > 0) {
                    if (paymentDialog == null) {
                        paymentDialog = new PaymentDialog(getActivity(), dataSyncViewModel,
                                transactionsViewModel, transactionId,
                                userViewModel, roomsViewModel) {
                            @Override
                            public void completed(String receiptNumber) {


                                try {
                                    BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(receiptNumber))));
    //                                        BusProvider.getInstance().post(new PrintModel("CHEAT", GsonHelper.getGson().toJson(transactionsViewModel.getTransaction(receiptNumber))));
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

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
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            Helper.showDialogMessage(getActivity(),
                    getContext().getString(R.string.error_no_transaction),
                    getContext().getString(R.string.header_message));
        }
    }

    private void doVoidTransactionFunction() {
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
    }

    private void doDiscountFunction() {
        if (!TextUtils.isEmpty(transactionId)) {
            try {

                if (transactionsViewModel.orderList(transactionId).size() > 0) {

                    if (getEditingOrderList().size() > 0) {
                        if (discountMenuDialog == null) {
                            boolean hasItemToDiscount = false;
                            for (Orders ord : transactionsViewModel.orderList(transactionId)) {
                                if (ord.getIs_discount_exempt() == 0) {
                                    hasItemToDiscount = true;
                                    break;
                                }
                            }
                            if (hasItemToDiscount) {
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
                            } else {
                                Helper.showDialogMessage(getActivity(),
                                        getContext().getString(R.string.no_item_to_discount_disc_exempt),
                                        getContext().getString(R.string.header_message));
                            }

                        }
                    } else {
                        Helper.showDialogMessage(getActivity(),
                                getContext().getString(R.string.no_item_to_discount),
                                getContext().getString(R.string.header_message));
                    }




                } else {
                    Helper.showDialogMessage(getActivity(),
                            getContext().getString(R.string.error_no_items_disc),
                            getContext().getString(R.string.header_message));
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            Helper.showDialogMessage(getActivity(),
                    getContext().getString(R.string.error_no_transaction_disc),
                    getContext().getString(R.string.header_message));
        }
    }

    private void doViewReceiptFunction() {
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
    }

    private void doTransferRoomFunction() {
        try {
            if (transactionsViewModel.loadedTransactionList(transactionId).size() > 0) {
                Transactions currentTrans = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                if (currentTrans.getRoom_id() != 0) {
                    Rooms rooms = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                    if (rooms != null) {
                        Intent roomsActivityIntent = new Intent(getContext(), RoomsActivity.class);
                        roomsActivityIntent.putExtra(AppConstants.TRANS_ID, TextUtils.isEmpty(transactionId) ? 0 : Integer.valueOf(transactionId));
                        roomsActivityIntent.putExtra(AppConstants.TRANSFER, "y");
                        startActivityForResult(roomsActivityIntent, ROOM_SELECTED_RETURN);
                    } else {
                        Helper.showDialogMessage(getContext(), "No room attached to orders", getString(R.string.header_message));
                    }
                } else {
                    Helper.showDialogMessage(getActivity(), "No room attached to transaction", getString(R.string.header_message));
                }
            } else {
                Helper.showDialogMessage(getActivity(), "No room attached to transaction", getString(R.string.header_message));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doSoaFunction() {
        try {
            if (transactionsViewModel.loadedTransactionList(transactionId).size() > 0) {
                Transactions currentTrans = transactionsViewModel.loadedTransactionList(transactionId).get(0);
                if (currentTrans.getRoom_id() != 0) {
                    Rooms rooms = roomsViewModel.getRoomViaTransactionId(Integer.valueOf(transactionId));
                    if (rooms != null) {
                        BusProvider.getInstance().post(new PrintModel("SOA", GsonHelper.getGson().toJson(transactionsViewModel.getTransactionViaTransactionId(rooms.getTransaction_id()))));
                    } else {
                        Helper.showDialogMessage(getContext(), "No room/table attached to orders", getString(R.string.header_message));
                    }
                } else {
                    Helper.showDialogMessage(getActivity(), "No room/table attached to transaction", getString(R.string.header_message));
                }
            } else {
                Helper.showDialogMessage(getActivity(), "No room/table attached to transaction", getString(R.string.header_message));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            case ROOM_SELECTED_RETURN:
                if (resultCode == Activity.RESULT_OK) {

                    selectedRoomRate = GsonHelper.getGson().fromJson(data.getExtras().getString("selected_room"), RoomRates.class);

                    try {
                        if (!TextUtils.isEmpty(transactionId)) {

                            Rooms rooms = roomsViewModel.getRoomViaId(selectedRoomRate.getRoom_id());
                            rooms.setTransaction_id(transactionId);
                            roomsViewModel.update(rooms);

                            insertSelectedRoomRate();
                        } else {
                            if (transactionsList().size() > 0) {

                                Rooms rooms = roomsViewModel.getRoomViaId(selectedRoomRate.getRoom_id());
                                rooms.setTransaction_id(transactionId);
                                roomsViewModel.update(rooms);


                                insertSelectedRoomRate();
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

                                tr.add(new Transactions(
                                        controlNumber,
                                        getUser().getUsername(),
                                        Utils.getDateTimeToday(),
                                        0,
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                                ));

                                transactionsViewModel.insert(tr);


                            }
                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                break;
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

                    tr.add(new Transactions(
                            controlNumber,
                            getUser().getUsername(),
                            Utils.getDateTimeToday(),
                            0,
                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                    ));



//                    tr.add(new Transactions(
//                            controlNumber,
//                            getUser().getUsername(),
//                            Utils.getDateTimeToday(),
//                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
//                            0
//                            ));
                    transactionsViewModel.insert(tr);


                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void insertSelectedRoomRate() {
        List<Orders> orderList = new ArrayList<>();
        if (!TextUtils.isEmpty(transactionId)) {
            orderList.add(new Orders(
                    Integer.valueOf(transactionId),
                    selectedRoomRate.getRoom_rate_price_id(),
                    1,
                    selectedRoomRate.getAmount(),
                    selectedRoomRate.getAmount(),
                    selectedRoomRate.getRoom_rate_description(),
                    selectedRoomRate.getRoom_type_id(),
                    Utils.roundedOffTwoDecimal((selectedRoomRate.getAmount() / 1.12) * .12),
                    Utils.roundedOffTwoDecimal(selectedRoomRate.getAmount() / 1.12),
                    0.00,
                    0.00,
                    "ROOM RATE",
                    "ROOM RATE",
                    0,
                    0,
                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                    Utils.getDateTimeToday(),
                    1
            ));
            transactionsViewModel.insertOrder(orderList);
        }

        selectedRoomRate = null;
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
                    selectedProduct.getCategoryId(),
                    0,
                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                    Utils.getDateTimeToday(),
                    0
            ));
            transactionsViewModel.insertOrder(orderList);
        }

    }

    @Override
    public void longClicked(Orders orders) {
        if (openPriceDialog == null) {
            openPriceDialog = new OpenPriceDialog(getActivity(), transactionsViewModel, orders) {
                @Override
                public void openPriceSuccess(Orders orders) {
                    transactionsViewModel.recomputeTransactionWithDiscount(transactionId, discountViewModel);
                }
            };

            openPriceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    openPriceDialog = null;
                }
            });

            openPriceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    openPriceDialog = null;
                }
            });

            openPriceDialog.show();
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
